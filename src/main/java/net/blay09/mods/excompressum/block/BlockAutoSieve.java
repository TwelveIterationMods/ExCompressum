package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.TileAutoHammer;
import net.blay09.mods.excompressum.tile.TileAutoSieve;
import net.blay09.mods.excompressum.tile.TileAutoSieveBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAutoSieve extends BlockAutoSieveBase {

    public static final String name = "auto_sieve";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public BlockAutoSieve() {
        super(Material.IRON);
        setUnlocalizedName(registryName.toString());
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileAutoSieve();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileAutoSieve tileEntity = (TileAutoSieve) world.getTileEntity(pos);
        if(tileEntity != null) {
            NBTTagCompound tagCompound = stack.getTagCompound();
            if (tagCompound != null) {
                if (tagCompound.hasKey("EnergyStored")) {
                    tileEntity.getEnergyStorage().setEnergyStored(tagCompound.getInteger("EnergyStored"));
                }
            }
        }
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        updateRedstoneState(world, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        updateRedstoneState(world, pos);
    }

    private void updateRedstoneState(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof TileAutoSieveBase) {
            ((TileAutoSieveBase) tileEntity).setDisabledByRedstone(world.isBlockPowered(pos));
        }
    }
}
