package net.blay09.mods.excompressum.registry.woodencrucible;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.material.Fluid;

import java.util.Objects;

public class WoodenCrucibleRecipe extends ExCompressumRecipe<RecipeInput> {

    private final Ingredient ingredient;
    private final Identifier fluid;
    private final int amount;

    public WoodenCrucibleRecipe(Ingredient ingredient, Identifier fluid, Integer amount) {
        this.ingredient = ingredient;
        this.fluid = fluid;
        this.amount = amount;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Identifier getFluidId() {
        return fluid;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public RecipeSerializer<WoodenCrucibleRecipe> getSerializer() {
        return ModRecipeTypes.woodenCrucible.serializer();
    }

    @Override
    public RecipeType<WoodenCrucibleRecipe> getType() {
        return ModRecipeTypes.woodenCrucible.type();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeTypes.woodenCrucible.bookCategory();
    }

    public boolean matchesFluid(Fluid fluid) {
        final var fluidId = BuiltInRegistries.FLUID.getKey(fluid);
        return Objects.equals(fluidId, this.fluid);
    }

    public Fluid getFluid() {
        return BuiltInRegistries.FLUID.getValue(this.fluid);
    }

    private static final MapCodec<WoodenCrucibleRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.ingredient),
            Identifier.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.fluid),
            Codec.INT.fieldOf("amount").forGetter(recipe -> recipe.amount)
    ).apply(instance, WoodenCrucibleRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WoodenCrucibleRecipe> STREAM_CODEC = StreamCodec.of(WoodenCrucibleRecipe::encode, WoodenCrucibleRecipe::decode);

    private static WoodenCrucibleRecipe decode(RegistryFriendlyByteBuf buf) {
        final var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
        final var fluidId = Identifier.STREAM_CODEC.decode(buf);
        final var amount = ByteBufCodecs.INT.decode(buf);
        return new WoodenCrucibleRecipe(ingredient, fluidId, amount);
    }

    private static void encode(RegistryFriendlyByteBuf buf, WoodenCrucibleRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getIngredient());
        Identifier.STREAM_CODEC.encode(buf, recipe.getFluidId());
        ByteBufCodecs.INT.encode(buf, recipe.getAmount());
    }

    public static RecipeSerializer<WoodenCrucibleRecipe> serializer() {
        return new RecipeSerializer<>(CODEC, STREAM_CODEC);
    }
}
