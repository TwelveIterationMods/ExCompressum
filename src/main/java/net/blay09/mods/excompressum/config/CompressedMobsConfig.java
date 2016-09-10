package net.blay09.mods.excompressum.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.config.Configuration;

import java.util.Collections;
import java.util.List;

public class CompressedMobsConfig {
	public static float compressedMobChance;
	public static int compressedMobSize;
	public static List<String> compressedMobs = Lists.newArrayList();

	public static void load(Configuration config) {
		final String CATEGORY = "Compressed Mobs";
		compressedMobChance = config.getFloat("Spawn Chance", CATEGORY, 0.01f, 0f, 1f, "The chance for mobs to spawn as Compressed Mobs. Set to 0 to disable.");
		compressedMobSize = config.getInt("Decompression Size", CATEGORY, 9, 1, 9, "The amount of mobs that will spawn upon death of a compressed mob.");
		Collections.addAll(compressedMobs, config.getStringList("Allowed Mobs", CATEGORY, new String[] {
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
