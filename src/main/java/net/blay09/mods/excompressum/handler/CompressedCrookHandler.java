package net.blay09.mods.excompressum.handler;

public class CompressedCrookHandler {

	/* TODO global loot modifiers @SubscribeEvent
	public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
		if(!event.isSilkTouching() && event.getHarvester() != null) {
			ItemStack heldItem = event.getHarvester().getHeldItemMainhand();
			if(!heldItem.isEmpty() && heldItem.getItem() instanceof ICompressedCrook && ((ICompressedCrook) heldItem.getItem()).canCrook(heldItem, event.getWorld(), event.getState(), event.getHarvester())) {
				int fortune = event.getFortuneLevel();

				Collection<ItemStack> rewards = ExRegistro.rollCrookRewards(event.getHarvester(), event.getState(), fortune, event.getWorld().rand);
				if(!rewards.isEmpty()) {
					event.setDropChance(1f);
					event.getDrops().clear();
					event.getDrops().addAll(rewards);
				}

				// Roll drops twice when breaking leaves
				if(event.getState().getBlock() instanceof BlockLeaves) {
					NonNullList<ItemStack> list = NonNullList.create();
					event.getState().getBlock().getDrops(list, event.getWorld(), event.getPos(), event.getState(), fortune);
					event.getDrops().addAll(list);
				}
			}
		}
	}*/

}
