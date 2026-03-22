package net.blay09.mods.excompressum.registry.heavysieve;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.excompressum.api.recipe.HeavySieveRecipe;
import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ExCompressumSerializers;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Set;

public class HeavySieveRecipeImpl extends ExCompressumRecipe<RecipeInput> implements HeavySieveRecipe {

    private final Ingredient ingredient;
    private final LootTable lootTable;
    private final boolean waterlogged;
    private final Set<CommonMeshType> meshes;

    public HeavySieveRecipeImpl(Ingredient ingredient, LootTable lootTable, boolean waterlogged, List<CommonMeshType> meshes) {
        this.ingredient = ingredient;
        this.lootTable = lootTable;
        this.waterlogged = waterlogged;
        this.meshes = Set.copyOf(meshes);
    }

    @Override
    public RecipeSerializer<HeavySieveRecipeImpl> getSerializer() {
        return ModRecipeTypes.heavySieve.serializer();
    }

    @Override
    public RecipeType<HeavySieveRecipeImpl> getType() {
        return ModRecipeTypes.heavySieve.type();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeTypes.heavySieve.bookCategory();
    }

    @Override
    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public LootTable getLootTable() {
        return lootTable;
    }

    @Override
    public boolean isWaterlogged() {
        return waterlogged;
    }

    @Override
    public Set<CommonMeshType> getMeshes() {
        return meshes;
    }

    public List<CommonMeshType> getMeshesList() {
        return List.copyOf(meshes);
    }

    private static final MapCodec<HeavySieveRecipeImpl> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.ingredient),
            LootTable.DIRECT_CODEC.fieldOf("lootTable").forGetter(recipe -> recipe.lootTable),
            Codec.BOOL.fieldOf("waterlogged").forGetter(recipe -> recipe.waterlogged),
            CommonMeshType.CODEC.listOf().fieldOf("meshes").forGetter(recipe -> List.copyOf(recipe.meshes))
    ).apply(instance, HeavySieveRecipeImpl::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, HeavySieveRecipeImpl> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, HeavySieveRecipeImpl::getIngredient,
            ExCompressumSerializers.LOOT_TABLE_STREAM_CODEC, HeavySieveRecipeImpl::getLootTable,
            ByteBufCodecs.BOOL, HeavySieveRecipeImpl::isWaterlogged,
            CommonMeshType.LIST_STREAM_CODEC, HeavySieveRecipeImpl::getMeshesList,
            HeavySieveRecipeImpl::new);

    public static RecipeSerializer<HeavySieveRecipeImpl> serializer() {
        return new RecipeSerializer<>(CODEC, STREAM_CODEC);
    }
}
