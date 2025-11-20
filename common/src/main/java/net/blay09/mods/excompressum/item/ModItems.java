package net.blay09.mods.excompressum.item;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.DeferredItem;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tag.ModBlockTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ToolMaterial;

public class ModItems {
    public static DeferredItem chickenStick;
    public static DeferredItem compressedWoodenHammer;
    public static DeferredItem compressedStoneHammer;
    public static DeferredItem compressedIronHammer;
    public static DeferredItem compressedGoldenHammer;
    public static DeferredItem compressedDiamondHammer;
    public static DeferredItem compressedNetheriteHammer;
    public static DeferredItem compressedCrook;
    public static DeferredItem ironMesh;
    public static DeferredItem woodChippings;
    public static DeferredItem uncompressedCoal;
    public static DeferredItem batZapper;
    public static DeferredItem oreSmasher;
    public static DeferredItem uglySteelPlating;

    public static void initialize(BalmItemRegistrar items) {
        chickenStick = items.register("chicken_stick", ChickenStickItem::new,
                it -> it.tool(ChickenStickItem.CHICKEN_STICK_TIER, ModBlockTags.MINEABLE_WITH_CHICKEN_STICK, 6f, -3.2f, 0f)
        ).asDeferredItem();
        compressedWoodenHammer = items.register("compressed_wooden_hammer", CompressedHammerItem::new,
                it -> it.tool(ToolMaterial.WOOD, ModBlockTags.MINEABLE_WITH_HAMMER, 6f, -3.2f, 0f)
        ).asDeferredItem();
        compressedStoneHammer = items.register("compressed_stone_hammer", CompressedHammerItem::new,
                it -> it.tool(ToolMaterial.STONE, ModBlockTags.MINEABLE_WITH_HAMMER, 7f, -3.2f, 0f)
        ).asDeferredItem();
        compressedIronHammer = items.register("compressed_iron_hammer", CompressedHammerItem::new,
                it -> it.tool(ToolMaterial.IRON, ModBlockTags.MINEABLE_WITH_HAMMER, 6f, -3.1f, 0f)
        ).asDeferredItem();
        compressedGoldenHammer = items.register("compressed_golden_hammer", CompressedHammerItem::new,
                it -> it.tool(ToolMaterial.GOLD, ModBlockTags.MINEABLE_WITH_HAMMER, 6f, -3f, 0f)
        ).asDeferredItem();
        compressedDiamondHammer = items.register("compressed_diamond_hammer", CompressedHammerItem::new,
                it -> it.tool(ToolMaterial.DIAMOND, ModBlockTags.MINEABLE_WITH_HAMMER, 5f, -3f, 0f)
        ).asDeferredItem();
        compressedNetheriteHammer = items.register("compressed_netherite_hammer", CompressedHammerItem::new,
                it -> it.tool(ToolMaterial.NETHERITE, ModBlockTags.MINEABLE_WITH_HAMMER, 5f, -3f, 0f)
        ).asDeferredItem();
        compressedCrook = items.register("compressed_crook", CompressedCrookItem::new,
                it -> it.tool(ToolMaterial.WOOD, ModBlockTags.MINEABLE_WITH_CROOK, 6f, -3.2f, 0f)
                        .durability((int) (ToolMaterial.WOOD.durability() * 4f))
        ).asDeferredItem();
        ironMesh = items.register("iron_mesh", IronMeshItem::new, it -> it).asDeferredItem();
        woodChippings = items.register("wood_chippings", WoodChippingItem::new, it -> it).asDeferredItem();
        uncompressedCoal = items.register("uncompressed_coal", (properties) -> {
            final var item = new UncompressedCoalItem(properties);
            Balm.hooks().setBurnTime(item, 200);
            return item;
        }, it -> it).asDeferredItem();
        batZapper = items.register("bat_zapper", BatZapperItem::new).asDeferredItem();
        oreSmasher = items.register("ore_smasher", OreSmasherItem::new,
                        it -> it.shovel(ToolMaterial.DIAMOND, 6f, -3.2f))
                .asDeferredItem();
        uglySteelPlating = items.register("ugly_steel_plating", UglySteelPlatingItem::new, it -> it).asDeferredItem();
    }

    public static void initialize(BalmCreativeModeTabRegistrar creativeModeTabs) {
        creativeModeTabs.register(ExCompressum.MOD_ID, (id, builder) ->
                builder.title(Component.translatable(id.toLanguageKey("itemGroup")))
                        .icon(() -> ModItems.compressedDiamondHammer.createStack())
                        .displayItems((displayParameters, output) -> {
                            output.accept(ModItems.compressedWoodenHammer);
                            output.accept(ModItems.compressedStoneHammer);
                            output.accept(ModItems.compressedIronHammer);
                            output.accept(ModItems.compressedGoldenHammer);
                            output.accept(ModItems.compressedDiamondHammer);
                            output.accept(ModItems.compressedNetheriteHammer);
                            output.accept(ModItems.compressedCrook);
                            output.accept(ModItems.ironMesh);
                            output.accept(ModItems.woodChippings);
                            output.accept(ModItems.uncompressedCoal);
                            output.accept(ModItems.batZapper);
                            output.accept(ModItems.oreSmasher);
                            output.accept(ModItems.uglySteelPlating);
                            output.accept(ModItems.chickenStick);
                        }));
    }

}
