package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BlockCompressed extends Block {

    public enum Type {
        DUST,
        COBBLESTONE,
        GRAVEL,
        SAND,
        DIRT,
        FLINT,
        // TODO whatever else is added by Ex Nihilo Omnia
    }

    public BlockCompressed() {
        super(Material.ROCK);
        setRegistryName("compressed_dust");
        setHardness(4f);
        setResistance(6f);
        setSoundType(SoundType.STONE);
        setCreativeTab(ExCompressum.creativeTab);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
        for(int i = 0; i < Type.values().length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

}
