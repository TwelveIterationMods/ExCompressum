package net.blay09.mods.excompressum.forge.compat.jei;

import net.blay09.mods.excompressum.loot.LootTableEntry;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.loot.MergedLootTableEntry;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JeiCompressedHammerRecipe {

    private final List<ItemStack> inputs;
    private final List<MergedLootTableEntry> outputs;
    private final List<ItemStack> outputItems;

    public JeiCompressedHammerRecipe(CompressedHammerRecipe recipe) {
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
