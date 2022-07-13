package net.blay09.mods.excompressum.compat.botania;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EvolvedOrechidBlock extends FlowerBlock implements EntityBlock {

    public EvolvedOrechidBlock(BlockBehaviour.Properties properties) {
        super(MobEffects.DIG_SPEED, 10, properties);
    }

    @Nonnull
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext ctx) {
        Vec3 shift = state.getOffset(blockGetter, pos);
        return SHAPE.move(shift.x, shift.y, shift.z);
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return BotaniaCompat.RED_STRING_RELAY.equals(state.getBlock().getRegistryName()) || state.getBlock() == Blocks.MYCELIUM || super.canSustainPlant(state, world, pos, facing, plantable);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int event, int param) {
        super.triggerEvent(state, level, pos, event, param);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(event, param);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EvolvedOrechidBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack itemStack) {
        ((TileEntitySpecialFlower) level.getBlockEntity(pos)).setPlacedBy(level, pos, state, entity, itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter blockGetter, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Messages.styledLang("tooltip.evolvedOrechid.functionalFlower", Style.EMPTY.applyFormats(ChatFormatting.BLUE, ChatFormatting.ITALIC)));
        tooltip.add(Messages.styledLang("tooltip.evolvedOrechid.tagline", Style.EMPTY.applyFormats(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
    }

}
