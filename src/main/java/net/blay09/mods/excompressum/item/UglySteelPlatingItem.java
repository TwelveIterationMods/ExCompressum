package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.IUglyfiable;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class UglySteelPlatingItem extends Item {

    public static final String name = "ugly_steel_plating";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public UglySteelPlatingItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(Messages.lang("tooltip.ugly_steel_plating"));
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        final PlayerEntity player = context.getPlayer();
        final World world = context.getWorld();
        final BlockPos pos = context.getPos();
        final ItemStack heldItem = context.getItem();
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof IUglyfiable) {
            if (((IUglyfiable) state.getBlock()).uglify(player, world, pos, state, context.getHand(), context.getFace(), context.getHitVec())) {
                if (player == null || !player.abilities.isCreativeMode) {
                    heldItem.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

}
