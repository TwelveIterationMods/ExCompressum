package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public class GeneratedHeavySieveRecipe extends ExCompressumRecipe {

    private Ingredient input;
    private ResourceLocation source;
    private Integer rolls;

    public GeneratedHeavySieveRecipe(ResourceLocation id, Ingredient input, ResourceLocation source, @Nullable Integer rolls) {
        super(id, ModRecipeTypes.GENERATED_HEAVY_SIEVE);
        this.input = input;
        this.source = source;
        this.rolls = rolls;
    }

    public Ingredient getInput() {
        return input;
    }

    public ResourceLocation getSource() {
        return source;
    }

    @Nullable
    public Integer getRolls() {
        return rolls;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.generatedHeavySieveRecipe;
    }

    public void setInput(Ingredient input) {
        this.input = input;
    }

    public void setSource(ResourceLocation source) {
        this.source = source;
    }

    public void setRolls(Integer rolls) {
        this.rolls = rolls;
    }
}
