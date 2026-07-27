package net.blay09.mods.excompressum.registry.hammer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.excompressum.api.recipe.HammerRecipe;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ExCompressumSerializers;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.storage.loot.LootTable;

public class HammerRecipeImpl extends ExCompressumRecipe<RecipeInput> implements HammerRecipe {

    private final Ingredient ingredient;
    private final LootTable lootTable;

    public HammerRecipeImpl(Ingredient ingredient, LootTable lootTable) {
        this.ingredient = ingredient;
        this.lootTable = lootTable;
    }

    @Override
    public RecipeSerializer<HammerRecipeImpl> getSerializer() {
        return ModRecipeTypes.hammer.serializer();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeType<HammerRecipeImpl> getType() {
        return ModRecipeTypes.hammer.type();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeTypes.hammer.bookCategory();
    }

    @Override
    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public LootTable getLootTable() {
        return lootTable;
    }

    private static final MapCodec<HammerRecipeImpl> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.ingredient),
            LootTable.DIRECT_CODEC.fieldOf("lootTable").forGetter(recipe -> recipe.lootTable)
    ).apply(instance, HammerRecipeImpl::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, HammerRecipeImpl> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, HammerRecipeImpl::getIngredient,
            ExCompressumSerializers.LOOT_TABLE_STREAM_CODEC, HammerRecipeImpl::getLootTable,
            HammerRecipeImpl::new);

    public static RecipeSerializer<HammerRecipeImpl> serializer() {
        return new RecipeSerializer<>(CODEC, STREAM_CODEC);
    }
}
