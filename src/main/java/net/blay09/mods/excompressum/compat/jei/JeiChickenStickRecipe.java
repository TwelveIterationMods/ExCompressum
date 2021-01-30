package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.newregistry.chickenstick.ChickenStickRecipe;
import net.blay09.mods.excompressum.newregistry.hammer.HammerRecipe;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JeiChickenStickRecipe {

    private final List<ItemStack> inputs;
    private final List<MergedLootTableEntry> outputs;
    private final List<ItemStack> outputItems;

    public JeiChickenStickRecipe(ChickenStickRecipe recipe) {
        inputs = Arrays.asList(recipe.getInput().getMatchingStacks());
        List<LootTableEntry> entries = LootTableUtils.getLootTableEntries(recipe.getLootTable());
        outputs = LootTableUtils.mergeLootTableEntries(entries);
        outputItems = outputs.stream().map(MergedLootTableEntry::getItemStack).collect(Collectors.toList());
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public List<MergedLootTableEntry> getOutputs() {
        return outputs;
    }

    public List<ItemStack> getOutputItems() {
        return outputItems;
    }
}
