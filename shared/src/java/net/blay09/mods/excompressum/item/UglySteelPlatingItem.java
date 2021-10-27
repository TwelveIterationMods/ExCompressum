package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.IUglyfiable;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class UglySteelPlatingItem extends Item {

    public static final String name = "ugly_steel_plating";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public UglySteelPlatingItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Messages.lang("tooltip.ugly_steel_plating"));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        final Player player = context.getPlayer();
        final Level level = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final ItemStack heldItem = context.getItemInHand();
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof IUglyfiable) {
            if (((IUglyfiable) state.getBlock()).uglify(player, level, pos, state, context.getHand(), context.getClickedFace(), context.getClickLocation())) {
                if (player == null || !player.getAbilities().instabuild) {
                    heldItem.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

}
