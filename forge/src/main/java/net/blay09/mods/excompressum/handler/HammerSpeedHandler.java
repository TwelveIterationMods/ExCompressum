package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID)
public class HammerSpeedHandler {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        ItemStack heldItem = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
        if (heldItem.is(ModTags.HAMMERS) && event.getState().is(BlockTags.LOGS)) {
            float newSpeed = 2f;
            if (heldItem.getItem() instanceof DiggerItem) {
                newSpeed = ((DiggerItem) heldItem.getItem()).getTier().getSpeed();
            }
            event.setNewSpeed(newSpeed);
        }
    }

}
