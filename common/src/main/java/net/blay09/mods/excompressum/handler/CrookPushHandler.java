package net.blay09.mods.excompressum.handler;

import net.blay09.mods.balm.platform.event.EventHandling;
import net.blay09.mods.balm.platform.event.callback.PlayerCallback;
import net.blay09.mods.excompressum.item.CompressedCrookItem;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.world.InteractionHand;

public class CrookPushHandler {
    public static void initialize() {
        PlayerCallback.Attack.EVENT.register((player, target) -> {
            final var itemStack = player.getMainHandItem();
            if (itemStack.is(ModItems.compressedCrook)) {
                CompressedCrookItem.pushEntity(itemStack, player, target, InteractionHand.MAIN_HAND);
                return EventHandling.CANCEL;
            }
            return EventHandling.RESUME;
        });
    }
}
