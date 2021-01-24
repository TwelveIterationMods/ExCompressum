package net.blay09.mods.excompressum.registry.chickenstick;

import net.blay09.mods.excompressum.registry.*;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;

import javax.annotation.Nullable;
import java.util.*;

public class ChickenStickRegistry extends GroupedRegistry<
        RegistryGroup,
        RegistryOverride,
        RegistryOverride,
        ChickenStickHammerable,
        GroupedRegistryData<RegistryGroup, RegistryOverride, RegistryOverride, ChickenStickHammerable>> {

    private final List<ChickenStickHammerable> entries = new ArrayList<>();

    public ChickenStickRegistry() {
        super("ChickenStick");
    }

    public static List<ItemStack> rollHammerRewards(ChickenStickHammerable hammerable, LootContext context) {
        LootTable lootTable = hammerable.getLootTable(context);
        if (lootTable != null) {
            return lootTable.generate(context);
        }

        return Collections.emptyList();
    }

    public Collection<ChickenStickHammerable> getEntries() {
        return entries;
    }

    public boolean isHammerable(BlockState state) {
        return isHammerable(StupidUtils.getItemStackFromState(state));
    }

    public boolean isHammerable(ItemStack itemStack) {
        return getHammerable(itemStack) != null;
    }

    @Nullable
    public ChickenStickHammerable getHammerable(BlockState state) {
        return getHammerable(StupidUtils.getItemStackFromState(state));
    }

    @Nullable
    public ChickenStickHammerable getHammerable(ItemStack itemStack) {
        return entries.stream().filter(it -> it.getSource().test(itemStack)).findFirst().orElse(null);
    }

    @Override
    protected void reset() {
        super.reset();
        entries.clear();
    }

    @Override
    protected void loadEntry(ChickenStickHammerable entry, @Nullable RegistryOverride groupOverride, @Nullable RegistryOverride entryOverride) {
        entries.add(entry);
    }

    @Override
    protected Class<? extends ChickenStickRegistryData> getDataClass() {
        return ChickenStickRegistryData.class;
    }

    @Override
    protected ChickenStickRegistryData getEmptyData() {
        return new ChickenStickRegistryData();
    }
}
