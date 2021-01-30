package net.blay09.mods.excompressum.crafting;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.stream.Stream;

public class NihiloItemIngredient extends Ingredient {

    private final String key;
    private final int count;

    public NihiloItemIngredient(String key, int count) {
        super(getItemLists(key, count));
        this.key = key;
        this.count = count;
    }

    private static Stream<? extends IItemList> getItemLists(String key, int count) {
        ExNihiloProvider.NihiloItems nihiloItem = ExNihiloProvider.NihiloItems.valueOf(key);
        ItemStack itemStack = ExNihilo.getInstance().getNihiloItem(nihiloItem);
        if (itemStack.isEmpty()) {
            return Stream.empty();
        }

        ItemStack ingredientStack = ItemHandlerHelper.copyStackWithSize(itemStack, count);
        return Stream.of(new SingleItemList(ingredientStack));
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return new Serializer();
    }

    public static class Serializer implements IIngredientSerializer<NihiloItemIngredient> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public NihiloItemIngredient parse(PacketBuffer buffer) {
            return new NihiloItemIngredient(buffer.readString(), buffer.readByte());
        }

        @Override
        public NihiloItemIngredient parse(JsonObject json) {
            int count = json.has("count") ? json.get("count").getAsInt() : 1;
            return new NihiloItemIngredient(json.get("value").getAsString(), count);
        }

        @Override
        public void write(PacketBuffer buffer, NihiloItemIngredient ingredient) {
            buffer.writeString(ingredient.key);
            buffer.writeByte(ingredient.count);
        }
    }
}
