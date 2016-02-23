package net.blay09.mods.excompressum.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.TileEntityWoodenCrucible;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class BlockWoodenCrucible extends BlockContainer {

    public BlockWoodenCrucible() {
        super(Material.wood);
        setCreativeTab(CreativeTabs.tabDecorations);
        setHardness(2f);
        setBlockName(ExCompressum.MOD_ID + ":woodenCrucible");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for(int i = 0; i < 6; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = Blocks.log.getIcon(0, 0);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityWoodenCrucible();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        if (entityPlayer == null) {
            return false;
        }
        TileEntityWoodenCrucible tileEntity = (TileEntityWoodenCrucible) world.getTileEntity(x, y, z);
        ItemStack heldItem = entityPlayer.getCurrentEquippedItem();
        if (heldItem != null) {
            if (tileEntity.addItem(heldItem) && !entityPlayer.capabilities.isCreativeMode) {
                heldItem.stackSize--;
                if (heldItem.stackSize == 0) {
                    heldItem = null;
                }
            }

            FluidStack heldItemFluid = FluidContainerRegistry.getFluidForFilledItem(heldItem);
            if (heldItemFluid != null) {
                int available = tileEntity.fill(ForgeDirection.UP, heldItemFluid, false);
                if (available > 0) {
                    tileEntity.fill(ForgeDirection.UP, heldItemFluid, true);
                    if (!entityPlayer.capabilities.isCreativeMode) {
                        if (heldItem.getItem() == Items.potionitem && heldItem.getItemDamage() == 0) {
                            entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, new ItemStack(Items.glass_bottle, 1, 0));
                        } else {
                            entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, this.getContainer(heldItem));
                        }
                    }
                }
            } else if (FluidContainerRegistry.isContainer(heldItem)) {
                FluidStack fluidStack = tileEntity.drain(ForgeDirection.DOWN, Integer.MAX_VALUE, false);
                if (fluidStack != null) {
                    ItemStack filledStack = FluidContainerRegistry.fillFluidContainer(fluidStack, heldItem);
                    if (filledStack != null) {
                        FluidStack filledFluid = FluidContainerRegistry.getFluidForFilledItem(filledStack);
                        if (filledFluid != null) {
                            if (heldItem.stackSize > 1) {
                                boolean added = entityPlayer.inventory.addItemStackToInventory(filledStack);
                                if (!added) {
                                    return false;
                                }

                                heldItem.stackSize--;
                            } else {
                                entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, filledStack);
                            }

                            tileEntity.drain(ForgeDirection.DOWN, filledFluid.amount, true);
                            return true;
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

    public static void registerRecipes(Configuration config) {
        if (config.getBoolean("Wooden Crucible", "blocks", true, "If set to false, the recipe for the wooden crucible will be disabled.")) {
            for (int i = 0; i < 4; i++) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ExCompressum.woodenCrucible, 1, i), "p p", "p p", "psp", 'p', new ItemStack(Blocks.log, 1, i), 's', "slabWood"));
            }
            for (int i = 0; i < 2; i++) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ExCompressum.woodenCrucible, 1, 4 + i), "p p", "p p", "psp", 'p', new ItemStack(Blocks.log2, 1, i), 's', "slabWood"));
            }
        }
    }
}
