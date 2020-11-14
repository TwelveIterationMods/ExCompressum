package net.blay09.mods.excompressum.loot;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModLootModifiers {

    @SubscribeEvent
    public static void register(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().registerAll(
                new ChickenStickLootModifier.Serializer().setRegistryName(new ResourceLocation(ExCompressum.MOD_ID, "chicken_stick")),
                new CompressedCrookLootModifier.Serializer().setRegistryName(new ResourceLocation(ExCompressum.MOD_ID, "compressed_crook"))
        );
    }

}
