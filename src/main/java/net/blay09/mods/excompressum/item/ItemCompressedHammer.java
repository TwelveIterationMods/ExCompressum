package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.HashSet;

public class ItemCompressedHammer extends ItemCompressumTool implements ICompressedHammer {

    public ItemCompressedHammer(ToolMaterial material, String name) {
        super(6f, -3.2f, material, new HashSet<Block>());
        setRegistryName("compressed_hammer_" + name);
        setUnlocalizedName(getRegistryNameString());
        setCreativeTab(ExCompressum.creativeTab);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return CompressedHammerRegistry.isHammerable(state) || ExRegistro.isHammerable(state);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if ((CompressedHammerRegistry.isHammerable(state) || ExRegistro.isHammerable(state)) && state.getBlock().getHarvestLevel(state) <= toolMaterial.getHarvestLevel()) {
            return efficiencyOnProperMaterial * 0.75f;
        }
        return 0.8f;
    }

    @Override
    public boolean canHammer(ItemStack itemStack, World world, IBlockState state, EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public int getHammerLevel(ItemStack itemStack, World world, IBlockState state, EntityPlayer entityPlayer) {
        return toolMaterial.getHarvestLevel();
    }
}
