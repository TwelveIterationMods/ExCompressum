package net.blay09.mods.excompressum.config;

import net.minecraftforge.common.config.Configuration;

// TODO This should really become a registry. Custom baits and such!
public class BaitConfig {

	public static float baitWolfChance;
	public static float baitOcelotChance;
	public static float baitCowChance;
	public static float baitPigChance;
	public static float baitChickenChance;
	public static float baitSheepChance;
	public static float baitSquidChance;
	public static float baitChildChance;

	public static void load(Configuration config) {
		final String CATEGORY = "Baits";
		baitWolfChance = config.getFloat("Wolf Bait Chance", CATEGORY, 0.0005f, 0.0001f, 1f, "The chance (per tick) that a wolf bait will result in a wolf spawn.");
		baitOcelotChance = config.getFloat("Ocelot Bait Chance", CATEGORY, 0.0005f, 0.0001f, 1f, "The chance (per tick) that an ocelot bait will result in an ocelot spawn.");
		baitCowChance = config.getFloat("Cow Bait Chance", CATEGORY, 0.0005f, 0.0001f, 1f, "The chance (per tick) that a cow bait will result in a cow spawn.");
		baitPigChance = config.getFloat("Pig Bait Chance", CATEGORY, 0.0005f, 0.0001f, 1f, "The chance (per tick) that a pig bait will result in a pig spawn.");
		baitChickenChance = config.getFloat("Chicken Bait Chance", CATEGORY, 0.0005f, 0.0001f, 1f, "The chance (per tick) that a chicken bait will result in a chicken spawn.");
		baitSheepChance = config.getFloat("Sheep Bait Chance", CATEGORY, 0.0005f, 0.0001f, 1f, "The chance (per tick) that a sheep bait will result in a sheep spawn.");
		baitSquidChance = config.getFloat("Squid Bait Chance", CATEGORY, 0.0005f, 0.0001f, 1f, "The chance (per tick) that a squid bait will result in a squid spawn.");

		baitChildChance = config.getFloat("Bait Child Chance", CATEGORY, 0.5f, 0f, 1f, "The chance that an animal spawned from a bait will result in a child.");
	}

}
