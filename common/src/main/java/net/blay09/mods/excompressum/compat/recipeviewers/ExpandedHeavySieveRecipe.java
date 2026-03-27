package net.blay09.mods.excompressum.compat.recipeviewers;

import net.blay09.mods.excompressum.api.recipe.HeavySieveRecipe;
import net.blay09.mods.excompressum.loot.LootTableEntry;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.loot.MergedLootTableEntry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ExpandedHeavySieveRecipe {

    private final @Nullable Identifier id;
    private final HeavySieveRecipe recipe;
    private final Ingredient ingredient;
    private final List<ItemStack> meshItems;
    private final List<MergedLootTableEntry> outputs;
    private final List<ItemStack> outputItems;
    private final boolean waterlogged;

    public ExpandedHeavySieveRecipe(@Nullable Identifier id, HeavySieveRecipe recipe) {
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

    public HeavySieveRecipe getRecipe() {
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

    public @Nullable Identifier getId() {
        return id;
    }
}
