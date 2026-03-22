package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.*;
import net.blay09.mods.excompressum.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;

import static net.blay09.mods.excompressum.ExCompressum.id;
import static net.minecraft.client.data.models.BlockModelGenerators.*;

public class ModModelProvider extends FabricModelProvider {

    private static final PropertyDispatch<VariantMutator> ROTATION_HORIZONTAL_FACING = PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
            .select(Direction.EAST, Y_ROT_90)
            .select(Direction.SOUTH, Y_ROT_180)
            .select(Direction.WEST, Y_ROT_270)
            .select(Direction.NORTH, NOP);

    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generators.createNonTemplateModelBlock(ModBlocks.autoCompressor.asBlock());
        generators.createNonTemplateModelBlock(ModBlocks.rationingAutoCompressor.asBlock());
        createUglifyableHorizontalFacingModel(generators, ModBlocks.autoHammer.asBlock());
        generators.registerSimpleItemModel(ModBlocks.autoHammer.asBlock(), Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "item/auto_hammer"));
        generators.registerSimpleItemModel(ModBlocks.autoCompressedHammer.asBlock(),
                Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "item/auto_compressed_hammer"));
        generators.registerSimpleItemModel(ModBlocks.autoSieve.asBlock(), Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "item/auto_sieve"));
        generators.registerSimpleItemModel(ModBlocks.autoHeavySieve.asBlock(), Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "item/auto_heavy_sieve"));
        createUglifyableHorizontalFacingModel(generators, ModBlocks.autoCompressedHammer.asBlock());
        createUglifyableHorizontalFacingModel(generators, ModBlocks.autoSieve.asBlock());
        createUglifyableHorizontalFacingModel(generators, ModBlocks.autoHeavySieve.asBlock());

        for (final var entry : ModBlocks.woodenCrucibles.entrySet()) {
            assert entry.getKey() != null;
            final var model = createSimpleRetexturedModel(generators, entry.getValue().asBlock(), entry.getKey().getBaseBlock(), id("block/wooden_crucible"));
            final var stateGenerator = BlockModelGenerators.createSimpleBlock(entry.getValue().asBlock(), plainVariant(model));
            generators.blockStateOutput.accept(stateGenerator);
        }

        for (final var entry : ModBlocks.heavySieves.entrySet()) {
            assert entry.getKey() != null;
            final var model = createSimpleRetexturedModel(generators, entry.getValue().asBlock(), entry.getKey().getBaseBlock(), id("block/heavy_sieve"));
            final var stateGenerator = BlockModelGenerators.createSimpleBlock(entry.getValue().asBlock(), plainVariant(model));
            generators.blockStateOutput.accept(stateGenerator);
        }

        for (final var compressedBlock : ModBlocks.compressedBlocks.values()) {
            generators.createTrivialCube(compressedBlock.asBlock());
        }

        for (final var entry : ModBlocks.baits.entrySet()) {
            assert entry.getKey() != null;
            createBait(generators, (BaitBlock) entry.getValue().asBlock(), entry.getKey());
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.chickenStick.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedWoodenHammer.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedStoneHammer.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedIronHammer.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedGoldenHammer.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedDiamondHammer.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedNetheriteHammer.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.compressedCrook.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.batZapper.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.oreSmasher.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.ironMesh.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.woodChippings.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.uncompressedCoal.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.uglySteelPlating.asItem(), ModelTemplates.FLAT_ITEM);

        final var baitTexture = Identifier.fromNamespaceAndPath("excompressum", "item/bait");
        final var baitOverlayTexture = Identifier.fromNamespaceAndPath("excompressum", "item/bait_overlay");
        itemModelGenerator.generateLayeredItem(Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "item/bait"), baitTexture, baitOverlayTexture);
    }

    private Identifier createSimpleRetexturedModel(BlockModelGenerators generators, Block block, Block baseBlock, Identifier template) {
        final var modelTemplate = new ModelTemplate(Optional.of(template), Optional.empty(), TextureSlot.TEXTURE);
        final var textureMapping = new TextureMapping();
        textureMapping.put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(baseBlock));
        return modelTemplate.create(block, textureMapping, generators.modelOutput);
    }

    private void createUglifyableHorizontalFacingModel(BlockModelGenerators generators, Block block) {
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(ModBlockStateProperties.UGLY)
                        .select(true, plainVariant(ModelLocationUtils.getModelLocation(block, "_ugly")))
                        .select(false, plainVariant(ModelLocationUtils.getModelLocation(block))))
                .with(ROTATION_HORIZONTAL_FACING));
    }

    private void createBait(BlockModelGenerators generators, BaitBlock block, BaitType baitType) {
        generators.createAirLikeBlock(block, baitType.getDisplayItemFirst().getItem());
        final var itemModelLocation = Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "item/bait");
        generators.itemModelOutput.accept(block.asItem(),
                ItemModelUtils.tintedModel(itemModelLocation,
                        new Constant(block.getBaitType().getItemColor(0)),
                        new Constant(block.getBaitType().getItemColor(1))));
    }
}
