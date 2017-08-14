package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.block.BlockBait;
import net.blay09.mods.excompressum.block.BlockCompressed;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.config.BlockConfig;
import net.blay09.mods.excompressum.config.ItemConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes {

	public static void init() {
		// TODO move recipes to json
//		registerItems();
//
//		if (BlockConfig.isEnabled("Heavy Sieve")) {
//			for (int i = 0; i < 4; i++) {
//				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.heavySieve, 1, i), "p p", "ppp", "s s", 'p', new ItemStack(Blocks.LOG, 1, i), 's', "stickWood"));
//			}
//			for (int i = 0; i < 2; i++) {
//				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.heavySieve, 1, 4 + i), "p p", "ppp", "s s", 'p', new ItemStack(Blocks.LOG2, 1, i), 's', "stickWood"));
//			}
//		}
//
//		if (BlockConfig.isEnabled("Wooden Crucible")) {
//			for (int i = 0; i < 4; i++) {
//				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.woodenCrucible, 1, i), "p p", "p p", "psp", 'p', new ItemStack(Blocks.LOG, 1, i), 's', "slabWood"));
//			}
//			for (int i = 0; i < 2; i++) {
//				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.woodenCrucible, 1, 4 + i), "p p", "p p", "psp", 'p', new ItemStack(Blocks.LOG2, 1, i), 's', "slabWood"));
//			}
//		}
//
//		// Compressed Blocks
//		registerCompressedBlocks();
//
//		// Baits
//		registerBaits();
//
//		// Fuwafuwagy™ Powered
//		if (BlockConfig.isEnabled("Auto Compressor")) {
//			GameRegistry.addRecipe(new ItemStack(ModBlocks.autoCompressor), "#I#", "IBI", "#I#", '#', Blocks.CRAFTING_TABLE, 'B', Blocks.IRON_BLOCK, 'I', Items.IRON_INGOT);
//		}
//
//		if (BlockConfig.isEnabled("Auto Hammer")) {
//			ItemStack hammerDiamond = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_DIAMOND);
//			if(!hammerDiamond.isEmpty()) {
//				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoHammer), "IPI", "IHI", "IPI", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'H', hammerDiamond, 'I', "ingotIron"));
//			} else {
//				ExCompressum.logger.warn("No diamond hammer found - Auto Hammer recipe will be disabled.");
//			}
//		}
//
//		if (BlockConfig.isEnabled("Auto Compressed Hammer")) {
//			if(OreDictionary.getOres("blockSteel", false).isEmpty() || OreDictionary.getOres("ingotSteel", false).isEmpty()) {
//				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoCompressedHammer), "BPB", "IHI", "BPB", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'H', ModItems.compressedHammerDiamond, 'B', "blockIron", 'I', "ingotIron"));
//			} else {
//				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoCompressedHammer), "IPI", "IHI", "IPI", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'H', ModItems.compressedHammerDiamond, 'B', "blockSteel", 'I', "ingotSteel"));
//			}
//		}
//
//		if (BlockConfig.isEnabled("Auto Sieve")) {
//			ItemStack sieveBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.SIEVE);
//			if(!sieveBlock.isEmpty()) {
//				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoSieve), "BGB", "GSG", "IGI", 'B', "blockIron", 'S', sieveBlock, 'G', "paneGlassColorless", 'I', "ingotIron"));
//			} else {
//				ExCompressum.logger.warn("No Sieve found - Auto Sieve recipe will be disabled.");
//			}
//		}
//
//		if (BlockConfig.isEnabled("Auto Heavy Sieve")) {
//			if(OreDictionary.getOres("blockSteel", false).isEmpty() || OreDictionary.getOres("ingotSteel", false).isEmpty()) {
//				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoHeavySieve), "BGB", "GSG", "IGI", 'B', "blockIron", 'S', new ItemStack(ModBlocks.heavySieve, 1, OreDictionary.WILDCARD_VALUE), 'G', "paneGlassColorless", 'I', "ingotIron"));
//			} else {
//				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoHeavySieve), "BGB", "GSG", "IGI", 'B', "blockSteel", 'S', new ItemStack(ModBlocks.heavySieve, 1, OreDictionary.WILDCARD_VALUE), 'G', "paneGlassColorless", 'I', "ingotSteel"));
//			}
//		}
//
//		// Botania Magicz
//		if (Loader.isModLoaded(Compat.BOTANIA)) {
//			if (BlockConfig.isEnabled("Mana Sieve")) {
//				ResourceLocation manaSteelLocation = new ResourceLocation(Compat.BOTANIA, "storage");
//				if(Block.REGISTRY.containsKey(manaSteelLocation)) {
//					ItemStack sieveBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.SIEVE);
//					if(!sieveBlock.isEmpty()) {
//						ItemStack manaSteelBlockStack = new ItemStack(Block.REGISTRY.getObject(manaSteelLocation));
//						GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.manaSieve), "BGB", "GSG", "IGI", 'B', manaSteelBlockStack, 'S', sieveBlock, 'G', "paneGlassColorless", 'I', "ingotManasteel"));
//					} else {
//						ExCompressum.logger.warn("No Sieve found - Mana Sieve recipe will be disabled.");
//					}
//				} else {
//					ExCompressum.logger.warn("No Manasteel Block found - Mana Sieve recipe will be disabled.");
//				}
//
//			}
//		}
	}

	private static void registerItems() {
//		if(ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.IRON_MESH).isEmpty()) {
//			GameRegistry.addRecipe(new ItemStack(ModItems.ironMesh), "##", "##", '#', Blocks.IRON_BARS);
//		}
//
//		registerCompressedHammers();
//
//		if(ItemConfig.isEnabled("Compressed Crook")) {
//			ItemStack itemCrook = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.CROOK_WOODEN);
//			if(!itemCrook.isEmpty()) {
//				GameRegistry.addRecipe(new ItemStack(ModItems.compressedCrook), "## ", " # ", " # ", '#', itemCrook);
//			} else {
//				ExCompressum.logger.warn("No Crook found - Compressed Crook recipe will be disabled.");
//			}
//		}
//
//		if(ItemConfig.isEnabled("Uncompressed Coal")) {
//			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.uncompressedCoal, 8), Items.COAL);
//			GameRegistry.addShapelessRecipe(new ItemStack(Items.COAL), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal));
//		}
		GameRegistry.registerFuelHandler(new IFuelHandler() {
			@Override
			public int getBurnTime(ItemStack itemStack) {
				if(itemStack.getItem() == ModItems.uncompressedCoal) {
					return 200;
				}
				return 0;
			}
		});

//		if(ItemConfig.isEnabled("Bat Zapper")) {
//			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.batZapper), " RG", " SR", "S  ", 'R', Items.REDSTONE, 'G', Items.GLOWSTONE_DUST, 'S', "stickWood"));
//		}
//
//		if(ItemConfig.isEnabled("Ore Smasher")) {
//			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.oreSmasher), " CD", " SC", "S  ", 'C', Blocks.CRAFTING_TABLE, 'D', Items.DIAMOND, 'S', "stickWood"));
//		}
	}

	private static void registerCompressedHammers() {
//		if (ItemConfig.isEnabled("Compressed Wooden Hammer")) {
//			ItemStack nihiloHammer = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_WOODEN);
//			if (!nihiloHammer.isEmpty()) {
//				if(nihiloHammer.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
//					nihiloHammer = new ItemStack(nihiloHammer.getItem(), 1, 0);
//				}
//				GameRegistry.addRecipe(new ItemStack(ModItems.compressedHammerWood), "###", "###", "###", '#', nihiloHammer);
//			} else {
//				ExCompressum.logger.warn("No Wooden Hammer found - Compressed Wooden Hammer recipe will be disabled.");
//			}
//		}
//
//		if (ItemConfig.isEnabled("Compressed Stone Hammer")) {
//			ItemStack nihiloHammer = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_STONE);
//			if (!nihiloHammer.isEmpty()) {
//				if(nihiloHammer.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
//					nihiloHammer = new ItemStack(nihiloHammer.getItem(), 1, 0);
//				}
//				GameRegistry.addRecipe(new ItemStack(ModItems.compressedHammerStone), "###", "###", "###", '#', nihiloHammer);
//			} else {
//				ExCompressum.logger.warn("No Stone Hammer found - Compressed Stone Hammer recipe will be disabled.");
//			}
//		}
//
//		if (ItemConfig.isEnabled("Compressed Iron Hammer")) {
//			ItemStack nihiloHammer = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_IRON);
//			if (!nihiloHammer.isEmpty()) {
//				if(nihiloHammer.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
//					nihiloHammer = new ItemStack(nihiloHammer.getItem(), 1, 0);
//				}
//				GameRegistry.addRecipe(new ItemStack(ModItems.compressedHammerIron), "###", "###", "###", '#', nihiloHammer);
//			} else {
//				ExCompressum.logger.warn("No Iron Hammer found - Compressed Iron Hammer recipe will be disabled.");
//			}
//		}
//
//		if (ItemConfig.isEnabled("Compressed Gold Hammer")) {
//			ItemStack nihiloHammer = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_GOLD);
//			if (!nihiloHammer.isEmpty()) {
//				if(nihiloHammer.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
//					nihiloHammer = new ItemStack(nihiloHammer.getItem(), 1, 0);
//				}
//				GameRegistry.addRecipe(new ItemStack(ModItems.compressedHammerGold), "###", "###", "###", '#', nihiloHammer);
//			} else {
//				ExCompressum.logger.warn("No Gold Hammer found - Compressed Gold Hammer recipe will be disabled.");
//			}
//		}
//
//		if (ItemConfig.isEnabled("Compressed Diamond Hammer")) {
//			ItemStack nihiloHammer = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_DIAMOND);
//			if (!nihiloHammer.isEmpty()) {
//				if(nihiloHammer.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
//					nihiloHammer = new ItemStack(nihiloHammer.getItem(), 1, 0);
//				}
//				GameRegistry.addRecipe(new ItemStack(ModItems.compressedHammerDiamond), "###", "###", "###", '#', nihiloHammer);
//			} else {
//				ExCompressum.logger.warn("No Diamond Hammer found - Compressed Diamond Hammer recipe will be disabled.");
//			}
//		}
	}

	private static void registerCompressedBlocks() {
//		if (BlockConfig.isEnabled("Compressed Dust")) {
//			OreDictionary.registerOre("compressed1xDust", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.DUST.ordinal()));
//			ItemStack dustBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.DUST);
//			if(!dustBlock.isEmpty()) {
//				GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, 0), "###", "###", "###", '#', dustBlock);
//				GameRegistry.addShapelessRecipe(ItemHandlerHelper.copyStackWithSize(dustBlock, 9), new ItemStack(ModBlocks.compressedBlock, 1, 0));
//			} else {
//				ExCompressum.logger.warn("No Dust found - Compressed Dust recipe will be disabled.");
//			}
//		}
//		if (BlockConfig.isEnabled("Compressed Cobblestone")) {
//			OreDictionary.registerOre("compressed1xCobblestone", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.COBBLESTONE.ordinal()));
//			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.COBBLESTONE.ordinal()), "###", "###", "###", '#', Blocks.COBBLESTONE);
//			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.COBBLESTONE.ordinal()));
//		}
//		if (BlockConfig.isEnabled("Compressed Gravel")) {
//			OreDictionary.registerOre("compressed1xGravel", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.GRAVEL.ordinal()));
//			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.GRAVEL.ordinal()), "###", "###", "###", '#', Blocks.GRAVEL);
//			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.GRAVEL, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.GRAVEL.ordinal()));
//		}
//		if (BlockConfig.isEnabled("Compressed Sand")) {
//			OreDictionary.registerOre("compressed1xSand", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SAND.ordinal()));
//			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SAND.ordinal()), "###", "###", "###", '#', Blocks.SAND);
//			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.SAND, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SAND.ordinal()));
//		}
//		if (BlockConfig.isEnabled("Compressed Dirt")) {
//			OreDictionary.registerOre("compressed1xDirt", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.DIRT.ordinal()));
//			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.DIRT.ordinal()), "###", "###", "###", '#', new ItemStack(Blocks.DIRT, 1, 0));
//			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.DIRT, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.DIRT.ordinal()));
//		}
//		if (BlockConfig.isEnabled("Compressed Flint")) {
//			OreDictionary.registerOre("compressed1xFlint", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.FLINT.ordinal()));
//			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.FLINT.ordinal()), "###", "###", "###", '#', Items.FLINT);
//			GameRegistry.addShapelessRecipe(new ItemStack(Items.FLINT, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.FLINT.ordinal()));
//		}
//		if (BlockConfig.isEnabled("Compressed Ender Gravel")) {
//			ItemStack enderGravelBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.ENDER_GRAVEL);
//			if(!enderGravelBlock.isEmpty()) {
//				OreDictionary.registerOre("compressed1xEnderGravel", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.ENDER_GRAVEL.ordinal()));
//				GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.ENDER_GRAVEL.ordinal()), "###", "###", "###", '#', enderGravelBlock);
//				GameRegistry.addShapelessRecipe(ItemHandlerHelper.copyStackWithSize(enderGravelBlock, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.ENDER_GRAVEL.ordinal()));
//			} else {
//				ExCompressum.logger.warn("No Ender Gravel found - Compressed Ender Gravel recipe will be disabled.");
//			}
//		}
//		if (BlockConfig.isEnabled("Compressed Nether Gravel")) {
//			ItemStack netherGravelBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.NETHER_GRAVEL);
//			if(!netherGravelBlock.isEmpty()) {
//				OreDictionary.registerOre("compressed1xNetherGravel", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.NETHER_GRAVEL.ordinal()));
//				GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.NETHER_GRAVEL.ordinal()), "###", "###", "###", '#', netherGravelBlock);
//				GameRegistry.addShapelessRecipe(ItemHandlerHelper.copyStackWithSize(netherGravelBlock, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.NETHER_GRAVEL.ordinal()));
//			} else {
//				ExCompressum.logger.warn("No Nether Gravel found - Compressed Nether Gravel recipe will be disabled.");
//			}
//		}
//		if (BlockConfig.isEnabled("Compressed Soul Sand")) {
//			OreDictionary.registerOre("compressed1xSoulsand", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SOUL_SAND.ordinal()));
//			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SOUL_SAND.ordinal()), "###", "###", "###", '#', Blocks.SOUL_SAND);
//			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.SOUL_SAND, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SOUL_SAND.ordinal()));
//		}
//		if (BlockConfig.isEnabled("Compressed Netherrack")) {
//			OreDictionary.registerOre("compressed1xNetherrack", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.NETHERRACK.ordinal()));
//			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.NETHERRACK.ordinal()), "###", "###", "###", '#', Blocks.NETHERRACK);
//			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.NETHERRACK, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.NETHERRACK.ordinal()));
//		}
//		if (BlockConfig.isEnabled("Compressed End Stone")) {
//			OreDictionary.registerOre("compressed1xEndStone", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.END_STONE.ordinal()));
//			GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.END_STONE.ordinal()), "###", "###", "###", '#', Blocks.END_STONE);
//			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.END_STONE, 9), new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.END_STONE.ordinal()));
//		}
	}

	private static void registerBaits() {
//		if (BlockConfig.isEnabled("Wolf Bait")) {
//			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.WOLF.ordinal()), Items.BONE, Items.BEEF);
//		}
//		if (BlockConfig.isEnabled("Ocelot Bait")) {
//			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.OCELOT.ordinal()), Items.GUNPOWDER, new ItemStack(Items.FISH, 1, OreDictionary.WILDCARD_VALUE));
//			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.OCELOT.ordinal()), Items.GUNPOWDER, "listAllfishraw")); // Pam's Fishies
//		}
//		if (BlockConfig.isEnabled("Cow Bait")) {
//			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.COW.ordinal()), Items.WHEAT, Items.WHEAT);
//		}
//		if (BlockConfig.isEnabled("Pig Bait")) {
//			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.PIG.ordinal()), Items.CARROT, Items.CARROT);
//		}
//		if (BlockConfig.isEnabled("Chicken Bait")) {
//			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.CHICKEN.ordinal()), Items.WHEAT_SEEDS, Items.WHEAT_SEEDS);
//		}
//		if (BlockConfig.isEnabled("Sheep Bait")) {
//			ItemStack grassSeeds = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.SEEDS_GRASS);
//			if(grassSeeds != null && !grassSeeds.isEmpty()) {
//				GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.SHEEP.ordinal()), grassSeeds, Items.WHEAT);
//			} else {
//				GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.SHEEP.ordinal()), Items.WHEAT_SEEDS, Items.WHEAT);
//			}
//		}
//		if (BlockConfig.isEnabled("Squid Bait")) {
//			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.SQUID.ordinal()), Items.FISH, Items.FISH);
//			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.SQUID.ordinal()), "listAllfishraw", "listAllfishraw")); // Pam's Fishies
//		}
//		if(BlockConfig.isEnabled("Rabbit Bait")) {
//			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.RABBIT.ordinal()), Items.CARROT, Items.MELON_SEEDS);
//		}
//		if(BlockConfig.isEnabled("Horse Bait")) {
//			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.HORSE.ordinal()), Items.GOLDEN_APPLE, Items.GOLDEN_APPLE);
//		}
//		if(BlockConfig.isEnabled("Donkey Bait")) {
//			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, BlockBait.Type.DONKEY.ordinal()), Items.GOLDEN_CARROT, Items.GOLDEN_CARROT);
//		}
	}
}
