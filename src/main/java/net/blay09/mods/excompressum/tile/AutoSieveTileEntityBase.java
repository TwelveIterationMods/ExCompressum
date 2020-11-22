package net.blay09.mods.excompressum.tile;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.block.AutoSieveBaseBlock;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.container.AutoSieveContainer;
import net.blay09.mods.excompressum.container.ModContainers;
import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.utils.DefaultItemHandler;
import net.blay09.mods.excompressum.utils.ItemHandlerAutomation;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.blay09.mods.excompressum.utils.SubItemHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

public abstract class AutoSieveTileEntityBase extends BaseTileEntity implements ITickableTileEntity, INamedContainerProvider {

    private static final int UPDATE_INTERVAL = 20;
    private static final int PARTICLE_TICKS = 30;

    private final DefaultItemHandler itemHandler = new DefaultItemHandler(this, 22) {
        @Override
        public boolean isItemValid(int slot, ItemStack itemStack) {
            if (inputSlots.isInside(slot)) {
                return isSiftableWithMesh(itemStack, getSieveMesh());
            } else if (meshSlots.isInside(slot)) {
                return isMesh(itemStack);
            }
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            // Make sure the mesh slot is always synced.
            if (meshSlots.isInside(slot)) {
                isDirty = true;
            }
        }
    };
    private final SubItemHandler inputSlots = new SubItemHandler(itemHandler, 0, 1);
    private final SubItemHandler outputSlots = new SubItemHandler(itemHandler, 1, 21);
    private final SubItemHandler meshSlots = new SubItemHandler(itemHandler, 21, 22);

    private final ItemHandlerAutomation itemHandlerAutomation = new ItemHandlerAutomation(itemHandler) {
        @Override
        public boolean canExtractItem(int slot, int amount) {
            return super.canExtractItem(slot, amount) && outputSlots.isInside(slot);
        }

        @Override
        public boolean canInsertItem(int slot, ItemStack itemStack) {
            return super.canInsertItem(slot, itemStack) && (inputSlots.isInside(slot) || meshSlots.isInside(slot));
        }
    };

    private final LazyOptional<ItemHandlerAutomation> itemHandlerAutomationCap = LazyOptional.of(() -> itemHandlerAutomation);

    private ItemStack currentStack = ItemStack.EMPTY;
    private GameProfile customSkin;

    private int ticksSinceSync;
    protected boolean isDirty;

    private float progress;

    private float foodBoost;
    private int foodBoostTicks;

    private BlockState cachedState;
    public float armAngle;
    private int particleTicks;
    private int particleCount;

    private boolean isDisabledByRedstone;

