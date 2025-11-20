package net.blay09.mods.excompressum.compat.recipeviewers;

import net.blay09.mods.excompressum.api.recipe.SieveRecipe;
import net.blay09.mods.excompressum.loot.LootTableEntry;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.loot.MergedLootTableEntry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExpandedSieveRecipe {

    private final Identifier id;
    private final SieveRecipe recipe;
    private final Ingredient ingredient;
    private final List<ItemStack> meshItems;
    private final List<MergedLootTableEntry> outputs;
    private final List<ItemStack> outputItems;
    private final boolean waterlogged;

    public ExpandedSieveRecipe(Identifier id, SieveRecipe recipe) {
        this.id = id;
        this.recipe = recipe;
        meshItems = new ArrayList<>();
        for (final var meshType : recipe.getMeshes()) {
            for (final var mesh : SieveMeshRegistry.getEntries().values()) {
                if (mesh.getMeshType() == meshType) {
                    meshItems.add(mesh.getItemStack());
                }
            }
        }
        ingredient = recipe.getIngredient();
        List<LootTableEntry> entries = LootTableUtils.getLootTableEntries(recipe.getLootTable());
        outputs = LootTableUtils.mergeLootTableEntries(entries);
        outputItems = outputs.stream().map(MergedLootTableEntry::getItemStack).collect(Collectors.toList());
        waterlogged = recipe.isWaterlogged();
    }

    public SieveRecipe getRecipe() {
        return recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public List<ItemStack> getMeshItems() {
        return meshItems;
    }

    public List<MergedLootTableEntry> getOutputs() {
        return outputs;
    }

    public List<ItemStack> getOutputItems() {
        return outputItems;
    }

    public boolean isWaterlogged() {
        return waterlogged;
    }

    public Identifier getId() {
        return id;
    }
}
