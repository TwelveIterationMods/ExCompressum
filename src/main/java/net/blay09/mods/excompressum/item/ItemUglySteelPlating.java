package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.IUglyfiable;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemUglySteelPlating extends Item {

    public static final String name = "ugly_steel_plating";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ItemUglySteelPlating() {
        setCreativeTab(ExCompressum.creativeTab);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(I18n.format("tooltip.excompressum:ugly_steel_plating"));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        final PlayerEntity player = context.getPlayer();
        final World world = context.getWorld();
        final BlockPos pos = context.getPos();
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof IUglyfiable) {
            if (((IUglyfiable) state.getBlock()).uglify(player, world, pos, state, context.getHand(), context.getFace(), context.getHitVec())) {
                if (!player.abilities.isCreativeMode) {
                    player.getHeldItem(context.getHand()).shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

}
