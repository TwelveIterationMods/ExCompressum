package net.blay09.mods.excompressum.loot;

import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;

public class ModLoot {

    public static LootPoolEntryType nihiloItemEntry;

    public static void initialize(BalmLootTables lootTables) {
        lootTables.registerLootModifier(new ResourceLocation(ExCompressum.MOD_ID, "chicken_stick"), new ChickenStickLootModifier());
        lootTables.registerLootModifier(new ResourceLocation(ExCompressum.MOD_ID, "compressed_crook"), new CompressedCrookLootModifier());
        lootTables.registerLootModifier(new ResourceLocation(ExCompressum.MOD_ID, "compressed_hammer"), new CompressedHammerLootModifier());
        lootTables.registerLootModifier(new ResourceLocation(ExCompressum.MOD_ID, "hammer"), new HammerLootModifier());
        // TODO Registry.register(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, new ResourceLocation(ExCompressum.MOD_ID, "nihilo_item"), nihiloItemEntry = new LootPoolEntryType(new NihiloLootEntry.Serializer()));
    }

}
