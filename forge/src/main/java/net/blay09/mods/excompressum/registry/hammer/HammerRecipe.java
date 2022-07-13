package net.blay09.mods.excompressum.registry.hammer;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.IHammerRecipe;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class HammerRecipe extends ExCompressumRecipe implements IHammerRecipe {

    private Ingredient input;
    private LootTableProvider lootTable;

    public HammerRecipe(ResourceLocation id, Ingredient input, LootTableProvider lootTable) {
        super(id, ModRecipeTypes.HAMMER);
        this.input = input;
        this.lootTable = lootTable;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.hammerRecipe;
    }

    @Override
    public Ingredient getInput() {
        return input;
    }

    @Override
    public LootTableProvider getLootTable() {
        return lootTable;
    }

    public void setInput(Ingredient input) {
        this.input = input;
    }

    public void setLootTable(LootTableProvider lootTable) {
        this.lootTable = lootTable;
    }
}
