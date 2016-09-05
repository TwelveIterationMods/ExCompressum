package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.block.BlockAutoCompressor;
import net.blay09.mods.excompressum.block.BlockAutoHammer;
import net.blay09.mods.excompressum.block.BlockAutoHeavySieve;
import net.blay09.mods.excompressum.block.BlockAutoSieve;
import net.blay09.mods.excompressum.block.BlockBait;
import net.blay09.mods.excompressum.block.BlockCompressed;
import net.blay09.mods.excompressum.block.BlockHeavySieve;
import net.blay09.mods.excompressum.block.BlockManaSieve;
import net.blay09.mods.excompressum.item.ItemCompressedCrook;
import net.blay09.mods.excompressum.item.ItemCompressedHammer;
import net.blay09.mods.excompressum.item.ItemHeavySilkMesh;
import net.blay09.mods.excompressum.item.ItemOreSmasher;
import net.blay09.mods.excompressum.item.ItemUncompressedCoal;
import net.blay09.mods.excompressum.item.ItemWoodChipping;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

	public static void init(Configuration config) {
		ItemHeavySilkMesh.registerRecipes(config);
		ItemCompressedHammer.registerRecipes(config);
		ItemCompressedCrook.registerRecipes(config);
		ItemWoodChipping.registerRecipes(config);
		ItemUncompressedCoal.registerRecipes(config);

		if(config.getBoolean("Bat Zapper", "items", true, "If set to false, the recipe for the bat zapper will be disabled.")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.batZapper), " RG", " SR", "S  ", 'R', Items.REDSTONE, 'G', Items.GLOWSTONE_DUST, 'S', "stickWood"));
		}

		ItemOreSmasher.registerRecipes(config);

		BlockHeavySieve.registerRecipes(config);

		if (config.getBoolean("Wooden Crucible", "blocks", true, "If set to false, the recipe for the wooden crucible will be disabled.")) {
			for (int i = 0; i < 4; i++) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.woodenCrucible, 1, i), "p p", "p p", "psp", 'p', new ItemStack(Blocks.LOG, 1, i), 's', "slabWood"));
			}
			for (int i = 0; i < 2; i++) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.woodenCrucible, 1, 4 + i), "p p", "p p", "psp", 'p', new ItemStack(Blocks.LOG2, 1, i), 's', "slabWood"));
			}
		}

		BlockCompressed.registerRecipes(config);
		BlockBait.registerRecipes(config);
		BlockAutoCompressor.registerRecipes(config);
		BlockAutoHammer.registerRecipes(config);

		if(ModAPIManager.INSTANCE.hasAPI("CoFHAPI")) {
			if (config.getBoolean("Auto Compressed Hammer", "blocks", true, "Set this to false to disable the recipe for the auto compressed hammer.")) {
				if(OreDictionary.getOres("blockSteel", false).isEmpty() || OreDictionary.getOres("ingotSteel", false).isEmpty()) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoCompressedHammer), "BPB", "IHI", "BPB", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'H', ModItems.compressedHammerDiamond, 'B', "blockIron", 'I', "ingotIron"));
				} else {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoCompressedHammer), "IPI", "IHI", "IPI", 'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'H', ModItems.compressedHammerDiamond, 'B', "blockSteel", 'I', "ingotSteel"));
				}
			}
		}

		BlockManaSieve.registerRecipes(config);
		BlockAutoSieve.registerRecipes(config);
		BlockAutoHeavySieve.registerRecipes(config);
	}

}
