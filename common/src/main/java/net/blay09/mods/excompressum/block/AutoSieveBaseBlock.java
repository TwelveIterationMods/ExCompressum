package net.blay09.mods.excompressum.block;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.common.CommonCapabilities;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.autosieveskin.AutoSieveSkinRegistry;
import net.blay09.mods.excompressum.registry.autosieveskin.WhitelistEntry;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AutoSieveBaseBlock extends BaseEntityBlock implements IUglyfiable {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty UGLY = ModBlockStateProperties.UGLY;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private ItemStack lastHoverStack = ItemStack.EMPTY;
    private String currentRandomName;

    protected AutoSieveBaseBlock(Properties properties) {
        super(properties.strength(2f));
        registerDefaultState(defaultBlockState().setValue(UGLY, false).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, UGLY, WATERLOGGED);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (!(level.getBlockEntity(pos) instanceof AbstractAutoSieveBlockEntity autoSieve)) {
            return InteractionResult.FAIL;
        }

        final var heldItem = itemStack.getItem();
        if (itemStack.has(DataComponents.FOOD) && ExCompressumConfig.getActive().automation.allowAutoSieveFoodSpeedBoosts) {
            final var food = itemStack.get(DataComponents.FOOD);
            if (autoSieve.getFoodBoost() <= 1f && food != null) {
                autoSieve.applyFoodBoost(food);
                int countBefore = itemStack.getCount();
                itemStack.consume(1, player);
                final var remainder = itemStack.get(DataComponents.USE_REMAINDER);
                if (remainder != null) {
                    final var remainderStack = remainder.convertIntoRemainder(itemStack,
                            countBefore,
                            player.hasInfiniteMaterials(),
                            player::handleExtraItemsCreatedOnUse);
                    player.setItemInHand(hand, remainderStack);
                }
                level.levelEvent(2005, pos, 0);
            }
            return InteractionResult.SUCCESS;
        } else if (heldItem == Items.NAME_TAG && itemStack.has(DataComponents.CUSTOM_NAME)) {
            autoSieve.setSkinProfile(new ResolvableProfile(Optional.of(itemStack.get(DataComponents.CUSTOM_NAME).getString()),
                    Optional.empty(),
                    new PropertyMap()));
            return InteractionResult.CONSUME;
        }

        return super.useItemOn(itemStack, state, level, pos, player, hand, blockHitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        if (!player.isShiftKeyDown() && level.getBlockEntity(pos) instanceof MenuProvider menuProvider) {
            Balm.getNetworking().openMenu(player, menuProvider);
            return InteractionResult.SUCCESS;
        }

        return super.useWithoutItem(state, level, pos, player, blockHitResult);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null) {
            Container container = Balm.getCapabilities().getCapability(blockEntity, CommonCapabilities.CONTAINER);
            if (container != null) {
                return AbstractContainerMenu.getRedstoneSignalFromContainer(container);
            }
        }

        return 0;
    }

    protected Component getSkinTooltip(String name) {
        return Component.translatable("tooltip.excompressum.auto_sieve", name).withStyle(ChatFormatting.GRAY);
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        final var profile = stack.get(DataComponents.PROFILE);
        if (profile != null) {
            tooltip.accept(getSkinTooltip(profile.gameProfile().getName()));
        } else {
            if (currentRandomName == null) {
                updateRandomSkinName();
            }

            tooltip.accept(getSkinTooltip(currentRandomName));
        }

        if (lastHoverStack != stack) {
            updateRandomSkinName();
            lastHoverStack = stack;
        }
    }

    private void updateRandomSkinName() {
        WhitelistEntry randomSkin = AutoSieveSkinRegistry.getRandomSkin();
        currentRandomName = randomSkin != null ? randomSkin.getName() : "Steve";
    }

    @Override
    public boolean uglify(@Nullable Player player, Level level, BlockPos pos, BlockState state, InteractionHand hand, Direction facing, Vec3 hitVec) {
        if (!state.getValue(UGLY)) {
            level.setBlock(pos, state.setValue(UGLY, true), 3);
            return true;
        }
        return false;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        final Direction facing = context.getHorizontalDirection().getOpposite();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(FACING, facing).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos facingPos, BlockState facingState, RandomSource randomSource) {
        if (state.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, scheduledTickAccess, pos, direction, facingPos, facingState, randomSource);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        super.animateTick(state, level, pos, rand);

        if (!state.getValue(UGLY) && state.getValue(WATERLOGGED)) {
            float posX = pos.getX() + 0.5f;
            float posY = (float) (pos.getY() + 0.6f + Math.random() * 0.25f);
            float posZ = pos.getZ() + 0.5f;
            float speed = 0.25f;
            float motionX = 0f * speed;
            float motionY = (float) ((0.5f + Math.random() - 0.5f) * speed);
            float motionZ = 0f * speed;
            level.addParticle(ParticleTypes.BUBBLE, posX, posY, posZ, motionX, motionY, motionZ);
        }
    }

    public abstract BlockEntityType<?> getBlockEntityType();

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide
                ? createTickerHelper(type, (BlockEntityType<AbstractAutoSieveBlockEntity>) getBlockEntityType(), AbstractAutoSieveBlockEntity::clientTick)
                : createTickerHelper(type, (BlockEntityType<AbstractAutoSieveBlockEntity>) getBlockEntityType(), AbstractAutoSieveBlockEntity::serverTick);
    }
}
