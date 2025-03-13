package net.blay09.mods.excompressum.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.tag.ModBlockTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

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
        items.registerItem((identifier) -> chickenStick = new ChickenStickItem(itemProperties(identifier)
                        .tool(ChickenStickItem.CHICKEN_STICK_TIER, ModBlockTags.MINEABLE_WITH_CHICKEN_STICK, 6f, -3.2f, 0f)),
                id("chicken_stick"));
        items.registerItem((identifier) -> compressedWoodenHammer = new CompressedHammerItem(itemProperties(identifier)
                        .tool(ToolMaterial.WOOD, ModBlockTags.MINEABLE_WITH_HAMMER, 6f, -3.2f, 0f)),
                id("compressed_wooden_hammer"));
        items.registerItem((identifier) -> compressedStoneHammer = new CompressedHammerItem(itemProperties(identifier)
                        .tool(ToolMaterial.STONE, ModBlockTags.MINEABLE_WITH_HAMMER, 7f, -3.2f, 0f)),
                id("compressed_stone_hammer"));
        items.registerItem((identifier) -> compressedIronHammer = new CompressedHammerItem(itemProperties(identifier)
                        .tool(ToolMaterial.IRON, ModBlockTags.MINEABLE_WITH_HAMMER, 6f, -3.1f, 0f)),
                id("compressed_iron_hammer"));
        items.registerItem((identifier) -> compressedGoldenHammer = new CompressedHammerItem(itemProperties(identifier)
                        .tool(ToolMaterial.GOLD, ModBlockTags.MINEABLE_WITH_HAMMER, 6f, -3f, 0f)),
                id("compressed_golden_hammer"));
        items.registerItem((identifier) -> compressedDiamondHammer = new CompressedHammerItem(itemProperties(identifier)
                        .tool(ToolMaterial.DIAMOND, ModBlockTags.MINEABLE_WITH_HAMMER, 5f, -3f, 0f)),
                id("compressed_diamond_hammer"));
        items.registerItem((identifier) -> compressedNetheriteHammer = new CompressedHammerItem(itemProperties(identifier)
                        .tool(ToolMaterial.NETHERITE, ModBlockTags.MINEABLE_WITH_HAMMER, 5f, -3f, 0f)),
                id("compressed_netherite_hammer"));
        items.registerItem((identifier) -> compressedCrook = new CompressedCrookItem(itemProperties(identifier)
                        .tool(ToolMaterial.WOOD, ModBlockTags.MINEABLE_WITH_CROOK, 6f, -3.2f, 0f)
                        .durability((int) (ToolMaterial.WOOD.durability() * 2 * ExCompressumConfig.getActive().tools.compressedCrookDurabilityMultiplier))),
                id("compressed_crook"));
        items.registerItem((identifier) -> ironMesh = new IronMeshItem(itemProperties(identifier)), id("iron_mesh"));
        items.registerItem((identifier) -> woodChippings = new WoodChippingItem(itemProperties(identifier)), id("wood_chippings"));
        items.registerItem((identifier) -> {
            uncompressedCoal = new UncompressedCoalItem(itemProperties(identifier));
            Balm.getHooks().setBurnTime(uncompressedCoal, 200);
            return uncompressedCoal;
        }, id("uncompressed_coal"));
        items.registerItem((identifier) -> batZapper = new BatZapperItem(itemProperties(identifier)), id("bat_zapper"));
        items.registerItem((identifier) -> oreSmasher = new OreSmasherItem(itemProperties(identifier)
                .shovel(ToolMaterial.DIAMOND, 6f, -3.2f)), id("ore_smasher"));
        items.registerItem((identifier) -> uglySteelPlating = new UglySteelPlatingItem(itemProperties(identifier)), id("ugly_steel_plating"));

        creativeModeTab = items.registerCreativeModeTab(() -> new ItemStack(ModItems.compressedDiamondHammer), id("excompressum"));
    }

    private static Item.Properties itemProperties(ResourceLocation identifier) {
        return new Item.Properties().setId(itemId(identifier));
    }

    private static ResourceKey<Item> itemId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.ITEM, identifier);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, path);
    }

}
