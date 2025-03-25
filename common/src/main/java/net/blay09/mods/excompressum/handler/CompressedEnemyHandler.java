package net.blay09.mods.excompressum.handler;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.EntityAddedEvent;
import net.blay09.mods.balm.api.event.LivingDeathEvent;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.tag.ModEntityTags;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
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
        if (!level.isClientSide && entity instanceof Mob) {
            final var persistentData = Balm.getHooks().getPersistentData(entity);
            if (entity.getType().is(ModEntityTags.COMPRESSABLE)) {
                final var modData = persistentData.getCompound(ExCompressum.MOD_ID);
                final var noCompress = modData.flatMap(it -> it.getBoolean(NOCOMPRESS)).orElse(false);
                final var compressed = modData.flatMap(it -> it.getBoolean(COMPRESSED)).orElse(false);
                if (noCompress || compressed) {
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
        if (!level.isClientSide && persistentData.getCompound(ExCompressum.MOD_ID).flatMap(it -> it.getBoolean(COMPRESSED)).orElse(false)) {
            if (entity instanceof Mob) {
                if (damageSource.getEntity() instanceof Player player && !Balm.getHooks().isFakePlayer(player)) {
                    if (StupidUtils.hasSilkTouchModifier((LivingEntity) damageSource.getEntity())) {
                        return;
                    }

                    @SuppressWarnings("unchecked") final EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) entity.getType();

                    for (int i = 0; i < ExCompressumConfig.getActive().compressedMobs.compressedMobSize; i++) {
                        final var newEntity = entityType.create((ServerLevel) level, null, entity.blockPosition(), EntitySpawnReason.CONVERSION, false, false);
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

                        switch (newEntity) {
                            case ZombifiedPiglin ignored -> newEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                            case Skeleton ignored -> newEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                            case WitherSkeleton ignored -> newEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                            default -> {
                            }
                        }

                        final var tagCompound = new CompoundTag();
                        tagCompound.putBoolean(NOCOMPRESS, true);
                        Balm.getHooks().getPersistentData(newEntity).put(ExCompressum.MOD_ID, tagCompound);
                        newEntity.snapTo(entity.getX(), entity.getY() + 1, entity.getZ(), (float) Math.random(), (float) Math.random());
                        final var motion = 0.01;
                        newEntity.setDeltaMovement((level.random.nextGaussian() - 0.5) * motion, 0, (level.random.nextGaussian() - 0.5) * motion);
                        level.addFreshEntity(newEntity);
                    }
                }
            }
        }
    }

}
