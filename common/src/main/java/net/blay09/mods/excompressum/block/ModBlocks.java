package net.blay09.mods.excompressum.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class ModBlocks {

    public static Block[] compressedBlocks;
    public static Block[] heavySieves;
    public static Block[] woodenCrucibles;
    public static Block[] baits;
    public static Block autoHammer;
    public static Block autoCompressedHammer;
    public static Block autoHeavySieve;
    public static Block autoSieve;
    public static Block autoCompressor;
    public static Block rationingAutoCompressor;

    public static void initialize(BalmBlocks blocks) {
        blocks.register(() -> autoHammer = new AutoHammerBlock(), () -> itemBlock(autoHammer), id("auto_hammer"));
        blocks.register(() -> autoSieve = new AutoSieveBlock(), () -> itemBlock(autoSieve), id("auto_sieve"));
        blocks.register(() -> autoCompressedHammer = new AutoCompressedHammerBlock(), () -> itemBlock(autoCompressedHammer), id("auto_compressed_hammer"));
        blocks.register(() -> autoHeavySieve = new AutoHeavySieveBlock(), () -> itemBlock(autoHeavySieve), id("auto_heavy_sieve"));
        blocks.register(() -> autoCompressor = new AutoCompressorBlock(), () -> itemBlock(autoCompressor), id("auto_compressor"));
        blocks.register(() -> rationingAutoCompressor = new RationingAutoCompressorBlock(), () -> itemBlock(rationingAutoCompressor), id("rationing_auto_compressor"));

        compressedBlocks = registerEnumBlock(blocks, CompressedBlockType.values(), it -> CompressedBlock.namePrefix + it, CompressedBlock::new);
        heavySieves = registerEnumBlock(blocks, HeavySieveType.values(), it -> it + HeavySieveBlock.nameSuffix, HeavySieveBlock::new);
        woodenCrucibles = registerEnumBlock(blocks, WoodenCrucibleType.values(), it -> it + WoodenCrucibleBlock.nameSuffix, WoodenCrucibleBlock::new);
        baits = registerEnumBlock(blocks, BaitType.values(), it -> it + BaitBlock.nameSuffix, BaitBlock::new);
    }

    private static <T extends Enum<T> & StringRepresentable> Block[] registerEnumBlock(BalmBlocks blocks, T[] types, Function<String, String> nameFactory, Function<T, Block> factory) {
        Block[] blockArray = new Block[types.length];
        for (T type : types) {
            blocks.register(() -> blockArray[type.ordinal()] = factory.apply(type), () -> itemBlock(blockArray[type.ordinal()]), id(nameFactory.apply(type.getSerializedName())));
        }

        return blockArray;
    }

    private static BlockItem itemBlock(Block block) {
        return new BlockItem(block, Balm.getItems().itemProperties());
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(ExCompressum.MOD_ID, path);
    }

}
