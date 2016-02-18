package net.blay09.mods.excompressum;

import exnihilo.items.hammers.IHammer;
import net.minecraft.item.ItemStack;

public interface ICompressedHammer extends IHammer {
    boolean isCompressedHammer(ItemStack stack);
}
