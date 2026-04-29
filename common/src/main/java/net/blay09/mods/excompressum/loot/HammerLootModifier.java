package net.blay09.mods.excompressum.loot;

import net.blay09.mods.balm.world.level.storage.loot.BalmLootModifier;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.hammer.HammerRegistry;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HammerLootModifier implements BalmLootModifier {

    private static final List<LootContext> activeContexts = new ArrayList<>();

    @Override
    public void apply(LootContext context, List<ItemStack> list, @Nullable ResourceKey<LootTable> lootTableId) {
        synchronized (activeContexts) {
            if (activeContexts.contains(context)) {
                return;
            }
        }

        BlockState state = context.getOptionalParameter(LootContextParams.BLOCK_STATE);
        if (state == null) {
            return;
        }

        final var tool = context.getOptionalParameter(LootContextParams.TOOL);
        if (tool == null || !tool.is(ModItemTags.HAMMERS)) {
            return;
        }

        ItemStack itemStack = StupidUtils.getItemStackFromState(state);
        if (ExRegistries.getHammerRegistry().isHammerable(context.getLevel(), itemStack)) {
            synchronized (activeContexts) {
                activeContexts.add(context);
            }
            List<ItemStack> loot = HammerRegistry.rollHammerRewards(context, itemStack);
            synchronized (activeContexts) {
                activeContexts.remove(context);
            }
            list.clear();
            list.addAll(loot);
            return;
        }

        if (ExNihilo.getInstance().isHammerable(context.getLevel(), state)) {
            synchronized (activeContexts) {
                activeContexts.add(context);
            }
            final var level = context.getLevel();
            List<ItemStack> loot = ExNihilo.getInstance().rollHammerRewards(level, state, tool, context.getRandom());
            synchronized (activeContexts) {
                activeContexts.remove(context);
            }
            list.clear();
            list.addAll(loot);
        }
    }

}