    public AutoSieveTileEntityBase(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void tick() {
        if (foodBoostTicks > 0) {
            foodBoostTicks--;
            if (foodBoostTicks <= 0) {
                foodBoost = 0f;
            }
        }

        if (!world.isRemote) {
            ticksSinceSync++;
            if (ticksSinceSync > UPDATE_INTERVAL) {
                if (isDirty) {
                    VanillaPacketHandler.sendTileEntityUpdate(this);
                    isDirty = false;
                }
                ticksSinceSync = 0;
            }
        }

        int effectiveEnergy = getEffectiveEnergy();
        if (!isDisabledByRedstone() && getEnergyStored() >= effectiveEnergy) {
            if (currentStack.isEmpty()) {
                ItemStack inputStack = inputSlots.getStackInSlot(0);
                SieveMeshRegistryEntry sieveMesh = getSieveMesh();
                if (!inputStack.isEmpty() && sieveMesh != null && isSiftableWithMesh(inputStack, sieveMesh)) {
                    boolean foundSpace = false;
                    for (int i = 0; i < outputSlots.getSlots(); i++) {
                        if (outputSlots.getStackInSlot(i).isEmpty()) {
                            foundSpace = true;
                        }
                    }
                    if (!foundSpace) {
                        return;
                    }
                    currentStack = inputStack.split(1);
                    if (inputStack.isEmpty()) {
                        inputSlots.setStackInSlot(0, ItemStack.EMPTY);
                    }
                    extractEnergy(effectiveEnergy, false);
                    VanillaPacketHandler.sendTileEntityUpdate(this);
                    progress = 0f;
                }
            } else {
                extractEnergy(effectiveEnergy, false);
                progress += getEffectiveSpeed();

                particleTicks = PARTICLE_TICKS;
                particleCount = (int) getSpeedMultiplier();

                isDirty = true;
                if (progress >= 1) {
                    if (!world.isRemote) {
                        SieveMeshRegistryEntry sieveMesh = getSieveMesh();
                        if (sieveMesh != null) {
                            Collection<ItemStack> rewards = rollSieveRewards(currentStack, sieveMesh, getEffectiveLuck(), world.rand);
                            for (ItemStack itemStack : rewards) {
                                if (!addItemToOutput(itemStack)) {
                                    world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, itemStack));
                                }
                            }
                            if (ExNihilo.getInstance().doMeshesHaveDurability()) {
                                ItemStack meshStack = meshSlots.getStackInSlot(0);
                                if (!meshStack.isEmpty()) {
                                    if (meshStack.attemptDamageItem(1, world.rand, null)) {
                                        getWorld().playSound(null, this.pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.5f, 2.5f);
                                        meshStack.shrink(1);
                                        meshSlots.setStackInSlot(0, ItemStack.EMPTY);
                                    }
                                }
                            }
                        } else {
                            if (!addItemToOutput(currentStack)) {
                                world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, currentStack));
                            }
                        }
                    }
                    progress = 0f;
                    currentStack = ItemStack.EMPTY;
                }
            }
        }

        if (particleTicks > 0 && world.isRemote) {
            particleTicks--;
            if (particleTicks <= 0) {
                particleCount = 0;
            }

            if (!currentStack.isEmpty() && !isUgly()) {
                final BlockState processingState = StupidUtils.getStateFromItemStack(currentStack);
                if (processingState != null) {
                    ExCompressum.proxy.spawnAutoSieveParticles(world, pos, getBlockState(), processingState, particleCount);
                }
            }
        }
    }

    protected abstract int extractEnergy(int energy, boolean simulate);

    private boolean addItemToOutput(ItemStack itemStack) {
        int firstEmptySlot = -1;
        for (int i = 0; i < outputSlots.getSlots(); i++) {
            ItemStack slotStack = outputSlots.getStackInSlot(i);
            if (slotStack.isEmpty()) {
                if (firstEmptySlot == -1) {
                    firstEmptySlot = i;
                }
            } else {
                if (slotStack.getCount() + itemStack.getCount() <= slotStack.getMaxStackSize() && slotStack.isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(slotStack, itemStack)) {
                    slotStack.grow(itemStack.getCount());
                    return true;
                }
            }
        }
        if (firstEmptySlot != -1) {
            outputSlots.setStackInSlot(firstEmptySlot, itemStack);
            return true;
        }
        return false;
    }

    public int getEffectiveEnergy() {
        return ExCompressumConfig.COMMON.autoSieveEnergy.get();
    }

    public float getEffectiveSpeed() {
        return (float) (ExCompressumConfig.COMMON.autoSieveSpeed.get() * getSpeedMultiplier());
    }

    private float getEffectiveLuck() {
        ItemStack meshStack = meshSlots.getStackInSlot(0);
        if (!meshStack.isEmpty()) {
            return ExNihilo.getInstance().getMeshFortune(meshStack);
        }
        return 0f;
    }

    public boolean isSiftableWithMesh(ItemStack itemStack, @Nullable SieveMeshRegistryEntry sieveMesh) {
        return ExNihilo.isSiftableWithMesh(getBlockState(), itemStack, sieveMesh);
    }

    private boolean isMesh(ItemStack itemStack) {
        return SieveMeshRegistry.getEntry(itemStack) != null;
    }

    public Collection<ItemStack> rollSieveRewards(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
        return ExNihilo.rollSieveRewards(getBlockState(), itemStack, sieveMesh, luck, rand);
    }

    @Override
    protected boolean hasUpdatePacket() {
        return true;
    }

    @Override
    protected void readFromNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        currentStack = ItemStack.read(tagCompound.getCompound("CurrentStack"));
        progress = tagCompound.getFloat("Progress");
        if (tagCompound.contains("CustomSkin")) {
            customSkin = NBTUtil.readGameProfile(tagCompound.getCompound("CustomSkin"));
            if (customSkin != null) {
                ExCompressum.proxy.preloadSkin(customSkin);
            }
        }
        foodBoost = tagCompound.getFloat("FoodBoost");
        foodBoostTicks = tagCompound.getInt("FoodBoostTicks");
        particleTicks = tagCompound.getInt("ParticleTicks");
        particleCount = tagCompound.getInt("ParticleCount");
        if (isSync) {
            meshSlots.setStackInSlot(0, ItemStack.read(tagCompound.getCompound("MeshStack")));
        } else {
            itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        }
        isDisabledByRedstone = tagCompound.getBoolean("IsDisabledByRedstone");
    }

    @Override
    protected void writeToNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        tagCompound.put("CurrentStack", currentStack.write(new CompoundNBT()));
        tagCompound.putFloat("Progress", progress);
        if (customSkin != null) {
            CompoundNBT customSkinTag = new CompoundNBT();
            NBTUtil.writeGameProfile(customSkinTag, customSkin);
            tagCompound.put("CustomSkin", customSkinTag);
        }
        tagCompound.putFloat("FoodBoost", foodBoost);
        tagCompound.putInt("FoodBoostTicks", foodBoostTicks);
        tagCompound.putInt("ParticleTicks", particleTicks);
        tagCompound.putInt("ParticleCount", particleCount);
        if (isSync) {
            ItemStack meshStack = meshSlots.getStackInSlot(0);
            tagCompound.put("MeshStack", meshStack.write(new CompoundNBT()));
        } else {
            tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        }
        tagCompound.putBoolean("IsDisabledByRedstone", isDisabledByRedstone());
    }

    public float getEnergyPercentage() {
        return (float) getEnergyStored() / (float) getMaxEnergyStored();
    }

    public abstract int getEnergyStored();

    public abstract void setEnergyStored(int energy);

    public abstract int getMaxEnergyStored();

    public boolean isProcessing() {
        return progress > 0f;
    }

    public float getProgress() {
        return progress;
    }

    public ItemStack getCurrentStack() {
        return currentStack;
    }

    public void setCustomSkin(@Nullable GameProfile customSkin) {
        this.customSkin = customSkin;
        grabProfile();
        isDirty = true;
        markDirty();
    }

    @Nullable
    public GameProfile getCustomSkin() {
        return customSkin;
    }

    private void grabProfile() {
        // I hope this doesn't break anything lol
        new Thread(() -> {
            try {
                if (!world.isRemote && customSkin != null && !StringUtils.isNullOrEmpty(customSkin.getName())) {
                    if (!customSkin.isComplete() || !customSkin.getProperties().containsKey("textures")) {
                        GameProfile gameProfile = ServerLifecycleHooks.getCurrentServer().getPlayerProfileCache().getGameProfileForUsername(customSkin.getName());
                        if (gameProfile != null) {
                            Property property = Iterables.getFirst(gameProfile.getProperties().get("textures"), null);
                            if (property == null) {
                                gameProfile = ServerLifecycleHooks.getCurrentServer().getMinecraftSessionService().fillProfileProperties(gameProfile, true);
                            }
                            customSkin = gameProfile;
                            isDirty = true;
                            markDirty();
                        }
                    }
                }
            } catch (ClassCastException ignored) {
                // This is really dumb
                // Vanilla's Yggdrasil can fail with a "com.google.gson.JsonPrimitive cannot be cast to com.google.gson.JsonObject" exception, likely their server was derping or whatever. I have no idea
                // And there doesn't seem to be safety checks for that in Vanilla code so I have to do it here.
            }
        }).start();
    }

    public float getSpeedMultiplier() {
        final float EFFICIENCY_BOOST = 0.25f;
        float boost = 1f;
        ItemStack meshStack = meshSlots.getStackInSlot(0);
        if (!meshStack.isEmpty()) {
            boost += EFFICIENCY_BOOST * ExNihilo.getInstance().getMeshEfficiency(meshStack);
        }
        return boost * getFoodBoost();
    }

    public float getFoodBoost() {
        return 1f + foodBoost;
    }

    public void setFoodBoost(int foodBoostTicks, float foodBoost) {
        this.foodBoostTicks = foodBoostTicks;
        this.foodBoost = foodBoost;
        this.isDirty = true;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (LazyOptional<T>) itemHandlerAutomationCap;
        }
        return super.getCapability(cap, side);
    }

    public DefaultItemHandler getItemHandler() {
        return itemHandler;
    }

    @Nullable
    public SieveMeshRegistryEntry getSieveMesh() {
        ItemStack meshStack = meshSlots.getStackInSlot(0);
        if (!meshStack.isEmpty()) {
            return SieveMeshRegistry.getEntry(meshStack);
        }
        return null;
    }

    public ItemStack getMeshStack() {
        return meshSlots.getStackInSlot(0);
    }

    public boolean isCorrectSieveMesh() {
        ItemStack inputStack = inputSlots.getStackInSlot(0);
        SieveMeshRegistryEntry sieveMesh = getSieveMesh();
        return inputStack.isEmpty() || sieveMesh == null || isSiftableWithMesh(inputStack, sieveMesh);
    }

    public boolean shouldAnimate() {
        return !currentStack.isEmpty() && getEnergyStored() >= getEffectiveEnergy() && !isDisabledByRedstone();
    }

    public boolean isUgly() {
        if (cachedState == null) {
            cachedState = world.getBlockState(pos);
        }
        if (cachedState.getBlock() instanceof AutoSieveBaseBlock) {
            return cachedState.get(AutoSieveBaseBlock.UGLY);
        }
        return false;
    }

    public boolean isWaterlogged() {
        if (cachedState == null) {
            cachedState = world.getBlockState(pos);
        }
        if (cachedState.getBlock() instanceof AutoSieveBaseBlock) {
            return cachedState.get(AutoSieveBaseBlock.WATERLOGGED);
        }
        return false;
    }

    public Direction getFacing() {
        if (cachedState == null) {
            cachedState = world.getBlockState(pos);
        }
        if (cachedState.getBlock() instanceof AutoSieveBaseBlock) {
            return cachedState.get(AutoSieveBaseBlock.FACING);
        }
        return Direction.NORTH;
    }

    public boolean isDisabledByRedstone() {
        return isDisabledByRedstone;
    }

    public void setDisabledByRedstone(boolean disabledByRedstone) {
        isDisabledByRedstone = disabledByRedstone;
        isDirty = true;
        ticksSinceSync = UPDATE_INTERVAL;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.excompressum.auto_sieve");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
        return new AutoSieveContainer(ModContainers.autoSieve, windowId, inv, this);
    }
}
