package net.blay09.mods.excompressum.api;

import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public interface ExNihiloProvider {

    @Deprecated
    enum NihiloMod implements IStringSerializable {
        NONE,
        OMNIA,
        ADSCENSIO,
        CREATIO,
        SEQUENTIA;

        @Override
        public String getString() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    enum NihiloItems {
        SEEDS_GRASS,
        CROOK_WOODEN,
        HAMMER_WOODEN,
        HAMMER_STONE,
        HAMMER_IRON,
        HAMMER_GOLD,
        HAMMER_DIAMOND,
        HAMMER_NETHERITE,
        SILK_WORM,
        SILK_MESH,
        DUST,
        NETHER_GRAVEL,
        ENDER_GRAVEL,
        INFESTED_LEAVES,
        IRON_MESH,
        SIEVE,
        ANDESITE_GRAVEL,
        DIORITE_GRAVEL,
        GRANITE_GRAVEL
    }

    ItemStack getNihiloItem(NihiloItems type);

    boolean isHammerable(BlockState state);

    List<ItemStack> rollHammerRewards(BlockState state, int miningLevel, float luck, Random rand);

    boolean isSiftableWithMesh(BlockState sieveState, BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh);

    Collection<ItemStack> rollSieveRewards(BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, Random rand);

    List<ItemStack> rollCrookRewards(ServerWorld world, BlockPos pos, BlockState state, @Nullable Entity entity, ItemStack tool, Random rand);

    LootTable generateHeavySieveLootTable(IItemProvider source, int count);

    boolean doMeshesHaveDurability();

    boolean doMeshesSplitLootTables();

    int getMeshFortune(ItemStack meshStack);

    int getMeshEfficiency(ItemStack meshStack);

    boolean isCompressableOre(ItemStack itemStack);

    boolean isHammerableOre(ItemStack itemStack);

}
