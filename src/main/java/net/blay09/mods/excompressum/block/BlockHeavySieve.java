package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.IRegisterModel;
import net.blay09.mods.excompressum.registry.sieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.tile.TileHeavySieve;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.List;

public class BlockHeavySieve extends BlockContainer implements IRegisterModel {

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
            return name().toLowerCase();
        }
    }

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("variant", Type.class);

    public BlockHeavySieve() {
        super(Material.WOOD);
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
        setRegistryName("heavy_sieve");
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
        for(int i = 0; i < BlockWoodenCrucible.Type.values.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileHeavySieve();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        // TODO wat is this
        /*PlayerInteractEvent event = new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, x, y, z, side, world); // eh? why is this here?
        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY || event.useBlock == Event.Result.DENY) {
            return false;
        }*/

        TileHeavySieve tileEntity = (TileHeavySieve) world.getTileEntity(pos);
        if(tileEntity != null) {
            if (tileEntity.getCurrentStack() == null && heldItem != null) {
                if (HeavySieveRegistry.isSiftable(heldItem)) {
                    tileEntity.addSiftable(heldItem);
                    if (!player.capabilities.isCreativeMode) {
                        heldItem.stackSize--;
                    }
                }
            } else {
                if (world.isRemote) {
                    tileEntity.processContents(player.capabilities.isCreativeMode);
                } else {
                    if (tileEntity.getCurrentStack() != null) {
                        if (ExCompressumConfig.allowHeavySieveAutomation || !(player instanceof FakePlayer)) {
                            tileEntity.processContents(player.capabilities.isCreativeMode);
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
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
