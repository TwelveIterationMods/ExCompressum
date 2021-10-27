package net.blay09.mods.excompressum.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Consumer;

public class NihiloLootEntry extends LootPoolSingletonContainer {

    private final ExNihiloProvider.NihiloItems nihiloItem;

    private NihiloLootEntry(ExNihiloProvider.NihiloItems nihiloItem, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
        super(weight, quality, conditions, functions);
        this.nihiloItem = nihiloItem;
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> stackConsumer, LootContext context) {
        stackConsumer.accept(ExNihilo.getInstance().getNihiloItem(nihiloItem).copy());
    }

    @Override
    public LootPoolEntryType getType() {
        return ModLoot.nihiloItemEntry;
    }

    public ExNihiloProvider.NihiloItems getNihiloItem() {
        return nihiloItem;
    }

    public static class Serializer extends LootPoolSingletonContainer.Serializer<NihiloLootEntry> {
        @Override
        public void serializeCustom(JsonObject object, NihiloLootEntry context, JsonSerializationContext conditions) {
            super.serializeCustom(object, context, conditions);
            object.addProperty("name", context.nihiloItem.name());
        }

        @Override
        protected NihiloLootEntry deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
            ExNihiloProvider.NihiloItems item = ExNihiloProvider.NihiloItems.valueOf(GsonHelper.getAsString(object, "name"));
            return new NihiloLootEntry(item, weight, quality, conditions, functions);
        }
    }
}
