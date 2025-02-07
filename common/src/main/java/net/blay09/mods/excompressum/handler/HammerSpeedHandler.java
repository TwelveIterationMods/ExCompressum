package net.blay09.mods.excompressum.handler;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.DigSpeedEvent;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HammerSpeedHandler {

    public static void initialize() {
        Balm.getEvents().onEvent(DigSpeedEvent.class, HammerSpeedHandler::onDigSpeed);
    }

    public static void onDigSpeed(DigSpeedEvent event) {
        ItemStack heldItem = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
        final var targetItem = StupidUtils.getItemStackFromState(event.getState());
        Level level = event.getPlayer().level();
        if ((heldItem.is(ModItemTags.HAMMERS) || heldItem.is(ModItemTags.COMPRESSED_HAMMERS)) && (ExRegistries.getHammerRegistry().isHammerable(level, targetItem) || ExRegistries.getCompressedHammerRegistry().isHammerable(level, targetItem))) {
            float newSpeed = 2f;
            if (heldItem.getItem() instanceof DiggerItem) {
                newSpeed = ((DiggerItem) heldItem.getItem()).getTier().getSpeed();
            }
            event.setSpeedOverride(newSpeed);
        }
    }

}
