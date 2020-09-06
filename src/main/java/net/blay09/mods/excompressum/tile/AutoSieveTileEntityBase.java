package net.blay09.mods.excompressum.tile;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.block.BlockAutoSieveBase;
import net.blay09.mods.excompressum.client.render.ParticleSieve;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.utils.DefaultItemHandler;
import net.blay09.mods.excompressum.utils.ItemHandlerAutomation;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.blay09.mods.excompressum.utils.SubItemHandler;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

public abstract class AutoSieveTileEntityBase extends BaseTileEntity implements ITickable {

    private static final int UPDATE_INTERVAL = 20;
    private static final int PARTICLE_TICKS = 30;

    private final DefaultItemHandler itemHandler = new DefaultItemHandler(this, 22) {
        @Override
        public boolean isItemValid(int slot, ItemStack itemStack) {
            if (inputSlots.isInside(slot)) {
                return isSiftable(itemStack);
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
        if (!isDisabledByRedstone() && getEnergyStored(null) >= effectiveEnergy) {
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
                if (getEffectiveSpeed() < 0f) {
                    System.out.println("WUT");
                }
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
                            if (ExRegistro.doMeshesHaveDurability()) {
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

            spawnParticles();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnParticles() {
        if (currentStack.isEmpty() || ModConfig.client.disableParticles || isUgly()) {
            return;
        }

        int particleSetting = Minecraft.getInstance().gameSettings.particleSetting;
        if (particleSetting == 2) { // Minimal Particle Settings
            return;
        }

        int actualParticleCount = particleCount;
        if (particleSetting == 1) { // Decreased Particle Settings
            float half = actualParticleCount / 2f;
            if (half < 1f && Math.random() <= 0.5f) {
                return;
            }

            actualParticleCount = (int) Math.ceil(half);
        }

        int metadata = getBlockMetadata();
        BlockState state = StupidUtils.getStateFromItemStack(currentStack);
        if (state != null) {
            for (int i = 0; i < actualParticleCount; i++) {
                double particleX = 0.5 + world.rand.nextFloat() * 0.4 - 0.2;
                double particleZ = 0.5 + world.rand.nextFloat() * 0.4 - 0.2;
                switch (Direction.getFront(metadata)) {
                    case WEST:
                        particleZ -= 0.125f;
                        break;
                    case EAST:
                        particleZ += 0.125f;
                        break;
                    case NORTH:
                        particleX += 0.125f;
                        break;
                    case SOUTH:
                        particleX -= 0.125f;
                        break;
                    default:
                        break;
                }
                Minecraft.getInstance().effectRenderer.addEffect(new ParticleSieve(world, pos, particleX, 0.2, particleZ, 0.5f, state));
            }
        }
    }

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
        return ModConfig.automation.autoSieveEnergy;
    }

    public float getEffectiveSpeed() {
        return ModConfig.automation.autoSieveSpeed * getSpeedMultiplier();
    }

    public float getEffectiveLuck() {
        ItemStack meshStack = meshSlots.getStackInSlot(0);
        if (!meshStack.isEmpty()) {
            return ExRegistro.getMeshFortune(meshStack);
        }
        return 0f;
    }

    public boolean isSiftable(ItemStack itemStack) {
        return ExRegistro.isSiftable(itemStack);
    }

    public boolean isSiftableWithMesh(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh) {
        return ExRegistro.isSiftableWithMesh(itemStack, sieveMesh);
    }

    public boolean isMesh(ItemStack itemStack) {
        return SieveMeshRegistry.getEntry(itemStack) != null;
    }

    public Collection<ItemStack> rollSieveRewards(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
        return ExRegistro.rollSieveRewards(itemStack, sieveMesh, luck, rand);
    }

    @Override
    protected boolean hasUpdatePacket() {
        return true;
    }

    @Override
    protected void readFromNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        currentStack = new ItemStack(tagCompound.getCompound("CurrentStack"));
        progress = tagCompound.getFloat("Progress");
        if (tagCompound.contains("CustomSkin")) {
            customSkin = NBTUtil.readGameProfileFromNBT(tagCompound.getCompound("CustomSkin"));
            if (customSkin != null) {
                ExCompressum.proxy.preloadSkin(customSkin);
            }
        }
        foodBoost = tagCompound.getFloat("FoodBoost");
        foodBoostTicks = tagCompound.getInt("FoodBoostTicks");
        particleTicks = tagCompound.getInt("ParticleTicks");
        particleCount = tagCompound.getInt("ParticleCount");
        if (isSync) {
            meshSlots.setStackInSlot(0, new ItemStack(tagCompound.getCompound("MeshStack")));
        } else {
            itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        }
        isDisabledByRedstone = tagCompound.getBoolean("IsDisabledByRedstone");
    }

    @Override
    protected void writeToNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        tagCompound.put("CurrentStack", currentStack.writeToNBT(new CompoundNBT()));
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
            tagCompound.put("MeshStack", meshStack.writeToNBT(new CompoundNBT()));
        } else {
            tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        }
        tagCompound.putBoolean("IsDisabledByRedstone", isDisabledByRedstone());
    }

    public float getEnergyPercentage() {
        return (float) getEnergyStored(null) / (float) getMaxEnergyStored();
    }

    public boolean isProcessing() {
        return progress > 0f;
    }

    public float getProgress() {
        return progress;
    }

    public ItemStack getCurrentStack() {
        return currentStack;
    }

    public void setCustomSkin(GameProfile customSkin) {
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
            boost += EFFICIENCY_BOOST * ExRegistro.getMeshEfficiency(meshStack);
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
    public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandlerAutomation;
        }
        return super.getCapability(capability, facing);
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
        return !currentStack.isEmpty() && getEnergyStored(null) >= getEffectiveEnergy() && !isDisabledByRedstone();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState) {
        cachedState = null;
        return oldState.getBlock() != newState.getBlock();
    }

    public boolean isUgly() {
        if (cachedState == null) {
            cachedState = world.getBlockState(pos);
        }
        if (cachedState.getBlock() instanceof BlockAutoSieveBase) {
            return cachedState.get(BlockAutoSieveBase.UGLY);
        }
        return false;
    }

    public Direction getFacing() {
        if (cachedState == null) {
            cachedState = world.getBlockState(pos);
        }
        if (cachedState.getBlock() instanceof BlockAutoSieveBase) {
            return cachedState.get(BlockAutoSieveBase.FACING);
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
}
