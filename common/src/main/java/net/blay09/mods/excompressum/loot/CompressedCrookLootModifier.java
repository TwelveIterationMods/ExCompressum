package net.blay09.mods.excompressum.loot;

import net.blay09.mods.balm.world.level.storage.loot.BalmLootModifier;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CompressedCrookLootModifier implements BalmLootModifier {

    private static final List<LootContext> activeContexts = new ArrayList<>();

    @Override
    public void apply(LootContext context, List<ItemStack> list, @Nullable ResourceKey<LootTable> lootTableId) {
        synchronized (activeContexts) {
            if (activeContexts.contains(context)) {
                return;
            }
        }

        BlockState state = context.getOptional(LootContextParams.BLOCK_STATE);
        Vec3 origin = context.getOptional(LootContextParams.ORIGIN);
        if (state == null || origin == null) {
            return;
        }

        ServerLevel level = context.getLevel();
        Entity entity = context.getOptional(LootContextParams.THIS_ENTITY);
        final var tool = context.getOptional(LootContextParams.TOOL);
        if (tool == null || !tool.is(ModItemTags.COMPRESSED_CROOKS)) {
            return;
        }

        BlockPos pos = BlockPos.containing(origin);

        if (StupidUtils.hasSilkTouchModifier(level, tool)) {
            return;
        }

        synchronized (activeContexts) {
            activeContexts.add(context);
        }
        List<ItemStack> loot = ExNihilo.getInstance().rollCrookRewards(level, pos, state, entity, tool, context.getRandom());
        synchronized (activeContexts) {
            activeContexts.remove(context);
        }
        list.clear();
        list.addAll(loot);
    }

}
