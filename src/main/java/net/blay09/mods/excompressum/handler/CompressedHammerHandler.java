package net.blay09.mods.excompressum.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.ICompressedHammer;
import net.blay09.mods.excompressum.registry.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;

import java.util.Collection;

public class CompressedHammerHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onHarvestBlock(BlockEvent.HarvestDropsEvent event) {
        if (event.world.isRemote || event.harvester == null || event.isSilkTouching) {
            return;
        }
        ItemStack heldItem = event.harvester.getHeldItem();
        if (heldItem != null && heldItem.getItem() == ExCompressum.chickenStick) {
            if (!ChickenStickRegistry.isValidBlock(event.block, event.blockMetadata)) {
                return;
            }
            Collection<Smashable> rewards = HammerRegistry.getRewards(event.block, event.blockMetadata);
            if (rewards == null || rewards.isEmpty()) {
                return;
            }
            event.drops.clear();
            event.dropChance = 1f;
            for (Smashable reward : rewards) {
                if (event.world.rand.nextFloat() <= reward.chance + (reward.luckMultiplier * event.fortuneLevel)) {
                    event.drops.add(new ItemStack(reward.item, 1, reward.meta));
                }
            }
            return;
        }
        Collection<Smashable> rewards = CompressedHammerRegistry.getRewards(event.block, event.blockMetadata);
        if (rewards == null || rewards.isEmpty()) {
            return;
        }
        if (isCompressedHammer(heldItem)) {
            event.drops.clear();
            event.dropChance = 1f;
            for (Smashable reward : rewards) {
                if (event.world.rand.nextFloat() <= reward.chance + (reward.luckMultiplier * event.fortuneLevel)) {
                    event.drops.add(new ItemStack(reward.item, 1, reward.meta));
                }
            }
        }
    }

    public boolean isCompressedHammer(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() instanceof ICompressedHammer && ((ICompressedHammer) itemStack.getItem()).isCompressedHammer(itemStack);
    }

}
