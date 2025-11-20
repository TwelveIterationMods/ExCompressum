package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends IntrinsicHolderTagsProvider<Item> {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ITEM, registriesFuture, (item) -> item.builtInRegistryHolder().key());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateRawBuilder(ModItemTags.SIEVES).addOptionalElement(sequentia("acacia_sieve"))
                .addOptionalElement(sequentia("birch_sieve"))
                .addOptionalElement(sequentia("dark_oak_sieve"))
                .addOptionalElement(sequentia("jungle_sieve"))
                .addOptionalElement(sequentia("oak_sieve"))
                .addOptionalElement(sequentia("spruce_sieve"))
                .addOptionalElement(sequentia("cherry_sieve"))
                .addOptionalElement(sequentia("mangrove_sieve"))
                .addOptionalElement(sequentia("warped_sieve"))
                .addOptionalElement(sequentia("crimson_sieve"))
                .addOptionalElement(deorum("acacia_sieve"))
                .addOptionalElement(deorum("birch_sieve"))
                .addOptionalElement(deorum("dark_oak_sieve"))
                .addOptionalElement(deorum("jungle_sieve"))
                .addOptionalElement(deorum("oak_sieve"))
                .addOptionalElement(deorum("spruce_sieve"))
                .addOptionalElement(deorum("cherry_sieve"))
                .addOptionalElement(deorum("mangrove_sieve"))
                .addOptionalElement(deorum("warped_sieve"))
                .addOptionalElement(deorum("crimson_sieve"))
                .addOptionalElement(fabricae("acacia_sieve"))
                .addOptionalElement(fabricae("birch_sieve"))
                .addOptionalElement(fabricae("dark_oak_sieve"))
                .addOptionalElement(fabricae("jungle_sieve"))
                .addOptionalElement(fabricae("oak_sieve"))
                .addOptionalElement(fabricae("spruce_sieve"))
                .addOptionalElement(fabricae("cherry_sieve"))
                .addOptionalElement(fabricae("mangrove_sieve"))
                .addOptionalElement(fabricae("warped_sieve"))
                .addOptionalElement(fabricae("crimson_sieve"))
                .addOptionalElement(fabricae("bamboo_sieve"));

        final var heavySieves = tag(ModItemTags.HEAVY_SIEVES);
        for (Block heavySieve : ModBlocks.heavySieves) {
            heavySieves.add(heavySieve.asItem());
        }
        final var rawHeavySieves = getOrCreateRawBuilder(ModItemTags.HEAVY_SIEVES);
        rawHeavySieves.addOptionalElement(deorum("acacia_compressed_sieve"))
                .addOptionalElement(deorum("birch_compressed_sieve"))
                .addOptionalElement(deorum("dark_oak_compressed_sieve"))
                .addOptionalElement(deorum("jungle_compressed_sieve"))
                .addOptionalElement(deorum("oak_compressed_sieve"))
                .addOptionalElement(deorum("spruce_compressed_sieve"))
                .addOptionalElement(deorum("cherry_compressed_sieve"))
                .addOptionalElement(deorum("mangrove_compressed_sieve"))
                .addOptionalElement(deorum("warped_compressed_sieve"))
                .addOptionalElement(deorum("crimson_compressed_sieve"));

        tag(ModItemTags.HAMMERS).addTag(ModItemTags.WOODEN_HAMMERS)
                .addTag(ModItemTags.STONE_HAMMERS)
                .addTag(ModItemTags.IRON_HAMMERS)
                .addTag(ModItemTags.COPPER_HAMMERS)
                .addTag(ModItemTags.GOLDEN_HAMMERS)
                .addTag(ModItemTags.DIAMOND_HAMMERS)
                .addTag(ModItemTags.NETHERITE_HAMMERS)
                .addTag(ModItemTags.EXOTIC_HAMMERS);
        getOrCreateRawBuilder(ModItemTags.WOODEN_HAMMERS).addOptionalElement(sequentia("wooden_hammer"))
                .addOptionalElement(deorum("wooden_hammer"))
                .addOptionalElement(fabricae("wooden_hammer"));
        getOrCreateRawBuilder(ModItemTags.STONE_HAMMERS).addOptionalElement(sequentia("stone_hammer"))
                .addOptionalElement(deorum("stone_hammer"))
                .addOptionalElement(fabricae("stone_hammer"));
        getOrCreateRawBuilder(ModItemTags.IRON_HAMMERS).addOptionalElement(sequentia("iron_hammer"))
                .addOptionalElement(deorum("iron_hammer"))
                .addOptionalElement(fabricae("iron_hammer"));
        getOrCreateRawBuilder(ModItemTags.COPPER_HAMMERS).addOptionalElement(sequentia("copper_hammer")).addOptionalElement(deorum("copper_hammer"));
        getOrCreateRawBuilder(ModItemTags.GOLDEN_HAMMERS).addOptionalElement(sequentia("golden_hammer"))
                .addOptionalElement(deorum("golden_hammer"))
                .addOptionalElement(fabricae("golden_hammer"));
        getOrCreateRawBuilder(ModItemTags.DIAMOND_HAMMERS).addOptionalElement(sequentia("diamond_hammer"))
                .addOptionalElement(deorum("diamond_hammer"))
                .addOptionalElement(fabricae("diamond_hammer"));
        getOrCreateRawBuilder(ModItemTags.NETHERITE_HAMMERS).addOptionalElement(sequentia("netherite_hammer"))
                .addOptionalElement(deorum("netherite_hammer"))
                .addOptionalElement(fabricae("netherite_hammer"));
        getOrCreateRawBuilder(ModItemTags.EXOTIC_HAMMERS).addOptionalElement(sequentia("bamboo_hammer"))
                .addOptionalElement(sequentia("andesite_hammer"))
                .addOptionalElement(sequentia("basalt_hammer"))
                .addOptionalElement(sequentia("blackstone_hammer"))
                .addOptionalElement(sequentia("bone_hammer"))
                .addOptionalElement(sequentia("calcite_hammer"))
                .addOptionalElement(sequentia("cherry_hammer"))
                .addOptionalElement(sequentia("deepslate_hammer"))
                .addOptionalElement(sequentia("diorite_hammer"))
                .addOptionalElement(sequentia("dripstone_hammer"))
                .addOptionalElement(sequentia("granite_hammer"))
                .addOptionalElement(sequentia("nether_brick_hammer"))
                .addOptionalElement(sequentia("red_nether_brick_hammer"))
                .addOptionalElement(sequentia("terracotta_hammer"))
                .addOptionalElement(sequentia("tuff_hammer"));

        final var woodenCrucibles = tag(ModItemTags.WOODEN_CRUCIBLES);
        for (final var woodenCrucible : ModBlocks.woodenCrucibles) {
            woodenCrucibles.add(woodenCrucible.asItem());
        }
        final var rawWoodenCrucibles = getOrCreateRawBuilder(ModItemTags.WOODEN_CRUCIBLES);
        rawWoodenCrucibles.addOptionalElement(sequentia("acacia_crucible"))
                .addOptionalElement(sequentia("birch_crucible"))
                .addOptionalElement(sequentia("cherry_crucible"))
                .addOptionalElement(sequentia("dark_oak_crucible"))
                .addOptionalElement(sequentia("jungle_crucible"))
                .addOptionalElement(sequentia("mangrove_crucible"))
                .addOptionalElement(sequentia("oak_crucible"))
                .addOptionalElement(sequentia("spruce_crucible"))
                .addOptionalElement(sequentia("crimson_crucible"))
                .addOptionalElement(sequentia("warped_crucible"));
        rawWoodenCrucibles.addOptionalElement(deorum("acacia_crucible"))
                .addOptionalElement(deorum("birch_crucible"))
                .addOptionalElement(deorum("cherry_crucible"))
                .addOptionalElement(deorum("dark_oak_crucible"))
                .addOptionalElement(deorum("jungle_crucible"))
                .addOptionalElement(deorum("mangrove_crucible"))
                .addOptionalElement(deorum("oak_crucible"))
                .addOptionalElement(deorum("spruce_crucible"))
                .addOptionalElement(deorum("crimson_crucible"))
                .addOptionalElement(deorum("warped_crucible"));
        rawWoodenCrucibles.addOptionalElement(fabricae("acacia_crucible"))
                .addOptionalElement(fabricae("birch_crucible"))
                .addOptionalElement(fabricae("cherry_crucible"))
                .addOptionalElement(fabricae("dark_oak_crucible"))
                .addOptionalElement(fabricae("jungle_crucible"))
                .addOptionalElement(fabricae("mangrove_crucible"))
                .addOptionalElement(fabricae("oak_crucible"))
                .addOptionalElement(fabricae("spruce_crucible"))
                .addOptionalElement(fabricae("crimson_crucible"))
                .addOptionalElement(fabricae("warped_crucible"))
                .addOptionalElement(fabricae("bamboo_crucible"));

        getOrCreateRawBuilder(ModItemTags.WOODEN_CROOKS).addOptionalElement(sequentia("wooden_crook"))
                .addOptionalElement(deorum("crook"))
                .addOptionalElement(fabricae("wooden_crook"));

        tag(ModItemTags.COMPRESSED_HAMMERS).add(ModItems.compressedWoodenHammer,
                        ModItems.compressedStoneHammer,
                        ModItems.compressedIronHammer,
                        ModItems.compressedDiamondHammer,
                        ModItems.compressedNetheriteHammer);
        getOrCreateRawBuilder(ModItemTags.COMPRESSED_HAMMERS)
                .addOptionalElement(deorum("compressed_wooden_hammer"))
                .addOptionalElement(deorum("compressed_stone_hammer"))
                .addOptionalElement(deorum("compressed_iron_hammer"))
                .addOptionalElement(deorum("compressed_golden_hammer"))
                .addOptionalElement(deorum("compressed_diamond_hammer"))
                .addOptionalElement(deorum("compressed_netherite_hammer"));

        tag(ModItemTags.COMPRESSED_CROOKS).addTag(ModItemTags.WOODEN_COMPRESSED_CROOKS);
        tag(ModItemTags.WOODEN_COMPRESSED_CROOKS).add(ModItems.compressedCrook);

        tag(ModItemTags.CHICKEN_STICKS).add(ModItems.chickenStick);

        final var baits = tag(ModItemTags.BAITS);
        for (Block bait : ModBlocks.baits) {
            baits.add(bait.asItem());
        }

        getOrCreateRawBuilder(ModItemTags.CRUSHED_ANDESITES).addOptionalElement(sequentia("crushed_andesite")).addOptionalElement(fabricae("crushed_andesite"));
        getOrCreateRawBuilder(ModItemTags.CRUSHED_DIORITES).addOptionalElement(sequentia("crushed_diorite")).addOptionalElement(fabricae("crushed_diorite"));
        getOrCreateRawBuilder(ModItemTags.CRUSHED_GRANITES).addOptionalElement(sequentia("crushed_granite")).addOptionalElement(fabricae("crushed_granite"));
        getOrCreateRawBuilder(ModItemTags.CRUSHED_NETHERRACKS).addOptionalElement(sequentia("crushed_netherrack")).addOptionalElement(deorum("crushed_netherrack")).addOptionalElement(fabricae("crushed_netherrack"));
        getOrCreateRawBuilder(ModItemTags.CRUSHED_END_STONES).addOptionalElement(sequentia("crushed_end_stone")).addOptionalElement(deorum("crushed_end_stone")).addOptionalElement(fabricae("crushed_endstone"));
        getOrCreateRawBuilder(ModItemTags.DUSTS).addOptionalElement(sequentia("dust")).addOptionalElement(deorum("dust")).addOptionalElement(fabricae("dust"));
    }

    private static Identifier sequentia(String name) {
        return Identifier.fromNamespaceAndPath(Compat.EXNIHILO_SEQUENTIA, name);
    }

    private static Identifier deorum(String name) {
        return Identifier.fromNamespaceAndPath(Compat.EX_DEORUM, name);
    }

    private static Identifier fabricae(String name) {
        return Identifier.fromNamespaceAndPath(Compat.FABRICAE_EX_NIHILO, name);
    }
}
