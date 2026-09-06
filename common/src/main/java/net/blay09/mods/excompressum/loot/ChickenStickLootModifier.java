package net.blay09.mods.excompressum.loot;

import net.blay09.mods.balm.world.level.storage.loot.BalmLootModifier;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChickenStickLootModifier implements BalmLootModifier {

    private static final List<LootContext> activeContexts = new ArrayList<>();

    @Override
    public void apply(LootContext context, List<ItemStack> list, @Nullable ResourceKey<LootTable> lootTableId) {
        synchronized (activeContexts) {
            if (activeContexts.contains(context)) {
                return;
            }
        }

        BlockState state = context.getOptional(LootContextParams.BLOCK_STATE);
        if (state == null) {
            return;
        }

        final var tool = context.getOptional(LootContextParams.TOOL);
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
