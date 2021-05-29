package net.blay09.mods.excompressum.compat.exnihilosequentia;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.IntList;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.IHammerRecipe;
import net.blay09.mods.excompressum.compat.jei.LootTableUtils;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.api.sievemesh.MeshType;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import novamachina.exnihilosequentia.api.ExNihiloRegistries;
import novamachina.exnihilosequentia.api.crafting.ItemStackWithChance;
import novamachina.exnihilosequentia.api.crafting.crook.CrookRecipe;
import novamachina.exnihilosequentia.api.crafting.hammer.HammerRecipe;
import novamachina.exnihilosequentia.api.crafting.sieve.MeshWithChance;
import novamachina.exnihilosequentia.api.crafting.sieve.SieveRecipe;
import novamachina.exnihilosequentia.common.block.InfestedLeavesBlock;
import novamachina.exnihilosequentia.common.item.mesh.EnumMesh;
import novamachina.exnihilosequentia.common.item.resources.EnumResource;
import novamachina.exnihilosequentia.common.utility.Config;

import javax.annotation.Nullable;
import java.util.*;

public class ExNihiloSequentiaAddon implements ExNihiloProvider {

    private final EnumMap<NihiloItems, ItemStack> itemMap = Maps.newEnumMap(NihiloItems.class);

    public ExNihiloSequentiaAddon() {
        MinecraftForge.EVENT_BUS.register(this);

        ExNihilo.instance = this;

        itemMap.put(NihiloItems.HAMMER_WOODEN, findItem("hammer_wood"));
        itemMap.put(NihiloItems.HAMMER_STONE, findItem("hammer_stone"));
        itemMap.put(NihiloItems.HAMMER_IRON, findItem("hammer_iron"));
        itemMap.put(NihiloItems.HAMMER_GOLD, findItem("hammer_gold"));
        itemMap.put(NihiloItems.HAMMER_DIAMOND, findItem("hammer_diamond"));
        itemMap.put(NihiloItems.HAMMER_NETHERITE, findItem("hammer_netherite"));
        itemMap.put(NihiloItems.CROOK_WOODEN, findItem("crook_wood"));
        itemMap.put(NihiloItems.SILK_MESH, findItem("mesh_string"));
        itemMap.put(NihiloItems.IRON_MESH, findItem("mesh_iron"));

        itemMap.put(NihiloItems.DUST, findBlock("dust"));
        itemMap.put(NihiloItems.SIEVE, findBlock("sieve"));
        itemMap.put(NihiloItems.INFESTED_LEAVES, findBlock("infested_leaves"));
        itemMap.put(NihiloItems.NETHER_GRAVEL, findBlock("crushed_netherrack"));
        itemMap.put(NihiloItems.ENDER_GRAVEL, findBlock("crushed_end_stone"));
        itemMap.put(NihiloItems.DIORITE_GRAVEL, findBlock("crushed_diorite"));
        itemMap.put(NihiloItems.ANDESITE_GRAVEL, findBlock("crushed_andesite"));
        itemMap.put(NihiloItems.GRANITE_GRAVEL, findBlock("crushed_granite"));

        ItemStack stringMeshItem = getNihiloItem(NihiloItems.SILK_MESH);
        if (!stringMeshItem.isEmpty()) {
            SieveMeshRegistryEntry stringMesh = new SieveMeshRegistryEntry(MeshType.STRING, stringMeshItem, EnumMesh.STRING);
            stringMesh.setMeshLevel(1);
            stringMesh.setModelName("string");
            SieveMeshRegistry.add(stringMesh);
        }

        ItemStack flintMeshItem = findItem("mesh_flint");
        if (!flintMeshItem.isEmpty()) {
            SieveMeshRegistryEntry flintMesh = new SieveMeshRegistryEntry(MeshType.FLINT, flintMeshItem, EnumMesh.FLINT);
            flintMesh.setMeshLevel(2);
            flintMesh.setModelName("flint");
            SieveMeshRegistry.add(flintMesh);
        }

        ItemStack ironMeshItem = getNihiloItem(NihiloItems.IRON_MESH);
        if (!ironMeshItem.isEmpty()) {
            SieveMeshRegistryEntry ironMesh = new SieveMeshRegistryEntry(MeshType.IRON, ironMeshItem, EnumMesh.IRON);
            ironMesh.setMeshLevel(3);
            ironMesh.setHeavy(true);
            ironMesh.setModelName("iron");
            SieveMeshRegistry.add(ironMesh);
        }

        ItemStack diamondMeshItem = findItem("mesh_diamond");
        if (!diamondMeshItem.isEmpty()) {
            SieveMeshRegistryEntry diamondMesh = new SieveMeshRegistryEntry(MeshType.DIAMOND, diamondMeshItem, EnumMesh.DIAMOND);
            diamondMesh.setMeshLevel(4);
            diamondMesh.setHeavy(true);
            diamondMesh.setModelName("diamond");
            SieveMeshRegistry.add(diamondMesh);
        }

        ItemStack emeraldMeshItem = findItem("mesh_emerald");
        if (!emeraldMeshItem.isEmpty()) {
            SieveMeshRegistryEntry emeraldMesh = new SieveMeshRegistryEntry(MeshType.EMERALD, emeraldMeshItem, EnumMesh.EMERALD);
            emeraldMesh.setMeshLevel(5);
            emeraldMesh.setHeavy(true);
            emeraldMesh.setModelName("emerald");
            SieveMeshRegistry.add(emeraldMesh);
        }

        ItemStack netheriteMeshItem = findItem("mesh_netherite");
        if (!netheriteMeshItem.isEmpty()) {
            SieveMeshRegistryEntry mesh = new SieveMeshRegistryEntry(MeshType.NETHERITE, netheriteMeshItem, EnumMesh.NETHERITE);
            mesh.setMeshLevel(6);
            mesh.setHeavy(true);
            mesh.setModelName("netherite");
            SieveMeshRegistry.add(mesh);
        }
    }

