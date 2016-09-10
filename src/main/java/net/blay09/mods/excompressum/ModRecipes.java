package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.block.BlockCompressed;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes {

	public static void init(Configuration config) {
		registerItems(config);

		if (config.getBoolean("Heavy Sieve", "blocks", true, "If set to false, the recipe for the heavy sieve will be disabled.")) {
			for (int i = 0; i < 4; i++) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.heavySieve, 1, i), "p p", "ppp", "s s", 'p', new ItemStack(Blocks.LOG, 1, i), 's', "stickWood"));
			}
			for (int i = 0; i < 2; i++) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.heavySieve, 1, 4 + i), "p p", "ppp", "s s", 'p', new ItemStack(Blocks.LOG2, 1, i), 's', "stickWood"));
			}
		}

		if (config.getBoolean("Wooden Crucible", "blocks", true, "If set to false, the recipe for the wooden crucible will be disabled.")) {
			for (int i = 0; i < 4; i++) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.woodenCrucible, 1, i), "p p", "p p", "psp", 'p', new ItemStack(Blocks.LOG, 1, i), 's', "slabWood"));
			}
			for (int i = 0; i < 2; i++) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.woodenCrucible, 1, 4 + i), "p p", "p p", "psp", 'p', new ItemStack(Blocks.LOG2, 1, i), 's', "slabWood"));
			}
		}

		// Compressed Blocks
		registerCompressedBlocks(config);

		// Baits
		registerBaits(config);

		// RF Thingies
		if(ModAPIManager.INSTANCE.hasAPI("CoFHAPI")) {
			if (config.getBoolean("Auto Compressor", "blocks", true, "Set this to false to disable the recipe for the auto compressor.")) {
				GameRegistry.addRecipe(new ItemStack(ModBlocks.autoCompressor), "#I#", "IBI", "#I#", '#', Blocks.CRAFTING_TABLE, 'B', Blocks.IRON_BLOCK, 'I', Items.IRON_INGOT);
			}

			if (config.getBoolean("Auto Hammer", "blocks", true, "Set this to false to disable the recipe for the auto hammer.")) {
				ItemStack hammerDiamond = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_DIAMOND);
				if(hammerDiamond != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoHammer), "IPI", "IHI", "IPI", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'H', hammerDiamond, 'I', "ingotIron"));
				} else {
					ExCompressum.logger.warn("No diamond hammer found - Auto Hammer recipe will be disabled.");
				}
			}

			if (config.getBoolean("Auto Compressed Hammer", "blocks", true, "Set this to false to disable the recipe for the auto compressed hammer.")) {
				if(OreDictionary.getOres("blockSteel", false).isEmpty() || OreDictionary.getOres("ingotSteel", false).isEmpty()) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoCompressedHammer), "BPB", "IHI", "BPB", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'H', ModItems.compressedHammerDiamond, 'B', "blockIron", 'I', "ingotIron"));
				} else {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoCompressedHammer), "IPI", "IHI", "IPI", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'H', ModItems.compressedHammerDiamond, 'B', "blockSteel", 'I', "ingotSteel"));
				}
			}

			if (config.getBoolean("Auto Sieve", "blocks", true, "Set this to false to disable the recipe for the auto sieve.")) {
				ItemStack sieveBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.SIEVE);
				if(sieveBlock != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoSieve), "BGB", "GSG", "IGI", 'B', "blockIron", 'S', sieveBlock, 'G', "paneGlassColorless", 'I', "ingotIron"));
				} else {
					ExCompressum.logger.warn("No Sieve found - Auto Sieve recipe will be disabled.");
				}
			}

			if (config.getBoolean("Auto Heavy Sieve", "blocks", true, "Set this to false to disable the recipe for the auto heavy sieve.")) {
				if(OreDictionary.getOres("blockSteel", false).isEmpty() || OreDictionary.getOres("ingotSteel", false).isEmpty()) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoHeavySieve), "BGB", "GSG", "IGI", 'B', "blockIron", 'S', new ItemStack(ModBlocks.heavySieve, 1, OreDictionary.WILDCARD_VALUE), 'G', "paneGlassColorless", 'I', "ingotSteel"));
				} else {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoHeavySieve), "BGB", "GSG", "IGI", 'B', "blockSteel", 'S', new ItemStack(ModBlocks.heavySieve, 1, OreDictionary.WILDCARD_VALUE), 'G', "paneGlassColorless", 'I', "ingotSteel"));
				}
			}
		}

		// Botania Magicz
		if (Loader.isModLoaded("Botania")) {
			if (config.getBoolean("Mana Sieve", "blocks", true, "Set this to false to disable the recipe for the mana sieve.")) {
				Block manaSteelBlock = Block.REGISTRY.getObject(new ResourceLocation(Compat.BOTANIA, "storage"));
				if(manaSteelBlock != Blocks.AIR) {
					ItemStack sieveBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.SIEVE);
					if(sieveBlock != null) {
						ItemStack manaSteelBlockStack = new ItemStack(manaSteelBlock);
						GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.manaSieve), "BGB", "GSG", "IGI", 'B', manaSteelBlockStack, 'S', sieveBlock, 'G', "paneGlassColorless", 'I', "ingotManasteel"));
					} else {
						ExCompressum.logger.warn("No Sieve found - Mana Sieve recipe will be disabled.");
					}
				} else {
					ExCompressum.logger.warn("No Manasteel Block found - Mana Sieve recipe will be disabled.");
				}

			}
		}
	}

	private static void registerItems(Configuration config) {
		ItemStack itemSilkMesh = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.SILK_MESH);
		if (itemSilkMesh != null) {
			GameRegistry.addRecipe(new ItemStack(ModItems.heavySilkMesh), "##", "##", '#', itemSilkMesh);
		} else {
			ExCompressum.logger.warn("No Silk Mesh found - Heavy Silk Mesh recipe will be disabled.");
		}

		registerCompressedHammers(config);

		if(config.getBoolean("Compressed Crook", "items", true, "If set to false, the recipe for the compressed crook will be disabled.")) {
			ItemStack itemCrook = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.CROOK_WOODEN);
			if(itemCrook != null) {
				GameRegistry.addRecipe(new ItemStack(ModItems.compressedCrook), "## ", " # ", " # ", '#', itemCrook);
			} else {
				ExCompressum.logger.warn("No Crook found - Compressed Crook recipe will be disabled.");
			}
		}

		if(config.getBoolean("Uncompressed Coal", "items", true, "If set to true, coal can be crafted into eight pieces of Uncompressed Coal which can smelt one item at a time in a furnace.")) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.uncompressedCoal, 8), Items.COAL);
			GameRegistry.addShapelessRecipe(new ItemStack(Items.COAL), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal));
		}
		GameRegistry.registerFuelHandler(new IFuelHandler() {
			@Override
			public int getBurnTime(ItemStack itemStack) {
				if(itemStack.getItem() == ModItems.uncompressedCoal) {
					return 200;
				}
				return 0;
			}
		});

		if(config.getBoolean("Bat Zapper", "items", true, "If set to false, the recipe for the bat zapper will be disabled.")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.batZapper), " RG", " SR", "S  ", 'R', Items.REDSTONE, 'G', Items.GLOWSTONE_DUST, 'S', "stickWood"));
		}

		if(config.getBoolean("Ore Smasher", "items", true, "If set to false, the recipe for the ore smasher will be disabled.")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.oreSmasher), " CD", " SC", "S  ", 'C', Blocks.CRAFTING_TABLE, 'D', Items.DIAMOND, 'S', "stickWood"));
		}
	}

	private static void registerCompressedHammers(Configuration config) {
		if (config.getBoolean("Compressed Wooden Hammer", "items", true, "If set to false, the recipe for the compressed wooden hammer will be disabled.")) {
			ItemStack itemHammerWood = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_WOODEN);
			if (itemHammerWood != null) {
				GameRegistry.addRecipe(new ItemStack(ModItems.compressedHammerWood), "###", "###", "###", '#', itemHammerWood);
			} else {
				ExCompressum.logger.warn("No Wooden Hammer found - Compressed Wooden Hammer recipe will be disabled.");
			}
		}

		if (config.getBoolean("Compressed Stone Hammer", "items", true, "If set to false, the recipe for the compressed stone hammer will be disabled.")) {
			ItemStack itemHammerStone = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_STONE);
			if (itemHammerStone != null) {
				GameRegistry.addRecipe(new ItemStack(ModItems.compressedHammerStone), "###", "###", "###", '#', itemHammerStone);
			} else {
				ExCompressum.logger.warn("No Stone Hammer found - Compressed Stone Hammer recipe will be disabled.");
			}
		}

		if (config.getBoolean("Compressed Iron Hammer", "items", true, "If set to false, the recipe for the compressed iron hammer will be disabled.")) {
			ItemStack itemHammerIron = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_IRON);
			if (itemHammerIron != null) {
				GameRegistry.addRecipe(new ItemStack(ModItems.compressedHammerIron), "###", "###", "###", '#', itemHammerIron);
			} else {
				ExCompressum.logger.warn("No Iron Hammer found - Compressed Iron Hammer recipe will be disabled.");
			}
		}

		if (config.getBoolean("Compressed Gold Hammer", "items", true, "If set to false, the recipe for the compressed gold hammer will be disabled.")) {
			ItemStack itemHammerGold = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_GOLD);
			if (itemHammerGold != null) {
				GameRegistry.addRecipe(new ItemStack(ModItems.compressedHammerGold), "###", "###", "###", '#', itemHammerGold);
			} else {
				ExCompressum.logger.warn("No Gold Hammer found - Compressed Gold Hammer recipe will be disabled.");
			}
		}

		if (config.getBoolean("Compressed Diamond Hammer", "items", true, "If set to false, the recipe for the compressed diamond hammer will be disabled.")) {
			ItemStack itemHammerDiamond = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_DIAMOND);
			if (itemHammerDiamond != null) {
				GameRegistry.addRecipe(new ItemStack(ModItems.compressedHammerDiamond), "###", "###", "###", '#', itemHammerDiamond);
			} else {
				ExCompressum.logger.warn("No Diamond Hammer found - Compressed Diamond Hammer recipe will be disabled.");
			}
		}
	}

	private static void registerCompressedBlocks(Configuration config) {
		boolean exUtilsLoaded = Loader.isModLoaded("ExtraUtilities"); // TODO does ExUtils still do this?
		if (config.getBoolean("Compressed Dust", "blocks", true, "Set this to false to disable the recipe for the compressed dust.")) {
			ItemStack dustBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.DUST);
			if(dustBlock != null) {
				GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, 0), "###", "###", "###", '#', dustBlock);
				GameRegistry.addShapelessRecipe(ItemHandlerHelper.copyStackWithSize(dustBlock, 9), new ItemStack(ModBlocks.compressedBlock, 1, 0));
			} else {
				ExCompressum.logger.warn("No Dust found - Compressed Dust recipe will be disabled.");
			}
		}
		if (config.getBoolean("Compressed Cobblestone", "blocks", !exUtilsLoaded, "Set this to false to disable the recipe for the compressed cobblestone.")) {
			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.COBBLESTONE.ordinal()), "###", "###", "###", '#', Blocks.COBBLESTONE);
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.COBBLESTONE.ordinal()));
		}
		if (config.getBoolean("Compressed Gravel", "blocks", !exUtilsLoaded, "Set this to false to disable the recipe for the compressed gravel.")) {
			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.GRAVEL.ordinal()), "###", "###", "###", '#', Blocks.GRAVEL);
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.GRAVEL, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.GRAVEL.ordinal()));
		}
		if (config.getBoolean("Compressed Sand", "blocks", !exUtilsLoaded, "Set this to false to disable the recipe for the compressed sand.")) {
			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SAND.ordinal()), "###", "###", "###", '#', Blocks.SAND);
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.SAND, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SAND.ordinal()));
		}
		if (config.getBoolean("Compressed Dirt", "blocks", !exUtilsLoaded, "Set this to false to disable the recipe for the compressed dirt.")) {
			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.DIRT.ordinal()), "###", "###", "###", '#', Blocks.DIRT);
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.DIRT, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.DIRT.ordinal()));
		}
		if (config.getBoolean("Compressed Flint", "blocks", true, "Set this to false to disable the recipe for the compressed flint.")) {
			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.FLINT.ordinal()), "###", "###", "###", '#', Items.FLINT);
			GameRegistry.addShapelessRecipe(new ItemStack(Items.FLINT, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.FLINT.ordinal()));
		}
		if (config.getBoolean("Compressed Ender Gravel", "blocks", true, "Set this to false to disable the recipe for the compressed ender gravel.")) {
			ItemStack enderGravelBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.ENDER_GRAVEL);
			if(enderGravelBlock != null) {
				GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.ENDER_GRAVEL.ordinal()), "###", "###", "###", '#', enderGravelBlock);
				GameRegistry.addShapelessRecipe(ItemHandlerHelper.copyStackWithSize(enderGravelBlock, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.ENDER_GRAVEL.ordinal()));
			} else {
				ExCompressum.logger.warn("No Ender Gravel found - Compressed Ender Gravel recipe will be disabled.");
			}
		}
		if (config.getBoolean("Compressed Nether Gravel", "blocks", true, "Set this to false to disable the recipe for the compressed nether gravel.")) {
			ItemStack netherGravelBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.NETHER_GRAVEL);
			if(netherGravelBlock != null) {
				GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.NETHER_GRAVEL.ordinal()), "###", "###", "###", '#', netherGravelBlock);
				GameRegistry.addShapelessRecipe(ItemHandlerHelper.copyStackWithSize(netherGravelBlock, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.NETHER_GRAVEL.ordinal()));
			} else {
				ExCompressum.logger.warn("No Nether Gravel found - Compressed Nether Gravel recipe will be disabled.");
			}
		}
	}

	private static void registerBaits(Configuration config) {
		if (config.getBoolean("Wolf Bait", "blocks", true, "If set to false, the recipe for the wolf bait will be disabled.")) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 0), Items.BONE, Items.BEEF);
		}
		if (config.getBoolean("Ocelot Bait", "blocks", true, "If set to false, the recipe for the ocelot bait will be disabled.")) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 1), Items.GUNPOWDER, new ItemStack(Items.FISH, 1, OreDictionary.WILDCARD_VALUE));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.bait, 1, 1), Items.GUNPOWDER, "listAllfishraw")); // Pam's Fishies
		}
		if (config.getBoolean("Cow Bait", "blocks", true, "If set to false, the recipe for the cow bait will be disabled.")) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 2), Items.WHEAT, Items.WHEAT);
		}
		if (config.getBoolean("Pig Bait", "blocks", true, "If set to false, the recipe for the pig bait will be disabled.")) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 3), Items.CARROT, Items.CARROT);
		}
		if (config.getBoolean("Chicken Bait", "blocks", true, "If set to false, the recipe for the chicken bait will be disabled.")) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 4), Items.WHEAT_SEEDS, Items.WHEAT_SEEDS);
		}
		if (config.getBoolean("Sheep Bait", "blocks", true, "If set to false, the recipe for the sheep bait will be disabled.")) {
			ItemStack grassSeeds = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.SEEDS_GRASS);
			if(grassSeeds != null) {
				GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 5), grassSeeds, Items.WHEAT);
			} else {
				ExCompressum.logger.warn("No Grass Seeds found - Sheep Bait recipe will be disabled!");
			}
		}
		if (config.getBoolean("Squid Bait", "blocks", false, "If set to false, the recipe for the squid bait will be disabled.")) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 6), Items.FISH, Items.FISH);
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.bait, 1, 6), "listAllfishraw", "listAllfishraw")); // Pam's Fishies
		}
	}
}
