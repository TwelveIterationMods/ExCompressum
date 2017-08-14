package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.item.*;
import net.blay09.mods.excompressum.tile.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(ExCompressum.MOD_ID)
public class ModBlocks {

	@GameRegistry.ObjectHolder(BlockCompressed.name)
	public static final Block compressedBlock = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockHeavySieve.name)
	public static final Block heavySieve = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockWoodenCrucible.name)
	public static final Block woodenCrucible = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockBait.name)
	public static final Block bait = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockAutoHammer.name)
	public static final Block autoHammer = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockAutoCompressedHammer.name)
	public static final Block autoCompressedHammer = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockAutoHeavySieve.name)
	public static final Block autoHeavySieve = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockAutoSieve.name)
	public static final Block autoSieve = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockManaSieve.name)
	public static final Block manaSieve = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockAutoCompressor.name)
	public static final Block autoCompressor = Blocks.AIR;

	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				new BlockCompressed().setRegistryName(BlockCompressed.name),
				new BlockHeavySieve().setRegistryName(BlockHeavySieve.name),
				new BlockWoodenCrucible().setRegistryName(BlockWoodenCrucible.name),
				new BlockBait().setRegistryName(BlockBait.name),
				new BlockAutoHammer().setRegistryName(BlockAutoHammer.name),
				new BlockAutoSieve().setRegistryName(BlockAutoSieve.name),
				new BlockAutoCompressedHammer().setRegistryName(BlockAutoCompressedHammer.name),
				new BlockAutoHeavySieve().setRegistryName(BlockAutoHeavySieve.name),
				new BlockAutoCompressor().setRegistryName(BlockAutoCompressor.name),
				new BlockManaSieve().setRegistryName(BlockManaSieve.name)
		);
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				new ItemBlockCompressed(compressedBlock).setRegistryName(BlockCompressed.name),
				new ItemBlockHeavySieve(heavySieve).setRegistryName(BlockHeavySieve.name),
				new ItemBlockWoodenCrucible(woodenCrucible).setRegistryName(BlockWoodenCrucible.name),
				new ItemBlockBait(bait).setRegistryName(BlockBait.name),
				new ItemBlock(autoHammer).setRegistryName(BlockAutoHammer.name),
				new ItemBlock(autoCompressedHammer).setRegistryName(BlockAutoCompressedHammer.name),
				new ItemBlock(autoSieve).setRegistryName(BlockAutoSieve.name),
				new ItemBlock(autoHeavySieve).setRegistryName(BlockAutoHeavySieve.name),
				new ItemBlock(autoCompressor).setRegistryName(BlockAutoCompressor.name),
				new ItemBlock(manaSieve).setRegistryName(BlockManaSieve.name)
		);
	}

	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(compressedBlock), 0, new ModelResourceLocation(BlockCompressed.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(heavySieve), 0, new ModelResourceLocation(BlockHeavySieve.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(woodenCrucible), 0, new ModelResourceLocation(BlockWoodenCrucible.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(bait), 0, new ModelResourceLocation(BlockBait.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(autoHammer), 0, new ModelResourceLocation(BlockAutoHammer.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(autoCompressedHammer), 0, new ModelResourceLocation(BlockAutoCompressedHammer.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(autoSieve), 0, new ModelResourceLocation(BlockAutoSieve.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(autoHeavySieve), 0, new ModelResourceLocation(BlockAutoHeavySieve.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(autoCompressor), 0, new ModelResourceLocation(BlockAutoCompressor.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(manaSieve), 0, new ModelResourceLocation(BlockManaSieve.registryName, "inventory"));
	}

	public static void registerTileEntity() {
		GameRegistry.registerTileEntity(TileWoodenCrucible.class, ExCompressum.MOD_ID + ":wooden_crucible");
		GameRegistry.registerTileEntity(TileHeavySieve.class, ExCompressum.MOD_ID + ":heavy_sieve");
		GameRegistry.registerTileEntity(TileBait.class, ExCompressum.MOD_ID + ":bait");

		GameRegistry.registerTileEntity(TileAutoHammer.class, ExCompressum.MOD_ID + ":auto_hammer");
		GameRegistry.registerTileEntity(TileAutoCompressedHammer.class, ExCompressum.MOD_ID + ":auto_compressed_hammer.json");
		GameRegistry.registerTileEntity(TileAutoSieve.class, ExCompressum.MOD_ID + ":auto_sieve");
		GameRegistry.registerTileEntity(TileAutoHeavySieve.class, ExCompressum.MOD_ID + ":auto_heavy_sieve");
		GameRegistry.registerTileEntity(TileAutoCompressor.class, ExCompressum.MOD_ID + ":auto_compressor");

		if (Loader.isModLoaded(Compat.BOTANIA)) {
			GameRegistry.registerTileEntity(TileAutoSieveMana.class, ExCompressum.MOD_ID + ":mana_sieve");
		}
	}
}
