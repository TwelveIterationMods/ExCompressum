package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.WoodenCrucibleTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class WoodenCrucibleBlock extends ContainerBlock {

    public static final String name = "wooden_crucible";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public WoodenCrucibleBlock(WoodenCrucibleType type) {
        super(Material.WOOD);
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new WoodenCrucibleTileEntity();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        WoodenCrucibleTileEntity tileEntity = (WoodenCrucibleTileEntity) world.getTileEntity(pos);
        if (tileEntity != null) {
        	ItemStack heldItem = player.getHeldItem(hand);
            ItemStack outputStack = tileEntity.getItemHandler().extractItem(0, 64, false);
            if (!outputStack.isEmpty()) {
                if (!player.inventory.addItemStackToInventory(outputStack)) {
                    world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, outputStack));
                }
                return true;
            }

            if (!heldItem.isEmpty()) {
                if (tileEntity.addItem(heldItem, false, false)) {
                    return true;
                }
            }
            FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
        }
        return true;
    }

}
