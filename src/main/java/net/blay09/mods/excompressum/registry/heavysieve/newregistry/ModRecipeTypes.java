package net.blay09.mods.excompressum.registry.heavysieve.newregistry;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipeTypes {
    public static IRecipeSerializer<HeavySieveRecipe> heavySieveRecipe;

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().registerAll(
                heavySieveRecipe = new HeavySieveRecipeSerializer()
        );
    }
}
