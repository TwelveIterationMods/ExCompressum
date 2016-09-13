package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.IRegisterModel;
import net.blay09.mods.excompressum.tile.TileWoodenCrucible;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class BlockWoodenCrucible extends BlockContainer implements IRegisterModel {

    public enum Type implements IStringSerializable {
        OAK,
        SPRUCE,
        BIRCH,
        JUNGLE,
        ACACIA,
        DARK_OAK;

        public static Type[] values = values();

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("variant", Type.class);

    public BlockWoodenCrucible() {
        super(Material.WOOD);
        setRegistryName("wooden_crucible");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        if(meta < 0 || meta >= Type.values.length) {
            return getDefaultState();
        }
        return getDefaultState().withProperty(VARIANT, Type.values[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    @Nullable
    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(this, 1, state.getValue(VARIANT).ordinal());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for(int i = 0; i < Type.values.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileWoodenCrucible();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileWoodenCrucible tileEntity = (TileWoodenCrucible) world.getTileEntity(pos);
        if(tileEntity != null) {
            ItemStack outputStack = tileEntity.getItemHandler().extractItem(0, 64, false);
            if (outputStack != null) {
                if (!world.isRemote) {
                    if (!player.inventory.addItemStackToInventory(outputStack)) {
                        world.spawnEntityInWorld(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, outputStack));
                    }
                }
                return true;
            }

            if (heldItem != null) {
                if (tileEntity.addItem(heldItem)) {
                    return true;
                }
            }

            IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
            FluidUtil.interactWithFluidHandler(heldItem, fluidHandler, player);
            return true;
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel(Item item) {
        ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack itemStack) {
                Type type = itemStack.getItemDamage() >= 0 && itemStack.getItemDamage() < Type.values.length ? Type.values[itemStack.getItemDamage()] : null;
                if(type != null) {
                    return new ModelResourceLocation(getRegistryName(), "variant=" + type.getName());
                } else {
                    return new ModelResourceLocation("missingno");
                }
            }
        });
    }

}
