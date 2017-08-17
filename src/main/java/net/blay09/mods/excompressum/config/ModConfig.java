package net.blay09.mods.excompressum.config;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraftforge.common.config.Config;

@Config(modid = ExCompressum.MOD_ID, category = "")
public class ModConfig {

	public static General general = new General();
	public static Baits baits = new Baits();
	public static Client client = new Client();
	public static Compat compat = new Compat();

	public static class General {
		@Config.Name("Enable Wood Chippings")
		@Config.Comment("If set to true, wood can be hammered into wood chippings, which can be composted into dirt.")
		public boolean enableWoodChippings = true;
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

		@Config.Name("Bait Child Chance")
		@Config.Comment("The chance (per second) that an animal spawned from a bait will result in a child.")
		@Config.RangeDouble(min = 0.0f, max = 1f)
		public float childChance = 0.5f;
	}

	public static class Compat {
		@Config.Name("Enable Smashing II Modifier (TConstruct)")
		@Config.Comment("If set to true, adding a double compressed diamond hammer will add the Smashing II modifier to a Tinkers Construct tool, which allows smashing of compressed blocks.")
		public boolean enableModifiers = true;

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

}
