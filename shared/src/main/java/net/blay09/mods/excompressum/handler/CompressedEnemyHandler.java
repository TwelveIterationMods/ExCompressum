package net.blay09.mods.excompressum.handler;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.EntityAddedEvent;
import net.blay09.mods.balm.api.event.LivingDeathEvent;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CompressedEnemyHandler {

    private static final String COMPRESSED = "Compressed";
    private static final String NOCOMPRESS = "NoCompress";

    public static void initialize() {
        Balm.getEvents().onEvent(EntityAddedEvent.class, CompressedEnemyHandler::onEntityAdded);
        Balm.getEvents().onEvent(LivingDeathEvent.class, CompressedEnemyHandler::onLivingDeath);
    }

    public static void onEntityAdded(EntityAddedEvent event) {
        final var level = event.getLevel();
        final var entity = event.getEntity();
        if (!level.isClientSide && (entity instanceof Mob || entity instanceof Ghast)) {
            final var persistentData = Balm.getHooks().getPersistentData(entity);
            final var registryName = Balm.getRegistries().getKey(entity.getType());
            final var isWhitelist = !ExCompressumConfig.getActive().compressedMobs.compressedMobAllowedMobsIsBlacklist;
            if (registryName != null && ExCompressumConfig.getActive().compressedMobs.compressedMobAllowedMobs.contains(registryName.toString()) == isWhitelist) {
                final var baseTag = persistentData.getCompound(ExCompressum.MOD_ID);
                if (baseTag.contains(NOCOMPRESS) || baseTag.contains(COMPRESSED)) {
                    return;
                }

                if (level.random.nextFloat() < ExCompressumConfig.getActive().compressedMobs.compressedMobChance) {
                    entity.setCustomNameVisible(true);
                    entity.setCustomName(Component.translatable("tooltip.excompressum.compressed_mob", entity.getName()));
                    final var tagCompound = new CompoundTag();
                    tagCompound.putBoolean(COMPRESSED, true);
                    persistentData.put(ExCompressum.MOD_ID, tagCompound);
                } else {
                    final var tagCompound = new CompoundTag();
                    tagCompound.putBoolean(NOCOMPRESS, true);
                    persistentData.put(ExCompressum.MOD_ID, tagCompound);
                }
            }
        }
    }

    public static void onLivingDeath(LivingDeathEvent event) {
        final var entity = event.getEntity();
        final var level = entity.level();
        final var damageSource = event.getDamageSource();
        final var persistentData = Balm.getHooks().getPersistentData(entity);
        if (!level.isClientSide && persistentData.getCompound(ExCompressum.MOD_ID).contains(COMPRESSED)) {
            if (entity instanceof Mob || entity instanceof Ghast) {
                if (damageSource.getEntity() instanceof Player player && !Balm.getHooks().isFakePlayer(player)) {
                    if (StupidUtils.hasSilkTouchModifier((LivingEntity) damageSource.getEntity())) {
                        return;
                    }

                    @SuppressWarnings("unchecked") final EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) entity.getType();

                    for (int i = 0; i < ExCompressumConfig.getActive().compressedMobs.compressedMobSize; i++) {
                        final var newEntity = entityType.create(((ServerLevel) level), null, null, entity.blockPosition(), MobSpawnType.CONVERSION, false, false);
                        if (newEntity == null) {
                            return;
                        }

                        if (entity.isBaby()) {
                            if (newEntity instanceof Zombie zombie) {
                                zombie.setBaby(true);
                            } else if (newEntity instanceof AgeableMob mob && entity instanceof AgeableMob from) {
                                mob.setAge(from.getAge());
                            }
                        }

                        if (newEntity instanceof ZombifiedPiglin) {
                            newEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                        } else if (newEntity instanceof Skeleton) {
                            newEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                        } else if (newEntity instanceof WitherSkeleton) {
                            newEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                        }

                        final var tagCompound = new CompoundTag();
                        tagCompound.putBoolean(NOCOMPRESS, true);
                        Balm.getHooks().getPersistentData(newEntity).put(ExCompressum.MOD_ID, tagCompound);
                        newEntity.moveTo(entity.getX(), entity.getY() + 1, entity.getZ(), (float) Math.random(), (float) Math.random());
                        final var motion = 0.01;
                        newEntity.setDeltaMovement((level.random.nextGaussian() - 0.5) * motion, 0, (level.random.nextGaussian() - 0.5) * motion);
                        level.addFreshEntity(newEntity);
                    }
                }
            }
        }
    }

}
