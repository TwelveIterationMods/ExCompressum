package net.blay09.mods.excompressum.block;

import com.mojang.authlib.GameProfile;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.AutoSieveSkinRegistry;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntityBase;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public abstract class AutoSieveBaseBlock extends ContainerBlock implements IUglyfiable {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty UGLY = BooleanProperty.create("ugly");

    private ItemStack lastHoverStack = ItemStack.EMPTY;
    private String currentRandomName;

    protected AutoSieveBaseBlock(Properties properties) {
        super(properties.hardnessAndResistance(2f));
        setDefaultState(getDefaultState().with(UGLY, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, UGLY);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote) {
            ItemStack heldItemStack = player.getHeldItem(hand);
            if (!heldItemStack.isEmpty()) {
                AutoSieveTileEntityBase tileEntity = (AutoSieveTileEntityBase) world.getTileEntity(pos);
                if (tileEntity != null) {
                    final Item heldItem = heldItemStack.getItem();
                    if (heldItem.isFood()) {
                        final Food food = Objects.requireNonNull(heldItem.getFood());
                        if (tileEntity.getFoodBoost() <= 1f) {
                            tileEntity.setFoodBoost((int) (food.getSaturation() * 640), Math.max(1f, food.getHealing() * 0.75f));
                            if (!player.abilities.isCreativeMode) {
                                ItemStack returnStack = heldItem.onItemUseFinish(heldItemStack, world, FakePlayerFactory.getMinecraft((ServerWorld) world));
                                if (returnStack != heldItemStack) {
                                    player.setHeldItem(hand, returnStack);
                                }
                            }
                            world.playEvent(2005, pos, 0);
                        }
                        return ActionResultType.SUCCESS;
                    } else if (heldItem == Items.NAME_TAG && heldItemStack.hasDisplayName()) {
                        tileEntity.setCustomSkin(new GameProfile(null, heldItemStack.getDisplayName().getString()));
                        if (!player.abilities.isCreativeMode) {
                            heldItemStack.shrink(1);
                        }
                        return ActionResultType.SUCCESS;
                    }
                }
            }
            if (!player.isSneaking()) {
                final TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof INamedContainerProvider) {
                    NetworkHooks.openGui(((ServerPlayerEntity) player), ((INamedContainerProvider) tileEntity), pos);
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            IItemHandler itemHandler = ((AutoSieveTileEntityBase) tileEntity).getItemHandler();
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    ItemEntity entityItem = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                    double motion = 0.05;
                    entityItem.setMotion(world.rand.nextGaussian() * motion, 0.2, world.rand.nextGaussian() * motion);
                    world.addEntity(entityItem);
                }
            }
            ItemStack currentStack = ((AutoSieveTileEntityBase) tileEntity).getCurrentStack();
            if (!currentStack.isEmpty()) {
                ItemEntity entityItem = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), currentStack);
                double motion = 0.05;
                entityItem.setMotion(world.rand.nextGaussian() * motion, 0.2, world.rand.nextGaussian() * motion);
                world.addEntity(entityItem);
            }
        }
        if (state.get(UGLY)) {
            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.uglySteelPlating)));
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        final Direction facing = context.getPlacementHorizontalFacing();
        return getDefaultState().with(FACING, facing);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        AutoSieveTileEntityBase tileEntity = (AutoSieveTileEntityBase) world.getTileEntity(pos);
        if (tileEntity != null) {
            boolean useRandomSkin = true;
            CompoundNBT tagCompound = stack.getTag();
            if (tagCompound != null) {
                if (tagCompound.contains("CustomSkin")) {
                    GameProfile customSkin = NBTUtil.readGameProfile(tagCompound.getCompound("CustomSkin"));
                    if (customSkin != null) {
                        tileEntity.setCustomSkin(customSkin);
                        useRandomSkin = false;
                    }
                }
            }
            if (!world.isRemote && useRandomSkin) {
                tileEntity.setCustomSkin(new GameProfile(null, AutoSieveSkinRegistry.getRandomSkin()));
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            final IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).resolve().orElse(null);
            return ItemHandlerHelper.calcRedstoneFromInventory(itemHandler);
        } else {
            return 0;
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tagCompound = stack.getTag();
        if (tagCompound != null && tagCompound.contains("CustomSkin")) {
            GameProfile customSkin = NBTUtil.readGameProfile(tagCompound.getCompound("CustomSkin"));
            if (customSkin != null) {
                tooltip.add(Messages.styledLang("tooltip." + getRegistryName().getPath(), TextFormatting.GRAY, customSkin.getName()));
            }
        } else {
            if (currentRandomName == null) {
                currentRandomName = AutoSieveSkinRegistry.getRandomSkin();
            }
            tooltip.add(Messages.styledLang("tooltip." + getRegistryName().getPath(), TextFormatting.GRAY, currentRandomName));
        }
        if (lastHoverStack != stack) {
            currentRandomName = AutoSieveSkinRegistry.getRandomSkin();
            lastHoverStack = stack;
        }
    }

    @Override
    public boolean uglify(@Nullable PlayerEntity player, World world, BlockPos pos, BlockState state, Hand hand, Direction facing, Vector3d hitVec) {
        if (!state.get(UGLY)) {
            world.setBlockState(pos, state.with(UGLY, true), 3);
            return true;
        }
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
