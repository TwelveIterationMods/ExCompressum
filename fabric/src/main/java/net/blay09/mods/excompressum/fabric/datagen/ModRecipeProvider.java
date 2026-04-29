package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.*;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles.get(WoodenCrucibleType.ACACIA))
                        .pattern("P P")
                        .pattern("P P")
                        .pattern("PSP")
                        .define('P', Blocks.ACACIA_LOG)
                        .define('S', ItemTags.WOODEN_SLABS)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles.get(WoodenCrucibleType.BIRCH))
                        .pattern("P P")
                        .pattern("P P")
                        .pattern("PSP")
                        .define('P', Blocks.BIRCH_LOG)
                        .define('S', ItemTags.WOODEN_SLABS)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles.get(WoodenCrucibleType.MANGROVE))
                        .pattern("P P")
                        .pattern("P P")
                        .pattern("PSP")
                        .define('P', Blocks.MANGROVE_LOG)
                        .define('S', ItemTags.WOODEN_SLABS)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles.get(WoodenCrucibleType.WARPED))
                        .pattern("P P")
                        .pattern("P P")
                        .pattern("PSP")
                        .define('P', Items.WARPED_STEM)
                        .define('S', Items.WARPED_SLAB)
                        .unlockedBy("has_warped_stem", has(Items.WARPED_STEM))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles.get(WoodenCrucibleType.CRIMSON))
                        .pattern("P P")
                        .pattern("P P")
                        .pattern("PSP")
                        .define('P', Items.CRIMSON_STEM)
                        .define('S', Items.CRIMSON_SLAB)
                        .unlockedBy("has_crimson_stem", has(Items.CRIMSON_STEM))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles.get(WoodenCrucibleType.DARK_OAK))
                        .pattern("P P")
                        .pattern("P P")
                        .pattern("PSP")
                        .define('P', Blocks.DARK_OAK_LOG)
                        .define('S', ItemTags.WOODEN_SLABS)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles.get(WoodenCrucibleType.JUNGLE))
                        .pattern("P P")
                        .pattern("P P")
                        .pattern("PSP")
                        .define('P', Blocks.JUNGLE_LOG)
                        .define('S', ItemTags.WOODEN_SLABS)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles.get(WoodenCrucibleType.OAK))
                        .pattern("P P")
                        .pattern("P P")
                        .pattern("PSP")
                        .define('P', Blocks.OAK_LOG)
                        .define('S', ItemTags.WOODEN_SLABS)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles.get(WoodenCrucibleType.SPRUCE))
                        .pattern("P P")
                        .pattern("P P")
                        .pattern("PSP")
                        .define('P', Blocks.SPRUCE_LOG)
                        .define('S', ItemTags.WOODEN_SLABS)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.woodenCrucibles.get(WoodenCrucibleType.CHERRY))
                        .pattern("P P")
                        .pattern("P P")
                        .pattern("PSP")
                        .define('P', Items.CHERRY_LOG)
                        .define('S', ItemTags.WOODEN_SLABS)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves.get(HeavySieveType.ACACIA))
                        .pattern("P P")
                        .pattern("PPP")
                        .pattern("S S")
                        .define('P', Blocks.ACACIA_LOG)
                        .define('S', Items.STICK)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves.get(HeavySieveType.BIRCH))
                        .pattern("P P")
                        .pattern("PPP")
                        .pattern("S S")
                        .define('P', Blocks.BIRCH_LOG)
                        .define('S', Items.STICK)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves.get(HeavySieveType.MANGROVE))
                        .pattern("P P")
                        .pattern("PPP")
                        .pattern("S S")
                        .define('P', Blocks.MANGROVE_LOG)
                        .define('S', Items.STICK)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves.get(HeavySieveType.WARPED))
                        .pattern("P P")
                        .pattern("PPP")
                        .pattern("S S")
                        .define('P', Items.WARPED_STEM)
                        .define('S', Items.STICK)
                        .unlockedBy("has_warped_stem", has(Items.WARPED_STEM))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves.get(HeavySieveType.CRIMSON))
                        .pattern("P P")
                        .pattern("PPP")
                        .pattern("S S")
                        .define('P', Items.CRIMSON_STEM)
                        .define('S', Items.STICK)
                        .unlockedBy("has_crimson_stem", has(Items.CRIMSON_STEM))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves.get(HeavySieveType.DARK_OAK))
                        .pattern("P P")
                        .pattern("PPP")
                        .pattern("S S")
                        .define('P', Blocks.DARK_OAK_LOG)
                        .define('S', Items.STICK)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves.get(HeavySieveType.JUNGLE))
                        .pattern("P P")
                        .pattern("PPP")
                        .pattern("S S")
                        .define('P', Blocks.JUNGLE_LOG)
                        .define('S', Items.STICK)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves.get(HeavySieveType.OAK))
                        .pattern("P P")
                        .pattern("PPP")
                        .pattern("S S")
                        .define('P', Blocks.OAK_LOG)
                        .define('S', Items.STICK)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves.get(HeavySieveType.SPRUCE))
                        .pattern("P P")
                        .pattern("PPP")
                        .pattern("S S")
                        .define('P', Blocks.SPRUCE_LOG)
                        .define('S', Items.STICK)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.heavySieves.get(HeavySieveType.CHERRY))
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

                shapeless(RecipeCategory.MISC, ModItems.uncompressedCoal, 9)
                        .requires(Items.COAL)
                        .unlockedBy("has_coal", has(Items.COAL))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, Items.COAL)
                        .requires(ModItems.uncompressedCoal, 9)
                        .unlockedBy("has_uncompressed_coal", has(ModItems.uncompressedCoal))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.WOLF))
                        .requires(Items.BEEF)
                        .requires(Items.BONE)
                        .unlockedBy("has_bone", has(Items.BONE))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.TURTLE))
                        .requires(Items.SEAGRASS)
                        .requires(Items.SEA_PICKLE)
                        .unlockedBy("has_sea_pickle", has(Items.SEA_PICKLE))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.SQUID))
                        .requires(ItemTags.FISHES)
                        .requires(ItemTags.FISHES)
                        .unlockedBy("has_fishes", has(ItemTags.FISHES))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.PIG))
                        .requires(Items.CARROT, 2)
                        .unlockedBy("has_carrot", has(Items.CARROT))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.RABBIT))
                        .requires(Items.CARROT)
                        .requires(Items.MELON_SEEDS)
                        .unlockedBy("has_melon_seeds", has(Items.MELON_SEEDS))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.POLAR_BEAR))
                        .requires(ItemTags.FISHES)
                        .requires(Items.SNOWBALL)
                        .unlockedBy("has_snowball", has(Items.SNOWBALL))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.PARROT))
                        .requires(Items.DYE.green())
                        .requires(Items.DYE.red())
                        .unlockedBy("has_green_dye", has(Items.DYE.green()))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.OCELOT))
                        .requires(Items.GUNPOWDER)
                        .requires(ItemTags.FISHES)
                        .unlockedBy("has_gunpowder", has(Items.GUNPOWDER))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.MOOSHROOM))
                        .requires(Items.RED_MUSHROOM)
                        .requires(Items.WHEAT)
                        .unlockedBy("has_red_mushroom", has(Items.RED_MUSHROOM))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.LLAMA))
                        .requires(Items.SUGAR)
                        .requires(Items.WHEAT)
                        .unlockedBy("has_sugar", has(Items.SUGAR))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.FOX))
                        .requires(Items.SWEET_BERRIES)
                        .requires(Items.RABBIT)
                        .unlockedBy("has_sweet_berries", has(Items.SWEET_BERRIES))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.HORSE))
                        .requires(Items.GOLDEN_APPLE)
                        .requires(Items.GOLDEN_APPLE)
                        .unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.DONKEY))
                        .requires(Items.GOLDEN_CARROT)
                        .requires(Items.GOLDEN_CARROT)
                        .unlockedBy("has_golden_carrot", has(Items.GOLDEN_CARROT))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.COW))
                        .requires(Items.WHEAT, 2)
                        .unlockedBy("has_wheat", has(Items.WHEAT))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.SHEEP))
                        .requires(Items.WHEAT, 2)
                        .requires(Items.WHEAT_SEEDS, 2)
                        .unlockedBy("has_wheat_seeds", has(Items.WHEAT_SEEDS))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.CAT))
                        .requires(Items.CARROT, 2)
                        .requires(Items.LEAD)
                        .unlockedBy("has_lead", has(Items.LEAD))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModBlocks.baits.get(BaitType.CHICKEN))
                        .requires(Items.WHEAT_SEEDS, 2)
                        .unlockedBy("has_wheat_seeds", has(Items.WHEAT_SEEDS))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.ANDESITE))
                        .requires(Items.ANDESITE, 9)
                        .unlockedBy("has_andesite", has(Items.ANDESITE))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.DIORITE))
                        .requires(Items.DIORITE, 9)
                        .unlockedBy("has_diorite", has(Items.DIORITE))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.GRANITE))
                        .requires(Items.GRANITE, 9)
                        .unlockedBy("has_granite", has(Items.GRANITE))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.COBBLESTONE))
                        .requires(Items.COBBLESTONE, 9)
                        .unlockedBy("has_cobblestone", has(Items.COBBLESTONE))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.SAND))
                        .requires(Items.SAND, 9)
                        .unlockedBy("has_sand", has(Items.SAND))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.DIRT))
                        .requires(Items.DIRT, 9)
                        .unlockedBy("has_dirt", has(Items.DIRT))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.GRAVEL))
                        .requires(Items.GRAVEL, 9)
                        .unlockedBy("has_gravel", has(Items.GRAVEL))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.NETHERRACK))
                        .requires(Items.NETHERRACK, 9)
                        .unlockedBy("has_netherrack", has(Items.NETHERRACK))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.END_STONE))
                        .requires(Items.END_STONE, 9)
                        .unlockedBy("has_end_stone", has(Items.END_STONE))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.FLINT))
                        .requires(Items.FLINT, 9)
                        .unlockedBy("has_flint", has(Items.FLINT))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.SOUL_SAND))
                        .requires(Items.SOUL_SAND, 9)
                        .unlockedBy("has_soul_sand", has(Items.SOUL_SAND))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_ANDESITE))
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

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_DIORITE))
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

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_GRANITE))
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

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.DUST))
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

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_END_STONE))
                        .requires(ModItemTags.CRUSHED_END_STONES)
                        .requires(ModItemTags.CRUSHED_END_STONES)
                        .requires(ModItemTags.CRUSHED_END_STONES)
                        .requires(ModItemTags.CRUSHED_END_STONES)
                        .requires(ModItemTags.CRUSHED_END_STONES)
                        .requires(ModItemTags.CRUSHED_END_STONES)
                        .requires(ModItemTags.CRUSHED_END_STONES)
                        .requires(ModItemTags.CRUSHED_END_STONES)
                        .requires(ModItemTags.CRUSHED_END_STONES)
                        .unlockedBy("has_crushed_end_stone", has(ModItemTags.CRUSHED_END_STONES))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_NETHERRACK))
                        .requires(ModItemTags.CRUSHED_NETHERRACKS)
                        .requires(ModItemTags.CRUSHED_NETHERRACKS)
                        .requires(ModItemTags.CRUSHED_NETHERRACKS)
                        .requires(ModItemTags.CRUSHED_NETHERRACKS)
                        .requires(ModItemTags.CRUSHED_NETHERRACKS)
                        .requires(ModItemTags.CRUSHED_NETHERRACKS)
                        .requires(ModItemTags.CRUSHED_NETHERRACKS)
                        .requires(ModItemTags.CRUSHED_NETHERRACKS)
                        .requires(ModItemTags.CRUSHED_NETHERRACKS)
                        .unlockedBy("has_crushed_netherrack", has(ModItemTags.CRUSHED_NETHERRACKS))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.ANDESITE, 9)
                        .requires(ModBlocks.compressedBlocks.get(CompressedBlockType.ANDESITE))
                        .unlockedBy("has_compressed_andesite", has(ModBlocks.compressedBlocks.get(CompressedBlockType.ANDESITE)))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.DIORITE, 9)
                        .requires(ModBlocks.compressedBlocks.get(CompressedBlockType.DIORITE))
                        .unlockedBy("has_compressed_diorite", has(ModBlocks.compressedBlocks.get(CompressedBlockType.DIORITE)))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.GRANITE, 9)
                        .requires(ModBlocks.compressedBlocks.get(CompressedBlockType.GRANITE))
                        .unlockedBy("has_compressed_granite", has(ModBlocks.compressedBlocks.get(CompressedBlockType.GRANITE)))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE, 9)
                        .requires(ModBlocks.compressedBlocks.get(CompressedBlockType.COBBLESTONE))
                        .unlockedBy("has_compressed_cobblestone", has(ModBlocks.compressedBlocks.get(CompressedBlockType.COBBLESTONE)))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.SAND, 9)
                        .requires(ModBlocks.compressedBlocks.get(CompressedBlockType.SAND))
                        .unlockedBy("has_compressed_sand", has(ModBlocks.compressedBlocks.get(CompressedBlockType.SAND)))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.DIRT, 9)
                        .requires(ModBlocks.compressedBlocks.get(CompressedBlockType.DIRT))
                        .unlockedBy("has_compressed_dirt", has(ModBlocks.compressedBlocks.get(CompressedBlockType.DIRT)))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.GRAVEL, 9)
                        .requires(ModBlocks.compressedBlocks.get(CompressedBlockType.GRAVEL))
                        .unlockedBy("has_compressed_gravel", has(ModBlocks.compressedBlocks.get(CompressedBlockType.GRAVEL)))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.NETHERRACK, 9)
                        .requires(ModBlocks.compressedBlocks.get(CompressedBlockType.NETHERRACK))
                        .unlockedBy("has_compressed_netherrack", has(ModBlocks.compressedBlocks.get(CompressedBlockType.NETHERRACK)))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.END_STONE, 9)
                        .requires(ModBlocks.compressedBlocks.get(CompressedBlockType.END_STONE))
                        .unlockedBy("has_compressed_end_stone", has(ModBlocks.compressedBlocks.get(CompressedBlockType.END_STONE)))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Items.FLINT, 9)
                        .requires(ModBlocks.compressedBlocks.get(CompressedBlockType.FLINT))
                        .unlockedBy("has_compressed_flint", has(ModBlocks.compressedBlocks.get(CompressedBlockType.FLINT)))
                        .save(exporter);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.SOUL_SAND, 9)
                        .requires(ModBlocks.compressedBlocks.get(CompressedBlockType.SOUL_SAND))
                        .unlockedBy("has_compressed_soul_sand", has(ModBlocks.compressedBlocks.get(CompressedBlockType.SOUL_SAND)))
                        .save(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return ExCompressum.MOD_ID;
    }
}
