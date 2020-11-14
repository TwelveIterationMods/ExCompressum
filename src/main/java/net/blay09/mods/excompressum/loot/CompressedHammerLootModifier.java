package net.blay09.mods.excompressum.loot;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompressedHammerLootModifier extends LootModifier {

    private static final List<LootContext> activeContexts = new ArrayList<>();

    public CompressedHammerLootModifier(ILootCondition[] conditionsIn) {
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


        CompressedHammerable hammerable = ExRegistries.getCompressedHammerRegistry().getHammerable(state);
        if (hammerable != null && hammerable.getLootTable() != null) {
            synchronized (activeContexts) {
                activeContexts.add(context);
            }
            LootTable lootTable = context.getLootTable(hammerable.getLootTable());
            List<ItemStack> loot = lootTable.generate(context);
            synchronized (activeContexts) {
                activeContexts.remove(context);
            }
            return loot;
        }

        // TODO normal hammering

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
