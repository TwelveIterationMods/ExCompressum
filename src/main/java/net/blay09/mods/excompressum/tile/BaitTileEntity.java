package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.block.BaitBlock;
import net.blay09.mods.excompressum.block.BaitType;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Collection;

public class BaitTileEntity extends TileEntity implements ITickableTileEntity {

    private static final int ENVIRONMENTAL_CHECK_INTERVAL = 20 * 10;
    private static final int MAX_BAITS_IN_AREA = 2;
    private static final int MIN_ENV_IN_AREA = 10;
    private static final int MAX_ANIMALS_IN_AREA = 2;
    private static final int SPAWN_CHECK_INTERVAL = 20;
    private static final int MIN_DISTANCE_NO_PLAYERS = 6;

    public BaitTileEntity() {
        super(ModTileEntities.bait);
    }

    private EnvironmentalCondition environmentStatus;
    private int ticksSinceEnvironmentalCheck;
    private int ticksSinceSpawnCheck;

    @Override
    public void tick() {
        final BaitType baitType = getBaitType();

        ticksSinceEnvironmentalCheck++;

        ticksSinceSpawnCheck++;
        if (ticksSinceSpawnCheck >= SPAWN_CHECK_INTERVAL) {
            if (!world.isRemote && world.rand.nextFloat() <= baitType.getChance()) {
                if (checkSpawnConditions(true) == EnvironmentalCondition.CanSpawn) {
                    final float range = MIN_DISTANCE_NO_PLAYERS;
                    if (world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range)).isEmpty()) {
                        Entity entityLiving = baitType.createEntity(world);
                        if (entityLiving instanceof AgeableEntity && world.rand.nextFloat() <= ExCompressumConfig.COMMON.childBaitChance.get()) {
                            ((AgeableEntity) entityLiving).setGrowingAge(-24000);
                        }
                        entityLiving.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                        world.addEntity(entityLiving);
                        ((ServerWorld) world).spawnParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, 0, 0, 0, 0.0);
                        world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1f, 1f);

                        world.removeBlock(pos, false);
                    }
                }
            }
            ticksSinceSpawnCheck = 0;
        }
    }

    public EnvironmentalCondition checkSpawnConditions(boolean checkNow) {
        if (checkNow || ticksSinceEnvironmentalCheck > ENVIRONMENTAL_CHECK_INTERVAL) {
            BaitType baitType = getBaitType();
            Collection<BaitEnvironmentCondition> envBlocks = baitType.getEnvironmentConditions();
            final int range = 5;
            final int rangeVertical = 3;
            int countBait = 0;
            int countEnvBlocks = 0;
            boolean foundWater = false;
            for (int x = pos.getX() - range; x < pos.getX() + range; x++) {
                for (int y = pos.getY() - rangeVertical; y < pos.getY() + rangeVertical; y++) {
                    for (int z = pos.getZ() - range; z < pos.getZ() + range; z++) {
                        BlockPos testPos = new BlockPos(x, y, z);
                        BlockState blockState = world.getBlockState(testPos);
                        FluidState fluidState = world.getFluidState(testPos);
                        if (blockState.getBlock() instanceof BaitBlock) {
                            countBait++;
                        } else if (fluidState.getFluid() == Fluids.WATER || fluidState.getFluid() == Fluids.FLOWING_WATER) {
                            foundWater = true;
                        }

                        for (BaitEnvironmentCondition envBlock : envBlocks) {
                            if (envBlock.test(blockState, fluidState)) {
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

    public BaitType getBaitType() {
        return ((BaitBlock) getBlockState().getBlock()).getBaitType();
    }

}
