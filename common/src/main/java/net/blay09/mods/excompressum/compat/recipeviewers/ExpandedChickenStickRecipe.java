package net.blay09.mods.excompressum.compat.recipeviewers;

import net.blay09.mods.excompressum.loot.LootTableEntry;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.loot.MergedLootTableEntry;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.stream.Collectors;

public class ExpandedChickenStickRecipe {

    private final Ingredient ingredient;
    private final List<MergedLootTableEntry> outputs;
    private final List<ItemStack> outputItems;

    public ExpandedChickenStickRecipe(ChickenStickRecipe recipe) {
        ingredient = recipe.getInput();
        List<LootTableEntry> entries = LootTableUtils.getLootTableEntries(recipe.getLootTable());
        outputs = LootTableUtils.mergeLootTableEntries(entries);
        outputItems = outputs.stream().map(MergedLootTableEntry::getItemStack).collect(Collectors.toList());
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public List<MergedLootTableEntry> getOutputs() {
        return outputs;
    }

    public List<ItemStack> getOutputItems() {
        return outputItems;
    }
}
