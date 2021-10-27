package net.blay09.mods.excompressum.item;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ICompressedHammer {
    boolean canHammer(ItemStack itemStack, Level level, BlockState state, Player entityPlayer);

    int getHammerLevel(ItemStack itemStack, Level level, BlockState state, Player entityPlayer);
}
