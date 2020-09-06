package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID)
public class HammerHandler {

	/* TODO global loot modifiers @SubscribeEvent
	public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
		if(!event.isSilkTouching() && event.getHarvester() != null) {
			ItemStack heldItem = event.getHarvester().getHeldItemMainhand();
			if(!heldItem.isEmpty() && heldItem.getItem() instanceof IHammer && ((IHammer) heldItem.getItem()).canHammer(heldItem, event.getWorld(), event.getState(), event.getHarvester())) {
				int fortune = event.getFortuneLevel();
				event.setDropChance(1f);
				event.getDrops().clear();
				event.getDrops().addAll(ExRegistro.rollHammerRewards(event.getState(), ((IHammer) heldItem.getItem()).getHammerLevel(heldItem, event.getWorld(), event.getState(), event.getHarvester()), fortune, event.getWorld().rand));
			}
		}
	}*/

}
