package net.blay09.mods.excompressum.loot;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickHammerable;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
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

public class ChickenStickLootModifier extends LootModifier {

    private static final List<LootContext> activeContexts = new ArrayList<>();

    public ChickenStickLootModifier(ILootCondition[] conditionsIn) {
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

        ChickenStickHammerable hammerable = ExRegistries.getChickenStickRegistry().getHammerable(state);
        if(hammerable != null) {
            synchronized (activeContexts) {
                activeContexts.add(context);
            }
            List<ItemStack> loot = ChickenStickRegistry.rollHammerRewards(hammerable, context);
            synchronized (activeContexts) {
                activeContexts.remove(context);
            }
            return loot;
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<ChickenStickLootModifier> {
        @Override
        public ChickenStickLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            return new ChickenStickLootModifier(conditionsIn);
        }

        @Override
        public JsonObject write(ChickenStickLootModifier instance) {
            return new JsonObject();
        }
    }

}
