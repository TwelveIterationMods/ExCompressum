package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.entity.AngryChickenEntity;
import net.blay09.mods.excompressum.entity.ModEntities;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID)
public class ChickenStickHandler {

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if (!ExCompressumConfig.tools.allowChickenStickCreation) {
            return;
        }
        if (event.getTarget() instanceof ChickenEntity && !((ChickenEntity) event.getTarget()).isChild()) {
            ItemStack heldItem = event.getPlayer().getHeldItem(Hand.MAIN_HAND);
            if (!heldItem.isEmpty() && heldItem.getItem() == Items.STICK) {
                event.getTarget().remove();
                if (!event.getTarget().world.isRemote) {
                    heldItem.shrink(1);
                    if (heldItem.isEmpty()) {
                        event.getPlayer().setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                    }

                    AngryChickenEntity angryChicken = ModEntities.angryChicken.create(event.getTarget().world);
                    angryChicken.setLocationAndAngles(event.getTarget().getPosX(), event.getTarget().getPosY(), event.getTarget().getPosZ(), event.getTarget().rotationYaw, event.getTarget().rotationPitch);
                    event.getTarget().world.addEntity(angryChicken);
                    event.getTarget().world.playSound(angryChicken.getPosX(), angryChicken.getPosY(), angryChicken.getPosZ(), SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.NEUTRAL, 1f, 0.5f, false);
                }
                event.setCanceled(true);
            }
        }
    }

	/* TODO global loot modifiers @SubscribeEvent
	public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
		if (event.getHarvester() != null) {
			ItemStack heldItem = event.getHarvester().getHeldItemMainhand();
			if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemChickenStick && ChickenStickRegistry.isHammerable(event.getState())) {
				final int fortune = 0;
				List<ItemStack> rewards = Lists.newArrayList(CompressedHammerRegistry.rollHammerRewards(event.getState(), fortune, event.getWorld().rand));
				rewards.addAll(ExRegistro.rollHammerRewards(event.getState(), 0, fortune, event.getWorld().rand));
				if (!rewards.isEmpty()) {
					event.setDropChance(1f);
					event.getDrops().clear();
					event.getDrops().addAll(rewards);

					((ItemChickenStick) heldItem.getItem()).playChickenSound(event.getWorld(), event.getPos());
					if (event.getWorld().rand.nextFloat() <= ModConfig.tools.chickenStickSpawnChance) {
						EntityChicken entityChicken = new EntityChicken(event.getWorld());
						entityChicken.setPosition(event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5);
						event.getWorld().spawnEntity(entityChicken);
					}
				}
			}
		}
	}*/

}
