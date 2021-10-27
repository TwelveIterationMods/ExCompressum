package net.blay09.mods.excompressum.compat.botania;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EvolvedOrechidBlock extends FlowerBlock implements IWandable, IWandHUD {

    public EvolvedOrechidBlock(BlockBehaviour.Properties properties) {
        super(Effects.HASTE, 10, properties);
    }

    @Nonnull
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext ctx) {
        Vec3 shift = state.getOffset(blockGetter, pos);
        return SHAPE.withOffset(shift.x, shift.y, shift.z);
    }

    @Override
    protected boolean isValidGround(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return BotaniaCompat.RED_STRING_RELAY.equals(state.getBlock().getRegistryName()) || state.getBlock() == Blocks.MYCELIUM || super.isValidGround(state, blockGetter, pos);
    }

    @Override
    public boolean eventReceived(BlockState state, Level level, BlockPos pos, int event, int param) {
        super.eventReceived(state, level, pos, event, param);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.receiveClientEvent(event, param);
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new EvolvedOrechidBlockEntity();
    }

    @Override
    public void onBlockPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        ((TileEntitySpecialFlower) level.getBlockEntity(pos)).onBlockPlacedBy(level, pos, state, entity, stack);
    }

    @Override
    public void renderHUD(PoseStack poseStack, Minecraft minecraft, Level world, BlockPos pos) {
        ((TileEntitySpecialFlower) world.getBlockEntity(pos)).renderHUD(poseStack, minecraft);
    }

    @Override
    public boolean onUsedByWand(Player playerEntity, ItemStack itemStack, Level world, BlockPos pos, Direction direction) {
        return ((TileEntitySpecialFlower) world.getBlockEntity(pos)).onWanded(playerEntity, itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter blockGetter, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Messages.styledLang("tooltip.evolvedOrechid.functionalFlower", Style.EMPTY.createStyleFromFormattings(ChatFormatting.BLUE, ChatFormatting.ITALIC)));
        tooltip.add(Messages.styledLang("tooltip.evolvedOrechid.tagline", Style.EMPTY.createStyleFromFormattings(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
    }
}
