package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.newregistry.compressedhammer.CompressedHammerRecipe;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JeiCompressedHammerRecipe {

    private final List<ItemStack> inputs;
    private final List<LootTableEntry> outputs;
    private final List<ItemStack> outputItems;

    public JeiCompressedHammerRecipe(CompressedHammerRecipe entry) {
        inputs = Arrays.asList(entry.getInput().getMatchingStacks());
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
