package net.blay09.mods.excompressum.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.entity.EntityAngryChicken;
import net.blay09.mods.excompressum.entity.EntityItemNoCombine;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class ChickenStickHandler {

	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
		if(ExCompressum.createChickenStickChance == 0f) {
			return;
		}
		if(event.target instanceof EntityChicken && !((EntityChicken) event.target).isChild()) {
			if(event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == Items.stick) {
				event.target.setDead();
				if(!event.target.worldObj.isRemote) {
					if (Math.random() <= ExCompressum.createChickenStickChance) {
						event.target.worldObj.playSoundAtEntity(event.target, "mob.chicken.say", 1f, 2f);
						event.target.worldObj.playSoundAtEntity(event.target, "mob.chicken.hurt", 1f, 2f);
						for(int i = 0; i < 3; i++) {
							EntityItem entityItem = new EntityItemNoCombine(event.target.worldObj, event.target.posX, event.target.posY, event.target.posZ, new ItemStack(Items.feather));
							float motion = 0.05f;
							entityItem.delayBeforeCanPickup = 20;
							entityItem.motionX = event.target.worldObj.rand.nextGaussian() * motion;
							entityItem.motionY = event.target.worldObj.rand.nextGaussian() * motion + 0.2F;
							entityItem.motionZ = event.target.worldObj.rand.nextGaussian() * motion;
							event.target.worldObj.spawnEntityInWorld(entityItem);
						}
						if (event.entityPlayer.getHeldItem().stackSize == 1) {
							event.entityPlayer.inventory.mainInventory[event.entityPlayer.inventory.currentItem] = new ItemStack(ModItems.chickenStick);
						} else {
							event.entityPlayer.getHeldItem().stackSize--;
							if (!event.entityPlayer.inventory.addItemStackToInventory(new ItemStack(ModItems.chickenStick))) {
								event.entityPlayer.dropPlayerItemWithRandomChoice(new ItemStack(ModItems.chickenStick), false);
							}
						}
					} else {
						EntityAngryChicken angryChicken = new EntityAngryChicken(event.target.worldObj);
						angryChicken.setLocationAndAngles(event.target.posX, event.target.posY, event.target.posZ, event.target.rotationYaw, event.target.rotationPitch);
						event.target.worldObj.spawnEntityInWorld(angryChicken);
						event.target.worldObj.playSoundAtEntity(angryChicken, "mob.chicken.hurt", 1f, 0.5f);
					}
				}
				event.setCanceled(true);
			}
		}
	}

}
