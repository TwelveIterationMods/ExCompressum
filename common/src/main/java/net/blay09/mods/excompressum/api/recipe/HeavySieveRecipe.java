package net.blay09.mods.excompressum.api.recipe;

import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Set;

public interface HeavySieveRecipe {

    Ingredient getIngredient();

    LootTable getLootTable();

    ResourceLocation getRecipeId();

    CommonMeshType getMinimumMesh();

    Set<CommonMeshType> getMeshes();

    boolean isWaterlogged();
}
