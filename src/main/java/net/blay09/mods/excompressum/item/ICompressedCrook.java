package net.blay09.mods.excompressum.item;


import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ICompressedCrook {
    boolean canCrook(ItemStack itemStack, World world, BlockState state, PlayerEntity entityPlayer);
}
