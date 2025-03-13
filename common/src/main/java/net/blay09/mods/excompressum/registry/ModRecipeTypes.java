package net.blay09.mods.excompressum.registry;

import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipe;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipeImpl;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipeImpl;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipeImpl;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import static net.blay09.mods.excompressum.ExCompressum.id;

public class ModRecipeTypes {

    public static final ResourceLocation COMPRESSED_HAMMER = id("compressed_hammer");
    public static final ResourceLocation CHICKEN_STICK = id("chicken_stick");
    public static final ResourceLocation HAMMER = id("hammer");
    public static final ResourceLocation HEAVY_SIEVE_GENERATED = id("heavy_sieve_generated");
    public static final ResourceLocation HEAVY_SIEVE = id("heavy_sieve");
    public static final ResourceLocation WOODEN_CRUCIBLE = id("wooden_crucible");

    public static RecipeType<CompressedHammerRecipeImpl> compressedHammerRecipeType;
    public static RecipeType<ChickenStickRecipe> chickenStickRecipeType;
    public static RecipeType<HammerRecipeImpl> hammerRecipeType;
    public static RecipeType<GeneratedHeavySieveRecipe> generatedHeavySieveRecipeType;
    public static RecipeType<HeavySieveRecipeImpl> heavySieveRecipeType;
    public static RecipeType<WoodenCrucibleRecipe> woodenCrucibleRecipeType;

    public static RecipeSerializer<HeavySieveRecipeImpl> heavySieveRecipeSerializer;
    public static RecipeSerializer<GeneratedHeavySieveRecipe> generatedHeavySieveRecipeSerializer;
    public static RecipeSerializer<CompressedHammerRecipeImpl> compressedHammerRecipeSerializer;
    public static RecipeSerializer<HammerRecipeImpl> hammerRecipeSerializer;
    public static RecipeSerializer<ChickenStickRecipe> chickenStickRecipeSerializer;
    public static RecipeSerializer<WoodenCrucibleRecipe> woodenCrucibleRecipeSerializer;

    public static RecipeBookCategory heavySieveRecipeBookCategory;
    public static RecipeBookCategory hammerRecipeBookCategory;
    public static RecipeBookCategory woodenCrucibleRecipeBookCategory;
    public static RecipeBookCategory chickenStickRecipeBookCategory;
    public static RecipeBookCategory compressedHammerRecipeBookCategory;

    public static void initialize(BalmRecipes recipes) {
        recipes.registerRecipeType((identifier) -> compressedHammerRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return identifier.getPath();
            }
        }, COMPRESSED_HAMMER);
        recipes.registerRecipeSerializer(() -> compressedHammerRecipeSerializer = new CompressedHammerRecipeImpl.Serializer(), COMPRESSED_HAMMER);
        recipes.registerRecipeBookCategory(() -> compressedHammerRecipeBookCategory = new RecipeBookCategory(), COMPRESSED_HAMMER);
        recipes.registerRecipeType((identifier) -> chickenStickRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return identifier.getPath();
            }
        }, CHICKEN_STICK);
        recipes.registerRecipeSerializer(() -> chickenStickRecipeSerializer = new ChickenStickRecipe.Serializer(), CHICKEN_STICK);
        recipes.registerRecipeBookCategory(() -> chickenStickRecipeBookCategory = new RecipeBookCategory(), CHICKEN_STICK);
        recipes.registerRecipeType((identifier) -> hammerRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return identifier.getPath();
            }
        }, HAMMER);
        recipes.registerRecipeSerializer(() -> hammerRecipeSerializer = new HammerRecipeImpl.Serializer(), HAMMER);
        recipes.registerRecipeBookCategory(() -> hammerRecipeBookCategory = new RecipeBookCategory(), HAMMER);
        recipes.registerRecipeType((identifier) -> generatedHeavySieveRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return identifier.getPath();
            }
        }, HEAVY_SIEVE_GENERATED);
        recipes.registerRecipeSerializer(() -> generatedHeavySieveRecipeSerializer = new GeneratedHeavySieveRecipe.Serializer(), HEAVY_SIEVE_GENERATED);
        recipes.registerRecipeType((identifier) -> heavySieveRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return identifier.getPath();
            }
        }, HEAVY_SIEVE);
        recipes.registerRecipeSerializer(() -> heavySieveRecipeSerializer = new HeavySieveRecipeImpl.Serializer(), HEAVY_SIEVE);
        recipes.registerRecipeBookCategory(() -> heavySieveRecipeBookCategory = new RecipeBookCategory(), HEAVY_SIEVE);
        recipes.registerRecipeType((identifier) -> woodenCrucibleRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return identifier.getPath();
            }
        }, WOODEN_CRUCIBLE);
        recipes.registerRecipeSerializer(() -> woodenCrucibleRecipeSerializer = new WoodenCrucibleRecipe.Serializer(), WOODEN_CRUCIBLE);
        recipes.registerRecipeBookCategory(() -> woodenCrucibleRecipeBookCategory = new RecipeBookCategory(), WOODEN_CRUCIBLE);
    }
}
