package net.blay09.mods.excompressum.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import static net.blay09.mods.excompressum.ExCompressum.id;

public class ModEntityTags {
    public static final TagKey<EntityType<?>> COMPRESSABLE = TagKey.create(Registries.ENTITY_TYPE, id("compressable"));
}
