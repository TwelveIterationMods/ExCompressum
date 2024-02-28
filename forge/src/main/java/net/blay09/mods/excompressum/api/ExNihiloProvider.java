package net.blay09.mods.excompressum.api;

import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public interface ExNihiloProvider {

    @Deprecated
    enum NihiloMod implements StringRepresentable {
        NONE,
        OMNIA,
        ADSCENSIO,
        CREATIO,
        SEQUENTIA;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    enum NihiloItems {
        SEEDS_GRASS,
        HAMMER_WOODEN,
        HAMMER_STONE,
        HAMMER_IRON,
        HAMMER_GOLD,
        HAMMER_DIAMOND,
        HAMMER_NETHERITE,
        SILK_WORM,
        DUST,
        NETHER_GRAVEL,
        ENDER_GRAVEL,
        INFESTED_LEAVES,
        SIEVE,
        ANDESITE_GRAVEL,
        DIORITE_GRAVEL,
        GRANITE_GRAVEL,
        /** used to disable our iron mesh recipe **/
        IRON_MESH
    }

    ItemStack getNihiloItem(NihiloItems type);

    boolean isHammerable(BlockState state);

    List<ItemStack> rollHammerRewards(BlockState state, ItemStack tool, RandomSource rand);

    boolean isSiftableWithMesh(BlockState sieveState, BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh);

    Collection<ItemStack> rollSieveRewards(BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand);

    List<ItemStack> rollCrookRewards(ServerLevel level, BlockPos pos, BlockState state, @Nullable Entity entity, ItemStack tool, RandomSource rand);

    LootTable generateHeavySieveLootTable(BlockState sieveState, ItemLike source, int count, SieveMeshRegistryEntry mesh);

    boolean doMeshesHaveDurability();

    boolean doMeshesSplitLootTables();

    int getMeshFortune(ItemStack meshStack);

    int getMeshEfficiency(ItemStack meshStack);

    boolean isCompressableOre(ItemStack itemStack);

    boolean isHammerableOre(ItemStack itemStack);

    List<IHammerRecipe> getHammerRecipes();

}
