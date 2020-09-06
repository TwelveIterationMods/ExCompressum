package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.block.BaitType;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.config.ModConfig;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Collection;

public class BaitTileEntity extends TileEntity implements ITickable {

    private static final int ENVIRONMENTAL_CHECK_INTERVAL = 20 * 10;
    private static final int MAX_BAITS_IN_AREA = 2;
    private static final int MIN_ENV_IN_AREA = 10;
    private static final int MAX_ANIMALS_IN_AREA = 2;
    private static final int SPAWN_CHECK_INTERVAL = 20;
    private static final int MIN_DISTANCE_NO_PLAYERS = 6;

    public enum EnvironmentalCondition {
        CanSpawn("info.excompressum:baitCanSpawn"),
        NearbyBait("info.excompressum:baitNearbyBait"),
        WrongEnv("info.excompressum:baitWrongEnv"),
        NearbyAnimal("info.excompressum:baitNearbyAnimal"),
        NoWater("info.excompressum:baitNoWater");

        public final String langKey;

        EnvironmentalCondition(String langKey) {
            this.langKey = langKey;
        }
    }

    public static class BaitBlockCondition {
        private final BlockState state;
        private final boolean isWildcard;

        public BaitBlockCondition(BlockState state, boolean isWildcard) {
            this.state = state;
            this.isWildcard = isWildcard;
        }

        public BlockState getState() {
            return state;
        }

        public boolean isWildcard() {
            return isWildcard;
        }
    }

    private ItemStack renderItemMain = ItemStack.EMPTY;
    private ItemStack renderItemSub = ItemStack.EMPTY;
    private EnvironmentalCondition environmentStatus;
    private int ticksSinceEnvironmentalCheck;
    private int ticksSinceSpawnCheck;

    @Override
    public void update() {
        if (renderItemMain.isEmpty()) {
            renderItemMain = getBaitDisplayItem(getBlockMetadata(), 0);
        }
        if (renderItemSub.isEmpty()) {
            renderItemSub = getBaitDisplayItem(getBlockMetadata(), 1);
        }

        ticksSinceEnvironmentalCheck++;

        ticksSinceSpawnCheck++;
        if (ticksSinceSpawnCheck >= SPAWN_CHECK_INTERVAL) {
            int metadata = getBlockMetadata();
            if (!world.isRemote && world.rand.nextFloat() <= getBaitChance(metadata)) {
                if (checkSpawnConditions(true) == EnvironmentalCondition.CanSpawn) {
                    final float range = MIN_DISTANCE_NO_PLAYERS;
                    if (world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range)).isEmpty()) {
                        LivingEntity entityLiving = getBaitEntityLiving(world, metadata);
                        if (entityLiving != null) {
                            if (entityLiving instanceof AgeableEntity && world.rand.nextFloat() <= ModConfig.baits.childChance) {
                                ((AgeableEntity) entityLiving).setGrowingAge(-24000);
                            }
                            entityLiving.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                            world.addEntity(entityLiving);
                            ((ServerWorld) world).spawnParticle(ParticleTypes.EXPLOSION_LARGE, false, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, 0, 0, 0, 0.0);
                            world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1f, 1f);
                        }
                        world.removeBlock(pos);
                    }
                }
            }
            ticksSinceSpawnCheck = 0;
        }
    }

    private static ItemStack getBaitDisplayItem(int metadata, int i) {
        BaitType type = BaitType.fromId(metadata);
        if (type != null) {
            return i == 0 ? type.getDisplayItemFirst() : type.getDisplayItemSecond();
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    private static LivingEntity getBaitEntityLiving(World world, int metadata) {
        BaitType type = BaitType.fromId(metadata);
        return type != null ? type.createEntity(world) : null;
    }

    private float getBaitChance(int metadata) {
        BaitType type = BaitType.fromId(metadata);
        return type != null ? type.getChance() : 0f;
    }

    public ItemStack getRenderItem(int i) {
        return i == 0 ? renderItemMain : renderItemSub;
    }

    public EnvironmentalCondition checkSpawnConditions(boolean checkNow) {
        if (checkNow || ticksSinceEnvironmentalCheck > ENVIRONMENTAL_CHECK_INTERVAL) {
            int metadata = getBlockMetadata();
            BaitType baitType = BaitType.fromId(metadata);
            Collection<BaitBlockCondition> envBlocks = baitType != null ? baitType.getEnvironmentConditions() : null;
            if (envBlocks == null) {
                throw new RuntimeException("The bait you placed (metadata " + metadata + ") was not properly defined - this should not happen - please report to the mod developer!");
            }
            final int range = 5;
            final int rangeVertical = 3;
            int countBait = 0;
            int countEnvBlocks = 0;
            boolean foundWater = false;
            for (int x = pos.getX() - range; x < pos.getX() + range; x++) {
                for (int y = pos.getY() - rangeVertical; y < pos.getY() + rangeVertical; y++) {
                    for (int z = pos.getZ() - range; z < pos.getZ() + range; z++) {
                        BlockPos testPos = new BlockPos(x, y, z);
                        BlockState state = world.getBlockState(testPos);
                        if (state.getBlock() == ModBlocks.bait) {
                            countBait++;
                        } else if (state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER) {
                            foundWater = true;
                        }

                        for (BaitBlockCondition envBlock : envBlocks) {
                            // There's something really weird happening where something here is null even though it's impossible.
                            // If that happens, just satisfy the condition no matter what to prevent a crash and prevent baits from not working.
                            // This will hopefully fix itself when baits become data-pack driven in 1.13.
                            if (envBlock == null || envBlock.getState() == null) {
                                countEnvBlocks++;
								continue;
                            }

                            if (state.getBlock() == envBlock.getState().getBlock()) {
                                if (!envBlock.isWildcard()) {
                                    int meta = state.getBlock().getMetaFromState(state);
                                    int envMeta = envBlock.getState().getBlock().getMetaFromState(envBlock.getState());
                                    if (meta != envMeta) {
                                        continue;
                                    }
                                }
                                countEnvBlocks++;
                            }
                        }
                    }
                }
            }
            if (!foundWater) {
                environmentStatus = EnvironmentalCondition.NoWater;
            } else if (countBait > MAX_BAITS_IN_AREA) {
                environmentStatus = EnvironmentalCondition.NearbyBait;
            } else if (countEnvBlocks < MIN_ENV_IN_AREA) {
                environmentStatus = EnvironmentalCondition.WrongEnv;
            } else if (world.getEntitiesWithinAABB(AnimalEntity.class, new AxisAlignedBB(pos.getX() - range * 2, pos.getY() - rangeVertical, pos.getZ() - range * 2, pos.getX() + range * 2, pos.getY() + rangeVertical, pos.getZ() + range * 2)).size() > MAX_ANIMALS_IN_AREA) {
                environmentStatus = EnvironmentalCondition.NearbyAnimal;
            } else {
                environmentStatus = EnvironmentalCondition.CanSpawn;
            }
            ticksSinceEnvironmentalCheck = 0;
        }

        return environmentStatus;
    }

}
