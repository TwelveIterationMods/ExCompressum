package net.blay09.mods.excompressum.config;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraftforge.common.config.Config;

@Config(modid = ExCompressum.MOD_ID, category = "")
public class ModConfig {

	public static General general = new General();
	public static Baits baits = new Baits();
	public static CompressedMobs compressedMobs = new CompressedMobs();
	public static Tools tools = new Tools();
	public static Automation automation = new Automation();
	public static Client client = new Client();
	public static Compat compat = new Compat();

	public static class General {
		@Config.Name("Enable Wood Chippings")
		@Config.Comment("If set to true, wood can be hammered into wood chippings, which can be composted into dirt.")
		public boolean enableWoodChippings = true;

		@Config.Name("Disable Creatio Wooden Crucible")
		@Config.Comment("Set to false if you need the Ex Nihilo Creatio wooden crucible for some reason. Note it'll cause a recipe conflict you'll have to fix via other means.")
		public boolean disableCreatioWoodenCrucible = true;

		@Config.Name("Flatten Sieve Recipes")
		@Config.Comment("If enabled, all meshes can obtain the results from the lower tier meshes. Note if the same item is registered in multiple tiers, then the higher tier will have a chance to drop multiples.")
		public boolean flattenSieveRecipes = false;
	}

	public static class Client {
		@Config.Name("Skip Auto Sieve Skins")
		@Config.Comment("Set this to true to disable the loading of auto sieve skins from a remote server.")
		public boolean skipAutoSieveSkins = false;

		@Config.Name("Show Registry Warnings")
		@Config.Comment("Set this to true if you're a modpack dev to see Ex Compressum registry warnings in chat. Errors will always display.")
		public boolean showRegistryWarnings = false;

		@Config.Name("Disable Particles")
		@Config.Comment("If you're playing on a potato, setting this to true will disable particles from the Sieves and Auto Hammers from Ex Compressum.")
		public boolean disableParticles = false;
	}

