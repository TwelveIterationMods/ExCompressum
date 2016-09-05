package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.entity.EntityAngryChicken;
import net.blay09.mods.excompressum.entity.EntityItemNoCombine;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChickenStickHandler {

	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
		if(ExCompressumConfig.createChickenStickChance == 0f) {
			return;
		}
		if(event.getTarget() instanceof EntityChicken && !((EntityChicken) event.getTarget()).isChild()) {
			ItemStack heldItem = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
			if(heldItem != null && heldItem.getItem() == Items.STICK) {
				event.getTarget().setDead();
				if(!event.getTarget().worldObj.isRemote) {
					if (Math.random() <= ExCompressumConfig.createChickenStickChance) {
						event.getTarget().worldObj.playSound(event.getTarget().posX, event.getTarget().posY, event.getTarget().posZ, SoundEvents.ENTITY_CHICKEN_AMBIENT, SoundCategory.NEUTRAL, 1f, 2f, false); // TODO is .ambient == .say?
						event.getTarget().worldObj.playSound(event.getTarget().posX, event.getTarget().posY, event.getTarget().posZ, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.NEUTRAL, 1f, 2f, false);

						for(int i = 0; i < 3; i++) {
							EntityItem entityItem = new EntityItemNoCombine(event.getTarget().worldObj, event.getTarget().posX, event.getTarget().posY, event.getTarget().posZ, new ItemStack(Items.FEATHER));
							float motion = 0.05f;
							entityItem.setPickupDelay(20);
							entityItem.motionX = event.getTarget().worldObj.rand.nextGaussian() * motion;
							entityItem.motionY = event.getTarget().worldObj.rand.nextGaussian() * motion + 0.2F;
							entityItem.motionZ = event.getTarget().worldObj.rand.nextGaussian() * motion;
							event.getTarget().worldObj.spawnEntityInWorld(entityItem);
						}
						if (heldItem.stackSize == 1) {
							event.getEntityPlayer().inventory.mainInventory[event.getEntityPlayer().inventory.currentItem] = new ItemStack(ModItems.chickenStick);
						} else {
							heldItem.stackSize--;
							if (!event.getEntityPlayer().inventory.addItemStackToInventory(new ItemStack(ModItems.chickenStick))) {
								event.getEntityPlayer().dropItem(new ItemStack(ModItems.chickenStick), false);
							}
						}
					} else {
						EntityAngryChicken angryChicken = new EntityAngryChicken(event.getTarget().worldObj);
						angryChicken.setLocationAndAngles(event.getTarget().posX, event.getTarget().posY, event.getTarget().posZ, event.getTarget().rotationYaw, event.getTarget().rotationPitch);
						event.getTarget().worldObj.spawnEntityInWorld(angryChicken);
						event.getTarget().worldObj.playSound(angryChicken.posX, angryChicken.posY, angryChicken.posZ, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.NEUTRAL, 1f, 0.5f, false);
					}
				}
				event.setCanceled(true);
			}
		}
	}

}
