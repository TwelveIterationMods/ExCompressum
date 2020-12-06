package net.blay09.mods.excompressum.registry.compressedhammer;

import net.blay09.mods.excompressum.registry.GroupedRegistry;
import net.blay09.mods.excompressum.registry.GroupedRegistryData;
import net.blay09.mods.excompressum.registry.RegistryGroup;
import net.blay09.mods.excompressum.registry.RegistryOverride;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.*;

public class CompressedHammerRegistry extends GroupedRegistry<
        RegistryGroup,
        RegistryOverride,
        RegistryOverride,
        CompressedHammerable,
        GroupedRegistryData<RegistryGroup, RegistryOverride, RegistryOverride, CompressedHammerable>> {

    public static final LootParameter<ItemStack> SOURCE_STACK = new LootParameter<>(new ResourceLocation("excompressum", "source_stack"));
    private final List<CompressedHammerable> entries = new ArrayList<>();

    public CompressedHammerRegistry() {
        super("CompressedHammer");
    }

    public Collection<CompressedHammerable> getEntries() {
        return entries;
    }

    public boolean isHammerable(ItemStack itemStack) {
        return getHammerable(itemStack) != null;
    }

    public boolean isHammerable(BlockState state) {
        return getHammerable(state) != null;
    }

    @Nullable
    public CompressedHammerable getHammerable(BlockState state) {
        return getHammerable(StupidUtils.getItemStackFromState(state));
    }

    @Nullable
    public CompressedHammerable getHammerable(ItemStack itemStack) {
        return entries.stream().filter(it -> it.getSource().test(itemStack)).findFirst().orElse(null);
    }

    // TODO move somewhere else
    public static LootContext buildLootContext(ServerWorld world, ItemStack itemStack, Random random) {
        return new LootContext.Builder(world)
                .withRandom(random)
                .withParameter(SOURCE_STACK, itemStack)
                .build(new LootParameterSet.Builder().required(SOURCE_STACK).build());
    }

    public static List<ItemStack> rollHammerRewards(CompressedHammerable hammerable, LootContext context) {
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
    protected void loadEntry(CompressedHammerable entry, @Nullable RegistryOverride groupOverride, @Nullable RegistryOverride entryOverride) {
        entries.add(entry);
    }

    @Override
    protected Class<? extends CompressedHammerRegistryData> getDataClass() {
        return CompressedHammerRegistryData.class;
    }

    @Override
    protected CompressedHammerRegistryData getEmptyData() {
        return new CompressedHammerRegistryData();
    }
}
