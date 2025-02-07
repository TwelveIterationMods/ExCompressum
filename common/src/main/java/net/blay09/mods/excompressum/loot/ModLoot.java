package net.blay09.mods.excompressum.loot;

import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.resources.ResourceLocation;

public class ModLoot {

    public static void initialize(BalmLootTables lootTables) {
        lootTables.registerLootModifier(new ResourceLocation(ExCompressum.MOD_ID, "chicken_stick"), new ChickenStickLootModifier());
        lootTables.registerLootModifier(new ResourceLocation(ExCompressum.MOD_ID, "compressed_crook"), new CompressedCrookLootModifier());
        lootTables.registerLootModifier(new ResourceLocation(ExCompressum.MOD_ID, "compressed_hammer"), new CompressedHammerLootModifier());
        lootTables.registerLootModifier(new ResourceLocation(ExCompressum.MOD_ID, "hammer"), new HammerLootModifier());
    }

}
