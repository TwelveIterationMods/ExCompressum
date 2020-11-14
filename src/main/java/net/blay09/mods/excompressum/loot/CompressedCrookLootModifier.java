package net.blay09.mods.excompressum.loot;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class CompressedCrookLootModifier extends LootModifier {

    public CompressedCrookLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.get(LootParameters.BLOCK_STATE);
        Vector3d origin = context.get(LootParameters.field_237457_g_);
        if (state == null || origin == null) {
            return generatedLoot;
        }

        ServerWorld world = context.getWorld();
        Entity entity = context.get(LootParameters.THIS_ENTITY);
        ItemStack tool = context.get(LootParameters.TOOL);
        if (tool == null) {
            tool = ItemStack.EMPTY;
        }
        BlockPos pos = new BlockPos(origin);

        return ExRegistro.rollCrookRewards(world, pos, state, entity, tool, context.getRandom());
    }

    public static class Serializer extends GlobalLootModifierSerializer<CompressedCrookLootModifier> {
        @Override
        public CompressedCrookLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            return new CompressedCrookLootModifier(conditionsIn);
        }

        @Override
        public JsonObject write(CompressedCrookLootModifier instance) {
            return new JsonObject();
        }
    }

}
