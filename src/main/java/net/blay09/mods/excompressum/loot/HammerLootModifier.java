package net.blay09.mods.excompressum.loot;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerable;
import net.blay09.mods.excompressum.registry.hammer.HammerRegistry;
import net.blay09.mods.excompressum.registry.hammer.Hammerable;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HammerLootModifier extends LootModifier {

    private static final List<LootContext> activeContexts = new ArrayList<>();

    private HammerLootModifier(ILootCondition[] conditionsIn) {
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

        Hammerable hammerable = ExRegistries.getHammerRegistry().getHammerable(state);
        if (hammerable != null) {
            synchronized (activeContexts) {
                activeContexts.add(context);
            }
            List<ItemStack> loot = HammerRegistry.rollHammerRewards(hammerable, context);
            synchronized (activeContexts) {
                activeContexts.remove(context);
            }
            return loot;
        }

        /*if(BlockTags.LOGS.contains(state.getBlock())) {
            return Collections.singletonList(new ItemStack(ModItems.woodChipping, 4));
        }*/

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<HammerLootModifier> {
        @Override
        public HammerLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            return new HammerLootModifier(conditionsIn);
        }

        @Override
        public JsonObject write(HammerLootModifier instance) {
            return new JsonObject();
        }
    }

}
