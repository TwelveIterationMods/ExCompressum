package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID)
public class HammerSpeedHandler {

    @SubscribeEvent
    public static void asd(PlayerEvent.BreakSpeed event) {
        ItemStack heldItem = event.getPlayer().getHeldItem(Hand.MAIN_HAND);
        if (heldItem.getItem().getTags().contains(new ResourceLocation("excompressum", "hammer"))
                && BlockTags.LOGS.contains(event.getState().getBlock())) {
            float newSpeed = 2f;
            if(heldItem.getItem() instanceof ToolItem) {
                newSpeed = ((ToolItem) heldItem.getItem()).getTier().getEfficiency();
            }
            event.setNewSpeed(newSpeed);
        }
    }

}
