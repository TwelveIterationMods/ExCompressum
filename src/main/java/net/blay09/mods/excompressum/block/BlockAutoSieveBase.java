package net.blay09.mods.excompressum.block;

import com.mojang.authlib.GameProfile;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.AutoSieveSkinRegistry;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockAutoSieveBase extends ContainerBlock implements IUglyfiable {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty UGLY = BooleanProperty.create("ugly");

    private ItemStack lastHoverStack = ItemStack.EMPTY;
    private String currentRandomName;

    protected BlockAutoSieveBase(Material material) {
        super(material);
        setCreativeTab(ExCompressum.itemGroup);
        setHardness(2f);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, UGLY);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem(hand);
            if (!heldItem.isEmpty()) {
                AutoSieveTileEntityBase tileEntity = (AutoSieveTileEntityBase) world.getTileEntity(pos);
                if (tileEntity != null) {
                    if (heldItem.getItem() instanceof ItemFood) {
                        ItemFood itemFood = (ItemFood) heldItem.getItem();
                        if (tileEntity.getFoodBoost() <= 1f) {
                            tileEntity.setFoodBoost((int) (itemFood.getSaturationModifier(heldItem) * 640), Math.max(1f, itemFood.getHealAmount(heldItem) * 0.75f));
                            if (!player.capabilities.isCreativeMode) {
                                ItemStack returnStack = itemFood.onItemUseFinish(heldItem, world, FakePlayerFactory.getMinecraft((WorldServer) world));
                                if (returnStack != heldItem) {
                                    player.setHeldItem(hand, returnStack);
                                }
                            }
                            world.playEvent(2005, pos, 0);
                        }
                        return true;
                    } else if (heldItem.getItem() == Items.NAME_TAG && heldItem.hasDisplayName()) {
                        tileEntity.setCustomSkin(new GameProfile(null, heldItem.getDisplayName()));
                        if (!player.capabilities.isCreativeMode) {
                            heldItem.shrink(1);
                        }
                        return true;
                    }
                }
            }
            if (!player.isSneaking()) {
                player.openGui(ExCompressum.instance, GuiHandler.GUI_AUTO_SIEVE, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, BlockState state) {
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

        super.breakBlock(world, pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing facing = EnumFacing.getDirectionFromEntityLiving(pos, placer);
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        return getStateFromMeta(meta).withProperty(FACING, facing);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        AutoSieveTileEntityBase tileEntity = (AutoSieveTileEntityBase) world.getTileEntity(pos);
        if (tileEntity != null) {
            boolean useRandomSkin = true;
            CompoundNBT tagCompound = stack.getTag();
            if (tagCompound != null) {
                if (tagCompound.contains("CustomSkin")) {
                    GameProfile customSkin = NBTUtil.readGameProfileFromNBT(tagCompound.getCompound("CustomSkin"));
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
        return tileEntity != null ? ItemHandlerHelper.calcRedstoneFromInventory(tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) : 0;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        CompoundNBT tagCompound = stack.getTag();
        if (tagCompound != null && tagCompound.contains("CustomSkin")) {
            GameProfile customSkin = NBTUtil.readGameProfileFromNBT(tagCompound.getCompound("CustomSkin"));
            if (customSkin != null) {
                tooltip.add(TextFormatting.GRAY + I18n.format("tooltip." + getRegistryName(), customSkin.getName()));
            }
        } else {
            if (currentRandomName == null) {
                currentRandomName = AutoSieveSkinRegistry.getRandomSkin();
            }
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip." + getRegistryName(), currentRandomName));
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
}
