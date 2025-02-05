package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.*;
import net.blay09.mods.excompressum.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

import static net.minecraft.client.data.models.BlockModelGenerators.createHorizontalFacingDispatch;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generators.createNonTemplateModelBlock(ModBlocks.autoCompressor);
        generators.createNonTemplateModelBlock(ModBlocks.rationingAutoCompressor);
        createUglifyableHorizontalFacingModel(generators, ModBlocks.autoHammer);
        generators.registerSimpleItemModel(ModBlocks.autoHammer, ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "item/auto_hammer"));
        generators.registerSimpleItemModel(ModBlocks.autoCompressedHammer, ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "item/auto_compressed_hammer"));
        generators.registerSimpleItemModel(ModBlocks.autoSieve, ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "item/auto_sieve"));
        generators.registerSimpleItemModel(ModBlocks.autoHeavySieve, ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "item/auto_heavy_sieve"));
        createUglifyableHorizontalFacingModel(generators, ModBlocks.autoCompressedHammer);
        createUglifyableHorizontalFacingModel(generators, ModBlocks.autoSieve);
        createUglifyableHorizontalFacingModel(generators, ModBlocks.autoHeavySieve);

        for (final var woodenCrucibleType : WoodenCrucibleType.values()) {
            final var woodenCrucible = ModBlocks.woodenCrucibles[woodenCrucibleType.ordinal()];
            final var model = createSimpleRetexturedModel(generators, woodenCrucible, woodenCrucibleType.getBaseBlock(),
                    ResourceLocation.fromNamespaceAndPath("excompressum", "block/wooden_crucible"));
            final var stateGenerator = BlockModelGenerators.createSimpleBlock(woodenCrucible, model);
            generators.blockStateOutput.accept(stateGenerator);
        }

        for (final var heavySieveType : HeavySieveType.values()) {
            final var woodenCrucible = ModBlocks.heavySieves[heavySieveType.ordinal()];
            final var model = createSimpleRetexturedModel(generators, woodenCrucible, heavySieveType.getBaseBlock(),
                    ResourceLocation.fromNamespaceAndPath("excompressum", "block/heavy_sieve"));
            final var stateGenerator = BlockModelGenerators.createSimpleBlock(woodenCrucible, model);
            generators.blockStateOutput.accept(stateGenerator);
        }

        for (Block compressedBlock : ModBlocks.compressedBlocks) {
            generators.createTrivialCube(compressedBlock);
        }

        for (final var baitType : BaitType.values()) {
            final var bait = ModBlocks.baits[baitType.ordinal()];
            createBait(generators, (BaitBlock) bait, baitType);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.chickenStick, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedWoodenHammer, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedStoneHammer, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedIronHammer, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedGoldenHammer, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedDiamondHammer, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedNetheriteHammer, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedCrook, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.batZapper, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.oreSmasher, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.ironMesh, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.woodChippings, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.uncompressedCoal, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.uglySteelPlating, ModelTemplates.FLAT_ITEM);

        final var baitTexture = ResourceLocation.fromNamespaceAndPath("excompressum", "item/bait");
        final var baitOverlayTexture = ResourceLocation.fromNamespaceAndPath("excompressum", "item/bait_overlay");
        itemModelGenerator.generateLayeredItem(ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "item/bait"), baitTexture, baitOverlayTexture);
    }

    private ResourceLocation createSimpleRetexturedModel(BlockModelGenerators generators, Block block, Block baseBlock, ResourceLocation template) {
        final var modelTemplate = new ModelTemplate(Optional.of(template), Optional.empty(), TextureSlot.TEXTURE);
        final var textureMapping = new TextureMapping();
        textureMapping.put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(baseBlock));
        return modelTemplate.create(block, textureMapping, generators.modelOutput);
    }

    private void createUglifyableHorizontalFacingModel(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(ModBlockStateProperties.UGLY)
                        .select(true, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(block, "_ugly")))
                        .select(false, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(block))))
                .with(createHorizontalFacingDispatch()));
    }

    private void createBait(BlockModelGenerators generators, BaitBlock block, BaitType baitType) {
        generators.createAirLikeBlock(block, baitType.getDisplayItemFirst().getItem());
        final var itemModelLocation = ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "item/bait");
        generators.itemModelOutput.accept(block.asItem(),
                ItemModelUtils.tintedModel(itemModelLocation,
                        new Constant(block.getBaitType().getItemColor(0)),
                        new Constant(block.getBaitType().getItemColor(1))));
    }
}
