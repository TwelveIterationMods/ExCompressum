package net.blay09.mods.excompressum.block;

import com.mojang.authlib.GameProfile;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.autosieveskin.AutoSieveSkinRegistry;
import net.blay09.mods.excompressum.registry.autosieveskin.WhitelistEntry;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class AutoSieveBaseBlock extends BaseEntityBlock implements IUglyfiable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.getItemInHand(hand).is(ModItems.uglySteelPlating)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            ItemStack heldItemStack = player.getItemInHand(hand);
            if (!heldItemStack.isEmpty()) {
                AbstractAutoSieveBlockEntity tileEntity = (AbstractAutoSieveBlockEntity) level.getBlockEntity(pos);
                if (tileEntity != null) {
                    final Item heldItem = heldItemStack.getItem();
                    if (heldItem.isEdible() && ExCompressumConfig.getActive().automation.allowAutoSieveFoodSpeedBoosts) {
                        final FoodProperties food = Objects.requireNonNull(heldItem.getFoodProperties());
                        if (tileEntity.getFoodBoost() <= 1f) {
                            tileEntity.applyFoodBoost(food);
                            if (!player.getAbilities().instabuild) {
                                ItemStack returnStack = heldItem.finishUsingItem(heldItemStack, level, player); // TODO player here should be FakePlayer
                                if (returnStack != heldItemStack) {
                                    player.setItemInHand(hand, returnStack);
                                }
                            }
                            level.levelEvent(2005, pos, 0);
                        }
                        return InteractionResult.SUCCESS;
                    } else if (heldItem == Items.NAME_TAG && heldItemStack.hasCustomHoverName()) {
                        tileEntity.setCustomSkin(new GameProfile(null, heldItemStack.getDisplayName().getString()));
                        if (!player.getAbilities().instabuild) {
                            heldItemStack.shrink(1);
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            if (!player.isShiftKeyDown()) {
                final BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof MenuProvider menuProvider) {
                    Balm.getNetworking().openGui(player, menuProvider);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractAutoSieveBlockEntity autoSieve && newState.getBlock() != state.getBlock()) {
            Container container = autoSieve.getBackingContainer();
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack itemStack = container.getItem(i);
                if (!itemStack.isEmpty()) {
                    ItemEntity entityItem = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                    double motion = 0.05;
                    entityItem.setDeltaMovement(level.random.nextGaussian() * motion, 0.2, level.random.nextGaussian() * motion);
                    level.addFreshEntity(entityItem);
                }
            }
            ItemStack currentStack = autoSieve.getCurrentStack();
            if (!currentStack.isEmpty()) {
                ItemEntity entityItem = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), currentStack);
                double motion = 0.05;
                entityItem.setDeltaMovement(level.random.nextGaussian() * motion, 0.2, level.random.nextGaussian() * motion);
                level.addFreshEntity(entityItem);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        AbstractAutoSieveBlockEntity tileEntity = (AbstractAutoSieveBlockEntity) level.getBlockEntity(pos);
        if (tileEntity != null) {
            boolean useRandomSkin = true;
            CompoundTag tagCompound = stack.getTag();
            if (tagCompound != null) {
                if (tagCompound.contains("CustomSkin")) {
                    GameProfile customSkin = NbtUtils.readGameProfile(tagCompound.getCompound("CustomSkin"));
                    if (customSkin != null) {
                        tileEntity.setCustomSkin(customSkin);
                        useRandomSkin = false;
                    }
                }
            }
            if (!level.isClientSide && useRandomSkin) {
                WhitelistEntry randomSkin = AutoSieveSkinRegistry.getRandomSkin();
                if (randomSkin != null) {
                    tileEntity.setCustomSkin(new GameProfile(randomSkin.getUuid(), randomSkin.getName()));
                }
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null) {
            Container container = Balm.getProviders().getProvider(blockEntity, Container.class);
            if (container != null) {
                return AbstractContainerMenu.getRedstoneSignalFromContainer(container);
            }
        }

        return 0;
    }

    protected Component getSkinTooltip(String name) {
        return Component.translatable("tooltip.excompressum.auto_sieve", name).withStyle(ChatFormatting.GRAY);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter blockGetter, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tagCompound = stack.getTag();
        if (tagCompound != null && tagCompound.contains("CustomSkin")) {
            GameProfile customSkin = NbtUtils.readGameProfile(tagCompound.getCompound("CustomSkin"));
            if (customSkin != null) {
                tooltip.add(getSkinTooltip(customSkin.getName()));
            }
        } else {
            if (currentRandomName == null) {
                updateRandomSkinName();
            }

            tooltip.add(getSkinTooltip(currentRandomName));
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
        final Direction facing = context.getHorizontalDirection();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(FACING, facing).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
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
