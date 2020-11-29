package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
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
        if (!event.getWorld().isRemote && (event.getEntity() instanceof MobEntity || event.getEntity() instanceof GhastEntity)) {
            ResourceLocation registryName = event.getEntity().getType().getRegistryName();
            boolean isWhitelist = !ExCompressumConfig.COMMON.compressedMobAllowedMobsIsBlacklist.get();
            if (registryName != null && ExCompressumConfig.COMMON.compressedMobAllowedMobs.get().contains(registryName.toString()) == isWhitelist) {
                CompoundNBT baseTag = event.getEntity().getPersistentData().getCompound(ExCompressum.MOD_ID);
                if (baseTag.contains(NOCOMPRESS) || baseTag.contains(COMPRESSED)) {
                    return;
                }

                if (event.getEntity().world.rand.nextFloat() < ExCompressumConfig.COMMON.compressedMobChance.get()) {
                    event.getEntity().setCustomNameVisible(true);
                    event.getEntity().setCustomName(new TranslationTextComponent("excompressum.compressedMob", event.getEntity().getName()));
                    CompoundNBT tagCompound = new CompoundNBT();
                    tagCompound.putBoolean(COMPRESSED, true);
                    event.getEntity().getPersistentData().put(ExCompressum.MOD_ID, tagCompound);
                } else {
                    CompoundNBT tagCompound = new CompoundNBT();
                    tagCompound.putBoolean(NOCOMPRESS, true);
                    event.getEntity().getPersistentData().put(ExCompressum.MOD_ID, tagCompound);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (!event.getEntity().world.isRemote && event.getEntity().getPersistentData().getCompound(ExCompressum.MOD_ID).contains(COMPRESSED)) {
            if (event.getEntity() instanceof MobEntity || event.getEntity() instanceof GhastEntity) {
                if (event.getSource().getTrueSource() instanceof PlayerEntity && !(event.getSource().getTrueSource() instanceof FakePlayer)) {
                    if (StupidUtils.hasSilkTouchModifier((LivingEntity) event.getSource().getTrueSource())) {
                        return;
                    }

                    @SuppressWarnings("unchecked") final EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) event.getEntity().getType();

                    for (int i = 0; i < ExCompressumConfig.COMMON.compressedMobSize.get(); i++) {
                        LivingEntity entity = entityType.create(((ServerWorld) event.getEntity().world), null, null, null, event.getEntity().getPosition(), SpawnReason.CONVERSION, false, false);
                        if (entity == null) {
                            return;
                        }

                        if (((LivingEntity) event.getEntity()).isChild()) {
                            if (entity instanceof ZombieEntity) {
                                ((ZombieEntity) entity).setChild(true);
                            } else if (entity instanceof AgeableEntity && event.getEntity() instanceof AgeableEntity) {
                                ((AgeableEntity) entity).setGrowingAge(((AgeableEntity) event.getEntity()).getGrowingAge());
                            }
                        }

                        if (entity instanceof ZombifiedPiglinEntity) {
                            entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                        } else if (entity instanceof SkeletonEntity) {
                            entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
                        } else if (entity instanceof WitherSkeletonEntity) {
                            entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
                        }

                        CompoundNBT tagCompound = new CompoundNBT();
                        tagCompound.putBoolean(NOCOMPRESS, true);
                        entity.getPersistentData().put(ExCompressum.MOD_ID, tagCompound);
                        entity.setLocationAndAngles(event.getEntity().getPosX(), event.getEntity().getPosY() + 1, event.getEntity().getPosZ(), (float) Math.random(), (float) Math.random());
                        double motion = 0.01;
                        entity.setMotion((event.getEntity().world.rand.nextGaussian() - 0.5) * motion, 0, (event.getEntity().world.rand.nextGaussian() - 0.5) * motion);
                        event.getEntity().world.addEntity(entity);
                    }
                }
            }
        }
    }

}
