package net.blay09.mods.excompressum.registry.hammer;

import net.blay09.mods.excompressum.registry.GroupedRegistry;
import net.blay09.mods.excompressum.registry.GroupedRegistryData;
import net.blay09.mods.excompressum.registry.RegistryGroup;
import net.blay09.mods.excompressum.registry.RegistryOverride;
import net.blay09.mods.excompressum.utils.StupidUtils;
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
    private final List<Hammerable> entries = new ArrayList<>();

    public HammerRegistry() {
        super("Hammer");
    }

    public Collection<Hammerable> getEntries() {
        return entries;
    }

    public boolean isHammerable(ItemStack itemStack) {
        return getHammerable(itemStack) != null;
    }

    public boolean isHammerable(BlockState state) {
        return getHammerable(state) != null;
    }

    @Nullable
    public Hammerable getHammerable(BlockState state) {
        return getHammerable(StupidUtils.getItemStackFromState(state));
    }

    @Nullable
    public Hammerable getHammerable(ItemStack itemStack) {
        return entries.stream().filter(it -> it.getSource().test(itemStack)).findFirst().orElse(null);
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
        entries.add(entry);
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
