package net.blay09.mods.excompressum;

import com.google.common.collect.Lists;
import net.minecraftforge.common.config.Configuration;

import java.util.Collections;
import java.util.List;

// TODO go over the config file and clean things up a little
public class ExCompressumConfig {

	public static float compressedCrookDurabilityMultiplier;
	public static float compressedCrookSpeedMultiplier;

	public static boolean allowHeavySieveAutomation;
	public static boolean woodenCrucibleMakesClay;
	public static float woodenCrucibleSpeed;
	public static boolean woodenCrucibleFillFromRain;

	public static float baitWolfChance;
	public static float baitOcelotChance;
	public static float baitCowChance;
	public static float baitPigChance;
	public static float baitChickenChance;
	public static float baitSheepChance;
	public static float baitSquidChance;
	public static float baitChildChance;

	public static float createChickenStickChance;

	public static float compressedMobChance;
	public static int compressedMobSize;
	public static List<String> compressedMobs = Lists.newArrayList();

	public static int autoHammerEnergy;
	public static float autoHammerSpeed;
	public static int autoCompressedHammerEnergy;
	public static float autoCompressedHammerSpeed;
	public static int autoCompressorEnergy;
	public static float autoCompressorSpeed;
	public static int autoHeavySieveEnergy;
	public static float autoHeavySieveSpeed;
	public static int autoSieveEnergy;
	public static float autoSieveSpeed;
	public static float autoSieveBookDecay;
	public static float autoHammerDecay;

	public static boolean skipAutoSieveSkins;

	public static void load(Configuration config) {

		compressedCrookDurabilityMultiplier = config.getFloat("Compressed Crook Durability Multiplier", "general", 2f, 0.1f, 10f, "The multiplier applied to the Compressed Crook's durability (based on the normal wooden crook)");
		compressedCrookSpeedMultiplier = config.getFloat("Compressed Crook Speed Multiplier", "general", 4f, 0.1f, 10f, "The multiplier applied to the Compressed Crook's speed (based on the normal wooden crook)");
		allowHeavySieveAutomation = config.getBoolean("Allow Heavy Sieve Automation", "general", false, "Set this to true if you want to allow automation of the heavy sieve through fake players (i.e. Autonomous Activator)");
		woodenCrucibleMakesClay = config.getBoolean("Allow making clay in Wooden Crucible", "general", true, "Set this to true to be able to create clay by putting dust in a wooden crucible with water");
		woodenCrucibleSpeed = config.getFloat("Wooden Crucible Speed", "general", 0.5f, 0.1f, 1f, "The speed at which the wooden crucible extracts water. 0.1 is equivalent to a torch below a crucible, 0.3 is equivalent to fire below a crucible.");
		woodenCrucibleFillFromRain = config.getBoolean("Allow Wooden Crucible filling from Rain", "general", true, "Set this to true to allow wooden crucibles to fill from rain.");

		baitWolfChance = config.getFloat("Wolf Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that a wolf bait will result in a wolf spawn.");
		baitOcelotChance = config.getFloat("Ocelot Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that an ocelot bait will result in an ocelot spawn.");
		baitCowChance = config.getFloat("Cow Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that a cow bait will result in a cow spawn.");
		baitPigChance = config.getFloat("Pig Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that a pig bait will result in a pig spawn.");
		baitChickenChance = config.getFloat("Chicken Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that a chicken bait will result in a chicken spawn.");
		baitSheepChance = config.getFloat("Sheep Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that a sheep bait will result in a sheep spawn.");
		baitSquidChance = config.getFloat("Squid Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that a squid bait will result in a squid spawn.");
		baitChildChance = config.getFloat("Bait Child Chance", "baits", 0.5f, 0f, 1f, "The chance that an animal spawned from a bait will result in a child.");

		createChickenStickChance = config.getFloat("Create Chicken Stick Chance", "general", 0.2f, 0f, 1f, "The chance that hitting a chicken with a stick will create a chicken stick. 0 means disabled.");

		autoHammerSpeed = config.getFloat("Auto Hammer Speed", "general", 0.01f, 0.0001f, 0.1f, "The speed at which the auto hammer will smash stuff.");
		autoHammerEnergy = config.getInt("Auto Hammer Cost", "general", 40, 0, 100000, "The energy cost of the auto hammer per tick.");
		autoCompressedHammerSpeed = config.getFloat("Auto Compressed Hammer Speed", "general", 0.005f, 0.0001f, 0.1f, "The speed at which the auto compressed hammer will smash stuff.");
		autoCompressedHammerEnergy = config.getInt("Auto Compressed Hammer Cost", "general", 120, 0, 100000, "The energy cost of the auto compressed hammer per tick.");
		autoHeavySieveSpeed = config.getFloat("Auto Heavy Sieve Speed", "general", 0.005f, 0.0001f, 0.1f, "The speed at which the auto heavy sieve will sift stuff.");
		autoHeavySieveEnergy = config.getInt("Auto Heavy Sieve Cost", "general", 80, 0, 100000, "The energy cost of the auto heavy sieve per tick.");
		autoSieveSpeed = config.getFloat("Auto Sieve Speed", "general", 0.005f, 0.0001f, 0.1f, "The speed at which the auto sieve will sift stuff.");
		autoSieveEnergy = config.getInt("Auto Sieve Cost", "general", 40, 0, 100000, "The energy cost of the auto sieve per tick.");
		autoCompressorSpeed = config.getFloat("Auto Compressor Speed", "general", 0.1f, 0.0001f, 1f, "The speed at which the auto compressor will compress stuff.");
		autoCompressorEnergy = config.getInt("Auto Compressor Cost", "general", 5, 0, 100000, "The energy cost of the auto compressor per tick.");
		autoSieveBookDecay = config.getFloat("Auto Sieve Book Decay", "general", 0.003f, 0f, 0.1f, "The chance for the enchantment on books in a sieve to lose a level (per operation)");
		autoHammerDecay = config.getFloat("Auto Hammer Decay", "general", 0.5f, 0f, 0.1f, "The chance for the upgrade hammers in an auto hammer to lose durability (per operation)");

		skipAutoSieveSkins = config.getBoolean("Skip Auto Sieve Skins", "general", false, "Set this to true to disable the loading of auto sieve skins from a remote server.");

		compressedMobChance = config.getFloat("Compressed Mob Chance", "general", 0.01f, 0f, 1f, "The chance for mobs to spawn as Compressed Mobs. Set to 0 to disable.");
		compressedMobSize = config.getInt("Compressed Mob Size", "general", 9, 1, 9, "The amount of mobs that will spawn upon death of a compressed enemy.");
		Collections.addAll(compressedMobs, config.getStringList("Compressed Mobs", "general", new String[] {
				"Zombie",
				"Creeper",
				"Skeleton",
				"Spider",
				"CaveSpider",
				"Silverfish",
				"Witch",
				"Enderman",
				"PigZombie",
				"Blaze",
				"Chicken",
				"Sheep",
				"Cow",
				"Pig",
				"Ghast",
//                "Wither" okay, calm down Blay
		}, "A list of entity names that can spawn as compressed entities."));
	}
}
