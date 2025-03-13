package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.ImplementedContainer;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.Objects;

public class WoodenCrucibleBlockEntity extends BalmBlockEntity implements BalmFluidTankProvider, ImplementedContainer {

    private static final int RAIN_FILL_INTERVAL = 20;
    private static final int MELT_INTERVAL = 20;
    private static final int RAIN_FILL_SPEED = 8;
    private static final int SYNC_INTERVAL = 10;

    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    private final FluidTank fluidTank = new FluidTank(1999) {
        @Override
        public int fill(Fluid fluid, int maxFill, boolean simulate) {
            int result = super.fill(fluid, maxFill, simulate);
            if (getAmount() > 1000) {
                setAmount(1000);
            }
            return result;
        }

        @Override
        public boolean canFill(Fluid fluid) {
            return items.get(0).isEmpty() && isValidFluid(fluid);
        }

        @Override
        public int getCapacity() {
            return 1000;
        }

        @Override
        public void setChanged() {
            WoodenCrucibleBlockEntity.this.setChanged();
            isDirty = true;
        }
    };

    private boolean isValidFluid(Fluid fluid) {
        return fluid.isSame(Fluids.WATER);
    }

    private int ticksSinceSync;
    private boolean isDirty;
    private int ticksSinceRain;
    private int ticksSinceMelt;
    private Fluid currentTargetFluid;
    private int solidVolume;

    public WoodenCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.woodenCrucible.get(), pos, state);
    }

    public boolean addItem(ServerLevel level, ItemStack itemStack, boolean isAutomated, boolean simulate) {
        // When inserting dust, turn it into clay if we have enough liquid
        if (fluidTank.getAmount() >= 1000 && itemStack.is(ModItemTags.DUSTS)) {
            if (!simulate) {
                items.set(0, new ItemStack(Blocks.CLAY));
                fluidTank.setFluid(Fluids.EMPTY, 0);
                sync();
            }
            return true;
        }

        // Otherwise, try to add it as a recipe
        WoodenCrucibleRecipe recipe = ExRegistries.getWoodenCrucibleRegistry().getRecipe(level, itemStack);
        if (recipe != null) {
            if (fluidTank.isEmpty() || recipe.matchesFluid(fluidTank.getFluid())) {
                int capacityLeft = fluidTank.getCapacity() - fluidTank.getAmount() - solidVolume;
                if ((isAutomated && capacityLeft >= recipe.getAmount()) || (!isAutomated && capacityLeft > 0)) {
                    if (!simulate) {
                        currentTargetFluid = recipe.getFluid();
                        solidVolume += Math.min(capacityLeft, recipe.getAmount());
                        sync();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, WoodenCrucibleBlockEntity blockEntity) {
        blockEntity.serverTick();
    }

    public void serverTick() {
        // Fill the crucible from rain
        if (level.getLevelData().isRaining() && level.canSeeSkyFromBelowWater(worldPosition) && level.getBiome(worldPosition).value().hasPrecipitation()) {
            ticksSinceRain++;
            if (ticksSinceRain >= RAIN_FILL_INTERVAL) {
                fluidTank.fill(Fluids.WATER, RAIN_FILL_SPEED, false);
                ticksSinceRain = 0;
            }
        }

        // Melt down content
        if (currentTargetFluid != null) {
            ticksSinceMelt++;
            if (ticksSinceMelt >= MELT_INTERVAL && fluidTank.getAmount() < fluidTank.getCapacity()) {
                int amount = Math.min(ExCompressumConfig.getActive().automation.woodenCrucibleSpeed, solidVolume);
                fluidTank.fill(currentTargetFluid, amount, false);
                solidVolume = Math.max(0, solidVolume - amount);
                ticksSinceMelt = 0;
                isDirty = true;
            }
        }

        // Sync to clients
        ticksSinceSync++;
        if (ticksSinceSync >= SYNC_INTERVAL) {
            ticksSinceSync = 0;
            if (isDirty) {
                sync();
                isDirty = false;
            }
        }
    }

    @Override
    public void loadAdditional(CompoundTag tagCompound, HolderLookup.Provider provider) {
        solidVolume = tagCompound.getIntOr("SolidVolume", 0);
        tagCompound.getCompound("FluidTank").ifPresent(fluidTank::deserialize);
        ContainerHelper.loadAllItems(tagCompound, items, provider);
        tagCompound.getString("TargetFluid").map(ResourceLocation::parse).flatMap(BuiltInRegistries.FLUID::getOptional)
                .ifPresent(targetFluid -> currentTargetFluid = targetFluid);
    }

    @Override
    public void saveAdditional(CompoundTag tagCompound, HolderLookup.Provider provider) {
        if (currentTargetFluid != null) {
            final var fluidId = BuiltInRegistries.FLUID.getKey(currentTargetFluid);
            tagCompound.putString("TargetFluid", Objects.toString(fluidId));
        }
        tagCompound.putInt("SolidVolume", solidVolume);
        tagCompound.put("FluidTank", fluidTank.serialize());
        ContainerHelper.saveAllItems(tagCompound, items, provider);
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag, level.registryAccess());
    }

    @Override
    public FluidTank getFluidTank() {
        return fluidTank;
    }

    public int getSolidVolume() {
        return solidVolume;
    }

    public int getSolidCapacity() {
        return fluidTank.getCapacity();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }
}
