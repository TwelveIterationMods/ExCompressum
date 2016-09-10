package net.blay09.mods.excompressum.config;

import net.minecraftforge.common.config.Configuration;

public class AutomationConfig {
	public static int autoHammerEnergy;
	public static float autoHammerSpeed;
	public static float autoHammerDecay;

	public static int autoCompressedHammerEnergy;
	public static float autoCompressedHammerSpeed;

	public static int autoHeavySieveEnergy;
	public static float autoHeavySieveSpeed;

	public static int autoSieveEnergy;
	public static float autoSieveSpeed;
	public static float autoSieveBookDecay;

	public static int autoCompressorEnergy;
	public static float autoCompressorSpeed;

	public static boolean allowHeavySieveAutomation;

	public static void load(Configuration config) {
		// TODO measurements for speed

		autoHammerSpeed = config.getFloat("Speed", "Auto Hammer", 0.01f, 0.0001f, 0.1f, "The speed at which the auto hammer will smash stuff.");
		autoHammerEnergy = config.getInt("Energy Cost", "Auto Hammer", 40, 0, 100000, "The energy cost of the auto hammer per tick.");
		autoHammerDecay = config.getFloat("Hammer Decay Chance", "Auto Hammer", 0.5f, 0f, 0.1f, "The chance for the upgrade hammers in an auto hammer to lose durability (per operation)");

		autoCompressedHammerSpeed = config.getFloat("Speed", "Auto Compressed Hammer", 0.005f, 0.0001f, 0.1f, "The speed at which the auto compressed hammer will smash stuff.");
		autoCompressedHammerEnergy = config.getInt("Energy Cost", "Auto Compressed Hammer", 120, 0, 100000, "The energy cost of the auto compressed hammer per tick.");

		autoHeavySieveSpeed = config.getFloat("Speed", "Auto Heavy Sieve", 0.005f, 0.0001f, 0.1f, "The speed at which the auto heavy sieve will sift stuff.");
		autoHeavySieveEnergy = config.getInt("Energy Cost", "Auto Heavy Sieve", 80, 0, 100000, "The energy cost of the auto heavy sieve per tick.");

		autoSieveSpeed = config.getFloat("Speed", "Auto Sieve", 0.005f, 0.0001f, 0.1f, "The speed at which the auto sieve will sift stuff.");
		autoSieveEnergy = config.getInt("Energy Cost", "Auto Sieve", 40, 0, 100000, "The energy cost of the auto sieve per tick.");
		autoSieveBookDecay = config.getFloat("Book Decay Chance", "Auto Sieve", 0.003f, 0f, 0.1f, "The chance for the enchantment on books in a sieve to lose a level (per operation)");

		autoCompressorSpeed = config.getFloat("Speed", "Auto Compressor", 0.1f, 0.0001f, 1f, "The speed at which the auto compressor will compress stuff.");
		autoCompressorEnergy = config.getInt("Energy Cost", "Auto Compressor", 5, 0, 100000, "The energy cost of the auto compressor per tick.");

		allowHeavySieveAutomation = config.getBoolean("Allow Fake Players", "Heavy Sieve", false, "Set this to true if you want to allow automation of the heavy sieve through fake players (i.e. Autonomous Activator)");
	}

}
