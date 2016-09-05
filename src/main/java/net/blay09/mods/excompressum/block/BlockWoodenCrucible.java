package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.TileWoodenCrucible;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class BlockWoodenCrucible extends BlockContainer {

    public BlockWoodenCrucible() {
        super(Material.WOOD);
        setRegistryName("wooden_crucible");
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for(int i = 0; i < 6; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
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

                FluidStack heldItemFluid = FluidContainerRegistry.getFluidForFilledItem(heldItem);
                if (heldItemFluid != null) {
                    int available = tileEntity.fill(EnumFacing.UP, heldItemFluid, false);
                    if (available > 0) {
                        tileEntity.fill(EnumFacing.UP, heldItemFluid, true);
                        if (!player.capabilities.isCreativeMode) {
                            if (heldItem.getItem() == Items.POTIONITEM && heldItem.getItemDamage() == 0) {
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.GLASS_BOTTLE, 1, 0));
                            } else {
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, this.getContainer(heldItem));
                            }
                        }
                    }
                } else if (FluidContainerRegistry.isContainer(heldItem)) {
                    FluidStack fluidStack = tileEntity.drain(EnumFacing.DOWN, Integer.MAX_VALUE, false);
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

                                tileEntity.drain(EnumFacing.DOWN, filledFluid.amount, true);
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

}
