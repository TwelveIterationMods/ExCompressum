package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySiftable;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Collection;

public class HeavySieveTileEntity extends TileEntity implements ITickableTileEntity {

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

    public HeavySieveTileEntity() {
        super(ModTileEntities.heavySieve);
    }

    public boolean addSiftable(PlayerEntity player, ItemStack itemStack) {
        if (!currentStack.isEmpty() || meshStack.isEmpty() || !ExRegistries.getHeavySieveRegistry().isSiftable(getBlockState(), itemStack, getSieveMesh())) {
            return false;
        }
        currentStack = player.abilities.isCreativeMode ? ItemHandlerHelper.copyStackWithSize(itemStack, 1) : itemStack.split(1);
        progress = 0f;
        VanillaPacketHandler.sendTileEntityUpdate(this);
        return true;
    }

    @Override
    public void tick() {
        ticksSinceSync++;
        if (ticksSinceSync >= UPDATE_INTERVAL) {
            ticksSinceSync = 0;

            if (isDirty) {
                VanillaPacketHandler.sendTileEntityUpdate(this);
                isDirty = false;
            }
        }
        ticksSinceSecond++;
        if (ticksSinceSecond >= 20) {
            clicksSinceSecond = 0;
            ticksSinceSecond = 0;
        }

        if (particleTicks > 0) {
            particleTicks--;
            if (particleTicks <= 0) {
                particleCount = 0;
            }

            if (world.isRemote && !currentStack.isEmpty()) {
                final BlockState state = StupidUtils.getStateFromItemStack(currentStack);
                if (state != null) {
                    ExCompressum.proxy.spawnHeavySieveParticles(world, pos, state, particleCount);
                }
            }
        }
    }

    public boolean processContents(PlayerEntity player) {
        if (!currentStack.isEmpty() && !meshStack.isEmpty()) {
            if (player.abilities.isCreativeMode) {
                progress = 1f;
            } else {
                clicksSinceSecond++;
                if (clicksSinceSecond <= ExCompressumConfig.COMMON.heavySieveClicksPerSecond.get()) {
                    int efficiency = ExNihilo.getInstance().getMeshEfficiency(meshStack);
                    progress = Math.min(1f, progress + PROCESSING_INTERVAL * (1f + efficiency * EFFICIENCY_BOOST));
                }
            }

            if (progress >= 1f) {
                particleCount = 0;

                if (!world.isRemote) {
                    SieveMeshRegistryEntry sieveMesh = getSieveMesh();
                    if (sieveMesh != null) {
                        HeavySiftable siftable = ExRegistries.getHeavySieveRegistry().getSiftable(getBlockState(), currentStack, sieveMesh);
                        if(siftable != null) {
                            LootContext lootContext = HeavySieveRegistry.buildLootContext(((ServerWorld) world), currentStack, world.rand);
                            Collection<ItemStack> rewards = HeavySieveRegistry.rollSieveRewards(siftable, lootContext);
                            for (ItemStack itemStack : rewards) {
                                world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, itemStack));
                            }
                        }
                    } else {
                        world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, currentStack));
                    }
                    currentStack = ItemStack.EMPTY;
                    if (ExNihilo.getInstance().doMeshesHaveDurability() && sieveMesh != null) {
                        if (!sieveMesh.isHeavy()) {
                            getWorld().playSound(null, this.pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.5f, 2.5f);
                            meshStack = ItemStack.EMPTY;
                        } else {
                            meshStack.damageItem(1, player, it -> {
                                getWorld().playSound(null, this.pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.5f, 2.5f);
                                meshStack = ItemStack.EMPTY;
                            });
                        }
                    }
                    progress = 0f;
                    VanillaPacketHandler.sendTileEntityUpdate(this);
                }
            }

            particleTicks = PARTICLE_TICKS;
            particleCount++;
            isDirty = true;
            return true;
        }

        return false;
    }

    @Override
    public void read(BlockState state, CompoundNBT tagCompound) {
        super.read(state, tagCompound);
        currentStack = ItemStack.read(tagCompound.getCompound("Content"));
        meshStack = ItemStack.read(tagCompound.getCompound("Mesh"));
        progress = tagCompound.getFloat("Progress");
        particleTicks = tagCompound.getInt("ParticleTicks");
        particleCount = tagCompound.getInt("ParticleCount");
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        tagCompound.put("Content", currentStack.write(new CompoundNBT()));
        tagCompound.put("Mesh", meshStack.write(new CompoundNBT()));
        tagCompound.putFloat("Progress", progress);
        tagCompound.putInt("ParticleTicks", particleTicks);
        tagCompound.putInt("ParticleCount", particleCount);
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
    public void onDataPacket(NetworkManager networkManager, SUpdateTileEntityPacket packet) {
        read(getBlockState(), packet.getNbtCompound());
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
        VanillaPacketHandler.sendTileEntityUpdate(this);
    }
}
