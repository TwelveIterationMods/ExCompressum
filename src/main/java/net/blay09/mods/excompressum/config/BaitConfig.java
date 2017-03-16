package net.blay09.mods.excompressum.config;

import net.minecraftforge.common.config.Configuration;

public class BaitConfig {

	public static float baitWolfChance;
	public static float baitOcelotChance;
	public static float baitCowChance;
	public static float baitPigChance;
	public static float baitChickenChance;
	public static float baitSheepChance;
	public static float baitSquidChance;
	public static float baitRabbitChance;
	public static float baitHorseChance;
	public static float baitDonkeyChance;
	public static float baitChildChance;

	public static void load(Configuration config) {
		final String CATEGORY = "Baits";
		baitWolfChance = config.getFloat("Wolf Bait Chance", CATEGORY, 0.01f, 0.002f, 1f, "The chance (per second) that a wolf bait will result in a wolf spawn.");
		baitOcelotChance = config.getFloat("Ocelot Bait Chance", CATEGORY, 0.01f, 0.002f, 1f, "The chance (per second) that an ocelot bait will result in an ocelot spawn.");
		baitCowChance = config.getFloat("Cow Bait Chance", CATEGORY, 0.01f, 0.002f, 1f, "The chance (per second) that a cow bait will result in a cow spawn.");
		baitPigChance = config.getFloat("Pig Bait Chance", CATEGORY, 0.01f, 0.002f, 1f, "The chance (per second) that a pig bait will result in a pig spawn.");
		baitChickenChance = config.getFloat("Chicken Bait Chance", CATEGORY, 0.01f, 0.002f, 1f, "The chance (per second) that a chicken bait will result in a chicken spawn.");
		baitSheepChance = config.getFloat("Sheep Bait Chance", CATEGORY, 0.01f, 0.002f, 1f, "The chance (per second) that a sheep bait will result in a sheep spawn.");
		baitSquidChance = config.getFloat("Squid Bait Chance", CATEGORY, 0.01f, 0.002f, 1f, "The chance (per second) that a squid bait will result in a squid spawn.");
		baitRabbitChance = config.getFloat("Rabbit Bait Chance", CATEGORY, 0.01f, 0.002f, 1f, "The chance (per second) that a rabbit bait will result in a rabbit spawn.");
		baitHorseChance = config.getFloat("Horse Bait Chance", CATEGORY, 0.01f, 0.002f, 1f, "The chance (per second) that a horse bait will result in a horse spawn.");
		baitDonkeyChance = config.getFloat("Donkey Bait Chance", CATEGORY, 0.01f, 0.002f, 1f, "The chance (per second) that a donkey bait will result in a donkey spawn.");

		baitChildChance = config.getFloat("Bait Child Chance", CATEGORY, 0.5f, 0f, 1f, "The chance that an animal spawned from a bait will result in a child.");
	}

}
