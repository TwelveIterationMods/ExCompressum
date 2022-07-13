package net.blay09.mods.excompressum.loot;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CompressedHammerLootModifier extends LootModifier {

    private static final List<LootContext> activeContexts = new ArrayList<>();

    private CompressedHammerLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        synchronized (activeContexts) {
            if (activeContexts.contains(context)) {
                return generatedLoot;
            }
        }

        BlockState state = context.getParam(LootContextParams.BLOCK_STATE);
        if (state == null) {
            return generatedLoot;
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
            return loot;
        }

        if (ExNihilo.getInstance().isHammerable(state)) {
            ItemStack tool = context.getParam(LootContextParams.TOOL);
            return ExNihilo.getInstance().rollHammerRewards(state, tool != null ? tool : ItemStack.EMPTY, context.getRandom());
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<CompressedHammerLootModifier> {
        @Override
        public CompressedHammerLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            return new CompressedHammerLootModifier(conditionsIn);
        }

        @Override
        public JsonObject write(CompressedHammerLootModifier instance) {
            return new JsonObject();
        }
    }

}
