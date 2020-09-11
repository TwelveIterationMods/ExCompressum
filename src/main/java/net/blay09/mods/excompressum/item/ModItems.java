package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
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
                chickenStick = new ItemChickenStick(itemProperties()).setRegistryName(ItemChickenStick.name),
                compressedHammerWood = new ItemCompressedHammer(ItemTier.WOOD, "wood").setRegistryName(ItemCompressedHammer.namePrefix + "wood"),
                compressedHammerStone = new ItemCompressedHammer(ItemTier.STONE, "stone").setRegistryName(ItemCompressedHammer.namePrefix + "stone"),
                compressedHammerIron = new ItemCompressedHammer(ItemTier.IRON, "iron").setRegistryName(ItemCompressedHammer.namePrefix + "iron"),
                compressedHammerGold = new ItemCompressedHammer(ItemTier.GOLD, "gold").setRegistryName(ItemCompressedHammer.namePrefix + "gold"),
                compressedHammerDiamond = new ItemCompressedHammer(ItemTier.DIAMOND, "diamond").setRegistryName(ItemCompressedHammer.namePrefix + "diamond"),
                doubleCompressedDiamondHammer = new ItemDoubleCompressedDiamondHammer(itemProperties()).setRegistryName(ItemDoubleCompressedDiamondHammer.name),
                compressedCrook = new ItemCompressedCrook(itemProperties()).setRegistryName(ItemCompressedCrook.name),
                ironMesh = new ItemIronMesh(itemProperties()).setRegistryName(ItemIronMesh.name),
                woodChipping = new ItemWoodChipping(itemProperties()).setRegistryName(ItemWoodChipping.name),
                uncompressedCoal = new ItemUncompressedCoal(itemProperties()).setRegistryName(ItemUncompressedCoal.name),
                batZapper = new ItemBatZapper(itemProperties()).setRegistryName(ItemBatZapper.name),
                oreSmasher = new ItemOreSmasher(itemProperties()).setRegistryName(ItemOreSmasher.name),
                uglySteelPlating = new ItemUglySteelPlating(itemProperties()).setRegistryName(ItemUglySteelPlating.name)
        );
    }

    private static Item.Properties itemProperties() {
        return new Item.Properties().group(ExCompressum.itemGroup);
    }

}
