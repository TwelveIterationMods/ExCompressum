package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.item.IHammer;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
public class HammerHandler {

	@SubscribeEvent
	public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
		if(!event.isSilkTouching() && event.getHarvester() != null) {
			ItemStack heldItem = event.getHarvester().getHeldItemMainhand();
			if(!heldItem.isEmpty() && heldItem.getItem() instanceof IHammer && ((IHammer) heldItem.getItem()).canHammer(heldItem, event.getWorld(), event.getState(), event.getHarvester())) {
				int fortune = event.getFortuneLevel();
				event.setDropChance(1f);
				event.getDrops().clear();
				event.getDrops().addAll(ExRegistro.rollHammerRewards(event.getState(), ((IHammer) heldItem.getItem()).getHammerLevel(heldItem, event.getWorld(), event.getState(), event.getHarvester()), fortune, event.getWorld().rand));
			}
		}
	}

}
