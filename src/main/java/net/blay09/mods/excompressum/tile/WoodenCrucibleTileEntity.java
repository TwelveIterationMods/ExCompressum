package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.woodencrucible.WoodenCrucibleRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class WoodenCrucibleTileEntity extends TileEntity implements ITickableTileEntity {

    private static final int RAIN_FILL_INTERVAL = 20;
    private static final int MELT_INTERVAL = 20;
    private static final int RAIN_FILL_SPEED = 8;
    private static final int SYNC_INTERVAL = 10;

    private ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            ItemStack copy = stack.copy();
            if (addItem(copy, true, simulate)) {
                return copy.isEmpty() ? ItemStack.EMPTY : copy;
            }
            return stack;
        }
    };

    private FluidTank fluidTank = new FluidTank(1999, it -> itemHandler.getStackInSlot(0).isEmpty() && it.getFluid().getAttributes().getTemperature(it) <= 300) {

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int result = super.fill(resource, action);
            if (fluid.getAmount() > 1000) {
                fluid.setAmount(1000);
            }
            return result;
        }

        @Override
        public int getCapacity() {
            return 1000;
        }

        @Override
        protected void onContentsChanged() {
            markDirty();
            isDirty = true;
        }
    };

    private final LazyOptional<FluidTank> fluidTankCap = LazyOptional.of(() -> fluidTank);
    private final LazyOptional<IItemHandler> itemHandlerCap = LazyOptional.of(() -> itemHandler);

    private int ticksSinceSync;
    private boolean isDirty;
    private int ticksSinceRain;
    private int ticksSinceMelt;
    private WoodenCrucibleRegistryEntry currentMeltable;
    private int solidVolume;

    public WoodenCrucibleTileEntity() {
        super(ModTileEntities.woodenCrucible);
    }

    public boolean addItem(ItemStack itemStack, boolean isAutomated, boolean simulate) {
        // When inserting dust, turn it into clay if we have enough liquid
        if (fluidTank.getFluidAmount() >= 1000 && ExNihilo.isNihiloItem(itemStack, ExNihiloProvider.NihiloItems.DUST)) {
            itemStack.shrink(1);
            if (!simulate) {
                itemHandler.setStackInSlot(0, new ItemStack(Blocks.CLAY));
                fluidTank.setFluid(null);
                VanillaPacketHandler.sendTileEntityUpdate(this);
            }
            return true;
        }

        // Otherwise, try to add it as a meltable
        WoodenCrucibleRegistryEntry meltable = WoodenCrucibleRegistry.getEntry(itemStack);
        if (meltable != null) {
            if (fluidTank.getFluid() == null || fluidTank.getFluidAmount() == 0 || fluidTank.getFluid().getFluid() == meltable.getFluid()) {
                int capacityLeft = fluidTank.getCapacity() - fluidTank.getFluidAmount() - solidVolume;
                if ((isAutomated && capacityLeft >= meltable.getAmount()) || (!isAutomated && capacityLeft > 0)) {
                    itemStack.shrink(1);
                    if (!simulate) {
                        currentMeltable = meltable;
                        solidVolume += Math.min(capacityLeft, meltable.getAmount());
                        VanillaPacketHandler.sendTileEntityUpdate(this);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            // Fill the crucible from rain
            if (world.getWorldInfo().isRaining() && world.canBlockSeeSky(pos) && world.getBiome(pos).getDownfall() > 0f) {
                ticksSinceRain++;
                if (ticksSinceRain >= RAIN_FILL_INTERVAL) {
                    fluidTank.fill(new FluidStack(Fluids.WATER, RAIN_FILL_SPEED), IFluidHandler.FluidAction.EXECUTE);
                    ticksSinceRain = 0;
                }
            }

            // Melt down content
            if (currentMeltable != null) {
                ticksSinceMelt++;
                if (ticksSinceMelt >= MELT_INTERVAL && fluidTank.getFluidAmount() < fluidTank.getCapacity()) {
                    int amount = Math.min(ExCompressumConfig.COMMON.woodenCrucibleSpeed.get(), solidVolume);
                    fluidTank.fill(new FluidStack(currentMeltable.getFluid(), amount), IFluidHandler.FluidAction.EXECUTE);
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
                    VanillaPacketHandler.sendTileEntityUpdate(this);
                    isDirty = false;
                }
            }
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT tagCompound) {
        super.read(state, tagCompound);
        solidVolume = tagCompound.getInt("SolidVolume");
        fluidTank.readFromNBT(tagCompound.getCompound("FluidTank"));
        itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        if (tagCompound.contains("Content")) {
            currentMeltable = WoodenCrucibleRegistry.getEntry(ItemStack.read(tagCompound.getCompound("Content")));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        if (currentMeltable != null) {
            tagCompound.put("Content", currentMeltable.getItemStack().write(new CompoundNBT()));
        }
        tagCompound.putInt("SolidVolume", solidVolume);
        tagCompound.put("FluidTank", fluidTank.writeToNBT(new CompoundNBT()));
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        return tagCompound;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(getBlockState(), pkt.getNbtCompound());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (LazyOptional<T>) fluidTankCap;
        } else if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (LazyOptional<T>) itemHandlerCap;
        }
        return super.getCapability(cap, side);
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    public int getSolidVolume() {
        return solidVolume;
    }

    public int getSolidCapacity() {
        return fluidTank.getCapacity();
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}
