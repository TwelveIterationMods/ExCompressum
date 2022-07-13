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
    public static Item woodChippings;
    public static Item uncompressedCoal;
    public static Item batZapper;
    public static Item oreSmasher;
    public static Item uglySteelPlating;
    public static Item manaHammer;

    public static final CreativeModeTab creativeModeTab = Balm.getItems().createCreativeModeTab(id("excompressum"), () -> new ItemStack(compressedHammerIron));

    public static void initialize(BalmItems items) {
        items.registerItem(() -> chickenStick = new ChickenStickItem(itemProperties()), id("chicken_stick"));
        items.registerItem(() -> compressedHammerWood = new CompressedHammerItem(Tiers.WOOD, itemProperties()), id("compressed_hammer_wood"));
        items.registerItem(() -> compressedHammerStone = new CompressedHammerItem(Tiers.STONE, itemProperties()), id("compressed_hammer_stone"));
        items.registerItem(() -> compressedHammerIron = new CompressedHammerItem(Tiers.IRON, itemProperties()), id("compressed_hammer_iron"));
        items.registerItem(() -> compressedHammerGold = new CompressedHammerItem(Tiers.GOLD, itemProperties()), id("compressed_hammer_gold"));
        items.registerItem(() -> compressedHammerDiamond = new CompressedHammerItem(Tiers.DIAMOND, itemProperties()), id("compressed_hammer_diamond"));
        items.registerItem(() -> compressedHammerNetherite = new CompressedHammerItem(Tiers.NETHERITE, itemProperties()), id("compressed_hammer_netherite"));
        items.registerItem(() -> doubleCompressedDiamondHammer = new UniversalHammerHeadItem(itemProperties()), id("universal_hammer_head"));
        items.registerItem(() -> compressedCrook = new CompressedCrookItem(itemProperties()), id("compressed_crook"));
        items.registerItem(() -> ironMesh = new IronMeshItem(itemProperties()), id("iron_mesh"));
        items.registerItem(() -> woodChippings = new WoodChippingItem(itemProperties()), id("wood_chippings"));
        items.registerItem(() -> uncompressedCoal = new UncompressedCoalItem(itemProperties()), id("uncompressed_coal"));
        items.registerItem(() -> batZapper = new BatZapperItem(itemProperties()), id("bat_zapper"));
        items.registerItem(() -> oreSmasher = new OreSmasherItem(itemProperties()), id("ore_smasher"));
        items.registerItem(() -> uglySteelPlating = new UglySteelPlatingItem(itemProperties()), id("ugly_steel_plating"));
        items.registerItem(() -> manaHammer = BotaniaCompat.createManaHammerItem(optionalItemProperties(BotaniaCompat.MOD_ID)), id("mana_hammer"));
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
