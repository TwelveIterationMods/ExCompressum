package net.blay09.mods.excompressum.registry.hammer;

import net.blay09.mods.excompressum.registry.GroupedRegistry;
import net.blay09.mods.excompressum.registry.GroupedRegistryData;
import net.blay09.mods.excompressum.registry.RegistryGroup;
import net.blay09.mods.excompressum.registry.RegistryOverride;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.*;

public class HammerRegistry extends GroupedRegistry<
        RegistryGroup,
        RegistryOverride,
        RegistryOverride,
        Hammerable,
        GroupedRegistryData<RegistryGroup, RegistryOverride, RegistryOverride, Hammerable>> {

    public static final LootParameter<ItemStack> SOURCE_STACK = new LootParameter<>(new ResourceLocation("excompressum", "source_stack"));
    private final Map<ResourceLocation, Hammerable> entries = new HashMap<>();
    private final Map<ResourceLocation, Hammerable> tagEntries = new HashMap<>();

    public HammerRegistry() {
        super("Hammer");
    }

    public Collection<Hammerable> getEntries() {
        return entries.values();
    }

    public Collection<Hammerable> getTagEntries() {
        return tagEntries.values();
    }

    public boolean isHammerable(ItemStack itemStack) {
        return getHammerable(itemStack) != null;
    }

    public boolean isHammerable(BlockState state) {
        return getHammerable(state) != null;
    }

    @Nullable
    public Hammerable getHammerable(BlockState state) {
        return getHammerable(state.getBlock().getRegistryName(), state.getBlock().getTags());
    }

    @Nullable
    public Hammerable getHammerable(ItemStack itemStack) {
        return getHammerable(itemStack.getItem().getRegistryName(), itemStack.getItem().getTags());
    }

    @Nullable
    public Hammerable getHammerable(@Nullable ResourceLocation registryName, Set<ResourceLocation> tags) {
        Hammerable hammerable = entries.get(registryName);
        if (hammerable != null) {
            return hammerable;
        }

        for (ResourceLocation tag : tags) {
            hammerable = tagEntries.get(tag);
            if(hammerable != null) {
                return hammerable;
            }
        }

        return null;
    }

    // TODO move somewhere else
    public static LootContext buildLootContext(ServerWorld world, ItemStack itemStack, float luck, Random random) {
        return new LootContext.Builder(world)
                .withRandom(random)
                .withParameter(SOURCE_STACK, itemStack)
                .withLuck(luck)
                .build(new LootParameterSet.Builder().required(SOURCE_STACK).build());
    }

    public static List<ItemStack> rollHammerRewards(Hammerable hammerable, LootContext context) {
        LootTable lootTable = hammerable.getLootTable(context);
        if (lootTable != null) {
            return lootTable.generate(context);
        }

        return Collections.emptyList();
    }

    @Override
    protected void reset() {
        super.reset();
        entries.clear();
    }

    @Override
    protected void loadEntry(Hammerable entry, @Nullable RegistryOverride groupOverride, @Nullable RegistryOverride entryOverride) {
        if (entry.getSource().isTag()) {
            tagEntries.put(entry.getSource().getResourceLocation(), entry);
        } else {
            entries.put(entry.getSource().getResourceLocation(), entry);
        }
    }

    @Override
    protected Class<? extends HammerRegistryData> getDataClass() {
        return HammerRegistryData.class;
    }

    @Override
    protected HammerRegistryData getEmptyData() {
        return new HammerRegistryData();
    }
}
