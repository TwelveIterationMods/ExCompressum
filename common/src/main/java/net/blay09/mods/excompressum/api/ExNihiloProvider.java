package net.blay09.mods.excompressum.api;

import net.blay09.mods.excompressum.api.recipe.CompressedHammerRecipe;
import net.blay09.mods.excompressum.api.recipe.HammerRecipe;
import net.blay09.mods.excompressum.api.recipe.HeavySieveRecipe;
import net.blay09.mods.excompressum.api.recipe.SieveRecipe;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface ExNihiloProvider {

    boolean isHammerableCompressed(ItemStack itemStack);

    boolean isHammerable(Level level, BlockState state);

    List<ItemStack> rollHammerRewards(Level level, BlockState state, ItemStack tool, RandomSource rand);

    boolean isSiftableWithMesh(Level level, BlockState sieveState, BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh);

    boolean isHeavySiftableWithMesh(BlockState sieveState, BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh);

    Collection<ItemStack> rollSieveRewards(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand);

    Collection<ItemStack> rollHeavySieveRewards(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand);

    Collection<ItemStack> rollCompressedHammerRewards(Level level, LootContext context, ItemStack itemStack);

    List<ItemStack> rollCrookRewards(ServerLevel level, BlockPos pos, BlockState state, @Nullable Entity entity, ItemStack tool, RandomSource rand);

    LootTable generateHeavySieveLootTable(Level level, BlockState sieveState, ItemLike source, int count, SieveMeshRegistryEntry mesh);

    boolean doMeshesHaveDurability();

    int getMeshFortune(ItemStack meshStack);

    int getMeshEfficiency(ItemStack meshStack);

    boolean isCompressableOre(ItemStack itemStack);

    boolean isHammerableOre(ItemStack itemStack);

    List<HammerRecipe> getHammerRecipes();

    List<CompressedHammerRecipe> getCompressedHammerRecipes();

    List<SieveRecipe> getSieveRecipes();

    List<HeavySieveRecipe> getHeavySieveRecipes();

}
