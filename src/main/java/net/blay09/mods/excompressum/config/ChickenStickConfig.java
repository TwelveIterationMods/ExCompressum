package net.blay09.mods.excompressum.config;

import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nullable;
import java.util.Map;

public class ChickenStickConfig {
	public static float chickenStickCreationChance;
	public static float chickenStickSoundChance;
	public static float chickenStickSpawnChance;
	public static String[] chickenStickSounds;
	public static final Map<String, String> chickenStickNames = Maps.newHashMap();

	public static String chickenStickName;

	public static void load(Configuration config) {
		final String CATEGORY = "Chicken Stick";
		chickenStickCreationChance = config.getFloat("Creation Chance", CATEGORY, 0.2f, 0f, 1f, "The chance that hitting a chicken with a stick will create a chicken stick. 0 means disabled.");
		chickenStickSpawnChance = config.getFloat("Chicken Spawn Chance", CATEGORY, 0.008f, 0f, 1f, "The chance for the chicken stick to spawn a chicken. Set to 0 to disable.");
		chickenStickSoundChance = config.getFloat("Sound Chance", CATEGORY, 0.2f, 0f, 1f, "The chance for the chicken stick to make sounds when breaking blocks. Set to 0 to disable.");
		chickenStickSounds = config.getStringList("Sound List", CATEGORY, new String[] {
				"entity.chicken.environment",
				"entity.chicken.hurt",
				"entity.chicken.egg",
				"entity.chicken.step"
		}, "The sound names the chicken stick will randomly play.");
		String[] chickenStickNameList = config.getStringList("Custom Names", CATEGORY, new String[] {}, "Format: Username=ItemName, Username can be * to affect all users");
		chickenStickNames.put("wyld", "The Cluckington");
		chickenStickNames.put("slowpoke101", "Dark Matter Hammer");
		chickenStickNames.put("jake_evans", "Cock Stick");
		for(String name : chickenStickNameList) {
			String[] s = name.split("=");
			if(s.length >= 2) {
				chickenStickNames.put(s[0].toLowerCase(), s[1]);
			}
		}

	}

	@Nullable
	public static String getChickenStickName() {
		return chickenStickName;
	}

	public static void setChickenStickName(String name) {
		chickenStickName = name;
	}
}
