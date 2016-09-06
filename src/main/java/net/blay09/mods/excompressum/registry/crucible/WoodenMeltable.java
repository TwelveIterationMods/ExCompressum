package net.blay09.mods.excompressum.registry.crucible;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class WoodenMeltable {
    public final ItemStack itemStack;
    public final FluidStack fluidStack;
    public final Block appearance;
    public final int appearanceMeta;

    public WoodenMeltable(ItemStack itemStack, FluidStack fluidStack, Block appearance, int appearanceMeta) {
        this.itemStack = itemStack;
        this.fluidStack = fluidStack;
        this.appearance = appearance;
        this.appearanceMeta = appearanceMeta;
    }
}
