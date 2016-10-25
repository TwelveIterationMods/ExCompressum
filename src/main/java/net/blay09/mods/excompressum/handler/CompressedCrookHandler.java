package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.item.ICompressedCrook;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.BlockLeaves;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;

public class CompressedCrookHandler {

	@SubscribeEvent
	public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
		if(!event.isSilkTouching()) {
			ItemStack heldItem = event.getHarvester().getHeldItemMainhand();
			if(heldItem != null && heldItem.getItem() instanceof ICompressedCrook && ((ICompressedCrook) heldItem.getItem()).canCrook(heldItem, event.getWorld(), event.getState(), event.getHarvester())) {
				int fortune = event.getFortuneLevel();

				Collection<ItemStack> rewards = ExRegistro.rollCrookRewards(event.getHarvester(), event.getState(), fortune, event.getWorld().rand);
				if(!rewards.isEmpty()) {
					event.setDropChance(1f);
					event.getDrops().clear();
					event.getDrops().addAll(rewards);
				}

				// Roll drops twice when breaking leaves
				if(event.getState().getBlock() instanceof BlockLeaves) {
					event.getDrops().addAll(event.getState().getBlock().getDrops(event.getWorld(), event.getPos(), event.getState(), fortune));
				}
			}
		}
	}

}