    private ItemStack findItem(String name) {
        ResourceLocation location = new ResourceLocation(Compat.EXNIHILO_SEQUENTIA, name);
        if (ForgeRegistries.ITEMS.containsKey(location)) {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(location), 1);
        }

        return ItemStack.EMPTY;
    }

    private ItemStack findBlock(String name) {
        ResourceLocation location = new ResourceLocation(Compat.EXNIHILO_SEQUENTIA, name);
        if (ForgeRegistries.BLOCKS.containsKey(location)) {
            return new ItemStack(ForgeRegistries.BLOCKS.getValue(location), 1);
        }

        return ItemStack.EMPTY;
    }


    @Override
    public ItemStack getNihiloItem(NihiloItems type) {
        ItemStack itemStack = itemMap.get(type);
        return itemStack != null ? itemStack : ItemStack.EMPTY;
    }

    @Override
    public boolean isHammerable(BlockState state) {
        return ExNihiloRegistries.HAMMER_REGISTRY.isHammerable(state.getBlock());
    }

    @Override
    public List<ItemStack> rollHammerRewards(BlockState state, ItemStack toolItem, Random rand) {
        List<ItemStackWithChance> possibleDrops = ExNihiloRegistries.HAMMER_REGISTRY.getResult(state.getBlock());
        List<ItemStack> drops = new ArrayList<>();
        for (ItemStackWithChance itemStackWithChance : possibleDrops) {
            if (rand.nextFloat() <= itemStackWithChance.getChance()) {
                drops.add(itemStackWithChance.getStack().copy());
            }
        }

        return drops;
    }

    @Override
    public boolean isSiftableWithMesh(BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh) {
        boolean waterlogged = sieveState.hasProperty(BlockStateProperties.WATERLOGGED) && sieveState.get(BlockStateProperties.WATERLOGGED);
        EnumMesh mesh = sieveMesh != null ? (EnumMesh) sieveMesh.getBackingMesh() : EnumMesh.NONE;
        return ExNihiloRegistries.SIEVE_REGISTRY.isBlockSiftable(state.getBlock(), mesh, waterlogged);
    }

    @Override
    public Collection<ItemStack> rollSieveRewards(BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
        boolean waterlogged = sieveState.hasProperty(BlockStateProperties.WATERLOGGED) && sieveState.get(BlockStateProperties.WATERLOGGED);
        List<SieveRecipe> recipes = ExNihiloRegistries.SIEVE_REGISTRY.getDrops(state.getBlock(), ((EnumMesh) sieveMesh.getBackingMesh()), waterlogged);
        if (recipes != null) {
            List<ItemStack> list = new ArrayList<>();
            for (SieveRecipe recipe : recipes) {
                int tries = rand.nextInt((int) luck + 1) + 1;
                for (int i = 0; i < tries; i++) {
                    for (MeshWithChance roll : recipe.getRolls()) {
                        if (rand.nextDouble() < (double) roll.getChance()) {
                            ItemStack itemStack = recipe.getDrop();
                            list.add(itemStack.copy());
                        }
                    }
                }
            }
            return list;
        }

        return Collections.emptyList();
    }

    @Override
    public List<ItemStack> rollCrookRewards(ServerWorld world, BlockPos pos, BlockState state, @Nullable Entity entity, ItemStack tool, Random rand) {
        final float luck = getLuckFromTool(tool);
        if (state.getBlock() instanceof InfestedLeavesBlock) {
            List<ItemStack> list = new ArrayList<>();
            list.add(new ItemStack(Items.STRING, rand.nextInt(Config.getMaxBonusStringCount()) + Config.getMinStringCount()));
            if (rand.nextDouble() <= 0.8) {
                list.add(new ItemStack(EnumResource.SILKWORM.getRegistryObject().get()));
            }
            return list;
        } else if (!BlockTags.LEAVES.contains(state.getBlock())) {
            return Collections.emptyList();
        }

        List<CrookRecipe> recipes = ExNihiloRegistries.CROOK_REGISTRY.getDrops(state.getBlock());
        if (recipes != null) {
            List<ItemStack> list = new ArrayList<>();

            for (int i = 0; i < Config.getVanillaSimulateDropCount(); i++) {
                List<ItemStack> items = Block.getDrops(state, world, pos, null);
                list.addAll(items);
            }

            for (CrookRecipe recipe : recipes) {
                for (ItemStackWithChance drop : recipe.getOutput()) {
                    float fortuneChanceBonus = 0.1f;
                    if (rand.nextFloat() <= drop.getChance() + fortuneChanceBonus * luck) {
                        list.add(drop.getStack().copy());
                    }
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    private float getLuckFromTool(ItemStack tool) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
    }

    @Override
    public LootTable generateHeavySieveLootTable(BlockState sieveState, IItemProvider source, int times, SieveMeshRegistryEntry mesh) {
        if (!(mesh.getBackingMesh() instanceof EnumMesh)) {
            return LootTable.EMPTY_LOOT_TABLE;
        }

        boolean waterlogged = sieveState.hasProperty(BlockStateProperties.WATERLOGGED) && sieveState.get(BlockStateProperties.WATERLOGGED);
        LootTable.Builder tableBuilder = LootTable.builder();
        List<SieveRecipe> recipes = ExNihiloRegistries.SIEVE_REGISTRY.getDrops(source, ((EnumMesh) mesh.getBackingMesh()), waterlogged);
        for (SieveRecipe recipe : recipes) {
            ItemStack itemStack = recipe.getDrop();
            for (MeshWithChance roll : recipe.getRolls()) {
                LootPool.Builder poolBuilder = LootPool.builder();
                poolBuilder.rolls(ConstantRange.of(times));
                StandaloneLootEntry.Builder<?> entryBuilder = buildLootEntry(itemStack);
                entryBuilder.acceptCondition(RandomChance.builder(roll.getChance()));
                poolBuilder.addEntry(entryBuilder);
                tableBuilder.addLootPool(poolBuilder);
            }
        }
        return tableBuilder.build();
    }

    @Override
    public boolean doMeshesHaveDurability() {
        return false;
    }

    @Override
    public boolean doMeshesSplitLootTables() {
        return true;
    }

    @Override
    public int getMeshFortune(ItemStack meshStack) {
        return 0;
    }

    @Override
    public int getMeshEfficiency(ItemStack meshStack) {
        return 0;
    }

    @Override
    public boolean isCompressableOre(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isHammerableOre(ItemStack itemStack) {
        return false;
    }

    @Override
    public List<IHammerRecipe> getHammerRecipes() {
        ArrayListMultimap<IntList, HammerRecipe> groupedRecipes = ArrayListMultimap.create();
        for (HammerRecipe hammerRecipe : ExNihiloRegistries.HAMMER_REGISTRY.getRecipeList()) {
            groupedRecipes.put(hammerRecipe.getInput().getValidItemStacksPacked(), hammerRecipe);
        }

        List<IHammerRecipe> result = new ArrayList<>();
        for (IntList packedStacks : groupedRecipes.keySet()) {
            LootTable.Builder tableBuilder = LootTable.builder();
            for (HammerRecipe hammerRecipe : groupedRecipes.get(packedStacks)) {
                for (ItemStackWithChance itemStackWithChance : hammerRecipe.getOutput()) {
                    LootPool.Builder poolBuilder = LootPool.builder();
                    StandaloneLootEntry.Builder<?> entryBuilder = buildLootEntry(itemStackWithChance);
                    poolBuilder.addEntry(entryBuilder);
                    tableBuilder.addLootPool(poolBuilder);
                }
            }

            HammerRecipe firstRecipe = groupedRecipes.get(packedStacks).get(0);
            Ingredient input = firstRecipe.getInput();
            LootTableProvider lootTableProvider = new LootTableProvider(tableBuilder.build());
            result.add(new net.blay09.mods.excompressum.registry.hammer.HammerRecipe(firstRecipe.getId(), input, lootTableProvider));
        }

        return result;
    }

    private StandaloneLootEntry.Builder<?> buildLootEntry(ItemStack outputItem) {
        return LootTableUtils.buildLootEntry(outputItem, -1f);
    }

    private StandaloneLootEntry.Builder<?> buildLootEntry(ItemStackWithChance outputItem) {
        return LootTableUtils.buildLootEntry(outputItem.getStack(), outputItem.getChance());
    }

}
