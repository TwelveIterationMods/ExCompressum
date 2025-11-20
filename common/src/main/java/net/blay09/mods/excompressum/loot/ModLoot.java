package net.blay09.mods.excompressum.loot;

import net.blay09.mods.balm.world.level.storage.loot.BalmLootTables;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.resources.Identifier;

public class ModLoot {

    public static void initialize(BalmLootTables lootTables) {
        lootTables.registerLootModifier(Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "chicken_stick"), new ChickenStickLootModifier());
        lootTables.registerLootModifier(Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "compressed_crook"), new CompressedCrookLootModifier());
        lootTables.registerLootModifier(Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "compressed_hammer"), new CompressedHammerLootModifier());
        lootTables.registerLootModifier(Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "hammer"), new HammerLootModifier());
    }

}
