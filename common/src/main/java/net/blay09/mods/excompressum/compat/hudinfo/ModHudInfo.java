package net.blay09.mods.excompressum.compat.hudinfo;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.excompressum.block.ModBlocks;

import static net.blay09.mods.excompressum.ExCompressum.id;

public class ModHudInfo {
    public static void initialize(BalmModSupportHudInfo hudInfo) {
        hudInfo.registerBlockInfo(id("auto_sieve"), ModBlocks.autoSieve, new AutoSieveBlockInfoProvider());
        hudInfo.registerBlockInfo(id("auto_heavy_sieve"), ModBlocks.autoHeavySieve, new AutoSieveBlockInfoProvider());
        hudInfo.registerBlockInfo(id("auto_hammer"), ModBlocks.autoHammer, new AutoHammerBlockInfoProvider());

        ModBlocks.baits.forEach((baitType, block) -> {
            assert baitType != null;
            hudInfo.registerBlockInfo(id(baitType.getSerializedName() + "_bait"), block, new BaitBlockInfoProvider());
        });
        ModBlocks.woodenCrucibles.forEach((woodenCrucibleType, block) -> {
            assert woodenCrucibleType != null;
            hudInfo.registerBlockInfo(id(woodenCrucibleType.getSerializedName() + "_crucible"), block, new WoodenCrucibleBlockInfoProvider());
        });
        ModBlocks.heavySieves.forEach((heavySieveType, block) -> {
            assert heavySieveType != null;
            hudInfo.registerBlockInfo(id(heavySieveType.getSerializedName() + "_heavy_sieve"), block, new HeavySieveBlockInfoProvider());
        });
    }
}
