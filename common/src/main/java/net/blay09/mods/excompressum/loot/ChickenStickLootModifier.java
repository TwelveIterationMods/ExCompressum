package net.blay09.mods.excompressum.loot;

import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.List;

public class ChickenStickLootModifier implements BalmLootModifier {

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
        if(tool == null || !tool.is(ModItemTags.CHICKEN_STICKS)) {
            return;
        }

        ItemStack itemStack = new ItemStack(state.getBlock());
        if (ExRegistries.getChickenStickRegistry().isHammerable(context.getLevel(), itemStack)) {
            synchronized (activeContexts) {
                activeContexts.add(context);
            }
            List<ItemStack> loot = ChickenStickRegistry.rollHammerRewards(context.getLevel(), context, itemStack);
            synchronized (activeContexts) {
                activeContexts.remove(context);
            }
            list.clear();
            list.addAll(loot);
        }
    }

}