	public static class Baits {
		@Config.Name("Wolf Bait Chance")
		@Config.Comment("The chance (per second) that a wolf bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float wolfChance = 0.01f;

		@Config.Name("Ocelot Bait Chance")
		@Config.Comment("The chance (per second) that a ocelot bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float ocelotChance = 0.01f;

		@Config.Name("Cow Bait Chance")
		@Config.Comment("The chance (per second) that a cow bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float cowChance = 0.01f;

		@Config.Name("Pig Bait Chance")
		@Config.Comment("The chance (per second) that a pig bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float pigChance = 0.01f;

		@Config.Name("Chicken Bait Chance")
		@Config.Comment("The chance (per second) that a chicken bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float chickenChance = 0.01f;

		@Config.Name("Sheep Bait Chance")
		@Config.Comment("The chance (per second) that a sheep bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float sheepChance = 0.01f;

		@Config.Name("Squid Bait Chance")
		@Config.Comment("The chance (per second) that a squid bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float squidChance = 0.01f;

		@Config.Name("Rabbit Bait Chance")
		@Config.Comment("The chance (per second) that a rabbit bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float rabbitChance = 0.01f;

		@Config.Name("Horse Bait Chance")
		@Config.Comment("The chance (per second) that a horse bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float horseChance = 0.01f;

		@Config.Name("Donkey Bait Chance")
		@Config.Comment("The chance (per second) that a donkey bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float donkeyChance = 0.01f;

		@Config.Name("Parrot Bait Chance")
		@Config.Comment("The chance (per second) that a parrot bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float parrotChance = 0.01f;

		@Config.Name("Polar Bear Bait Chance")
		@Config.Comment("The chance (per second) that a polar bear bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float polarBearChance = 0.01f;

		@Config.Name("Llama Bait Chance")
		@Config.Comment("The chance (per second) that a llama bait will result in a spawn.")
		@Config.RangeDouble(min = 0.002f, max = 1f)
		public float llamaChance = 0.01f;

		@Config.Name("Bait Child Chance")
		@Config.Comment("The chance (per second) that an animal spawned from a bait will result in a child.")
		@Config.RangeDouble(min = 0.0f, max = 1f)
		public float childChance = 0.5f;
	}

	public static class CompressedMobs {
		@Config.Name("Chance")
		@Config.Comment("The chance for mobs to spawn as Compressed Mobs. Set to 0 to disable.")
		@Config.RangeDouble(min = 0f, max = 1f)
		public float chance  = 0.01f;

		@Config.Name("Decompression Size")
		@Config.Comment("The amount of mobs that will spawn upon death of a compressed mob.")
		@Config.RangeInt(min = 1, max = 9)
		public int size = 9;

		@Config.Name("Allowed Mobs")
		@Config.Comment("A list of entity registry names that can spawn as compressed entities.")
		public String[] allowedMobs = new String[] {
				"minecraft:zombie",
				"minecraft:creeper",
				"minecraft:skeleton",
				"minecraft:spider",
				"minecraft:cave_spider",
				"minecraft:silverfish",
				"minecraft:witch",
				"minecraft:enderman",
				"minecraft:pig_zombie",
				"minecraft:blaze",
				"minecraft:chicken",
				"minecraft:sheep",
				"minecraft:cow",
				"minecraft:pig",
				"minecraft:ghast"
		};
	}

	public static class Tools {
		@Config.Name("Allow Chicken Stick Creation")
		@Config.Comment("If true, hitting a chicken with a stick will turn it into an Angry Chicken, which will drop a Chicken Stick when killed.")
		public boolean allowChickenStickCreation = true;

		@Config.Name("Chicken Stick Sound Chance")
		@Config.Comment("The chance for the chicken stick to make sounds when breaking blocks. Set to 0 to disable.")
		@Config.RangeDouble(min = 0f, max = 1f)
		public float chickenStickSoundChance = 0.2f;

		@Config.Name("Chicken Stick Chicken Spawn Chance")
		@Config.Comment("The chance for the chicken stick to spawn a chicken. Set to 0 to disable.")
		@Config.RangeDouble(min = 0f, max = 1f)
		public float chickenStickSpawnChance = 0.008f;

		@Config.Name("Chicken Stick Sound List")
		@Config.Comment("The sound names the chicken stick will randomly play.")
		public String[] chickenStickSounds = new String[] {
				"entity.chicken.ambient",
				"entity.chicken.hurt",
				"entity.chicken.egg",
				"entity.chicken.step"
		};

		@Config.Name("Chicken Stick Custom Names")
		@Config.Comment("Format: Username=ItemName, Username can be * to affect all users")
		public String[] chickenStickNames = new String[] {
				"wyld=The Cluckington",
				"slowpoke101=Dark Matter Hammer",
				"jake_evans=Cock Stick"
		};

		@Config.Name("Compressed Crook Durability Multiplier")
		@Config.Comment("The multiplier applied to the Compressed Crook's durability (based on the normal wooden crook)")
		@Config.RangeDouble(min = 0.1f, max = 10f)
		public float compressedCrookDurabilityMultiplier = 2f;

		@Config.Name("Compressed Crook Speed Multiplier")
		@Config.Comment("The multiplier applied to the Compressed Crook's speed (based on the normal wooden crook)")
		@Config.RangeDouble(min = 0.1f, max = 10f)
		public float compressedCrookSpeedMultiplier = 4f;
	}

	public static class Compat {
		@Config.Name("Enable Smashing II Modifier (TConstruct)")
		@Config.Comment("If set to true, adding a double compressed diamond hammer will add the Smashing II modifier to a Tinkers Construct tool, which allows smashing of compressed blocks.")
		public boolean enableSmashingModifier = true;

		@Config.Name("Enable Compressing Modifier (TConstruct)")
		@Config.Comment("If set to true, adding an auto compressor will add the Compressing modifier to a Tinkers Construct tool, which will automatically compress hammered compressed blocks.")
		public boolean enableCompressingModifier = true;

		@Config.Name("Enable Evolved Orechid (Botania)")
		@Config.Comment("Setting this to false will disable the Evolved Orechid.")
		public boolean enableEvolvedOrechid = true;

		@Config.Name("Disable Vanilla Orechid")
		@Config.Comment("If set to true, Botania's Orechid will not show up in the lexicon and will not be craftable.")
		public boolean disableVanillaOrechid = false;

		@Config.Name("Mana Sieve Mana Cost")
		@Config.Comment("The mana cost of the Mana Sieve per Tick.")
		@Config.RangeInt(min = 1, max = 10)
		public int manaSieveCost = 1;

		@Config.Name("Evolved Orechid Mana Cost")
		@Config.Comment("The mana cost of the Evolved Orechid. GoG Orechid is 700, vanilla Orechid is 17500.")
		@Config.RangeInt(min = 0, max = 175000)
		public int evolvedOrechidCost = 700;

		@Config.Name("Evolved Orechid Delay")
		@Config.Comment("The ore generation delay for the Evolved Orechid in ticks. GoG Orechid is 2, vanilla Orechid is 100.")
		@Config.RangeInt(min = 1, max = 1200)
		public int evolvedOrechidDelay = 2;
	}

	public static class Automation {
		@Config.Name("Auto Hammer Energy Cost")
		@Config.Comment("The energy cost of the auto hammer per tick.")
		@Config.RangeInt(min = 0, max = 100000)
		public int autoHammerEnergy = 40;

		@Config.Name("Auto Hammer Speed")
		@Config.Comment("The speed at which the auto hammer will smash stuff.")
		@Config.RangeDouble(min = 0.001f, max = 0.1f)
		public float autoHammerSpeed = 0.0075f;

		@Config.Name("Auto Hammer Decay Chance")
		@Config.Comment("The chance for the upgrade hammers in an auto hammer to lose durability (per operation)")
		@Config.RangeDouble(min = 0f, max = 1f)
		public float autoHammerDecay = 0.5f;

		@Config.Name("Auto Compressed Hammer Energy Cost")
		@Config.Comment("The energy cost of the auto compressed hammer per tick.")
		@Config.RangeInt(min = 0, max = 100000)
		public int autoCompressedHammerEnergy = 120;

		@Config.Name("Auto Compressed Hammer Speed")
		@Config.Comment("The speed at which the auto compressed hammer will smash stuff.")
		@Config.RangeDouble(min = 0.001f, max = 0.1f)
		public float autoCompressedHammerSpeed = 0.0075f / 2f;

		@Config.Name("Auto Heavy Sieve Energy Cost")
		@Config.Comment("The energy cost of the auto heavy sieve per tick.")
		@Config.RangeInt(min = 0, max = 100000)
		public int autoHeavySieveEnergy = 120;

		@Config.Name("Auto Heavy Sieve Speed")
		@Config.Comment("The speed at which the auto heavy sieve will sift stuff.")
		@Config.RangeDouble(min = 0.001f, max = 0.1f)
		public float autoHeavySieveSpeed = 0.0075f / 2f;

		@Config.Name("Auto Sieve Energy Cost")
		@Config.Comment("The energy cost of the auto sieve per tick.")
		@Config.RangeInt(min = 0, max = 100000)
		public int autoSieveEnergy = 40;

		@Config.Name("Auto Sieve Speed")
		@Config.Comment("The speed at which the auto sieve will sift stuff.")
		@Config.RangeDouble(min = 0.001f, max = 1f)
		public float autoSieveSpeed = 0.0075f;

		@Config.Name("Auto Compressor Energy Cost")
		@Config.Comment("The energy cost of the auto compressor per tick.")
		@Config.RangeInt(min = 0, max = 100000)
		public int autoCompressorEnergy = 5;

		@Config.Name("Auto Compressor Speed")
		@Config.Comment("The speed at which the auto compressor will compress stuff.")
		@Config.RangeDouble(min = 0.001f, max = 1f)
		public float autoCompressorSpeed = 0.1f;

		@Config.Name("Heavy Sieve Allows Fake Players")
		@Config.Comment("Set this to true if you want to allow automation of the heavy sieve through fake players (i.e. Autonomous Activator)")
		public boolean allowHeavySieveAutomation = false;

		@Config.Name("Heavy Sieve Clicks per Second")
		@Config.Comment("The maximum amount of clicks per second on a heavy sieve. Clicks above this will be ignored.")
		@Config.RangeInt(min = 6, max = 24)
		public int heavySieveClicksPerSecond = 6;

		@Config.Name("Wooden Crucible Speed")
		@Config.Comment("The rate at which the wooden crucible extracts water. Measured in liquid per second.")
		@Config.RangeInt(min = 1, max = 20)
		public int woodenCrucibleSpeed = 8;
	}

}
