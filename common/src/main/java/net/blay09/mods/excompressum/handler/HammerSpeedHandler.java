package net.blay09.mods.excompressum.handler;

import net.blay09.mods.balm.platform.event.callback.BlockCallback;
import net.blay09.mods.excompressum.tag.ModBlockTags;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class HammerSpeedHandler {

    public static void initialize() {
        BlockCallback.DigSpeed.EVENT.register(HammerSpeedHandler::onDigSpeed);
    }

    public static float onDigSpeed(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player, float speed) {
        final var heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if ((heldItem.is(ModItemTags.HAMMERS) || heldItem.is(ModItemTags.COMPRESSED_HAMMERS)) && state.is(ModBlockTags.MINEABLE_WITH_HAMMER)) {
            final var tool = heldItem.get(DataComponents.TOOL);
            if (tool != null) {
                final var bestSpeed = tool.rules().stream().map(it -> it.speed().orElse(0f)).max(Float::compare);
                return bestSpeed.orElse(tool.defaultMiningSpeed());
            }
        }

        return speed;
    }

}
