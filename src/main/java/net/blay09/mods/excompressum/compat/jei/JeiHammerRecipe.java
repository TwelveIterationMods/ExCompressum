package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.newregistry.hammer.HammerRecipe;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JeiHammerRecipe {

    private final List<ItemStack> inputs;
    private final List<LootTableEntry> outputs;
    private final List<ItemStack> outputItems;

    public JeiHammerRecipe(HammerRecipe recipe) {
        inputs = Arrays.asList(recipe.getInput().getMatchingStacks());
        outputs = LootTableUtils.getLootTableEntries(recipe.getLootTable());
        outputItems = outputs.stream().map(LootTableEntry::getItemStack).collect(Collectors.toList());
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public List<LootTableEntry> getOutputs() {
        return outputs;
    }

    public List<ItemStack> getOutputItems() {
        return outputItems;
    }
}
