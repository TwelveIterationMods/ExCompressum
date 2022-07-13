package net.blay09.mods.excompressum.loot;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.hammer.HammerRegistry;
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

public class HammerLootModifier extends LootModifier {

    private static final List<LootContext> activeContexts = new ArrayList<>();

    private HammerLootModifier(LootItemCondition[] conditionsIn) {
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

        ItemStack itemStack = StupidUtils.getItemStackFromState(state);
        if (ExRegistries.getHammerRegistry().isHammerable(context.getLevel(), itemStack)) {
            synchronized (activeContexts) {
                activeContexts.add(context);
            }
            List<ItemStack> loot = HammerRegistry.rollHammerRewards(context, itemStack);
            synchronized (activeContexts) {
                activeContexts.remove(context);
            }
            return loot;
        }

        if (ExNihilo.getInstance().isHammerable(state)) {
            ItemStack tool = context.getParam(LootContextParams.TOOL);
            return ExNihilo.getInstance().rollHammerRewards(state, tool != null ? tool : ItemStack.EMPTY, context.getRandom());
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<HammerLootModifier> {
        @Override
        public HammerLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            return new HammerLootModifier(conditionsIn);
        }

        @Override
        public JsonObject write(HammerLootModifier instance) {
            return new JsonObject();
        }
    }

}
