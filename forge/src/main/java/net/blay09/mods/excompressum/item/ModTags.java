package net.blay09.mods.excompressum.item;

import net.blay09.mods.balm.api.BalmRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static TagKey<Block> MINEABLE_WITH_HAMMER;
    public static TagKey<Block> MINEABLE_WITH_CROOK;
    public static TagKey<Item> HAMMERS;
    public static TagKey<Item> COMPRESSED_HAMMERS;
    public static TagKey<Item> CHICKEN_STICKS;
    public static TagKey<Item> COMPRESSED_CROOKS;

    public static void initialize(BalmRegistries registries) {
        MINEABLE_WITH_HAMMER = BlockTags.create(new ResourceLocation("excompressum", "mineable/hammer")); // TODO registries.getBlockTag(new ResourceLocation("excompressum", "mineable/hammer"));
        MINEABLE_WITH_CROOK = BlockTags.create(new ResourceLocation("excompressum", "mineable/crook")); // TODO registries.getBlockTag(new ResourceLocation("excompressum", "mineable/hammer"));
        HAMMERS = registries.getItemTag(new ResourceLocation("excompressum", "hammer"));
        COMPRESSED_HAMMERS = registries.getItemTag(new ResourceLocation("excompressum", "compressed_hammer"));
        CHICKEN_STICKS = registries.getItemTag(new ResourceLocation("excompressum", "chicken_sticks"));
        COMPRESSED_CROOKS = registries.getItemTag(new ResourceLocation("excompressum", "compressed_crooks"));
    }
}
