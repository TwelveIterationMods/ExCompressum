package net.blay09.mods.excompressum.registry.heavysieve;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.*;

public class GeneratedHeavySieveRecipe extends ExCompressumRecipe<RecipeInput> {

    private final Ingredient ingredient;
    private final Identifier sourceItem;
    private final int rolls;

    public GeneratedHeavySieveRecipe(Ingredient ingredient, Identifier sourceItem, int rolls) {
        this.ingredient = ingredient;
        this.sourceItem = sourceItem;
        this.rolls = rolls;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Identifier getSourceItem() {
        return sourceItem;
    }

    public int getRolls() {
        return rolls;
    }

    @Override
    public RecipeSerializer<GeneratedHeavySieveRecipe> getSerializer() {
        return ModRecipeTypes.generatedHeavySieve.serializer();
    }

    @Override
    public RecipeType<GeneratedHeavySieveRecipe> getType() {
        return ModRecipeTypes.generatedHeavySieve.type();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeTypes.heavySieve.bookCategory();
    }

    private static final MapCodec<GeneratedHeavySieveRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.ingredient),
            Identifier.CODEC.fieldOf("source").forGetter(recipe -> recipe.sourceItem),
            Codec.INT.fieldOf("rolls").orElse(-1).forGetter(recipe -> recipe.rolls)
    ).apply(instance, GeneratedHeavySieveRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GeneratedHeavySieveRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, GeneratedHeavySieveRecipe::getIngredient,
            Identifier.STREAM_CODEC, GeneratedHeavySieveRecipe::getSourceItem,
            ByteBufCodecs.INT, GeneratedHeavySieveRecipe::getRolls,
            GeneratedHeavySieveRecipe::new);

    public static RecipeSerializer<GeneratedHeavySieveRecipe> serializer() {
        return new RecipeSerializer<>(CODEC, STREAM_CODEC);
    }
}
