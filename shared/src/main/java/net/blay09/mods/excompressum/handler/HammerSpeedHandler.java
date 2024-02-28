package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;

public class HammerSpeedHandler {

    public static void initialize() {
        // TODO Balm.getEvents().onEvent(BreakSpeedEvent.class, HammerSpeedHandler::onBreakSpeed);
    }

    /*public static void onBreakSpeed(BreakSpeedEvent event) {
        ItemStack heldItem = event.getEntity().getItemInHand(InteractionHand.MAIN_HAND);
        if (heldItem.is(ModTags.HAMMERS) && event.getState().is(BlockTags.LOGS)) {
            float newSpeed = 2f;
            if (heldItem.getItem() instanceof DiggerItem) {
                newSpeed = ((DiggerItem) heldItem.getItem()).getTier().getSpeed();
            }
            event.setNewSpeed(newSpeed);
        }
    }*/

}
