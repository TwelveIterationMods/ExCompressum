package net.blay09.mods.excompressum.loot;

import com.google.gson.JsonObject;
import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.item.ModTags;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CompressedCrookLootModifier implements BalmLootModifier {

    private static final List<LootContext> activeContexts = new ArrayList<>();

    @Override
    public void apply(LootContext context, List<ItemStack> list) {
        synchronized (activeContexts) {
            if (activeContexts.contains(context)) {
                return;
            }
        }

        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (state == null || origin == null) {
            return;
        }

        ServerLevel world = context.getLevel();
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool == null || !tool.is(ModTags.COMPRESSED_CROOKS)) {
            return;
        }

        BlockPos pos = new BlockPos((int) Math.floor(origin.x), (int) Math.floor(origin.y), (int) Math.floor(origin.z)); // TODO huh?

        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
            return;
        }

        synchronized (activeContexts) {
            activeContexts.add(context);
        }
        List<ItemStack> loot = ExNihilo.getInstance().rollCrookRewards(world, pos, state, entity, tool, context.getRandom());
        synchronized (activeContexts) {
            activeContexts.remove(context);
        }
        list.clear();
        list.addAll(loot);
    }

}
