package net.blay09.mods.excompressum.handler;

public class CompressedHammerHandler {

	/* TODO global loot modifiers @SubscribeEvent
	public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
		if(!event.isSilkTouching() && event.getHarvester() != null) {
			ItemStack heldItem = event.getHarvester().getHeldItemMainhand();
			if(!heldItem.isEmpty() && heldItem.getItem() instanceof ICompressedHammer && ((ICompressedHammer) heldItem.getItem()).canHammer(heldItem, event.getWorld(), event.getState(), event.getHarvester())) {
				int fortune = event.getFortuneLevel();
				List<ItemStack> rewards = Lists.newArrayList(CompressedHammerRegistry.rollHammerRewards(event.getState(), fortune, event.getWorld().rand));
				rewards.addAll(ExRegistro.rollHammerRewards(event.getState(), ((ICompressedHammer) heldItem.getItem()).getHammerLevel(heldItem, event.getWorld(), event.getState(), event.getHarvester()), fortune, event.getWorld().rand));
				if(!rewards.isEmpty()) {
					event.setDropChance(1f);
					event.getDrops().clear();
					event.getDrops().addAll(rewards);
				}
			}
		}
	}*/

}
