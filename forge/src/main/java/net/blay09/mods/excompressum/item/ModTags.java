package net.blay09.mods.excompressum.item;

import net.blay09.mods.balm.api.BalmRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static TagKey<Block> MINEABLE_WITH_HAMMER;
    public static TagKey<Item> HAMMER;

    public static void initialize(BalmRegistries registries) {
        MINEABLE_WITH_HAMMER = BlockTags.create(new ResourceLocation("excompressum", "mineable/hammer")); // TODO registries.getBlockTag(new ResourceLocation("excompressum", "mineable/hammer"));
        HAMMER = registries.getItemTag(new ResourceLocation("excompressum", "hammer"));
    }
}
