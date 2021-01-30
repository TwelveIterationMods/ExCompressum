package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class GeneratedHeavySieveRecipe extends ExCompressumRecipe {
    public static final IRecipeType<GeneratedHeavySieveRecipe> TYPE = IRecipeType.register(ExCompressum.MOD_ID + ":heavy_sieve_generated");

    private final Ingredient input;
    private final ResourceLocation source;
    private final Integer rolls;

    public GeneratedHeavySieveRecipe(ResourceLocation id, Ingredient input, ResourceLocation source, @Nullable Integer rolls) {
        super(id, TYPE);
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
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.generatedHeavySieveRecipe;
    }
}
