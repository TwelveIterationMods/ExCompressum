package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.api.IHammerRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JeiHammerRecipe {

    private final List<ItemStack> inputs;
    private final List<MergedLootTableEntry> outputs;
    private final List<ItemStack> outputItems;

    public JeiHammerRecipe(IHammerRecipe recipe) {
        inputs = Arrays.asList(recipe.getInput().getItems());
        List<LootTableEntry> entries = LootTableUtils.getLootTableEntries(recipe.getId(), recipe.getLootTable());
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
