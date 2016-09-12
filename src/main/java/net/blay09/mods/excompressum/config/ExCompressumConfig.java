package net.blay09.mods.excompressum.config;

import net.minecraftforge.common.config.Configuration;

public class ExCompressumConfig {

	public static boolean skipAutoSieveSkins;


	public static boolean enableWoodChippings;

	public static void load(Configuration config) {
		skipAutoSieveSkins = config.getBoolean("Skip Auto Sieve Skins", "general", false, "Set this to true to disable the loading of auto sieve skins from a remote server.");

		enableWoodChippings = config.getBoolean("Wood Chippings", "items", true, "If set to true, wood can be hammered into wood chippings, which can be composted into dirt.");

		ProcessingConfig.load(config);
		CompressedMobsConfig.load(config);
		ToolsConfig.load(config);
		BaitConfig.load(config);
	}
}
