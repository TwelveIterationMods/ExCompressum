package net.blay09.mods.excompressum.registry.compressedhammer;

import net.blay09.mods.excompressum.registry.GroupedRegistry;
import net.blay09.mods.excompressum.registry.GroupedRegistryData;
import net.blay09.mods.excompressum.registry.RegistryGroup;
import net.blay09.mods.excompressum.registry.RegistryOverride;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
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
    private final Map<ResourceLocation, CompressedHammerable> entries = new HashMap<>();

    public CompressedHammerRegistry() {
        super("CompressedHammer");
    }

    public Collection<CompressedHammerable> getEntries() {
        return entries.values();
    }

    public boolean isHammerable(ItemStack itemStack) {
        final ResourceLocation registryName = itemStack.getItem().getRegistryName();
        return entries.containsKey(registryName);
    }

    public boolean isHammerable(BlockState state) {
        final ResourceLocation registryName = state.getBlock().getRegistryName();
        return entries.containsKey(registryName);
    }

    @Nullable
    public CompressedHammerable getHammerable(BlockState state) {
        final ResourceLocation registryName = state.getBlock().getRegistryName();
        return entries.get(registryName);
    }

    @Nullable
    public CompressedHammerable getHammerable(ItemStack itemStack) {
        final ResourceLocation registryName = itemStack.getItem().getRegistryName();
        return entries.get(registryName);
    }

    // TODO move somewhere else
    public static LootContext buildLootContext(ServerWorld world, ItemStack itemStack, float luck, Random random) {
        return new LootContext.Builder(world)
                .withRandom(random)
                .withParameter(SOURCE_STACK, itemStack)
                .withLuck(luck)
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
        entries.put(entry.getSource(), entry);
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
