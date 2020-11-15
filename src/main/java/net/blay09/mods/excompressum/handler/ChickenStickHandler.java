package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.entity.AngryChickenEntity;
import net.blay09.mods.excompressum.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID)
public class ChickenStickHandler {

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if (!ExCompressumConfig.COMMON.allowChickenStickCreation.get()) {
            return;
        }

        Entity chicken = event.getTarget();
        if (chicken instanceof ChickenEntity && !((ChickenEntity) chicken).isChild()) {
            ItemStack heldItem = event.getPlayer().getHeldItem(Hand.MAIN_HAND);
            if (!heldItem.isEmpty() && heldItem.getItem() == Items.STICK) {
                chicken.remove();

                World world = chicken.world;
                if (!world.isRemote) {
                    if (!event.getPlayer().abilities.isCreativeMode) {
                        heldItem.shrink(1);
                    }

                    if (heldItem.isEmpty()) {
                        event.getPlayer().setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                    }

                    AngryChickenEntity angryChicken = ModEntities.angryChicken.create(world);
                    angryChicken.setLocationAndAngles(chicken.getPosX(), chicken.getPosY(), chicken.getPosZ(), chicken.rotationYaw, chicken.rotationPitch);
                    angryChicken.setPositionAndRotation(chicken.getPosX(), chicken.getPosY(), chicken.getPosZ(), chicken.rotationYaw, chicken.rotationPitch);
                    angryChicken.setRotationYawHead(((ChickenEntity) chicken).rotationYawHead);
                    world.addEntity(angryChicken);
                    world.playSound(null, angryChicken.getPosition(), SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.HOSTILE, 1f, 0.5f);
                    world.playSound(null, angryChicken.getPosition(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 1f, 0.5f);
                    ((ServerWorld) world).spawnParticle(ParticleTypes.ANGRY_VILLAGER, angryChicken.getPosX(), angryChicken.getPosY(), angryChicken.getPosZ(), 200, 0.25f, 0.1f, 0.25f, 1f);
                }
                event.setCanceled(true);
            }
        }
    }

	/* TODO chicken stick needs to play chicken sounds / spawn chickens
	public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
					((ItemChickenStick) heldItem.getItem()).playChickenSound(event.getWorld(), event.getPos());
					if (event.getWorld().rand.nextFloat() <= ModConfig.tools.chickenStickSpawnChance) {
						EntityChicken entityChicken = new EntityChicken(event.getWorld());
						entityChicken.setPosition(event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5);
						event.getWorld().spawnEntity(entityChicken);
					}
	}*/

}
