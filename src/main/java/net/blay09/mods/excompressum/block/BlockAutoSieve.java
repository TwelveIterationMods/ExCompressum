package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.tile.TileAutoSieve;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAutoSieve extends BlockAutoSieveBase {

    public BlockAutoSieve(String registryName) {
        super(Material.IRON);
        setRegistryName(registryName);
        setUnlocalizedName(getRegistryName().toString());
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
                    tileEntity.setEnergyStored(tagCompound.getInteger("EnergyStored"));
                }
            }
        }
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

}
