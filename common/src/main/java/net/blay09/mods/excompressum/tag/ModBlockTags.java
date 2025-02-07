package net.blay09.mods.excompressum.tag;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> MINEABLE_WITH_CROOK = TagKey.create(Registries.BLOCK, new ResourceLocation(ExCompressum.MOD_ID, "mineable/crook"));
    public static final TagKey<Block> MINEABLE_WITH_HAMMER = TagKey.create(Registries.BLOCK, new ResourceLocation(ExCompressum.MOD_ID, "mineable/hammer"));
}
