package net.blay09.mods.excompressum.config;

import net.minecraftforge.common.config.Configuration;

public class ExCompressumConfig {

	public static int woodenCrucibleSpeed;

	public static boolean skipAutoSieveSkins;

	public static float compressedCrookDurabilityMultiplier;
	public static float compressedCrookSpeedMultiplier;


	public static boolean enableWoodChippings;


	public static void load(Configuration config) {
		skipAutoSieveSkins = config.getBoolean("Skip Auto Sieve Skins", "general", false, "Set this to true to disable the loading of auto sieve skins from a remote server.");
		enableWoodChippings = config.getBoolean("Wood Chippings", "items", true, "If set to true, wood can be hammered into wood chippings, which can be composted into dirt.");

		woodenCrucibleSpeed = config.getInt("Speed", "Wooden Crucible", 8, 1, 20, "The rate at which the wooden crucible extracts water. Measured in liquid per second.");

		compressedCrookDurabilityMultiplier = config.getFloat("Durability Multiplier", "Compressed Crook", 2f, 0.1f, 10f, "The multiplier applied to the Compressed Crook's durability (based on the normal wooden crook)");
		compressedCrookSpeedMultiplier = config.getFloat("Speed Multiplier", "Compressed Crook", 4f, 0.1f, 10f, "The multiplier applied to the Compressed Crook's speed (based on the normal wooden crook)");

		AutomationConfig.load(config);
		CompressedMobsConfig.load(config);
		ChickenStickConfig.load(config);
		BaitConfig.load(config);
	}
}
