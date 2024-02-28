package net.blay09.mods.excompressum.tag;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> BAITS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "baits"));
    public static final TagKey<Item> SIEVES = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "sieves"));
    public static final TagKey<Item> HEAVY_SIEVES = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "heavy_sieves"));
    public static final TagKey<Item> CHICKEN_STICKS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "chicken_sticks"));
    public static final TagKey<Item> HAMMERS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "hammers"));
    public static final TagKey<Item> WOODEN_HAMMERS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "wooden_hammers"));
    public static final TagKey<Item> STONE_HAMMERS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "stone_hammers"));
    public static final TagKey<Item> IRON_HAMMERS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "iron_hammers"));
    public static final TagKey<Item> GOLDEN_HAMMERS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "golden_hammers"));
    public static final TagKey<Item> DIAMOND_HAMMERS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "diamond_hammers"));
    public static final TagKey<Item> NETHERITE_HAMMERS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "netherite_hammers"));
    public static final TagKey<Item> COMPRESSED_HAMMERS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "compressed_hammers"));
    public static final TagKey<Item> WOODEN_CROOKS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "wooden_crooks"));
    public static final TagKey<Item> WOODEN_COMPRESSED_CROOKS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "wooden_compressed_crooks"));
    public static final TagKey<Item> WOODEN_CRUCIBLES = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "wooden_crucibles"));

    public static final TagKey<Item> CRUSHED_ANDESITES = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "crushed_andesites"));
    public static final TagKey<Item> CRUSHED_DIORITES = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "crushed_diorites"));
    public static final TagKey<Item> CRUSHED_GRANITES = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "crushed_granites"));
    public static final TagKey<Item> CRUSHED_NETHERRACKS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "crushed_netherracks"));
    public static final TagKey<Item> CRUSHED_END_STONES = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "crushed_end_stones"));
    public static final TagKey<Item> DUSTS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "dusts"));

    public static final TagKey<Item> CRUCIBLES = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "crucibles")); // TODO needed?
    public static final TagKey<Item> CROOKS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "crooks")); // TODO needed?
    public static final TagKey<Item> COMPRESSED_CROOKS = TagKey.create(Registries.ITEM, new ResourceLocation(ExCompressum.MOD_ID, "compressed_crooks")); // TODO needed?
}
