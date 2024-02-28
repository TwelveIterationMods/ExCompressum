package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.block.*;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
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
        shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles[WoodenCrucibleType.ACACIA.ordinal()])
                .pattern("P P")
                .pattern("P P")
                .pattern("PSP")
                .define('P', Blocks.ACACIA_LOG)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles[WoodenCrucibleType.BIRCH.ordinal()])
                .pattern("P P")
                .pattern("P P")
                .pattern("PSP")
                .define('P', Blocks.BIRCH_LOG)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles[WoodenCrucibleType.MANGROVE.ordinal()])
                .pattern("P P")
                .pattern("P P")
                .pattern("PSP")
                .define('P', Blocks.MANGROVE_LOG)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles[WoodenCrucibleType.WARPED.ordinal()])
                .pattern("P P")
                .pattern("P P")
                .pattern("PSP")
                .define('P', Items.WARPED_STEM)
                .define('S', Items.WARPED_SLAB)
                .unlockedBy("has_warped_stem", has(Items.WARPED_STEM))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles[WoodenCrucibleType.CRIMSON.ordinal()])
                .pattern("P P")
                .pattern("P P")
                .pattern("PSP")
                .define('P', Items.CRIMSON_STEM)
                .define('S', Items.CRIMSON_SLAB)
                .unlockedBy("has_crimson_stem", has(Items.CRIMSON_STEM))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles[WoodenCrucibleType.DARK_OAK.ordinal()])
                .pattern("P P")
                .pattern("P P")
                .pattern("PSP")
                .define('P', Blocks.DARK_OAK_LOG)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles[WoodenCrucibleType.JUNGLE.ordinal()])
                .pattern("P P")
                .pattern("P P")
                .pattern("PSP")
                .define('P', Blocks.JUNGLE_LOG)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles[WoodenCrucibleType.OAK.ordinal()])
                .pattern("P P")
                .pattern("P P")
                .pattern("PSP")
                .define('P', Blocks.OAK_LOG)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles[WoodenCrucibleType.SPRUCE.ordinal()])
                .pattern("P P")
                .pattern("P P")
                .pattern("PSP")
                .define('P', Blocks.SPRUCE_LOG)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles[WoodenCrucibleType.CHERRY.ordinal()])
                .pattern("P P")
                .pattern("P P")
                .pattern("PSP")
                .define('P', Items.CHERRY_LOG)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves[HeavySieveType.ACACIA.ordinal()])
                .pattern("P P")
                .pattern("PPP")
                .pattern("S S")
                .define('P', Blocks.ACACIA_LOG)
                .define('S', Items.STICK)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves[HeavySieveType.BIRCH.ordinal()])
                .pattern("P P")
                .pattern("PPP")
                .pattern("S S")
                .define('P', Blocks.BIRCH_LOG)
                .define('S', Items.STICK)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves[HeavySieveType.MANGROVE.ordinal()])
                .pattern("P P")
                .pattern("PPP")
                .pattern("S S")
                .define('P', Blocks.MANGROVE_LOG)
                .define('S', Items.STICK)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves[HeavySieveType.WARPED.ordinal()])
                .pattern("P P")
                .pattern("PPP")
                .pattern("S S")
                .define('P', Items.WARPED_STEM)
                .define('S', Items.STICK)
                .unlockedBy("has_warped_stem", has(Items.WARPED_STEM))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves[HeavySieveType.CRIMSON.ordinal()])
                .pattern("P P")
                .pattern("PPP")
                .pattern("S S")
                .define('P', Items.CRIMSON_STEM)
                .define('S', Items.STICK)
                .unlockedBy("has_crimson_stem", has(Items.CRIMSON_STEM))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves[HeavySieveType.DARK_OAK.ordinal()])
                .pattern("P P")
                .pattern("PPP")
                .pattern("S S")
                .define('P', Blocks.DARK_OAK_LOG)
                .define('S', Items.STICK)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves[HeavySieveType.JUNGLE.ordinal()])
                .pattern("P P")
                .pattern("PPP")
                .pattern("S S")
                .define('P', Blocks.JUNGLE_LOG)
                .define('S', Items.STICK)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves[HeavySieveType.OAK.ordinal()])
                .pattern("P P")
                .pattern("PPP")
                .pattern("S S")
                .define('P', Blocks.OAK_LOG)
                .define('S', Items.STICK)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves[HeavySieveType.SPRUCE.ordinal()])
                .pattern("P P")
                .pattern("PPP")
                .pattern("S S")
                .define('P', Blocks.SPRUCE_LOG)
                .define('S', Items.STICK)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves[HeavySieveType.CHERRY.ordinal()])
                .pattern("P P")
                .pattern("PPP")
                .pattern("S S")
                .define('P', Items.CHERRY_LOG)
                .define('S', Items.STICK)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .save(exporter);

        shaped(RecipeCategory.MISC, ModItems.uglySteelPlating)
                .pattern(" I ")
                .pattern("IPI")
                .pattern(" I ")
                .define('P', Items.POTATO)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_potato", has(Items.POTATO))
                .save(exporter);

        shaped(RecipeCategory.MISC, ModItems.ironMesh)
                .pattern("II")
                .pattern("II")
                .define('I', Items.IRON_BARS)
                .unlockedBy("has_iron_bars", has(Items.IRON_BARS))
                .save(exporter);

        shaped(RecipeCategory.TOOLS, ModItems.oreSmasher)
                .pattern(" CD")
                .pattern(" SC")
                .pattern("S  ")
                .define('C', Items.CRAFTING_TABLE)
                .define('D', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(exporter);

        shaped(RecipeCategory.TOOLS, ModItems.batZapper)
                .pattern(" RG")
                .pattern(" SR")
                .pattern("S  ")
                .define('R', Items.REDSTONE)
                .define('G', Items.GLOWSTONE_DUST)
                .define('S', Items.STICK)
                .unlockedBy("has_glowstone_dust", has(Items.GLOWSTONE_DUST))
                .save(exporter);

        shaped(RecipeCategory.TOOLS, ModItems.compressedCrook)
                .pattern("CC")
                .pattern(" C")
                .pattern(" C")
                .define('C', ModItemTags.WOODEN_CROOKS)
                .unlockedBy("has_wooden_crook", has(ModItemTags.WOODEN_CROOKS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.autoCompressor)
                .pattern("CIC")
                .pattern("IBI")
                .pattern("CIC")
                .define('C', Items.CRAFTING_TABLE)
                .define('B', Items.IRON_BLOCK)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.rationingAutoCompressor)
                .pattern(" G ")
                .pattern("GCG")
                .pattern(" G ")
                .define('C', ModBlocks.autoCompressor)
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_auto_compressor", has(ModBlocks.autoCompressor))
                .save(exporter);

        shaped(RecipeCategory.TOOLS, ModItems.compressedWoodenHammer)
                .pattern("HHH")
                .pattern("HHH")
                .pattern("HHH")
                .define('H', ModItemTags.WOODEN_HAMMERS)
                .unlockedBy("has_wooden_hammer", has(ModItemTags.WOODEN_HAMMERS))
                .save(exporter);

        shaped(RecipeCategory.TOOLS, ModItems.compressedStoneHammer)
                .pattern("HHH")
                .pattern("HHH")
                .pattern("HHH")
                .define('H', ModItemTags.STONE_HAMMERS)
                .unlockedBy("has_stone_hammer", has(ModItemTags.STONE_HAMMERS))
                .save(exporter);

        shaped(RecipeCategory.TOOLS, ModItems.compressedIronHammer)
                .pattern("HHH")
                .pattern("HHH")
                .pattern("HHH")
                .define('H', ModItemTags.IRON_HAMMERS)
                .unlockedBy("has_iron_hammer", has(ModItemTags.IRON_HAMMERS))
                .save(exporter);

        shaped(RecipeCategory.TOOLS, ModItems.compressedGoldenHammer)
                .pattern("HHH")
                .pattern("HHH")
                .pattern("HHH")
                .define('H', ModItemTags.GOLDEN_HAMMERS)
                .unlockedBy("has_golden_hammer", has(ModItemTags.GOLDEN_HAMMERS))
                .save(exporter);

        shaped(RecipeCategory.TOOLS, ModItems.compressedDiamondHammer)
                .pattern("HHH")
                .pattern("HHH")
                .pattern("HHH")
                .define('H', ModItemTags.DIAMOND_HAMMERS)
                .unlockedBy("has_diamond_hammer", has(ModItemTags.DIAMOND_HAMMERS))
                .save(exporter);

        shaped(RecipeCategory.TOOLS, ModItems.compressedNetheriteHammer)
                .pattern("HHH")
                .pattern("HHH")
                .pattern("HHH")
                .define('H', ModItemTags.NETHERITE_HAMMERS)
                .unlockedBy("has_netherite_hammer", has(ModItemTags.NETHERITE_HAMMERS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.autoHammer)
                .pattern("IPI")
                .pattern("IHI")
                .pattern("IPI")
                .define('H', ModItemTags.DIAMOND_HAMMERS)
                .define('I', Items.IRON_INGOT)
                .define('P', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .unlockedBy("has_diamond_hammer", has(ModItemTags.DIAMOND_HAMMERS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.autoCompressedHammer)
                .pattern("BPB")
                .pattern("IHI")
                .pattern("BPB")
                .define('H', ModItems.compressedDiamondHammer)
                .define('I', Items.IRON_INGOT)
                .define('B', Items.IRON_BLOCK)
                .define('P', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .unlockedBy("has_compressed_diamond_hammer", has(ModItems.compressedDiamondHammer))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.autoSieve)
                .pattern("BGB")
                .pattern("GSG")
                .pattern("IGI")
                .define('G', Items.GLASS_PANE)
                .define('S', ModItemTags.SIEVES)
                .define('I', Items.IRON_INGOT)
                .define('B', Items.IRON_BLOCK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.autoHeavySieve)
                .pattern("BGB")
                .pattern("GSG")
                .pattern("IGI")
                .define('G', Items.GLASS_PANE)
                .define('S', ModItemTags.HEAVY_SIEVES)
                .define('I', Items.IRON_INGOT)
                .define('B', Items.IRON_BLOCK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModItems.uncompressedCoal)
                .requires(Items.COAL)
                .unlockedBy("has_coal", has(Items.COAL))
                .save(exporter);

        shapeless(RecipeCategory.MISC, Items.COAL)
                .requires(ModItems.uncompressedCoal, 9)
                .unlockedBy("has_uncompressed_coal", has(ModItems.uncompressedCoal))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.WOLF.ordinal()])
                .requires(Items.BEEF)
                .requires(Items.BONE)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.TURTLE.ordinal()])
                .requires(Items.SEAGRASS)
                .requires(Items.SEA_PICKLE)
                .unlockedBy("has_sea_pickle", has(Items.SEA_PICKLE))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.SQUID.ordinal()])
                .requires(ItemTags.FISHES)
                .requires(ItemTags.FISHES)
                .unlockedBy("has_fishes", has(ItemTags.FISHES))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.PIG.ordinal()])
                .requires(Items.CARROT, 2)
                .unlockedBy("has_carrot", has(Items.CARROT))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.RABBIT.ordinal()])
                .requires(Items.CARROT)
                .requires(Items.MELON_SEEDS)
                .unlockedBy("has_melon_seeds", has(Items.MELON_SEEDS))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.POLAR_BEAR.ordinal()])
                .requires(ItemTags.FISHES)
                .requires(Items.SNOWBALL)
                .unlockedBy("has_snowball", has(Items.SNOWBALL))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.PARROT.ordinal()])
                .requires(Items.GREEN_DYE)
                .requires(Items.RED_DYE)
                .unlockedBy("has_green_dye", has(Items.GREEN_DYE))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.OCELOT.ordinal()])
                .requires(Items.GUNPOWDER)
                .requires(ItemTags.FISHES)
                .unlockedBy("has_gunpowder", has(Items.GUNPOWDER))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.MOOSHROOM.ordinal()])
                .requires(Items.RED_MUSHROOM)
                .requires(Items.WHEAT)
                .unlockedBy("has_red_mushroom", has(Items.RED_MUSHROOM))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.LLAMA.ordinal()])
                .requires(Items.SUGAR)
                .requires(Items.WHEAT)
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.FOX.ordinal()])
                .requires(Items.SWEET_BERRIES)
                .requires(Items.RABBIT)
                .unlockedBy("has_sweet_berries", has(Items.SWEET_BERRIES))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.HORSE.ordinal()])
                .requires(Items.GOLDEN_APPLE)
                .requires(Items.GOLDEN_APPLE)
                .unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.DONKEY.ordinal()])
                .requires(Items.GOLDEN_CARROT)
                .requires(Items.GOLDEN_CARROT)
                .unlockedBy("has_golden_carrot", has(Items.GOLDEN_CARROT))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.COW.ordinal()])
                .requires(Items.WHEAT, 2)
                .unlockedBy("has_wheat", has(Items.WHEAT))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.SHEEP.ordinal()])
                .requires(Items.WHEAT, 2)
                .requires(Items.WHEAT_SEEDS, 2)
                .unlockedBy("has_wheat_seeds", has(Items.WHEAT_SEEDS))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.CAT.ordinal()])
                .requires(Items.CARROT, 2)
                .requires(Items.LEAD)
                .unlockedBy("has_lead", has(Items.LEAD))
                .save(exporter);

        shapeless(RecipeCategory.MISC, ModBlocks.baits[BaitType.CHICKEN.ordinal()])
                .requires(Items.WHEAT_SEEDS, 2)
                .unlockedBy("has_wheat_seeds", has(Items.WHEAT_SEEDS))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.ANDESITE.ordinal()])
                .requires(Items.ANDESITE, 9)
                .unlockedBy("has_andesite", has(Items.ANDESITE))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.DIORITE.ordinal()])
                .requires(Items.DIORITE, 9)
                .unlockedBy("has_diorite", has(Items.DIORITE))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.GRANITE.ordinal()])
                .requires(Items.GRANITE, 9)
                .unlockedBy("has_granite", has(Items.GRANITE))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.COBBLESTONE.ordinal()])
                .requires(Items.COBBLESTONE, 9)
                .unlockedBy("has_cobblestone", has(Items.COBBLESTONE))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.SAND.ordinal()])
                .requires(Items.SAND, 9)
                .unlockedBy("has_sand", has(Items.SAND))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.DIRT.ordinal()])
                .requires(Items.DIRT, 9)
                .unlockedBy("has_dirt", has(Items.DIRT))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.GRAVEL.ordinal()])
                .requires(Items.GRAVEL, 9)
                .unlockedBy("has_gravel", has(Items.GRAVEL))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.NETHERRACK.ordinal()])
                .requires(Items.NETHERRACK, 9)
                .unlockedBy("has_netherrack", has(Items.NETHERRACK))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.END_STONE.ordinal()])
                .requires(Items.END_STONE, 9)
                .unlockedBy("has_end_stone", has(Items.END_STONE))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.FLINT.ordinal()])
                .requires(Items.FLINT, 9)
                .unlockedBy("has_flint", has(Items.FLINT))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.SOUL_SAND.ordinal()])
                .requires(Items.SOUL_SAND, 9)
                .unlockedBy("has_soul_sand", has(Items.SOUL_SAND))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_ANDESITE.ordinal()])
                .requires(ModItemTags.CRUSHED_ANDESITES)
                .requires(ModItemTags.CRUSHED_ANDESITES)
                .requires(ModItemTags.CRUSHED_ANDESITES)
                .requires(ModItemTags.CRUSHED_ANDESITES)
                .requires(ModItemTags.CRUSHED_ANDESITES)
                .requires(ModItemTags.CRUSHED_ANDESITES)
                .requires(ModItemTags.CRUSHED_ANDESITES)
                .requires(ModItemTags.CRUSHED_ANDESITES)
                .requires(ModItemTags.CRUSHED_ANDESITES)
                .unlockedBy("has_crushed_andesite", has(ModItemTags.CRUSHED_ANDESITES))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_DIORITE.ordinal()])
                .requires(ModItemTags.CRUSHED_DIORITES)
                .requires(ModItemTags.CRUSHED_DIORITES)
                .requires(ModItemTags.CRUSHED_DIORITES)
                .requires(ModItemTags.CRUSHED_DIORITES)
                .requires(ModItemTags.CRUSHED_DIORITES)
                .requires(ModItemTags.CRUSHED_DIORITES)
                .requires(ModItemTags.CRUSHED_DIORITES)
                .requires(ModItemTags.CRUSHED_DIORITES)
                .requires(ModItemTags.CRUSHED_DIORITES)
                .unlockedBy("has_crushed_diorite", has(ModItemTags.CRUSHED_DIORITES))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_GRANITE.ordinal()])
                .requires(ModItemTags.CRUSHED_GRANITES)
                .requires(ModItemTags.CRUSHED_GRANITES)
                .requires(ModItemTags.CRUSHED_GRANITES)
                .requires(ModItemTags.CRUSHED_GRANITES)
                .requires(ModItemTags.CRUSHED_GRANITES)
                .requires(ModItemTags.CRUSHED_GRANITES)
                .requires(ModItemTags.CRUSHED_GRANITES)
                .requires(ModItemTags.CRUSHED_GRANITES)
                .requires(ModItemTags.CRUSHED_GRANITES)
                .unlockedBy("has_crushed_granite", has(ModItemTags.CRUSHED_GRANITES))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.DUST.ordinal()])
                .requires(ModItemTags.DUSTS)
                .requires(ModItemTags.DUSTS)
                .requires(ModItemTags.DUSTS)
                .requires(ModItemTags.DUSTS)
                .requires(ModItemTags.DUSTS)
                .requires(ModItemTags.DUSTS)
                .requires(ModItemTags.DUSTS)
                .requires(ModItemTags.DUSTS)
                .requires(ModItemTags.DUSTS)
                .unlockedBy("has_dust", has(ModItemTags.DUSTS))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_END_STONE.ordinal()])
                .requires(ModItemTags.CRUSHED_END_STONES)
                .requires(ModItemTags.CRUSHED_END_STONES)
                .requires(ModItemTags.CRUSHED_END_STONES)
                .requires(ModItemTags.CRUSHED_END_STONES)
                .requires(ModItemTags.CRUSHED_END_STONES)
                .requires(ModItemTags.CRUSHED_END_STONES)
                .requires(ModItemTags.CRUSHED_END_STONES)
                .requires(ModItemTags.CRUSHED_END_STONES)
                .requires(ModItemTags.CRUSHED_END_STONES)
                .unlockedBy("has_ender_gravel", has(ModItemTags.CRUSHED_END_STONES))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_NETHERRACK.ordinal()])
                .requires(ModItemTags.CRUSHED_NETHERRACKS)
                .requires(ModItemTags.CRUSHED_NETHERRACKS)
                .requires(ModItemTags.CRUSHED_NETHERRACKS)
                .requires(ModItemTags.CRUSHED_NETHERRACKS)
                .requires(ModItemTags.CRUSHED_NETHERRACKS)
                .requires(ModItemTags.CRUSHED_NETHERRACKS)
                .requires(ModItemTags.CRUSHED_NETHERRACKS)
                .requires(ModItemTags.CRUSHED_NETHERRACKS)
                .requires(ModItemTags.CRUSHED_NETHERRACKS)
                .unlockedBy("has_nether_gravel", has(ModItemTags.CRUSHED_NETHERRACKS))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.ANDESITE, 9)
                .requires(ModBlocks.compressedBlocks[CompressedBlockType.ANDESITE.ordinal()])
                .unlockedBy("has_compressed_andesite", has(ModBlocks.compressedBlocks[CompressedBlockType.ANDESITE.ordinal()]))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.DIORITE, 9)
                .requires(ModBlocks.compressedBlocks[CompressedBlockType.DIORITE.ordinal()])
                .unlockedBy("has_compressed_diorite", has(ModBlocks.compressedBlocks[CompressedBlockType.DIORITE.ordinal()]))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.GRANITE, 9)
                .requires(ModBlocks.compressedBlocks[CompressedBlockType.GRANITE.ordinal()])
                .unlockedBy("has_compressed_granite", has(ModBlocks.compressedBlocks[CompressedBlockType.GRANITE.ordinal()]))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE, 9)
                .requires(ModBlocks.compressedBlocks[CompressedBlockType.COBBLESTONE.ordinal()])
                .unlockedBy("has_compressed_cobblestone", has(ModBlocks.compressedBlocks[CompressedBlockType.COBBLESTONE.ordinal()]))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.SAND, 9)
                .requires(ModBlocks.compressedBlocks[CompressedBlockType.SAND.ordinal()])
                .unlockedBy("has_compressed_sand", has(ModBlocks.compressedBlocks[CompressedBlockType.SAND.ordinal()]))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.DIRT, 9)
                .requires(ModBlocks.compressedBlocks[CompressedBlockType.DIRT.ordinal()])
                .unlockedBy("has_compressed_dirt", has(ModBlocks.compressedBlocks[CompressedBlockType.DIRT.ordinal()]))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.GRAVEL, 9)
                .requires(ModBlocks.compressedBlocks[CompressedBlockType.GRAVEL.ordinal()])
                .unlockedBy("has_compressed_gravel", has(ModBlocks.compressedBlocks[CompressedBlockType.GRAVEL.ordinal()]))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.NETHERRACK, 9)
                .requires(ModBlocks.compressedBlocks[CompressedBlockType.NETHERRACK.ordinal()])
                .unlockedBy("has_compressed_netherrack", has(ModBlocks.compressedBlocks[CompressedBlockType.NETHERRACK.ordinal()]))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.END_STONE, 9)
                .requires(ModBlocks.compressedBlocks[CompressedBlockType.END_STONE.ordinal()])
                .unlockedBy("has_compressed_end_stone", has(ModBlocks.compressedBlocks[CompressedBlockType.END_STONE.ordinal()]))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, Items.FLINT, 9)
                .requires(ModBlocks.compressedBlocks[CompressedBlockType.FLINT.ordinal()])
                .unlockedBy("has_compressed_flint", has(ModBlocks.compressedBlocks[CompressedBlockType.FLINT.ordinal()]))
                .save(exporter);

        shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.SOUL_SAND, 9)
                .requires(ModBlocks.compressedBlocks[CompressedBlockType.SOUL_SAND.ordinal()])
                .unlockedBy("has_compressed_soul_sand", has(ModBlocks.compressedBlocks[CompressedBlockType.SOUL_SAND.ordinal()]))
                .save(exporter);

        // TODO shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.crushedAndesite, 9)
        // TODO         .requires(ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_ANDESITE.ordinal()])
        // TODO         .unlockedBy("has_compressed_crushed_andesite", has(ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_ANDESITE.ordinal()]))
        // TODO         .save(exporter);

        // TODO shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.crushedDiorite, 9)
        // TODO         .requires(ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_DIORITE.ordinal()])
        // TODO         .unlockedBy("has_compressed_crushed_diorite", has(ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_DIORITE.ordinal()]))
        // TODO         .save(exporter);

        // TODO shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.crushedGranite, 9)
        // TODO         .requires(ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_GRANITE.ordinal()])
        // TODO         .unlockedBy("has_compressed_crushed_granite", has(ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_GRANITE.ordinal()]))
        // TODO         .save(exporter);

        // TODO shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.dust, 9)
        // TODO         .requires(ModBlocks.compressedBlocks[CompressedBlockType.DUST.ordinal()])
        // TODO         .unlockedBy("has_compressed_dust", has(ModBlocks.compressedBlocks[CompressedBlockType.DUST.ordinal()]))
        // TODO         .save(exporter);

        // TODO shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.enderGravel, 9)
        // TODO         .requires(ModBlocks.compressedBlocks[CompressedBlockType.ENDER_GRAVEL.ordinal()])
        // TODO         .unlockedBy("has_compressed_ender_gravel", has(ModBlocks.compressedBlocks[CompressedBlockType.ENDER_GRAVEL.ordinal()]))
        // TODO         .save(exporter);

        // TODO shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.netherGravel, 9)
        // TODO         .requires(ModBlocks.compressedBlocks[CompressedBlockType.NETHER_GRAVEL.ordinal()])
        // TODO         .unlockedBy("has_compressed_nether_gravel", has(ModBlocks.compressedBlocks[CompressedBlockType.NETHER_GRAVEL.ordinal()]))
        // TODO         .save(exporter);
    }

}
