package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.excompressum.block.BaitBlock;
import net.blay09.mods.excompressum.block.BaitType;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

import java.util.Collection;
import java.util.stream.Collectors;

public class BaitBlockEntity extends BalmBlockEntity {

    private static final int ENVIRONMENTAL_CHECK_INTERVAL = 20 * 10;
    private static final int MAX_BAITS_IN_AREA = 2;
    private static final int MIN_ENV_IN_AREA = 10;
    private static final int MAX_ANIMALS_IN_AREA = 2;
    private static final int SPAWN_CHECK_INTERVAL = 20;
    private static final int MIN_DISTANCE_NO_PLAYERS = 6;

    public BaitBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.bait.get(), pos, state);
    }

    private EnvironmentalConditionResult environmentStatus;
    private int ticksSinceEnvironmentalCheck;
    private int ticksSinceSpawnCheck;

    public static void serverTick(Level level, BlockPos pos, BlockState state, BaitBlockEntity blockEntity) {
        blockEntity.serverTick();
    }

    public void serverTick() {
        final BaitType baitType = getBaitType();

        ticksSinceEnvironmentalCheck++;

        ticksSinceSpawnCheck++;
        if (ticksSinceSpawnCheck >= SPAWN_CHECK_INTERVAL) {
            if (!level.isClientSide && level.random.nextFloat() <= baitType.getChance()) {
                if (checkSpawnConditions(true) == EnvironmentalConditionResult.CanSpawn) {
                    final float range = MIN_DISTANCE_NO_PLAYERS;
                    if (level.getEntitiesOfClass(Player.class,
                            new AABB(worldPosition.getX() - range,
                                    worldPosition.getY() - range,
                                    worldPosition.getZ() - range,
                                    worldPosition.getX() + range,
                                    worldPosition.getY() + range,
                                    worldPosition.getZ() + range)).isEmpty()) {
                        Entity entity = baitType.createEntity(level);
                        if (entity instanceof AgeableMob mob && level.random.nextFloat() <= ExCompressumConfig.getActive().baits.childBaitChance) {
                            mob.setAge(-24000);
                        }
                        if (entity instanceof Turtle turtle) {
                            turtle.setHomePos(worldPosition);
                        }
                        if (entity instanceof Llama llama) {
                            final var candidates = Llama.Variant.values();
                            llama.setVariant(candidates[level.random.nextInt(candidates.length)]);
                        }
                        entity.setPos(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
                        level.addFreshEntity(entity);
                        ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION,
                                worldPosition.getX() + 0.5,
                                worldPosition.getY() + 0.5,
                                worldPosition.getZ() + 0.5,
                                1,
                                0,
                                0,
                                0,
                                0.0);
                        level.playSound(null, worldPosition, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 1f, 1f);

                        level.removeBlock(worldPosition, false);
                    }
                }
            }
            ticksSinceSpawnCheck = 0;
        }
    }

    public EnvironmentalConditionResult checkSpawnConditions(boolean checkNow) {
        if (checkNow || ticksSinceEnvironmentalCheck > ENVIRONMENTAL_CHECK_INTERVAL) {
            BaitType baitType = getBaitType();
            Collection<BaitEnvironmentCondition> envBlocks = baitType.getEnvironmentConditions();
            final int range = 5;
            final int rangeVertical = 3;
            int countBait = 0;
            int countEnvBlocks = 0;
            boolean foundWater = false;
            for (int x = worldPosition.getX() - range; x < worldPosition.getX() + range; x++) {
                for (int y = worldPosition.getY() - rangeVertical; y < worldPosition.getY() + rangeVertical; y++) {
                    for (int z = worldPosition.getZ() - range; z < worldPosition.getZ() + range; z++) {
                        BlockPos testPos = new BlockPos(x, y, z);
                        BlockState blockState = level.getBlockState(testPos);
                        FluidState fluidState = level.getFluidState(testPos);
                        if (blockState.getBlock() instanceof BaitBlock) {
                            countBait++;
                        } else if (fluidState.getType() == Fluids.WATER || fluidState.getType() == Fluids.FLOWING_WATER) {
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
                environmentStatus = EnvironmentalConditionResult.NoWater;
            } else if (countBait > MAX_BAITS_IN_AREA) {
                environmentStatus = EnvironmentalConditionResult.NearbyBait;
            } else if (countEnvBlocks < MIN_ENV_IN_AREA) {
                String listOfBlocks = envBlocks.stream()
                        .map(BaitEnvironmentCondition::getDisplayName)
                        .map(Component::getString)
                        .collect(Collectors.joining(", "));
                environmentStatus = EnvironmentalConditionResult.wrongEnv(listOfBlocks);
            } else if (level.getEntitiesOfClass(Animal.class,
                    new AABB(worldPosition.getX() - range * 2,
                            worldPosition.getY() - rangeVertical,
                            worldPosition.getZ() - range * 2,
                            worldPosition.getX() + range * 2,
                            worldPosition.getY() + rangeVertical,
                            worldPosition.getZ() + range * 2)).size() > MAX_ANIMALS_IN_AREA) {
                environmentStatus = EnvironmentalConditionResult.NearbyAnimal;
            } else {
                environmentStatus = EnvironmentalConditionResult.CanSpawn;
            }
            ticksSinceEnvironmentalCheck = 0;
        }

        return environmentStatus;
    }

    public BaitType getBaitType() {
        return ((BaitBlock) getBlockState().getBlock()).getBaitType();
    }

}
