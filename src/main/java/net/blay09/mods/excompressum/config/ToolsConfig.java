package net.blay09.mods.excompressum.config;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;

public class ToolsConfig {
	public static boolean allowChickenStickCreation;
	public static float chickenStickSoundChance;
	public static float chickenStickSpawnChance;
	public static ResourceLocation[] chickenStickSounds;
	public static final Map<String, String> chickenStickNames = Maps.newHashMap();

	public static float compressedCrookDurabilityMultiplier;
	public static float compressedCrookSpeedMultiplier;

	public static String chickenStickName;

	public static void load(Configuration config) {
		final String CATEGORY = "tools";
		allowChickenStickCreation = config.getBoolean("Allow Creation", CATEGORY, true, "If true, hitting a chicken with a stick will turn it into an Angry Chicken, which will drop a Chicken Stick when killed.");
		chickenStickSpawnChance = config.getFloat("Chicken Spawn Chance", CATEGORY, 0.008f, 0f, 1f, "The chance for the chicken stick to spawn a chicken. Set to 0 to disable.");
		chickenStickSoundChance = config.getFloat("Sound Chance", CATEGORY, 0.2f, 0f, 1f, "The chance for the chicken stick to make sounds when breaking blocks. Set to 0 to disable.");
		String[] chickenStickSoundNames = config.getStringList("Sound List", CATEGORY, new String[]{
				"entity.chicken.ambient",
				"entity.chicken.hurt",
				"entity.chicken.egg",
				"entity.chicken.step"
		}, "The sound names the chicken stick will randomly play.");
		chickenStickSounds = new ResourceLocation[chickenStickSoundNames.length];
		for(int i = 0; i < chickenStickSoundNames.length; i++) {
			chickenStickSounds[i] = new ResourceLocation(chickenStickSoundNames[i]);
		}

		String[] chickenStickNameList = config.getStringList("Custom Names", CATEGORY, new String[] {}, "Format: Username=ItemName, Username can be * to affect all users");
		chickenStickNames.put("wyld", "The Cluckington");
		chickenStickNames.put("slowpoke101", "Dark Matter Hammer");
		chickenStickNames.put("jake_evans", "Cock Stick");
		for(String name : chickenStickNameList) {
			String[] s = name.split("=");
			if(s.length >= 2) {
				chickenStickNames.put(s[0].toLowerCase(Locale.ENGLISH), s[1]);
			}
		}

		compressedCrookDurabilityMultiplier = config.getFloat("Compressed Crook Durability Multiplier", "tools", 2f, 0.1f, 10f, "The multiplier applied to the Compressed Crook's durability (based on the normal wooden crook)");
		compressedCrookSpeedMultiplier = config.getFloat("Compressed Crook Speed Multiplier", "tools", 4f, 0.1f, 10f, "The multiplier applied to the Compressed Crook's speed (based on the normal wooden crook)");
	}

	@Nullable
	public static String getChickenStickName() {
		return chickenStickName;
	}

	public static void setChickenStickName(String name) {
		chickenStickName = name;
	}
}
