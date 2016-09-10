package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.tile.TileAutoCompressedHammer;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockAutoCompressedHammer extends BlockAutoHammer {

    public BlockAutoCompressedHammer() {
        super("auto_compressed_hammer");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileAutoCompressedHammer();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state;
    }

    @Override
    public void registerModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
