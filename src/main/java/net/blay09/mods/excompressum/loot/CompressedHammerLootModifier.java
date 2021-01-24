package net.blay09.mods.excompressum.loot;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.newregistry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CompressedHammerLootModifier extends LootModifier {

    private static final List<LootContext> activeContexts = new ArrayList<>();

    private CompressedHammerLootModifier(ILootCondition[] conditionsIn) {
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

        BlockState state = context.get(LootParameters.BLOCK_STATE);
        if (state == null) {
            return generatedLoot;
        }

        synchronized (activeContexts) {
            activeContexts.add(context);
        }
        ItemStack itemStack = StupidUtils.getItemStackFromState(state);
        List<ItemStack> loot = CompressedHammerRegistry.rollHammerRewards(context, itemStack);
        synchronized (activeContexts) {
            activeContexts.remove(context);
        }
        if (!loot.isEmpty()) {
            return loot;
        }

        if (ExNihilo.getInstance().isHammerable(state)) {
            ItemStack tool = context.get(LootParameters.TOOL);
            return ExNihilo.getInstance().rollHammerRewards(state, tool != null ? tool : ItemStack.EMPTY, context.getRandom());
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<CompressedHammerLootModifier> {
        @Override
        public CompressedHammerLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            return new CompressedHammerLootModifier(conditionsIn);
        }

        @Override
        public JsonObject write(CompressedHammerLootModifier instance) {
            return new JsonObject();
        }
    }

}
