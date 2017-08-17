package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.*;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.tile.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
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
				new BlockAutoCompressor().setRegistryName(BlockAutoCompressor.name)
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
				new ItemBlock(autoCompressor).setRegistryName(BlockAutoCompressor.name)
		);
	}

	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(autoCompressedHammer), 0, new ModelResourceLocation(BlockAutoCompressedHammer.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(autoSieve), 0, new ModelResourceLocation(BlockAutoSieve.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(autoHeavySieve), 0, new ModelResourceLocation(BlockAutoHeavySieve.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(autoCompressor), 0, new ModelResourceLocation(BlockAutoCompressor.registryName, "inventory"));

		// Baits
		ResourceLocation[] baitVariants = new ResourceLocation[BlockBait.Type.values.length];
        for(int i = 0; i < baitVariants.length; i++) {
            baitVariants[i] = new ResourceLocation(ExCompressum.MOD_ID, "bait_" + BlockBait.Type.values[i].getName());
        }
        Item baitItem = Item.getItemFromBlock(bait);
        ModelBakery.registerItemVariants(baitItem, baitVariants);
        ModelLoader.setCustomMeshDefinition(baitItem, itemStack -> {
			BlockBait.Type type = itemStack.getItemDamage() >= 0 && itemStack.getItemDamage() < BlockBait.Type.values.length ? BlockBait.Type.values[itemStack.getItemDamage()] : null;
			if(type != null) {
				return new ModelResourceLocation(new ResourceLocation(ExCompressum.MOD_ID, "bait_" + type.getName()), "inventory");
			} else {
				return new ModelResourceLocation("missingno");
			}
		});

        // Compressed Blocks
		ResourceLocation[] variants = new ResourceLocation[BlockCompressed.Type.values.length];
		for(int i = 0; i < variants.length; i++) {
			variants[i] = new ResourceLocation(ExCompressum.MOD_ID, "compressed_" + BlockCompressed.Type.values[i].getName());
		}
		Item compressedBlockItem = Item.getItemFromBlock(compressedBlock);
		ModelBakery.registerItemVariants(compressedBlockItem, variants);
		ModelLoader.setCustomMeshDefinition(compressedBlockItem, itemStack -> {
			BlockCompressed.Type type = BlockCompressed.Type.fromId(itemStack.getItemDamage());
			if(type != null) {
				return new ModelResourceLocation(new ResourceLocation(ExCompressum.MOD_ID, "compressed_" + type.getName()), "inventory");
			} else {
				return new ModelResourceLocation("missingno");
			}
		});
		ModelLoader.setCustomStateMapper(compressedBlock, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(new ResourceLocation(ExCompressum.MOD_ID, "compressed_" + state.getValue(BlockCompressed.VARIANT).getName()), "normal");
			}
		});

		// Wooden Crucible
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(woodenCrucible), itemStack -> {
			BlockWoodenCrucible.Type type = itemStack.getItemDamage() >= 0 && itemStack.getItemDamage() < BlockWoodenCrucible.Type.values.length ? BlockWoodenCrucible.Type.values[itemStack.getItemDamage()] : null;
			if(type != null) {
				return new ModelResourceLocation(BlockWoodenCrucible.registryName, "variant=" + type.getName());
			} else {
				return new ModelResourceLocation("missingno");
			}
		});

		// Heavy Sieve
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(heavySieve), itemStack -> {
			BlockHeavySieve.Type type = itemStack.getItemDamage() >= 0 && itemStack.getItemDamage() < BlockHeavySieve.Type.values.length ? BlockHeavySieve.Type.values[itemStack.getItemDamage()] : null;
			if (type != null) {
				if (ExRegistro.doMeshesHaveDurability()) {
					return new ModelResourceLocation(BlockHeavySieve.registryName, "variant=" + type.getName() + ",with_mesh=false");
				} else {
					return new ModelResourceLocation(BlockHeavySieve.registryName, "variant=" + type.getName() + ",with_mesh=false"); // it's false here too because it was a dumb idea based on wrong thinking; don't want to remove it now though
				}
			} else {
				return new ModelResourceLocation("missingno");
			}
		});

		// Auto Hammer
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(autoHammer), stack -> new ModelResourceLocation(BlockAutoHammer.registryName, "facing=north,hammer_mod=" + ExRegistro.getNihiloMod().getName()));
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileWoodenCrucible.class, ExCompressum.MOD_ID + ":wooden_crucible");
		GameRegistry.registerTileEntity(TileHeavySieve.class, ExCompressum.MOD_ID + ":heavy_sieve");
		GameRegistry.registerTileEntity(TileBait.class, ExCompressum.MOD_ID + ":bait");

		GameRegistry.registerTileEntity(TileAutoHammer.class, ExCompressum.MOD_ID + ":auto_hammer");
		GameRegistry.registerTileEntity(TileAutoCompressedHammer.class, ExCompressum.MOD_ID + ":auto_compressed_hammer.json");
		GameRegistry.registerTileEntity(TileAutoSieve.class, ExCompressum.MOD_ID + ":auto_sieve");
		GameRegistry.registerTileEntity(TileAutoHeavySieve.class, ExCompressum.MOD_ID + ":auto_heavy_sieve");
		GameRegistry.registerTileEntity(TileAutoCompressor.class, ExCompressum.MOD_ID + ":auto_compressor");
	}
}
