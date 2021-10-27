package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.ExCompressum;
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
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID)
public class ChickenStickHandler {

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if (!ExCompressumConfig.COMMON.allowChickenStickCreation.get()) {
            return;
        }

        if (event.getTarget() instanceof Chicken chicken && !chicken.isBaby()) {
            ItemStack heldItem = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
            if (!heldItem.isEmpty() && heldItem.getItem() == Items.STICK) {
                chicken.remove(Entity.RemovalReason.DISCARDED);

                Level level = chicken.level;
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
                    ((ServerLevel) level).sendParticles(ParticleTypes.ANGRY_VILLAGER, angryChicken.getX(), angryChicken.getY(), angryChicken.getZ(), 200, 0.25f, 0.1f, 0.25f, 1f);
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        ItemStack heldItem = event.getPlayer().getMainHandItem();
        if(heldItem.getItem() instanceof ChickenStickItem chickenStickItem) {
            chickenStickItem.tryPlayChickenSound(event.getWorld(), event.getPos());

            if (event.getWorld().getRandom().nextFloat() <= ExCompressumConfig.COMMON.chickenStickSpawnChance.get()) {
                Chicken chicken = new Chicken(EntityType.CHICKEN, ((Level) event.getWorld()));
                chicken.setPos(event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5);
                event.getWorld().addFreshEntity(chicken);
            }
        }
    }

}
