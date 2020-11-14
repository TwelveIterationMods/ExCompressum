package net.blay09.mods.excompressum.loot;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickHammerable;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class ChickenStickLootModifier extends LootModifier {

    public ChickenStickLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        World world = context.getWorld();
        BlockState state = context.get(LootParameters.BLOCK_STATE);
        if (state == null) {
            return generatedLoot;
        }

        ChickenStickHammerable hammerable = ExRegistries.getChickenStickRegistry().getHammerable(state);
        if(hammerable != null && hammerable.getResult() != null) {
            return Lists.newArrayList(hammerable.getResult().copy());
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
