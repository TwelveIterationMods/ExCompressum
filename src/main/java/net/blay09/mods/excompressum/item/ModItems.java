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
    public static Item compressedHammerNetherite;
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
                chickenStick = new ChickenStickItem(itemProperties()).setRegistryName(ChickenStickItem.name),
                compressedHammerWood = new CompressedHammerItem(ItemTier.WOOD, itemProperties()).setRegistryName(CompressedHammerItem.namePrefix + "wood"),
                compressedHammerStone = new CompressedHammerItem(ItemTier.STONE, itemProperties()).setRegistryName(CompressedHammerItem.namePrefix + "stone"),
                compressedHammerIron = new CompressedHammerItem(ItemTier.IRON, itemProperties()).setRegistryName(CompressedHammerItem.namePrefix + "iron"),
                compressedHammerGold = new CompressedHammerItem(ItemTier.GOLD, itemProperties()).setRegistryName(CompressedHammerItem.namePrefix + "gold"),
                compressedHammerDiamond = new CompressedHammerItem(ItemTier.DIAMOND, itemProperties()).setRegistryName(CompressedHammerItem.namePrefix + "diamond"),
                compressedHammerNetherite = new CompressedHammerItem(ItemTier.NETHERITE, itemProperties()).setRegistryName(CompressedHammerItem.namePrefix + "netherite"),
                doubleCompressedDiamondHammer = new UniversalHammerHead(itemProperties()).setRegistryName(UniversalHammerHead.name),
                compressedCrook = new CompressedCrookItem(itemProperties()).setRegistryName(CompressedCrookItem.name),
                ironMesh = new IronMeshItem(itemProperties()).setRegistryName(IronMeshItem.name),
                woodChipping = new WoodChippingItem(itemProperties()).setRegistryName(WoodChippingItem.name),
                uncompressedCoal = new UncompressedCoalItem(itemProperties()).setRegistryName(UncompressedCoalItem.name),
                batZapper = new BatZapperItem(itemProperties()).setRegistryName(BatZapperItem.name),
                oreSmasher = new OreSmasherItem(itemProperties()).setRegistryName(OreSmasherItem.name),
                uglySteelPlating = new UglySteelPlatingItem(itemProperties()).setRegistryName(UglySteelPlatingItem.name)
        );
    }

    private static Item.Properties itemProperties() {
        return new Item.Properties().group(ExCompressum.itemGroup);
    }

}
