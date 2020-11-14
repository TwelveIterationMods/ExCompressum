package net.blay09.mods.excompressum.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootPoolEntryType;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.JSONUtils;

import java.util.function.Consumer;

public class NihiloLootEntry extends StandaloneLootEntry {

    private final ExNihiloProvider.NihiloItems nihiloItem;

    private NihiloLootEntry(ExNihiloProvider.NihiloItems nihiloItem, int weightIn, int qualityIn, ILootCondition[] conditionsIn, ILootFunction[] functionsIn) {
        super(weightIn, qualityIn, conditionsIn, functionsIn);
        this.nihiloItem = nihiloItem;
    }

    @Override
    protected void func_216154_a(Consumer<ItemStack> stackConsumer, LootContext context) {
        stackConsumer.accept(ExNihilo.getNihiloItem(nihiloItem).copy());
    }

    @Override
    public LootPoolEntryType func_230420_a_() {
        return ModLoot.nihiloItemEntry;
    }

    public static class Serializer extends StandaloneLootEntry.Serializer<NihiloLootEntry> {
        @Override
        public void doSerialize(JsonObject object, NihiloLootEntry context, JsonSerializationContext conditions) {
            super.doSerialize(object, context, conditions);
            object.addProperty("name", context.nihiloItem.name());
        }

        @Override
        protected NihiloLootEntry deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, ILootCondition[] conditions, ILootFunction[] functions) {
            ExNihiloProvider.NihiloItems item = ExNihiloProvider.NihiloItems.valueOf(JSONUtils.getString(object, "name"));
            return new NihiloLootEntry(item, weight, quality, conditions, functions);
        }
    }
}
