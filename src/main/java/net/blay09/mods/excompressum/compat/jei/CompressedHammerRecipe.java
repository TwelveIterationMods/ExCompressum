package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class CompressedHammerRecipe {

    private final ItemStack input;
    private final List<ItemStack> outputs;

    public CompressedHammerRecipe(CompressedHammerable entry) {
        input = ItemStack.EMPTY;
        outputs = new ArrayList<>();
    }

    public ItemStack getInput() {
        return input;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }
}
