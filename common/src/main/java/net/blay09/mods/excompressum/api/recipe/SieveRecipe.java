package net.blay09.mods.excompressum.api.recipe;

import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Set;

public interface SieveRecipe {

    Ingredient getIngredient();

    LootTable getLootTable();

    Set<CommonMeshType> getMeshes();

    boolean isWaterlogged();
}
