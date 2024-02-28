package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider<Item> {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ITEM, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(ModItemTags.SIEVES); // TODO

        final var heavySieves = getOrCreateTagBuilder(ModItemTags.HEAVY_SIEVES);
        for (Block heavySieve : ModBlocks.heavySieves) {
            heavySieves.add(heavySieve.asItem());
        }

        getOrCreateTagBuilder(ModItemTags.HAMMERS); // TODO

        getOrCreateTagBuilder(ModItemTags.CRUCIBLES).addTag(ModItemTags.WOODEN_CRUCIBLES);
        final var woodenCrucibles = getOrCreateTagBuilder(ModItemTags.WOODEN_CRUCIBLES);
        for (Block woodenCrucible : ModBlocks.woodenCrucibles) {
            woodenCrucibles.add(woodenCrucible.asItem());
        }

        getOrCreateTagBuilder(ModItemTags.CROOKS).addTag(ModItemTags.WOODEN_CROOKS);
        getOrCreateTagBuilder(ModItemTags.WOODEN_CROOKS); // TODO

        getOrCreateTagBuilder(ModItemTags.COMPRESSED_HAMMERS).add(ModItems.compressedWoodenHammer,
                ModItems.compressedStoneHammer,
                ModItems.compressedIronHammer,
                ModItems.compressedDiamondHammer,
                ModItems.compressedNetheriteHammer);

        getOrCreateTagBuilder(ModItemTags.COMPRESSED_CROOKS).addTag(ModItemTags.WOODEN_COMPRESSED_CROOKS);
        getOrCreateTagBuilder(ModItemTags.WOODEN_COMPRESSED_CROOKS).add(ModItems.compressedCrook);

        getOrCreateTagBuilder(ModItemTags.CHICKEN_STICKS).add(ModItems.chickenStick);

        final var baits = getOrCreateTagBuilder(ModItemTags.BAITS);
        for (Block bait : ModBlocks.baits) {
            baits.add(bait.asItem());
        }



    }
}
