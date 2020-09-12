package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntity;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class AutoSieveBlock extends AutoSieveBaseBlock {

    public static final String name = "auto_sieve";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public AutoSieveBlock() {
        super(Properties.create(Material.IRON));
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new AutoSieveTileEntity();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        AutoSieveTileEntity tileEntity = (AutoSieveTileEntity) world.getTileEntity(pos);
        if(tileEntity != null) {
            CompoundNBT tagCompound = stack.getTag();
            if (tagCompound != null) {
                if (tagCompound.contains("EnergyStored")) {
                    tileEntity.getEnergyStorage().setEnergyStored(tagCompound.getInt("EnergyStored"));
                }
            }
        }
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        updateRedstoneState(world, pos);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        updateRedstoneState(world, pos);
    }

    private void updateRedstoneState(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof AutoSieveTileEntityBase) {
            ((AutoSieveTileEntityBase) tileEntity).setDisabledByRedstone(world.isBlockPowered(pos));
        }
    }
}
