package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ToolItem;
import net.minecraft.world.World;

import java.util.HashSet;

public class ItemCompressedHammer extends ToolItem implements ICompressedHammer {

    public static final String namePrefix = "compressed_hammer_";

    public ItemCompressedHammer(ToolMaterial material, String name) {
        super(6f, -3.2f, material, new HashSet<>());
    }

    @Override
    public boolean canHarvestBlock(BlockState state, ItemStack stack) {
        return CompressedHammerRegistry.isHammerable(state) || ExRegistro.isHammerable(state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if ((CompressedHammerRegistry.isHammerable(state) || ExRegistro.isHammerable(state)) && state.getBlock().getHarvestLevel(state) <= toolMaterial.getHarvestLevel()) {
            return efficiency * 0.75f;
        }
        return 0.8f;
    }

    @Override
    public boolean canHammer(ItemStack itemStack, World world, BlockState state, PlayerEntity entityPlayer) {
        return true;
    }

    @Override
    public int getHammerLevel(ItemStack itemStack, World world, BlockState state, PlayerEntity entityPlayer) {
        return toolMaterial.getHarvestLevel();
    }
}
