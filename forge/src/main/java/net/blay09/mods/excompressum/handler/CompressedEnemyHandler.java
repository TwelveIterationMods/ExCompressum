package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CompressedEnemyHandler {

    private static final String COMPRESSED = "Compressed";
    private static final String NOCOMPRESS = "NoCompress";

    @SubscribeEvent
    public static void onSpawnEntity(EntityJoinWorldEvent event) {
        if (!event.getWorld().isClientSide && (event.getEntity() instanceof Mob || event.getEntity() instanceof Ghast)) {
            ResourceLocation registryName = event.getEntity().getType().getRegistryName();
            boolean isWhitelist = !ExCompressumConfig.getActive().compressedMobs.compressedMobAllowedMobsIsBlacklist;
            if (registryName != null && ExCompressumConfig.getActive().compressedMobs.compressedMobAllowedMobs.contains(registryName.toString()) == isWhitelist) {
                CompoundTag baseTag = event.getEntity().getPersistentData().getCompound(ExCompressum.MOD_ID);
                if (baseTag.contains(NOCOMPRESS) || baseTag.contains(COMPRESSED)) {
                    return;
                }

                if (event.getEntity().level.random.nextFloat() < ExCompressumConfig.getActive().compressedMobs.compressedMobChance) {
                    event.getEntity().setCustomNameVisible(true);
                    event.getEntity().setCustomName(new TranslatableComponent("excompressum.compressedMob", event.getEntity().getName()));
                    CompoundTag tagCompound = new CompoundTag();
                    tagCompound.putBoolean(COMPRESSED, true);
                    event.getEntity().getPersistentData().put(ExCompressum.MOD_ID, tagCompound);
                } else {
                    CompoundTag tagCompound = new CompoundTag();
                    tagCompound.putBoolean(NOCOMPRESS, true);
                    event.getEntity().getPersistentData().put(ExCompressum.MOD_ID, tagCompound);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (!event.getEntity().level.isClientSide && event.getEntity().getPersistentData().getCompound(ExCompressum.MOD_ID).contains(COMPRESSED)) {
            if (event.getEntity() instanceof Mob || event.getEntity() instanceof Ghast) {
                if (event.getSource().getEntity() instanceof Player && !(event.getSource().getEntity() instanceof FakePlayer)) {
                    if (StupidUtils.hasSilkTouchModifier((LivingEntity) event.getSource().getEntity())) {
                        return;
                    }

                    @SuppressWarnings("unchecked") final EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) event.getEntity().getType();

                    for (int i = 0; i < ExCompressumConfig.getActive().compressedMobs.compressedMobSize; i++) {
                        LivingEntity entity = entityType.create(((ServerLevel) event.getEntity().level), null, null, null, event.getEntity().blockPosition(), MobSpawnType.CONVERSION, false, false);
                        if (entity == null) {
                            return;
                        }

                        if (((LivingEntity) event.getEntity()).isBaby()) {
                            if (entity instanceof Zombie zombie) {
                                zombie.setBaby(true);
                            } else if (entity instanceof AgeableMob mob && event.getEntity() instanceof AgeableMob from) {
                                mob.setAge(from.getAge());
                            }
                        }

                        if (entity instanceof ZombifiedPiglin) {
                            entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                        } else if (entity instanceof Skeleton) {
                            entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                        } else if (entity instanceof WitherSkeleton) {
                            entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                        }

                        CompoundTag tagCompound = new CompoundTag();
                        tagCompound.putBoolean(NOCOMPRESS, true);
                        entity.getPersistentData().put(ExCompressum.MOD_ID, tagCompound);
                        entity.moveTo(event.getEntity().getX(), event.getEntity().getY() + 1, event.getEntity().getZ(), (float) Math.random(), (float) Math.random());
                        double motion = 0.01;
                        entity.setDeltaMovement((event.getEntity().level.random.nextGaussian() - 0.5) * motion, 0, (event.getEntity().level.random.nextGaussian() - 0.5) * motion);
                        event.getEntity().level.addFreshEntity(entity);
                    }
                }
            }
        }
    }

}
