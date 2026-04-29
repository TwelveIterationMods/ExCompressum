package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.tag.ModEntityTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityTypes;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagProvider extends EntityTypeTagsProvider {
    public ModEntityTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        tag(ModEntityTags.COMPRESSABLE).add(
                EntityTypes.ZOMBIE.builtInRegistryHolder().key(),
                EntityTypes.CREEPER.builtInRegistryHolder().key(),
                EntityTypes.SKELETON.builtInRegistryHolder().key(),
                EntityTypes.SPIDER.builtInRegistryHolder().key(),
                EntityTypes.CAVE_SPIDER.builtInRegistryHolder().key(),
                EntityTypes.SILVERFISH.builtInRegistryHolder().key(),
                EntityTypes.WITCH.builtInRegistryHolder().key(),
                EntityTypes.ENDERMAN.builtInRegistryHolder().key(),
                EntityTypes.BLAZE.builtInRegistryHolder().key(),
                EntityTypes.BEE.builtInRegistryHolder().key(),
                EntityTypes.CHICKEN.builtInRegistryHolder().key(),
                EntityTypes.SHEEP.builtInRegistryHolder().key(),
                EntityTypes.COW.builtInRegistryHolder().key(),
                EntityTypes.MOOSHROOM.builtInRegistryHolder().key(),
                EntityTypes.PIG.builtInRegistryHolder().key(),
                EntityTypes.GHAST.builtInRegistryHolder().key(),
                EntityTypes.DROWNED.builtInRegistryHolder().key(),
                EntityTypes.ELDER_GUARDIAN.builtInRegistryHolder().key(),
                EntityTypes.ENDERMITE.builtInRegistryHolder().key(),
                EntityTypes.CAT.builtInRegistryHolder().key(),
                EntityTypes.EVOKER.builtInRegistryHolder().key(),
                EntityTypes.HUSK.builtInRegistryHolder().key(),
                EntityTypes.HOGLIN.builtInRegistryHolder().key(),
                EntityTypes.GUARDIAN.builtInRegistryHolder().key(),
                EntityTypes.PIGLIN.builtInRegistryHolder().key(),
                EntityTypes.PIGLIN_BRUTE.builtInRegistryHolder().key(),
                EntityTypes.PILLAGER.builtInRegistryHolder().key(),
                EntityTypes.SHULKER.builtInRegistryHolder().key(),
                EntityTypes.STRAY.builtInRegistryHolder().key(),
                EntityTypes.VINDICATOR.builtInRegistryHolder().key(),
                EntityTypes.WITHER_SKELETON.builtInRegistryHolder().key(),
                EntityTypes.ZOGLIN.builtInRegistryHolder().key(),
                EntityTypes.ZOMBIE_VILLAGER.builtInRegistryHolder().key(),
                EntityTypes.ZOMBIFIED_PIGLIN.builtInRegistryHolder().key()
        );
    }

}
