package net.blay09.mods.excompressum.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.botania.BotaniaCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;

public class ModItems {

    public static Item chickenStick = new ChickenStickItem(itemProperties());
    public static Item compressedHammerWood = new CompressedHammerItem(Tiers.WOOD, itemProperties());
    public static Item compressedHammerStone = new CompressedHammerItem(Tiers.STONE, itemProperties());
    public static Item compressedHammerIron = new CompressedHammerItem(Tiers.IRON, itemProperties());
    public static Item compressedHammerGold = new CompressedHammerItem(Tiers.GOLD, itemProperties());
    public static Item compressedHammerDiamond = new CompressedHammerItem(Tiers.DIAMOND, itemProperties());
    public static Item compressedHammerNetherite = new CompressedHammerItem(Tiers.NETHERITE, itemProperties());
    public static Item doubleCompressedDiamondHammer = new UniversalHammerHeadItem(itemProperties());
    public static Item compressedCrook = new CompressedCrookItem(itemProperties());
    public static Item ironMesh = new IronMeshItem(itemProperties());
    public static Item woodChipping = new WoodChippingItem(itemProperties());
    public static Item uncompressedCoal = new UncompressedCoalItem(itemProperties());
    public static Item batZapper = new BatZapperItem(itemProperties());
    public static Item oreSmasher = new OreSmasherItem(itemProperties());
    public static Item uglySteelPlating = new UglySteelPlatingItem(itemProperties());
    public static Item manaHammer = BotaniaCompat.createManaHammerItem(optionalItemProperties(BotaniaCompat.MOD_ID));

    public static final CreativeModeTab creativeModeTab = Balm.getItems().createCreativeModeTab(id("excompressum"), () -> new ItemStack(compressedHammerIron));

    public static void initialize(BalmItems items) {
        items.registerItem(() -> chickenStick, id("chicken_stick"));
        items.registerItem(() -> compressedHammerWood, id("compressed_hammer_wood"));
        items.registerItem(() -> compressedHammerStone, id("compressed_hammer_stone"));
        items.registerItem(() -> compressedHammerIron, id("compressed_hammer_iron"));
        items.registerItem(() -> compressedHammerGold, id("compressed_hammer_gold"));
        items.registerItem(() -> compressedHammerDiamond, id("compressed_hammer_diamond"));
        items.registerItem(() -> compressedHammerNetherite, id("compressed_hammer_netherite"));
        items.registerItem(() -> doubleCompressedDiamondHammer, id("universal_hammer_head"));
        items.registerItem(() -> compressedCrook, id("compressed_crook"));
        items.registerItem(() -> ironMesh, id("iron_mesh"));
        items.registerItem(() -> woodChipping, id("wood_chipping"));
        items.registerItem(() -> uncompressedCoal, id("uncompressed_coal"));
        items.registerItem(() -> batZapper, id("bat_zapper"));
        items.registerItem(() -> oreSmasher, id("ore_smasher"));
        items.registerItem(() -> uglySteelPlating, id("ugly_steel_plating"));
        items.registerItem(() -> manaHammer, id("mana_hammer"));
    }

    private static Item.Properties itemProperties() {
        return new Item.Properties().tab(creativeModeTab);
    }

    private static Item.Properties optionalItemProperties(String modId) {
        Item.Properties properties = new Item.Properties();
        if (Balm.isModLoaded(modId)) {
            return properties.tab(creativeModeTab);
        }

        return properties;
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(ExCompressum.MOD_ID, path);
    }

}
