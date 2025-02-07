package net.blay09.mods.excompressum.block.entity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.container.DelegateContainer;
import net.blay09.mods.balm.api.container.SubContainer;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.block.AutoSieveBaseBlock;
import net.blay09.mods.excompressum.block.ModBlockStateProperties;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.menu.AutoSieveMenu;
import net.blay09.mods.excompressum.menu.ModMenus;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.StringUtils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractAutoSieveBlockEntity extends AbstractBaseBlockEntity implements BalmMenuProvider, BalmContainerProvider {

    private static final int UPDATE_INTERVAL = 20;
    private static final int PARTICLE_TICKS = 30;

    private final DefaultContainer backingContainer = new DefaultContainer(22) {
        @Override
        public boolean canPlaceItem(int slot, ItemStack itemStack) {
            if (inputSlots.containsOuterSlot(slot)) {
                return isSiftableWithMesh(itemStack, getSieveMesh());
            } else if (meshSlots.containsOuterSlot(slot)) {
                return isMesh(itemStack);
            }
            return true;
        }

        @Override
        public void slotChanged(int slot) {
            super.slotChanged(slot);
            // Make sure the mesh slot is always synced.
            if (meshSlots.containsOuterSlot(slot)) {
                isDirty = true;
            }
        }
    };

    private final ContainerData containerData = new ContainerData() {
        public int get(int id) {
            if (id == 0) {
                return (int) (100f * AbstractAutoSieveBlockEntity.this.getProgress());
            } else if (id == 1) {
                return AbstractAutoSieveBlockEntity.this.getEnergyStored();
            } else if (id == 2) {
                return AbstractAutoSieveBlockEntity.this.isDisabledByRedstone() ? 1 : 0;
            }
            return 0;
        }

        public void set(int id, int value) {
            if (id == 0) {
                AbstractAutoSieveBlockEntity.this.setProgress(value / 100f);
            } else if (id == 1) {
                AbstractAutoSieveBlockEntity.this.setEnergyStored(value);
            } else if (id == 2) {
                AbstractAutoSieveBlockEntity.this.setDisabledByRedstone(value == 1);
            }
        }

        public int getCount() {
            return 3;
        }
    };

    private final SubContainer inputSlots = new SubContainer(backingContainer, 0, 1);
    private final SubContainer outputSlots = new SubContainer(backingContainer, 1, 21);
    private final SubContainer meshSlots = new SubContainer(backingContainer, 21, 22);
    private final List<ItemStack> overflowBuffer = new ArrayList<>();

    private final DelegateContainer container = new DelegateContainer(backingContainer) {
        @Override
        public ItemStack removeItem(int slot, int count) {
            if (!outputSlots.containsOuterSlot(slot)) {
                return ItemStack.EMPTY;
            }

            return super.removeItem(slot, count);
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            if (!outputSlots.containsOuterSlot(slot)) {
                return ItemStack.EMPTY;
            }

            return super.removeItemNoUpdate(slot);
        }

        @Override
        public boolean canPlaceItem(int slot, ItemStack itemStack) {
            return super.canPlaceItem(slot, itemStack) && (inputSlots.containsOuterSlot(slot) || meshSlots.containsOuterSlot(slot));
        }

        @Override
        public boolean canExtractItem(int slot) {
            return outputSlots.containsOuterSlot(slot);
        }
    };

    private ItemStack currentStack = ItemStack.EMPTY;
    private GameProfile customSkin;

    private int ticksSinceSync;
    protected boolean isDirty;

    private float progress;

    private float foodBoost;
    private int foodBoostTicks;

    public float armAngle;
    private int particleTicks;
    private int particleCount;

    private boolean isDisabledByRedstone;

    public AbstractAutoSieveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, AbstractAutoSieveBlockEntity blockEntity) {
        blockEntity.clientTick();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractAutoSieveBlockEntity blockEntity) {
        blockEntity.serverTick();
    }

    public void clientTick() {
        if (particleTicks > 0) {
            particleTicks--;
            if (particleTicks <= 0) {
                particleCount = 0;
            }

            if (!currentStack.isEmpty() && !isUgly()) {
                final BlockState processingState = StupidUtils.getStateFromItemStack(currentStack);
                if (!processingState.isAir()) {
                    ExCompressum.proxy.get().spawnAutoSieveParticles(level, worldPosition, getBlockState(), processingState, particleCount);
                }
            }
        }
    }

    public void serverTick() {
        if (foodBoostTicks > 0) {
            foodBoostTicks--;
            if (foodBoostTicks <= 0) {
                foodBoost = 0f;
            }
        }

        ticksSinceSync++;
        if (ticksSinceSync > UPDATE_INTERVAL) {
            if (isDirty) {
                setChanged();
                sync();
                isDirty = false;
            }
            ticksSinceSync = 0;
        }

        int effectiveEnergy = getEffectiveEnergy();
        if (!isDisabledByRedstone() && overflowBuffer.isEmpty() && getEnergyStored() >= effectiveEnergy) {
            if (currentStack.isEmpty()) {
                ItemStack inputStack = inputSlots.getItem(0);
                SieveMeshRegistryEntry sieveMesh = getSieveMesh();
                if (!inputStack.isEmpty() && sieveMesh != null && isSiftableWithMesh(inputStack, sieveMesh)) {
                    boolean foundSpace = false;
                    for (int i = 0; i < outputSlots.getContainerSize(); i++) {
                        if (outputSlots.getItem(i).isEmpty()) {
                            foundSpace = true;
                        }
                    }
                    if (!foundSpace) {
                        return;
                    }
                    currentStack = inputStack.split(1);
                    if (inputStack.isEmpty()) {
                        inputSlots.setItem(0, ItemStack.EMPTY);
                    }
                    drainEnergy(effectiveEnergy, false);
                    sync();
                    progress = 0f;
                }
            } else {
                drainEnergy(effectiveEnergy, false);
                progress += getEffectiveSpeed();

                particleTicks = PARTICLE_TICKS;
                particleCount = (int) getSpeedMultiplier();

                isDirty = true;

                if (progress >= 1) {
                    if (!level.isClientSide) {
                        SieveMeshRegistryEntry sieveMesh = getSieveMesh();
                        if (sieveMesh != null) {
                            Collection<ItemStack> rewards = rollSieveRewards(level, currentStack, sieveMesh, getEffectiveLuck(), level.random);
                            for (ItemStack itemStack : rewards) {
                                if (!addItemToOutput(itemStack)) {
                                    overflowBuffer.add(itemStack);
                                }
                            }
                            if (ExNihilo.getInstance().doMeshesHaveDurability()) {
                                ItemStack meshStack = meshSlots.getItem(0);
                                if (!meshStack.isEmpty()) {
                                    if (meshStack.hurt(1, level.random, null)) {
                                        level.playSound(null, worldPosition, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 0.5f, 2.5f);
                                        meshStack.shrink(1);
                                        meshSlots.setItem(0, ItemStack.EMPTY);
                                    }
                                }
                            }
                        } else {
                            if (!addItemToOutput(currentStack)) {
                                overflowBuffer.add(currentStack);
                            }
                        }
                    }
                    progress = 0f;
                    currentStack = ItemStack.EMPTY;
                }
            }
        } else if (!overflowBuffer.isEmpty()) {
            if (addItemToOutput(overflowBuffer.get(0))) {
                overflowBuffer.remove(0);
            }
        }
    }

    protected abstract int drainEnergy(int energy, boolean simulate);

    private boolean addItemToOutput(ItemStack itemStack) {
        int firstEmptySlot = -1;
        for (int i = 0; i < outputSlots.getContainerSize(); i++) {
            ItemStack slotStack = outputSlots.getItem(i);
            if (slotStack.isEmpty()) {
                if (firstEmptySlot == -1) {
                    firstEmptySlot = i;
                }
            } else {
                if (slotStack.getCount() + itemStack.getCount() <= slotStack.getMaxStackSize() && ItemStack.isSameItemSameTags(slotStack, itemStack)) {
                    slotStack.grow(itemStack.getCount());
                    return true;
                }
            }
        }
        if (firstEmptySlot != -1) {
            outputSlots.setItem(firstEmptySlot, itemStack);
            return true;
        }
        return false;
    }

    public int getEffectiveEnergy() {
        return ExCompressumConfig.getActive().automation.autoSieveEnergy;
    }

    public float getEffectiveSpeed() {
        return (float) (ExCompressumConfig.getActive().automation.autoSieveSpeed * getSpeedMultiplier());
    }

    public float getEffectiveLuck() {
        ItemStack meshStack = meshSlots.getItem(0);
        if (!meshStack.isEmpty()) {
            return ExNihilo.getInstance().getMeshFortune(meshStack);
        }
        return 0f;
    }

    public boolean isSiftableWithMesh(ItemStack itemStack, @Nullable SieveMeshRegistryEntry sieveMesh) {
        return ExNihilo.isSiftableWithMesh(level, getBlockState(), itemStack, sieveMesh);
    }

    private boolean isMesh(ItemStack itemStack) {
        return SieveMeshRegistry.getEntry(itemStack) != null;
    }

    public Collection<ItemStack> rollSieveRewards(Level level, ItemStack itemStack, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
        return ExNihilo.rollSieveRewards(level, getBlockState(), itemStack, sieveMesh, luck, rand);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        currentStack = ItemStack.of(tag.getCompound("CurrentStack"));
        progress = tag.getFloat("Progress");
        if (tag.contains("CustomSkin")) {
            customSkin = NbtUtils.readGameProfile(tag.getCompound("CustomSkin"));
            if (customSkin != null) {
                ExCompressum.proxy.get().preloadSkin(customSkin);
            }
        }
        foodBoost = tag.getFloat("FoodBoost");
        foodBoostTicks = tag.getInt("FoodBoostTicks");
        particleTicks = tag.getInt("ParticleTicks");
        particleCount = tag.getInt("ParticleCount");
        backingContainer.deserialize(tag.getCompound("ItemHandler"));
        isDisabledByRedstone = tag.getBoolean("IsDisabledByRedstone");
        overflowBuffer.clear();
        for (final var overflowItem : tag.getList("OverflowBuffer", Tag.TAG_COMPOUND)) {
            overflowBuffer.add(ItemStack.of(((CompoundTag) overflowItem)));
        }

        if (tag.contains("MeshStack", Tag.TAG_COMPOUND)) {
            meshSlots.setItem(0, ItemStack.of(tag.getCompound("MeshStack")));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("CurrentStack", currentStack.save(new CompoundTag()));
        tag.putFloat("Progress", progress);
        if (customSkin != null) {
            CompoundTag customSkinTag = new CompoundTag();
            NbtUtils.writeGameProfile(customSkinTag, customSkin);
            tag.put("CustomSkin", customSkinTag);
        }
        tag.putFloat("FoodBoost", foodBoost);
        tag.putInt("FoodBoostTicks", foodBoostTicks);
        tag.putInt("ParticleTicks", particleTicks);
        tag.putInt("ParticleCount", particleCount);
        tag.put("ItemHandler", backingContainer.serialize());
        tag.putBoolean("IsDisabledByRedstone", isDisabledByRedstone());
        final var overflowList = new ListTag();
        for (ItemStack itemStack : overflowBuffer) {
            overflowList.add(itemStack.save(new CompoundTag()));
        }
        tag.put("OverflowBuffer", overflowList);
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag);
        ItemStack meshStack = meshSlots.getItem(0);
        tag.put("MeshStack", meshStack.save(new CompoundTag()));
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
        setChanged();
    }

    @Nullable
    public GameProfile getCustomSkin() {
        return customSkin;
    }

    private void grabProfile() {
        // I hope this doesn't break anything lol
        new Thread(() -> {
            try {
                if (!level.isClientSide && customSkin != null && !StringUtils.isEmpty(customSkin.getName())) {
                    if (!customSkin.isComplete() || !customSkin.getProperties().containsKey("textures")) {
                        Balm.getHooks().getServer().getProfileCache().get(customSkin.getName()).ifPresent(gameProfile -> {
                            Property property = Iterables.getFirst(gameProfile.getProperties().get("textures"), null);
                            if (property == null) {
                                gameProfile = Balm.getHooks().getServer().getSessionService().fillProfileProperties(gameProfile, true);
                            }
                            customSkin = gameProfile;
                            isDirty = true;
                            setChanged();
                        });
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
        ItemStack meshStack = meshSlots.getItem(0);
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

    @Nullable
    public SieveMeshRegistryEntry getSieveMesh() {
        ItemStack meshStack = meshSlots.getItem(0);
        if (!meshStack.isEmpty()) {
            return SieveMeshRegistry.getEntry(meshStack);
        }
        return null;
    }

    public ItemStack getMeshStack() {
        return meshSlots.getItem(0);
    }

    public boolean isCorrectSieveMesh() {
        ItemStack inputStack = inputSlots.getItem(0);
        SieveMeshRegistryEntry sieveMesh = getSieveMesh();
        return inputStack.isEmpty() || sieveMesh == null || isSiftableWithMesh(inputStack, sieveMesh);
    }

    public boolean shouldAnimate() {
        return !currentStack.isEmpty() && getEnergyStored() >= getEffectiveEnergy() && !isDisabledByRedstone();
    }

    public boolean isUgly() {
        BlockState state = getBlockState();
        if (state.hasProperty(ModBlockStateProperties.UGLY)) {
            return state.getValue(ModBlockStateProperties.UGLY);
        }
        return false;
    }

    public boolean isWaterlogged() {
        BlockState state = getBlockState();
        if (state.hasProperty(AutoSieveBaseBlock.WATERLOGGED)) {
            return state.getValue(AutoSieveBaseBlock.WATERLOGGED);
        }
        return false;
    }

    public Direction getFacing() {
        BlockState state = getBlockState();
        return state.hasProperty(AutoSieveBaseBlock.FACING) ? state.getValue(AutoSieveBaseBlock.FACING) : Direction.NORTH;
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
    public Component getDisplayName() {
        return Component.translatable("block.excompressum.auto_sieve");
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
        return new AutoSieveMenu(ModMenus.autoSieve.get(), windowId, inventory, this);
    }

    public SieveAnimationType getAnimationType() {
        return SieveAnimationType.DEFAULT; // TODO maybe make this based on skin now that mana sieve is gone
    }

    public void applyFoodBoost(FoodProperties food) {
        int foodBoostTicks = (int) food.getSaturationModifier() * 640;
        // If this food has no saturation (e.g. culinary construct has neither healing nor saturation in their Food object), just default to 1 saturation
        if (foodBoostTicks <= 0) {
            foodBoostTicks = 640;
        }
        float foodBoost = Math.max(1f, food.getNutrition() * 0.75f);
        setFoodBoost(foodBoostTicks, foodBoost);
    }

    public Container getBackingContainer() {
        return backingContainer;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    public ContainerData getContainerData() {
        return containerData;
    }
}
