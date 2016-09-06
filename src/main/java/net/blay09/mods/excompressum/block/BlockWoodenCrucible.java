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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class BlockWoodenCrucible extends BlockContainer implements IRegisterModel {

    public enum WoodType implements IStringSerializable {
        OAK,
        SPRUCE,
        BIRCH,
        JUNGLE,
        ACACIA,
        DARK_OAK;

        public static WoodType[] values = values();

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public static final PropertyEnum<WoodType> VARIANT = PropertyEnum.create("variant", WoodType.class);

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
        if(meta < 0 || meta >= BlockCompressed.Type.values.length) {
            return getDefaultState();
        }
        return getDefaultState().withProperty(VARIANT, WoodType.values[meta]);
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
        for(int i = 0; i < WoodType.values.length; i++) {
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
            if (heldItem != null) {
                if (tileEntity.addItem(heldItem) && !player.capabilities.isCreativeMode) {
                    heldItem.stackSize--;
                    if (heldItem.stackSize == 0) {
                        heldItem = null;
                    }
                }

                // TODO capability stuffs ItemFluidContainer

                FluidStack heldItemFluid = FluidContainerRegistry.getFluidForFilledItem(heldItem);
                if (heldItemFluid != null) {
                    if(tileEntity.getFluidTank().fill(heldItemFluid, true) > 0) {
                        if (!player.capabilities.isCreativeMode) {
                            if (heldItem.getItem() == Items.POTIONITEM && heldItem.getItemDamage() == 0) {
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.GLASS_BOTTLE, 1, 0));
                            } else {
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, this.getContainer(heldItem));
                            }
                        }
                    }
                } else if (FluidContainerRegistry.isContainer(heldItem)) {
                    FluidStack fluidStack = tileEntity.getFluidTank().drain(Integer.MAX_VALUE, false);
                    if (fluidStack != null) {
                        ItemStack filledStack = FluidContainerRegistry.fillFluidContainer(fluidStack, heldItem);
                        if (filledStack != null) {
                            FluidStack filledFluid = FluidContainerRegistry.getFluidForFilledItem(filledStack);
                            if (filledFluid != null) {
                                if (heldItem.stackSize > 1) {
                                    boolean added = player.inventory.addItemStackToInventory(filledStack);
                                    if (!added) {
                                        return false;
                                    }

                                    heldItem.stackSize--;
                                } else {
                                    player.inventory.setInventorySlotContents(player.inventory.currentItem, filledStack);
                                }

                                tileEntity.getFluidTank().drain(filledFluid.amount, true);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private ItemStack getContainer(ItemStack itemStack) {
        if (itemStack.stackSize == 1) {
            return itemStack.getItem().hasContainerItem(itemStack) ? itemStack.getItem().getContainerItem(itemStack) : null;
        } else {
            itemStack.splitStack(1);
            return itemStack;
        }
    }

    @Override
    public void registerModel(Item item) {
        /*ResourceLocation[] variants = new ResourceLocation[BlockCompressed.Type.values.length];
        for(int i = 0; i < variants.length; i++) {
            variants[i] = new ResourceLocation(ExCompressum.MOD_ID, "compressed_" + BlockCompressed.Type.values[i].getName());
        }
        ModelBakery.registerItemVariants(item, variants);*/
        ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack itemStack) {
                WoodType type = itemStack.getItemDamage() >= 0 && itemStack.getItemDamage() < WoodType.values.length ? WoodType.values[itemStack.getItemDamage()] : null;
                if(type != null) {
                    return new ModelResourceLocation(getRegistryName(), "variant=" + type.getName());
                } else {
                    return new ModelResourceLocation("missingno");
                }
            }
        });
    }

}
