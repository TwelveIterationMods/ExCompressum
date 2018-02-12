package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(ExCompressum.MOD_ID)
public class ModItems {

	@GameRegistry.ObjectHolder(ItemChickenStick.name)
	public static Item chickenStick;

	@GameRegistry.ObjectHolder(ItemCompressedHammer.namePrefix + "wood")
	public static Item compressedHammerWood;

	@GameRegistry.ObjectHolder(ItemCompressedHammer.namePrefix + "stone")
	public static Item compressedHammerStone;

	@GameRegistry.ObjectHolder(ItemCompressedHammer.namePrefix + "iron")
	public static Item compressedHammerIron;

	@GameRegistry.ObjectHolder(ItemCompressedHammer.namePrefix + "gold")
	public static Item compressedHammerGold;

	@GameRegistry.ObjectHolder(ItemCompressedHammer.namePrefix + "diamond")
	public static Item compressedHammerDiamond;

	@GameRegistry.ObjectHolder(ItemDoubleCompressedDiamondHammer.name)
	public static Item doubleCompressedDiamondHammer;

	@GameRegistry.ObjectHolder(ItemCompressedCrook.name)
	public static Item compressedCrook;

	@GameRegistry.ObjectHolder(ItemIronMesh.name)
	public static Item ironMesh;

	@GameRegistry.ObjectHolder(ItemWoodChipping.name)
	public static Item woodChipping;

	@GameRegistry.ObjectHolder(ItemUncompressedCoal.name)
	public static Item uncompressedCoal;

	@GameRegistry.ObjectHolder(ItemBatZapper.name)
	public static Item batZapper;

	@GameRegistry.ObjectHolder(ItemOreSmasher.name)
	public static Item oreSmasher;

	@GameRegistry.ObjectHolder(ItemUglySteelPlating.name)
	public static Item uglySteelPlating;

	public static void register(IForgeRegistry<Item> registry) {
		registry.registerAll(
				new ItemChickenStick().setRegistryName(ItemChickenStick.name),
				new ItemCompressedHammer(Item.ToolMaterial.WOOD, "wood").setRegistryName(ItemCompressedHammer.namePrefix + "wood"),
				new ItemCompressedHammer(Item.ToolMaterial.STONE, "stone").setRegistryName(ItemCompressedHammer.namePrefix + "stone"),
				new ItemCompressedHammer(Item.ToolMaterial.IRON, "iron").setRegistryName(ItemCompressedHammer.namePrefix + "iron"),
				new ItemCompressedHammer(Item.ToolMaterial.GOLD, "gold").setRegistryName(ItemCompressedHammer.namePrefix + "gold"),
				new ItemCompressedHammer(Item.ToolMaterial.DIAMOND, "diamond").setRegistryName(ItemCompressedHammer.namePrefix + "diamond"),
				new ItemDoubleCompressedDiamondHammer().setRegistryName(ItemDoubleCompressedDiamondHammer.name),
				new ItemCompressedCrook().setRegistryName(ItemCompressedCrook.name),
				new ItemIronMesh().setRegistryName(ItemIronMesh.name),
				(woodChipping = new ItemWoodChipping().setRegistryName(ItemWoodChipping.name)), // immediately set woodChipping because we need it for the Ore Dictionary ... I should stop using ObjectHolder
				new ItemUncompressedCoal().setRegistryName(ItemUncompressedCoal.name),
				new ItemBatZapper().setRegistryName(ItemBatZapper.name),
				new ItemOreSmasher().setRegistryName(ItemOreSmasher.name),
				new ItemUglySteelPlating().setRegistryName(ItemUglySteelPlating.name)
				);
	}

	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(chickenStick, 0, new ModelResourceLocation(ItemChickenStick.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(compressedHammerWood, 0, new ModelResourceLocation(ExCompressum.MOD_ID + ":" + ItemCompressedHammer.namePrefix + "wood", "inventory"));
		ModelLoader.setCustomModelResourceLocation(compressedHammerStone, 0, new ModelResourceLocation(ExCompressum.MOD_ID + ":" + ItemCompressedHammer.namePrefix + "stone", "inventory"));
		ModelLoader.setCustomModelResourceLocation(compressedHammerIron, 0, new ModelResourceLocation(ExCompressum.MOD_ID + ":" + ItemCompressedHammer.namePrefix + "iron", "inventory"));
		ModelLoader.setCustomModelResourceLocation(compressedHammerGold, 0, new ModelResourceLocation(ExCompressum.MOD_ID + ":" + ItemCompressedHammer.namePrefix + "gold", "inventory"));
		ModelLoader.setCustomModelResourceLocation(compressedHammerDiamond, 0, new ModelResourceLocation(ExCompressum.MOD_ID + ":" + ItemCompressedHammer.namePrefix + "diamond", "inventory"));
		ModelLoader.setCustomModelResourceLocation(doubleCompressedDiamondHammer, 0, new ModelResourceLocation(ItemDoubleCompressedDiamondHammer.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(compressedCrook, 0, new ModelResourceLocation(ItemCompressedCrook.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(ironMesh, 0, new ModelResourceLocation(ItemIronMesh.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(woodChipping, 0, new ModelResourceLocation(ItemWoodChipping.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(uncompressedCoal, 0, new ModelResourceLocation(ItemUncompressedCoal.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(batZapper, 0, new ModelResourceLocation(ItemBatZapper.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(oreSmasher, 0, new ModelResourceLocation(ItemOreSmasher.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(uglySteelPlating, 0, new ModelResourceLocation(ItemUglySteelPlating.registryName, "inventory"));
	}

}
