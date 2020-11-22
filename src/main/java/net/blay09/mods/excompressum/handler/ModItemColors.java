package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.BaitBlock;
import net.blay09.mods.excompressum.block.BaitType;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItemColors {

    @SubscribeEvent
    public static void initItemColors(ColorHandlerEvent.Item event) {
        if (ModBlocks.baits != null) {
            event.getItemColors().register((itemStack, tintIndex) -> {
                Block block = Block.getBlockFromItem(itemStack.getItem());
                if(block instanceof BaitBlock) {
                    BaitType baitType = ((BaitBlock) block).getBaitType();
                    return baitType.getItemColor(tintIndex);
                }

                return 0;
            }, ModBlocks.baits);
        }
    }

}
