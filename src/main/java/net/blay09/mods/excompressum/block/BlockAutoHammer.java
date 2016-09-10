package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.IRegisterModel;
import net.blay09.mods.excompressum.StupidUtils;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.tile.TileAutoHammer;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockAutoHammer extends BlockContainer implements IRegisterModel {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockAutoHammer(String registryName) {
        super(Material.IRON);
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
        setRegistryName(registryName);
        setUnlocalizedName(getRegistryName().toString());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).ordinal();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Nullable
    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(this, 1, state.getValue(FACING).ordinal());
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileAutoHammer();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!player.isSneaking()) {
            player.openGui(ExCompressum.instance, GuiHandler.GUI_AUTO_HAMMER, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity != null) {
            //noinspection ConstantConditions /// Forge needs jesus
            IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemHandler.getStackInSlot(i) != null) {
                    EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i));
                    double motion = 0.05;
                    entityItem.motionX = world.rand.nextGaussian() * motion;
                    entityItem.motionY = 0.2;
                    entityItem.motionZ = world.rand.nextGaussian() * motion;
                    world.spawnEntityInWorld(entityItem);
                }
            }
            ItemStack currentStack = ((TileAutoHammer) tileEntity).getCurrentStack();
            if (currentStack != null) {
                EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), currentStack);
                double motion = 0.05;
                entityItem.motionX = world.rand.nextGaussian() * motion;
                entityItem.motionY = 0.2;
                entityItem.motionZ = world.rand.nextGaussian() * motion;
                world.spawnEntityInWorld(entityItem);
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing facing = BlockPistonBase.getFacingFromEntity(pos, placer);
        if(facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        return getStateFromMeta(meta).withProperty(FACING, facing);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound != null && tagCompound.hasKey("EnergyStored")) {
            TileAutoHammer tileEntity = (TileAutoHammer) world.getTileEntity(pos);
            if(tileEntity != null) {
                tileEntity.setEnergyStored(tagCompound.getInteger("EnergyStored"));
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
        return StupidUtils.getComparatorOutput64(world, pos);
    }

    @Override
    public void registerModel(Item item) {
        ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                if(ExRegistro.getNihiloMod() == ExNihiloProvider.NihiloMod.Omnia) {
                    return new ModelResourceLocation(stack.getItem().getRegistryName(), "inventory_omnia");
                } else if(ExRegistro.getNihiloMod() == ExNihiloProvider.NihiloMod.Adscensio) {
                    return new ModelResourceLocation(stack.getItem().getRegistryName(), "inventory_adscensio");
                }
                return new ModelResourceLocation(stack.getItem().getRegistryName(), "inventory");
            }
        });
    }
}
