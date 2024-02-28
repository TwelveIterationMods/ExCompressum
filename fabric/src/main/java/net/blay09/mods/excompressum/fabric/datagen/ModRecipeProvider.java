package net.blay09.mods.excompressum.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
        // TODO
        /*shaped(RecipeCategory.DECORATIONS, ModBlocks.waystone)
                .pattern(" S ")
                .pattern("SWS")
                .pattern("OOO")
                .define('S', Blocks.STONE_BRICKS)
                .define('W', ModItems.warpStone)
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy("has_warp_stone", has(ModItems.warpStone))
                .save(exporter);

        shapeless(RecipeCategory.DECORATIONS, ModBlocks.mossyWaystone)
                .requires(ModBlocks.waystone)
                .requires(Blocks.VINE, 3)
                .unlockedBy("has_waystone", has(ModBlocks.waystone))
                .save(exporter, new ResourceLocation("waystones", "mossy_waystone_from_vines"));*/
    }

}
