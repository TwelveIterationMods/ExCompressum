package net.blay09.mods.excompressum.compat.exnihilosequentia;

import com.google.common.collect.Maps;
import com.novamachina.exnihilosequentia.common.api.ExNihiloRegistries;
import com.novamachina.exnihilosequentia.common.item.mesh.EnumMesh;
import com.novamachina.exnihilosequentia.common.registries.crook.CrookDropEntry;
import com.novamachina.exnihilosequentia.common.registries.sieve.SieveDropEntry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ExNihiloSequentiaAddon implements ExNihiloProvider {

    private final EnumMap<NihiloItems, ItemStack> itemMap = Maps.newEnumMap(NihiloItems.class);

    public ExNihiloSequentiaAddon() {
        MinecraftForge.EVENT_BUS.register(this);

        ExRegistro.instance = this;

        itemMap.put(NihiloItems.HAMMER_WOODEN, findItem("hammer_wood"));
        itemMap.put(NihiloItems.HAMMER_STONE, findItem("hammer_stone"));
        itemMap.put(NihiloItems.HAMMER_IRON, findItem("hammer_iron"));
        itemMap.put(NihiloItems.HAMMER_GOLD, findItem("hammer_gold"));
        itemMap.put(NihiloItems.HAMMER_DIAMOND, findItem("hammer_diamond"));
        itemMap.put(NihiloItems.CROOK_WOODEN, findItem("crook_wood"));
        itemMap.put(NihiloItems.SILK_MESH, findItem("mesh_string"));
        itemMap.put(NihiloItems.IRON_MESH, findItem("mesh_iron"));

        itemMap.put(NihiloItems.DUST, findBlock("dust"));
        itemMap.put(NihiloItems.SIEVE, findBlock("sieve"));
        itemMap.put(NihiloItems.INFESTED_LEAVES, findBlock("infested_leaves"));
        itemMap.put(NihiloItems.NETHER_GRAVEL, findBlock("crushed_netherrack"));
        itemMap.put(NihiloItems.ENDER_GRAVEL, findBlock("crushed_end_stone"));


        ItemStack stringMeshItem = getNihiloItem(NihiloItems.SILK_MESH);
        if (!stringMeshItem.isEmpty()) {
            SieveMeshRegistryEntry stringMesh = new SieveMeshRegistryEntry(stringMeshItem, EnumMesh.STRING);
            stringMesh.setMeshLevel(1);
            stringMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/string_mesh"));
            SieveMeshRegistry.add(stringMesh);
        }

        ItemStack flintMeshItem = findItem("mesh_flint");
        if (!flintMeshItem.isEmpty()) {
            SieveMeshRegistryEntry flintMesh = new SieveMeshRegistryEntry(flintMeshItem, EnumMesh.FLINT);
            flintMesh.setMeshLevel(2);
            flintMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/flint_mesh"));
            SieveMeshRegistry.add(flintMesh);
        }

        ItemStack ironMeshItem = getNihiloItem(NihiloItems.IRON_MESH);
        if (!ironMeshItem.isEmpty()) {
            SieveMeshRegistryEntry ironMesh = new SieveMeshRegistryEntry(ironMeshItem, EnumMesh.IRON);
            ironMesh.setMeshLevel(3);
            ironMesh.setHeavy(true);
            ironMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/iron_mesh"));
            SieveMeshRegistry.add(ironMesh);
        }

        ItemStack diamondMeshItem = findItem("mesh_diamond");
        if (!diamondMeshItem.isEmpty()) {
            SieveMeshRegistryEntry diamondMesh = new SieveMeshRegistryEntry(diamondMeshItem, EnumMesh.DIAMOND);
            diamondMesh.setMeshLevel(4);
            diamondMesh.setHeavy(true);
            diamondMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/diamond_mesh"));
            SieveMeshRegistry.add(diamondMesh);
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
        return ExNihiloRegistries.HAMMER_REGISTRY.isHammerable(state.getBlock().getRegistryName());
    }

    @Override
    public Collection<ItemStack> rollHammerRewards(BlockState state, int miningLevel, float luck, Random rand) {
        return Collections.singletonList(new ItemStack(ExNihiloRegistries.HAMMER_REGISTRY.getResult(state.getBlock().getRegistryName())));
    }

    @Override
    public boolean isSiftable(BlockState state) {
        return ExNihiloRegistries.SIEVE_REGISTRY.isBlockSiftable(state.getBlock(), EnumMesh.NONE, false); // TODO waterlogged support
    }

    @Override
    public boolean isSiftableWithMesh(BlockState state, SieveMeshRegistryEntry sieveMesh) {
        EnumMesh mesh = sieveMesh != null ? (EnumMesh) sieveMesh.getBackingMesh() : EnumMesh.NONE;
        return ExNihiloRegistries.SIEVE_REGISTRY.isBlockSiftable(state.getBlock(), mesh, false); // TODO waterlogged support
    }

    @Override
    public Collection<ItemStack> rollSieveRewards(BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
        List<SieveDropEntry> rewards = ExNihiloRegistries.SIEVE_REGISTRY.getDrops(state.getBlock(), ((EnumMesh) sieveMesh.getBackingMesh()), false); // TODO waterlogged support
        if (rewards != null) {
            List<ItemStack> list = new ArrayList<>();
            for (SieveDropEntry reward : rewards) {
                if (reward.getResult() == null) {
                    ExCompressum.logger.error("Tried to roll sieve rewards from a null reward entry: {} (base chance: {}, mesh level: {})", state.getBlock().getRegistryName(), reward.getRarity(), sieveMesh.getMeshLevel());
                    continue;
                }

                int tries = rand.nextInt((int) luck + 1) + 1;
                for (int i = 0; i < tries; i++) {
                    if (rand.nextDouble() < (double) reward.getRarity()) {
                        ItemStack itemStack = createItemStack(reward.getResult());
                        if (!itemStack.isEmpty()) {
                            list.add(itemStack);
                        }
                    }
                }
            }
            return list;
        }

        return Collections.emptyList();
    }

    private ItemStack createItemStack(ResourceLocation registryName) {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(registryName));
    }

    @Override
    public Collection<ItemStack> rollCrookRewards(LivingEntity player, BlockState state, float luck, Random rand) {
        if (!BlockTags.LEAVES.contains(state.getBlock())) {
            return Collections.emptyList();
        }

        List<CrookDropEntry> rewards = ExNihiloRegistries.CROOK_REGISTRY.getDrops();
        if (rewards != null) {
            List<ItemStack> list = new ArrayList<>();
            for (CrookDropEntry reward : rewards) {
                float fortuneChanceBonus = 0.1f;
                if (rand.nextFloat() <= reward.getRarity() + fortuneChanceBonus * luck) {
                    ItemStack itemStack = createItemStack(reward.getItem());
                    if (!itemStack.isEmpty()) {
                        list.add(itemStack);
                    }
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    public Collection<HeavySieveReward> generateHeavySieveRewards(ItemStack sourceStack, int count) {
        // TODO revisit, this is utterly broken right now:
        List<HeavySieveReward> rewards = new ArrayList<>();
        for (EnumMesh mesh : EnumMesh.values()) {
            List<SieveDropEntry> entries = ExNihiloRegistries.SIEVE_REGISTRY.getDrops(sourceStack.getItem().getRegistryName(), mesh, false);
            if (entries != null) {
                for (SieveDropEntry entry : entries) {
                    for (int i = 0; i < count; i++) {
                        ItemStack itemStack = createItemStack(entry.getResult());
                        if (!itemStack.isEmpty()) {
                            rewards.add(new HeavySieveReward(itemStack, entry.getRarity(), mesh.getId())); // TODO id == meshlevel ???
                        }
                    }
                }
            }
        }

        return rewards;
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

    //	@SubscribeEvent Wood Chippings won't work in Adscensio until it ports the RegistryReloadedEvent to 1.11.2
//	public void onRegistryReload(RegistryReloadedEvent event) {
//		if(ExCompressumConfig.enableWoodChippings) {
//			HammerRegistry.register(Blocks.LOG.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 1f, 0f, true);
//			HammerRegistry.register(Blocks.LOG.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 0.75f, 0f, true);
//			HammerRegistry.register(Blocks.LOG.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 0.5f, 0f, true);
//			HammerRegistry.register(Blocks.LOG.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 0.25f, 0f, true);
//
//			HammerRegistry.register(Blocks.LOG2.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 1f, 0f, true);
//			HammerRegistry.register(Blocks.LOG2.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 0.75f, 0f, true);
//			HammerRegistry.register(Blocks.LOG2.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 0.5f, 0f, true);
//			HammerRegistry.register(Blocks.LOG2.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 0.25f, 0f, true);
//
//			List<ItemStack> oreDictStacks = OreDictionary.getOres("dustWood", false);
//			for (ItemStack itemStack : oreDictStacks) {
//				CompostRegistry.register(itemStack.getItem(), itemStack.getItemDamage(), 0.125f, Blocks.DIRT.getDefaultState(), new Color(0xFFC77826));
//			}
//		}
//	}
}
