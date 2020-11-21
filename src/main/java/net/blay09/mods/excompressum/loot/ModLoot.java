package net.blay09.mods.excompressum.loot;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.loot.LootPoolEntryType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModLoot {

    public static LootPoolEntryType nihiloItemEntry;

    public static void registerLootEntries() {
        Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, new ResourceLocation(ExCompressum.MOD_ID, "nihilo_item"), nihiloItemEntry = new LootPoolEntryType(new NihiloLootEntry.Serializer()));
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().registerAll(
                new ChickenStickLootModifier.Serializer().setRegistryName(new ResourceLocation(ExCompressum.MOD_ID, "chicken_stick")),
                new CompressedCrookLootModifier.Serializer().setRegistryName(new ResourceLocation(ExCompressum.MOD_ID, "compressed_crook")),
                new CompressedHammerLootModifier.Serializer().setRegistryName(new ResourceLocation(ExCompressum.MOD_ID, "compressed_hammer")),
                new HammerLootModifier.Serializer().setRegistryName(new ResourceLocation(ExCompressum.MOD_ID, "hammer"))
        );
    }

}
