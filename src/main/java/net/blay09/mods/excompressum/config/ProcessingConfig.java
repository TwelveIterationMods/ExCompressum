package net.blay09.mods.excompressum.config;

import net.minecraftforge.common.config.Configuration;

public class ProcessingConfig {

	public static final float AUTO_HAMMER_BASE_SPEED = 0.0075f;
	public static final float AUTO_COMPRESSED_HAMMER_BASE_SPEED = AUTO_HAMMER_BASE_SPEED / 2f;
	public static final float AUTO_SIEVE_SPEED = 0.0075f;
	public static final float AUTO_HEAVY_SIEVE_SPEED = AUTO_SIEVE_SPEED / 2f;

	public static int autoHammerEnergy;
	public static float autoHammerSpeed;
	public static float autoHammerDecay;

	public static int autoCompressedHammerEnergy;
	public static float autoCompressedHammerSpeed;

	public static int autoHeavySieveEnergy;
	public static float autoHeavySieveSpeed;

	public static int autoSieveEnergy;
	public static float autoSieveSpeed;

	public static int autoCompressorEnergy;
	public static float autoCompressorSpeed;

	public static boolean allowHeavySieveAutomation;

	public static int woodenCrucibleSpeed;

	public static void load(Configuration config) {
		final String CATEGORY = "processing";

		final String AUTO_HAMMER = CATEGORY + ".auto_hammer";
		autoHammerSpeed = config.getFloat("Speed", AUTO_HAMMER, AUTO_HAMMER_BASE_SPEED, 0.0001f, 0.1f, "The speed at which the auto hammer will smash stuff.");
		autoHammerEnergy = config.getInt("Energy Cost", AUTO_HAMMER, 40, 0, 100000, "The energy cost of the auto hammer per tick.");
		autoHammerDecay = config.getFloat("Hammer Decay Chance", AUTO_HAMMER, 0.5f, 0f, 0.1f, "The chance for the upgrade hammers in an auto hammer to lose durability (per operation)");

		final String AUTO_COMPRESSED_HAMMER = CATEGORY + ".auto_compressed_hammer";
		autoCompressedHammerSpeed = config.getFloat("Speed", AUTO_COMPRESSED_HAMMER, AUTO_COMPRESSED_HAMMER_BASE_SPEED, 0.0001f, 0.1f, "The speed at which the auto compressed hammer will smash stuff.");
		autoCompressedHammerEnergy = config.getInt("Energy Cost", AUTO_COMPRESSED_HAMMER, 120, 0, 100000, "The energy cost of the auto compressed hammer per tick.");

		final String AUTO_SIEVE = CATEGORY + ".auto_sieve";
		autoSieveSpeed = config.getFloat("Speed", AUTO_SIEVE, AUTO_SIEVE_SPEED, 0.001f, 1f, "The speed at which the auto sieve will sift stuff.");
		autoSieveEnergy = config.getInt("Energy Cost", AUTO_SIEVE, 40, 0, 100000, "The energy cost of the auto sieve per tick.");

		final String AUTO_HEAVY_SIEVE = CATEGORY + ".auto_heavy_sieve";
		autoHeavySieveSpeed = config.getFloat("Speed", AUTO_HEAVY_SIEVE, AUTO_HEAVY_SIEVE_SPEED, 0.001f, 1f, "The speed at which the auto heavy sieve will sift stuff.");
		autoHeavySieveEnergy = config.getInt("Energy Cost", AUTO_HEAVY_SIEVE, 120, 0, 100000, "The energy cost of the auto heavy sieve per tick.");

		final String AUTO_COMPRESSOR = CATEGORY + ".auto_compressor";
		autoCompressorSpeed = config.getFloat("Speed", AUTO_COMPRESSOR, 0.1f, 0.0001f, 1f, "The speed at which the auto compressor will compress stuff.");
		autoCompressorEnergy = config.getInt("Energy Cost", AUTO_COMPRESSOR, 5, 0, 100000, "The energy cost of the auto compressor per tick.");

		final String HEAVY_SIEVE = CATEGORY + ".heavy_sieve";
		allowHeavySieveAutomation = config.getBoolean("Allow Fake Players", HEAVY_SIEVE, false, "Set this to true if you want to allow automation of the heavy sieve through fake players (i.e. Autonomous Activator)");

		final String WOODEN_CRUCIBLE = CATEGORY + ".wooden_crucible";
		woodenCrucibleSpeed = config.getInt("Speed", WOODEN_CRUCIBLE, 8, 1, 20, "The rate at which the wooden crucible extracts water. Measured in liquid per second.");
	}

}
