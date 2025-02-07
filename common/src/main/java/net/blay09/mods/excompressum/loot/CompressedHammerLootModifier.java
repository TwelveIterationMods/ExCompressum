package net.blay09.mods.excompressum.loot;

import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.List;

public class CompressedHammerLootModifier implements BalmLootModifier {

    private static final List<LootContext> activeContexts = new ArrayList<>();

    @Override
    public void apply(LootContext context, List<ItemStack> list) {
        synchronized (activeContexts) {
            if (activeContexts.contains(context)) {
                return;
            }
        }

        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state == null) {
            return;
        }

        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool == null || !tool.is(ModItemTags.COMPRESSED_HAMMERS)) {
            return;
        }

        synchronized (activeContexts) {
            activeContexts.add(context);
        }
        ItemStack itemStack = StupidUtils.getItemStackFromState(state);
        List<ItemStack> loot = CompressedHammerRegistry.rollHammerRewards(context.getLevel(), context, itemStack);
        synchronized (activeContexts) {
            activeContexts.remove(context);
        }
        if (!loot.isEmpty()) {
            list.clear();
            list.addAll(loot);
            return;
        }

        if (ExNihilo.getInstance().isHammerable(context.getLevel(), state)) {
            synchronized (activeContexts) {
                activeContexts.add(context);
            }
            final var level = context.getLevel();
            List<ItemStack> nihiLoot = ExNihilo.getInstance().rollHammerRewards(level, state, tool, context.getRandom());
            synchronized (activeContexts) {
                activeContexts.remove(context);
            }
            list.clear();
            list.addAll(nihiLoot);
        }
    }

}
