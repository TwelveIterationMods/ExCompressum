package net.blay09.mods.excompressum.registry;

import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipe;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipeSerializer;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipeSerializer;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipeImpl;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipeImpl;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipeSerializer;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipeSerializer;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipeImpl;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipeSerializer;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipeTypes {

    public static final ResourceLocation COMPRESSED_HAMMER = new ResourceLocation(ExCompressum.MOD_ID, "compressed_hammer");
    public static final ResourceLocation CHICKEN_STICK = new ResourceLocation(ExCompressum.MOD_ID, "chicken_stick");
    public static final ResourceLocation HAMMER = new ResourceLocation(ExCompressum.MOD_ID, "hammer");
    public static final ResourceLocation HEAVY_SIEVE_GENERATED = new ResourceLocation(ExCompressum.MOD_ID, "heavy_sieve_generated");
    public static final ResourceLocation HEAVY_SIEVE = new ResourceLocation(ExCompressum.MOD_ID, "heavy_sieve");
    public static final ResourceLocation WOODEN_CRUCIBLE = new ResourceLocation(ExCompressum.MOD_ID, "wooden_crucible");

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

    public static void initialize(BalmRecipes recipes) {
        recipes.registerRecipeType(() -> compressedHammerRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return COMPRESSED_HAMMER.getPath();
            }
        }, () -> compressedHammerRecipeSerializer = new CompressedHammerRecipeSerializer(), COMPRESSED_HAMMER);
        recipes.registerRecipeType(() -> chickenStickRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return CHICKEN_STICK.getPath();
            }
        }, () -> chickenStickRecipeSerializer = new ChickenStickRecipeSerializer(), CHICKEN_STICK);
        recipes.registerRecipeType(() -> hammerRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return HAMMER.getPath();
            }
        }, () -> hammerRecipeSerializer = new HammerRecipeSerializer(), HAMMER);
        recipes.registerRecipeType(() -> generatedHeavySieveRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return HEAVY_SIEVE_GENERATED.getPath();
            }
        }, () -> generatedHeavySieveRecipeSerializer = new GeneratedHeavySieveRecipeSerializer(), HEAVY_SIEVE_GENERATED);
        recipes.registerRecipeType(() -> heavySieveRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return HEAVY_SIEVE.getPath();
            }
        }, () -> heavySieveRecipeSerializer = new HeavySieveRecipeSerializer(), HEAVY_SIEVE);
        recipes.registerRecipeType(() -> woodenCrucibleRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return WOODEN_CRUCIBLE.getPath();
            }
        }, () -> woodenCrucibleRecipeSerializer = new WoodenCrucibleRecipeSerializer(), WOODEN_CRUCIBLE);

    }
}
