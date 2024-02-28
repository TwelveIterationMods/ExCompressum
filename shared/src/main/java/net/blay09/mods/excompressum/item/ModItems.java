package net.blay09.mods.excompressum.item;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;

public class ModItems {
    public static DeferredObject<CreativeModeTab> creativeModeTab;

    public static Item chickenStick;
    public static Item compressedWoodenHammer;
    public static Item compressedStoneHammer;
    public static Item compressedIronHammer;
    public static Item compressedGoldenHammer;
    public static Item compressedDiamondHammer;
    public static Item compressedNetheriteHammer;
    public static Item compressedCrook;
    public static Item ironMesh;
    public static Item woodChippings;
    public static Item uncompressedCoal;
    public static Item batZapper;
    public static Item oreSmasher;
    public static Item uglySteelPlating;

    public static void initialize(BalmItems items) {
        items.registerItem(() -> chickenStick = new ChickenStickItem(items.itemProperties()), id("chicken_stick"));
        items.registerItem(() -> compressedWoodenHammer = new CompressedHammerItem(Tiers.WOOD, items.itemProperties()), id("compressed_wooden_hammer"));
        items.registerItem(() -> compressedStoneHammer = new CompressedHammerItem(Tiers.STONE, items.itemProperties()), id("compressed_stone_hammer"));
        items.registerItem(() -> compressedIronHammer = new CompressedHammerItem(Tiers.IRON, items.itemProperties()), id("compressed_iron_hammer"));
        items.registerItem(() -> compressedGoldenHammer = new CompressedHammerItem(Tiers.GOLD, items.itemProperties()), id("compressed_golden_hammer"));
        items.registerItem(() -> compressedDiamondHammer = new CompressedHammerItem(Tiers.DIAMOND, items.itemProperties()), id("compressed_diamond_hammer"));
        items.registerItem(() -> compressedNetheriteHammer = new CompressedHammerItem(Tiers.NETHERITE, items.itemProperties()), id("compressed_netherite_hammer"));
        items.registerItem(() -> compressedCrook = new CompressedCrookItem(items.itemProperties()), id("compressed_crook"));
        items.registerItem(() -> ironMesh = new IronMeshItem(items.itemProperties()), id("iron_mesh"));
        items.registerItem(() -> woodChippings = new WoodChippingItem(items.itemProperties()), id("wood_chippings"));
        items.registerItem(() -> uncompressedCoal = new UncompressedCoalItem(items.itemProperties()), id("uncompressed_coal"));
        items.registerItem(() -> batZapper = new BatZapperItem(items.itemProperties()), id("bat_zapper"));
        items.registerItem(() -> oreSmasher = new OreSmasherItem(items.itemProperties()), id("ore_smasher"));
        items.registerItem(() -> uglySteelPlating = new UglySteelPlatingItem(items.itemProperties()), id("ugly_steel_plating"));

        creativeModeTab = items.registerCreativeModeTab(() -> new ItemStack(ModItems.compressedDiamondHammer), id("excompressum"));
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(ExCompressum.MOD_ID, path);
    }

}
