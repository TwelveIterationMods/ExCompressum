package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.api.Hammerable;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HammerRecipe {

    private final List<ItemStack> inputs;
    private final List<LootTableEntry> outputs;
    private final List<ItemStack> outputItems;

    public HammerRecipe(Hammerable entry) {
        inputs = Arrays.asList(entry.getSource().getMatchingStacks());
        outputs = LootTableUtils.getLootTableEntries(entry.getLootTable());
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
