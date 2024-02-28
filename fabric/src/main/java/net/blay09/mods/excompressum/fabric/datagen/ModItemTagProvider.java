package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider<Item> {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ITEM, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(ModItemTags.SIEVES)
                .addOptional(sequentia("acacia_sieve"))
                .addOptional(sequentia("birch_sieve"))
                .addOptional(sequentia("dark_oak_sieve"))
                .addOptional(sequentia("jungle_sieve"))
                .addOptional(sequentia("oak_sieve"))
                .addOptional(sequentia("spruce_sieve"))
                .addOptional(sequentia("cherry_sieve"))
                .addOptional(sequentia("mangrove_sieve"))
                .addOptional(sequentia("warped_sieve"))
                .addOptional(sequentia("crimson_sieve"));

        final var heavySieves = getOrCreateTagBuilder(ModItemTags.HEAVY_SIEVES);
        for (Block heavySieve : ModBlocks.heavySieves) {
            heavySieves.add(heavySieve.asItem());
        }

        getOrCreateTagBuilder(ModItemTags.HAMMERS).addTag(ModItemTags.WOODEN_HAMMERS)
                .addTag(ModItemTags.STONE_HAMMERS)
                .addTag(ModItemTags.IRON_HAMMERS)
                .addTag(ModItemTags.GOLDEN_HAMMERS)
                .addTag(ModItemTags.DIAMOND_HAMMERS)
                .addTag(ModItemTags.NETHERITE_HAMMERS);
        getOrCreateTagBuilder(ModItemTags.WOODEN_HAMMERS).addOptional(sequentia("wooden_hammer"));
        getOrCreateTagBuilder(ModItemTags.STONE_HAMMERS).addOptional(sequentia("stone_hammer"));
        getOrCreateTagBuilder(ModItemTags.IRON_HAMMERS).addOptional(sequentia("iron_hammer"));
        getOrCreateTagBuilder(ModItemTags.GOLDEN_HAMMERS).addOptional(sequentia("golden_hammer"));
        getOrCreateTagBuilder(ModItemTags.DIAMOND_HAMMERS).addOptional(sequentia("diamond_hammer"));
        getOrCreateTagBuilder(ModItemTags.NETHERITE_HAMMERS).addOptional(sequentia("netherite_hammer"));

        getOrCreateTagBuilder(ModItemTags.CRUCIBLES).addTag(ModItemTags.WOODEN_CRUCIBLES);
        final var woodenCrucibles = getOrCreateTagBuilder(ModItemTags.WOODEN_CRUCIBLES);
        for (Block woodenCrucible : ModBlocks.woodenCrucibles) {
            woodenCrucibles.add(woodenCrucible.asItem());
        }

        getOrCreateTagBuilder(ModItemTags.CROOKS).addTag(ModItemTags.WOODEN_CROOKS);
        getOrCreateTagBuilder(ModItemTags.WOODEN_CROOKS).addOptional(sequentia("wooden_crook"));

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

        getOrCreateTagBuilder(ModItemTags.CRUSHED_ANDESITES).addOptional(sequentia("crushed_andesite"));
        getOrCreateTagBuilder(ModItemTags.CRUSHED_DIORITES).addOptional(sequentia("crushed_diorite"));
        getOrCreateTagBuilder(ModItemTags.CRUSHED_GRANITES).addOptional(sequentia("crushed_granite"));
        getOrCreateTagBuilder(ModItemTags.CRUSHED_NETHERRACKS).addOptional(sequentia("crushed_netherrack"));
        getOrCreateTagBuilder(ModItemTags.CRUSHED_END_STONES).addOptional(sequentia("crushed_end_stone"));
        getOrCreateTagBuilder(ModItemTags.DUSTS).addOptional(sequentia("dust"));
    }

    @NotNull
    private static ResourceLocation sequentia(String name) {
        return new ResourceLocation("exnihilosequentia", name);
    }
}
