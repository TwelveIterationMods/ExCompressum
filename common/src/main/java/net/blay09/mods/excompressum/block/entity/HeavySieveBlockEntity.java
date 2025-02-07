package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import org.jetbrains.annotations.Nullable;
import java.util.Collection;

public class HeavySieveBlockEntity extends BalmBlockEntity {

    private static final float PROCESSING_INTERVAL = 0.075f;
    private static final int UPDATE_INTERVAL = 5;
    private static final int PARTICLE_TICKS = 20;
    private static final float EFFICIENCY_BOOST = 0.25f;

    private ItemStack meshStack = ItemStack.EMPTY;
    private ItemStack currentStack = ItemStack.EMPTY;

    private float progress;
    private int clicksSinceSecond;

    private boolean isDirty;
    private int ticksSinceSync;
    private int ticksSinceSecond;

    private int particleTicks;
    private int particleCount;

    public HeavySieveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.heavySieve.get(), pos, state);
    }

    public boolean addSiftable(Player player, ItemStack itemStack) {
        if (!currentStack.isEmpty() || meshStack.isEmpty() || !ExRegistries.getHeavySieveRegistry().isSiftable(level, getBlockState(), itemStack, getSieveMesh())) {
            return false;
        }
        currentStack = player.getAbilities().instabuild ? ContainerUtils.copyStackWithSize(itemStack, 1) : itemStack.split(1);
        progress = 0f;
        sync();
        return true;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, HeavySieveBlockEntity blockEntity) {
        blockEntity.clientTick();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, HeavySieveBlockEntity blockEntity) {
        blockEntity.serverTick();
    }

    public void clientTick() {
        if (particleTicks > 0) {
            particleTicks--;
            if (particleTicks <= 0) {
                particleCount = 0;
            }

            if (!currentStack.isEmpty()) {
                final BlockState state = StupidUtils.getStateFromItemStack(currentStack);
                if (!state.isAir()) {
                    ExCompressum.proxy.get().spawnHeavySieveParticles(level, worldPosition, state, particleCount);
                }
            }
        }
    }

    public void serverTick() {
        ticksSinceSync++;
        if (ticksSinceSync >= UPDATE_INTERVAL) {
            ticksSinceSync = 0;

            if (isDirty) {
                sync();
                isDirty = false;
            }
        }
        ticksSinceSecond++;
        if (ticksSinceSecond >= 20) {
            clicksSinceSecond = 0;
            ticksSinceSecond = 0;
        }
    }

    public boolean processContents(Player player) {
        if (currentStack.isEmpty() || meshStack.isEmpty()) {
            return false;
        }

        if (player.getAbilities().instabuild) {
            progress = 1f;
        } else {
            clicksSinceSecond++;
            if (clicksSinceSecond <= ExCompressumConfig.getActive().automation.heavySieveClicksPerSecond) {
                int efficiency = ExNihilo.getInstance().getMeshEfficiency(meshStack);
                progress = Math.min(1f, progress + PROCESSING_INTERVAL * (1f + efficiency * EFFICIENCY_BOOST));
            }
        }

        if (progress >= 1f) {
            particleCount = 0;

            if (!level.isClientSide) {
                SieveMeshRegistryEntry sieveMesh = getSieveMesh();
                if (sieveMesh != null) {
                    LootContext lootContext = LootTableUtils.buildLootContext(((ServerLevel) level), currentStack);
                    Collection<ItemStack> rewards = HeavySieveRegistry.rollSieveRewards(level, lootContext, getBlockState(), sieveMesh, currentStack);
                    for (ItemStack itemStack : rewards) {
                        level.addFreshEntity(new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, itemStack));
                    }
                } else {
                    level.addFreshEntity(new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, currentStack));
                }
                currentStack = ItemStack.EMPTY;
                if (ExNihilo.getInstance().doMeshesHaveDurability() && sieveMesh != null) {
                    if (!sieveMesh.isHeavy()) {
                        level.playSound(null, worldPosition, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 0.5f, 2.5f);
                        meshStack = ItemStack.EMPTY;
                    } else {
                        meshStack.hurtAndBreak(1, player, it -> {
                            level.playSound(null, worldPosition, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 0.5f, 2.5f);
                            meshStack = ItemStack.EMPTY;
                        });
                    }
                }
                progress = 0f;
                sync();
            }
        }

        setChanged();

        particleTicks = PARTICLE_TICKS;
        particleCount++;
        isDirty = true;
        return true;

    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        currentStack = ItemStack.of(tagCompound.getCompound("Content"));
        meshStack = ItemStack.of(tagCompound.getCompound("Mesh"));
        progress = tagCompound.getFloat("Progress");
        particleTicks = tagCompound.getInt("ParticleTicks");
        particleCount = tagCompound.getInt("ParticleCount");
    }

    @Override
    public void saveAdditional(CompoundTag tagCompound) {
        super.saveAdditional(tagCompound);
        tagCompound.put("Content", currentStack.save(new CompoundTag()));
        tagCompound.put("Mesh", meshStack.save(new CompoundTag()));
        tagCompound.putFloat("Progress", progress);
        tagCompound.putInt("ParticleTicks", particleTicks);
        tagCompound.putInt("ParticleCount", particleCount);
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag);
    }

    public ItemStack getCurrentStack() {
        return currentStack;
    }

    public ItemStack getMeshStack() {
        return meshStack;
    }

    public float getProgress() {
        return progress;
    }

    @Nullable
    public SieveMeshRegistryEntry getSieveMesh() {
        if (!meshStack.isEmpty()) {
            return SieveMeshRegistry.getEntry(meshStack);
        }
        return null;
    }

    public void setMeshStack(ItemStack meshStack) {
        this.meshStack = meshStack;
        setChanged();
        sync();
    }
}
