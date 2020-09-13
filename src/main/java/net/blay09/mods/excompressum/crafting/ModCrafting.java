package net.blay09.mods.excompressum.crafting;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCrafting {
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        /*CraftingHelper.register(AndCondition.Serializer.INSTANCE);
        CraftingHelper.register(FalseCondition.Serializer.INSTANCE);
        CraftingHelper.register(ItemExistsCondition.Serializer.INSTANCE);
        CraftingHelper.register(ModLoadedCondition.Serializer.INSTANCE);
        CraftingHelper.register(NotCondition.Serializer.INSTANCE);
        CraftingHelper.register(OrCondition.Serializer.INSTANCE);
        CraftingHelper.register(TrueCondition.Serializer.INSTANCE);
        CraftingHelper.register(TagEmptyCondition.Serializer.INSTANCE);*/
    }
}
