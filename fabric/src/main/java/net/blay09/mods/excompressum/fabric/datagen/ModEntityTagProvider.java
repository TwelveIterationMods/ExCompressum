package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.tag.ModEntityTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagProvider extends FabricTagProvider<EntityType<?>> {
    public ModEntityTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ENTITY_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        getOrCreateTagBuilder(ModEntityTags.COMPRESSABLE).add(
                EntityType.ZOMBIE,
                EntityType.CREEPER,
                EntityType.SKELETON,
                EntityType.SPIDER,
                EntityType.CAVE_SPIDER,
                EntityType.SILVERFISH,
                EntityType.WITCH,
                EntityType.ENDERMAN,
                EntityType.BLAZE,
                EntityType.BEE,
                EntityType.CHICKEN,
                EntityType.SHEEP,
                EntityType.COW,
                EntityType.MOOSHROOM,
                EntityType.PIG,
                EntityType.GHAST,
                EntityType.DROWNED,
                EntityType.ELDER_GUARDIAN,
                EntityType.ENDERMITE,
                EntityType.CAT,
                EntityType.EVOKER,
                EntityType.HUSK,
                EntityType.HOGLIN,
                EntityType.GUARDIAN,
                EntityType.PIGLIN,
                EntityType.PIGLIN_BRUTE,
                EntityType.PILLAGER,
                EntityType.SHULKER,
                EntityType.STRAY,
                EntityType.VINDICATOR,
                EntityType.WITHER_SKELETON,
                EntityType.ZOGLIN,
                EntityType.ZOMBIE_VILLAGER,
                EntityType.ZOMBIFIED_PIGLIN
        );
    }

}
