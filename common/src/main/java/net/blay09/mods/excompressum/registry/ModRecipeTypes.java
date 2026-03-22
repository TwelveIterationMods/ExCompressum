package net.blay09.mods.excompressum.registry;

import net.blay09.mods.balm.world.item.crafting.BalmRecipeTypeRegistrar;
import net.blay09.mods.balm.world.item.crafting.DeferredRecipeType;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipe;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipeImpl;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipeImpl;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipeImpl;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.minecraft.world.item.crafting.RecipeInput;

public class ModRecipeTypes {

    public static DeferredRecipeType<RecipeInput, CompressedHammerRecipeImpl> compressedHammer;
    public static DeferredRecipeType<RecipeInput, ChickenStickRecipe> chickenStick;
    public static DeferredRecipeType<RecipeInput, HammerRecipeImpl> hammer;
    public static DeferredRecipeType<RecipeInput, GeneratedHeavySieveRecipe> generatedHeavySieve;
    public static DeferredRecipeType<RecipeInput, HeavySieveRecipeImpl> heavySieve;
    public static DeferredRecipeType<RecipeInput, WoodenCrucibleRecipe> woodenCrucible;

    public static void initialize(BalmRecipeTypeRegistrar recipes) {
        compressedHammer = recipes.register("compressed_hammer", CompressedHammerRecipeImpl.class)
                .withSerializer(CompressedHammerRecipeImpl::serializer)
                .withRecipeBookCategory()
                .asDeferredRecipeType();

        chickenStick = recipes.register("chicken_stick", ChickenStickRecipe.class)
                .withSerializer(ChickenStickRecipe::serializer)
                .withRecipeBookCategory()
                .asDeferredRecipeType();

        hammer = recipes.register("hammer", HammerRecipeImpl.class)
                .withSerializer(HammerRecipeImpl::serializer)
                .withRecipeBookCategory()
                .asDeferredRecipeType();

        generatedHeavySieve = recipes.register("generated_heavy_sieve", GeneratedHeavySieveRecipe.class)
                .withSerializer(GeneratedHeavySieveRecipe::serializer)
                .withRecipeBookCategory()
                .asDeferredRecipeType();

        heavySieve = recipes.register("heavy_sieve", HeavySieveRecipeImpl.class)
                .withSerializer(HeavySieveRecipeImpl::serializer)
                .withRecipeBookCategory()
                .asDeferredRecipeType();

        woodenCrucible = recipes.register("wooden_crucible", WoodenCrucibleRecipe.class)
                .withSerializer(WoodenCrucibleRecipe::serializer)
                .withRecipeBookCategory()
                .asDeferredRecipeType();
    }
}
