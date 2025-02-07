package net.blay09.mods.excompressum.handler;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.BreakBlockEvent;
import net.blay09.mods.balm.api.event.PlayerAttackEvent;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.entity.AngryChickenEntity;
import net.blay09.mods.excompressum.entity.ModEntities;
import net.blay09.mods.excompressum.item.ChickenStickItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ChickenStickHandler {

    public static void initialize() {
        Balm.getEvents().onEvent(PlayerAttackEvent.class, ChickenStickHandler::onPlayerAttack);
        Balm.getEvents().onEvent(BreakBlockEvent.class, ChickenStickHandler::onBlockBreak);
    }

    public static void onPlayerAttack(PlayerAttackEvent event) {
        if (!ExCompressumConfig.getActive().tools.allowChickenStickCreation) {
            return;
        }

        if (event.getTarget() instanceof Chicken chicken && !chicken.isBaby()) {
            ItemStack heldItem = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
            if (!heldItem.isEmpty() && heldItem.getItem() == Items.STICK) {
                chicken.remove(Entity.RemovalReason.DISCARDED);

                Level level = chicken.level();
                if (!level.isClientSide) {
                    if (!event.getPlayer().getAbilities().instabuild) {
                        heldItem.shrink(1);
                    }

                    if (heldItem.isEmpty()) {
                        event.getPlayer().setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    }

                    AngryChickenEntity angryChicken = ModEntities.angryChicken.get().create(level);
                    angryChicken.moveTo(chicken.getX(), chicken.getY(), chicken.getZ(), chicken.getYRot(), chicken.getXRot());
                    angryChicken.absMoveTo(chicken.getX(), chicken.getY(), chicken.getZ(), chicken.getYRot(), chicken.getXRot());
                    angryChicken.setYHeadRot(chicken.yHeadRot);
                    level.addFreshEntity(angryChicken);
                    level.playSound(null, angryChicken.blockPosition(), SoundEvents.CHICKEN_HURT, SoundSource.HOSTILE, 1f, 0.5f);
                    level.playSound(null, angryChicken.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1f, 0.5f);
                    ((ServerLevel) level).sendParticles(ParticleTypes.ANGRY_VILLAGER,
                            angryChicken.getX(),
                            angryChicken.getY(),
                            angryChicken.getZ(),
                            200,
                            0.25f,
                            0.1f,
                            0.25f,
                            1f);
                }
                event.setCanceled(true);
            }
        }
    }

    public static void onBlockBreak(BreakBlockEvent event) {
        final var heldItem = event.getPlayer().getMainHandItem();
        if (heldItem.getItem() instanceof ChickenStickItem chickenStickItem) {
            final var level = event.getLevel();
            chickenStickItem.tryPlayChickenSound(level, event.getPos());

            if (level.getRandom().nextFloat() <= ExCompressumConfig.getActive().tools.chickenStickSpawnChance) {
                final var chicken = new Chicken(EntityType.CHICKEN, level);
                chicken.setPos(event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5);
                level.addFreshEntity(chicken);
            }
        }
    }

}
