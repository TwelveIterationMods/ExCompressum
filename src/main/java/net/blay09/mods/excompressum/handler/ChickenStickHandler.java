package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.config.ToolsConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.entity.EntityAngryChicken;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChickenStickHandler {

	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
		if(!ToolsConfig.allowChickenStickCreation) {
			return;
		}
		if(event.getTarget() instanceof EntityChicken && !((EntityChicken) event.getTarget()).isChild()) {
			ItemStack heldItem = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
			if(heldItem != null && heldItem.getItem() == Items.STICK) {
				event.getTarget().setDead();
				if(!event.getTarget().worldObj.isRemote) {
					heldItem.stackSize--;
					EntityAngryChicken angryChicken = new EntityAngryChicken(event.getTarget().worldObj);
					angryChicken.setLocationAndAngles(event.getTarget().posX, event.getTarget().posY, event.getTarget().posZ, event.getTarget().rotationYaw, event.getTarget().rotationPitch);
					event.getTarget().worldObj.spawnEntityInWorld(angryChicken);
					event.getTarget().worldObj.playSound(angryChicken.posX, angryChicken.posY, angryChicken.posZ, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.NEUTRAL, 1f, 0.5f, false);
				}
				event.setCanceled(true);
			}
		}
	}

}
