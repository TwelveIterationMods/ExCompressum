package net.blay09.mods.excompressum.compat.botania;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EvolvedOrechidBlock extends FlowerBlock implements IWandable, IWandHUD {

    public EvolvedOrechidBlock(Properties properties) {
        super(Effects.HASTE, 10, properties);
    }

    @Nonnull
    public VoxelShape getShape(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, ISelectionContext ctx) {
        Vector3d shift = state.getOffset(world, pos);
        return SHAPE.withOffset(shift.x, shift.y, shift.z);
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return BotaniaCompat.RED_STRING_RELAY.equals(state.getBlock().getRegistryName()) || state.getBlock() == Blocks.MYCELIUM || super.isValidGround(state, worldIn, pos);
    }

    @Override
    public boolean eventReceived(BlockState state, World world, BlockPos pos, int event, int param) {
        super.eventReceived(state, world, pos, event, param);
        TileEntity tileentity = world.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(event, param);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EvolvedOrechidTileEntity();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        ((TileEntitySpecialFlower) world.getTileEntity(pos)).onBlockPlacedBy(world, pos, state, entity, stack);
    }

    @Override
    public void renderHUD(MatrixStack matrixStack, Minecraft minecraft, World world, BlockPos pos) {
        ((TileEntitySpecialFlower) world.getTileEntity(pos)).renderHUD(matrixStack, minecraft);
    }

    @Override
    public boolean onUsedByWand(PlayerEntity playerEntity, ItemStack itemStack, World world, BlockPos pos, Direction direction) {
        return ((TileEntitySpecialFlower) world.getTileEntity(pos)).onWanded(playerEntity, itemStack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(Messages.styledLang("tooltip.evolvedOrechid.functionalFlower", Style.EMPTY.createStyleFromFormattings(TextFormatting.BLUE, TextFormatting.ITALIC)));
        tooltip.add(Messages.styledLang("tooltip.evolvedOrechid.tagline", Style.EMPTY.createStyleFromFormattings(TextFormatting.GRAY, TextFormatting.ITALIC)));
    }
}
