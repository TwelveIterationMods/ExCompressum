package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
	public static Item chickenStick;
	public static Item compressedHammerWood;
	public static Item compressedHammerStone;
	public static Item compressedHammerIron;
	public static Item compressedHammerGold;
	public static Item compressedHammerDiamond;
	public static Item doubleCompressedDiamondHammer;
	public static Item compressedCrook;
	public static Item ironMesh;
	public static Item woodChipping;
	public static Item uncompressedCoal;
	public static Item batZapper;
	public static Item oreSmasher;
	public static Item uglySteelPlating;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
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
				new ItemUncompressedCoal(new Item.Properties()).setRegistryName(ItemUncompressedCoal.name),
				new ItemBatZapper().setRegistryName(ItemBatZapper.name),
				new ItemOreSmasher().setRegistryName(ItemOreSmasher.name),
				new ItemUglySteelPlating().setRegistryName(ItemUglySteelPlating.name)
				);
	}

}
