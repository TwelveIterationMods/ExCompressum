package net.blay09.mods.excompressum.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ExCompressumConfig {

    public static class Common {
        public final ForgeConfigSpec.BooleanValue enableWoodChippings;
        public final ForgeConfigSpec.BooleanValue disableCreatioWoodenCrucible;
        public final ForgeConfigSpec.BooleanValue flattenSieveRecipes;
        public final ForgeConfigSpec.IntValue heavySieveDefaultRolls;

        public final ForgeConfigSpec.DoubleValue wolfBaitChance;
        public final ForgeConfigSpec.DoubleValue ocelotBaitChance;
        public final ForgeConfigSpec.DoubleValue cowBaitChance;
        public final ForgeConfigSpec.DoubleValue pigBaitChance;
        public final ForgeConfigSpec.DoubleValue chickenBaitChance;
        public final ForgeConfigSpec.DoubleValue sheepBaitChance;
        public final ForgeConfigSpec.DoubleValue squidBaitChance;
        public final ForgeConfigSpec.DoubleValue rabbitBaitChance;
        public final ForgeConfigSpec.DoubleValue horseBaitChance;
        public final ForgeConfigSpec.DoubleValue donkeyBaitChance;
        public final ForgeConfigSpec.DoubleValue parrotBaitChance;
        public final ForgeConfigSpec.DoubleValue polarBearBaitChance;
        public final ForgeConfigSpec.DoubleValue llamaBaitChance;
        public final ForgeConfigSpec.DoubleValue childBaitChance;

        public final ForgeConfigSpec.DoubleValue compressedMobChance;
        public final ForgeConfigSpec.IntValue compressedMobSize;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> compressedMobAllowedMobs;

		public final ForgeConfigSpec.BooleanValue allowChickenStickCreation;
		public final ForgeConfigSpec.DoubleValue chickenStickSoundChance;
		public final ForgeConfigSpec.DoubleValue chickenStickSpawnChance;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> chickenStickSounds;
		public final ForgeConfigSpec.DoubleValue compressedCrookDurabilityMultiplier;
		public final ForgeConfigSpec.DoubleValue compressedCrookSpeedMultiplier;

		public final ForgeConfigSpec.BooleanValue enableSmashingModifier;
		public final ForgeConfigSpec.BooleanValue enableCompressingModifier;
		public final ForgeConfigSpec.BooleanValue enableEvolvedOrechid;
		public final ForgeConfigSpec.BooleanValue disableVanillaOrechid;
		public final ForgeConfigSpec.IntValue manaSieveCost;
		public final ForgeConfigSpec.IntValue evolvedOrechidCost;
		public final ForgeConfigSpec.IntValue evolvedOrechidDelay;

		public final ForgeConfigSpec.IntValue autoHammerEnergy;
		public final ForgeConfigSpec.DoubleValue autoHammerSpeed;
		public final ForgeConfigSpec.DoubleValue autoHammerDecay;
		public final ForgeConfigSpec.IntValue autoCompressedHammerEnergy;
		public final ForgeConfigSpec.DoubleValue autoCompressedHammerSpeed;
		public final ForgeConfigSpec.IntValue autoSieveEnergy;
		public final ForgeConfigSpec.IntValue autoHeavySieveEnergy;
		public final ForgeConfigSpec.DoubleValue autoHeavySieveSpeed;
		public final ForgeConfigSpec.DoubleValue autoSieveSpeed;
		public final ForgeConfigSpec.IntValue autoCompressorEnergy;
		public final ForgeConfigSpec.DoubleValue autoCompressorSpeed;
		public final ForgeConfigSpec.BooleanValue allowHeavySieveAutomation;
		public final ForgeConfigSpec.IntValue heavySieveClicksPerSecond;
		public final ForgeConfigSpec.IntValue woodenCrucibleSpeed;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("general");

            enableWoodChippings = builder
                    .comment("If set to true, wood can be hammered into wood chippings, which can be composted into dirt.")
                    .translation("excompressum.config.enableWoodChippings")
                    .define("enableWoodChippings", true);

            disableCreatioWoodenCrucible = builder
                    .comment("Set to false if you need the Ex Nihilo Creatio wooden crucible for some reason. Note it'll cause a recipe conflict you'll have to fix via other means.")
                    .translation("excompressum.config.disableCreatioWoodenCrucible")
                    .define("disableCreatioWoodenCrucible", true);

            flattenSieveRecipes = builder
                    .comment("If enabled, all meshes can obtain the results from the lower tier meshes. Note if the same item is registered in multiple tiers, then the higher tier will have a chance to drop multiples.")
                    .translation("excompressum.config.flattenSieveRecipes")
                    .define("flattenSieveRecipes", false);

			heavySieveDefaultRolls = builder
					.comment("The amount of times the heavy sieve should roll for compressed entries. For example, a value of 7 means for every compressed gravel you only get the equivalent drops of 7 sifted gravel blocks (2 loss). A value of 9 would therefore mean no loss.")
					.translation("excompressum.config.heavySieveDefaultRolls")
					.defineInRange("heavySieveDefaultRolls", 7, 1, Integer.MAX_VALUE);

            builder.pop();

            builder.push("baits");

            wolfBaitChance = builder
                    .comment("The chance (per second) that a wolf bait will result in a spawn.")
                    .translation("excompressum.config.wolfChance")
                    .defineInRange("wolfChance", 0.01f, 0.002f, 1f);

            ocelotBaitChance = builder
                    .comment("The chance (per second) that a ocelot bait will result in a spawn.")
                    .translation("excompressum.config.ocelotChance")
                    .defineInRange("ocelotChance", 0.01f, 0.002f, 1f);

            cowBaitChance = builder
                    .comment("The chance (per second) that a cow bait will result in a spawn.")
                    .translation("excompressum.config.cowChance")
                    .defineInRange("cowChance", 0.01f, 0.002f, 1f);

            pigBaitChance = builder
                    .comment("The chance (per second) that a pig bait will result in a spawn.")
                    .translation("excompressum.config.pigChance")
                    .defineInRange("pigChance", 0.01f, 0.002f, 1f);

            chickenBaitChance = builder
                    .comment("The chance (per second) that a chicken bait will result in a spawn.")
                    .translation("excompressum.config.chickenChance")
                    .defineInRange("chickenChance", 0.01f, 0.002f, 1f);

            sheepBaitChance = builder
                    .comment("The chance (per second) that a sheep bait will result in a spawn.")
                    .translation("excompressum.config.sheepChance")
                    .defineInRange("sheepChance", 0.01f, 0.002f, 1f);

            squidBaitChance = builder
                    .comment("The chance (per second) that a squid bait will result in a spawn.")
                    .translation("excompressum.config.squidChance")
                    .defineInRange("squidChance", 0.01f, 0.002f, 1f);

            rabbitBaitChance = builder
                    .comment("The chance (per second) that a rabbit bait will result in a spawn.")
                    .translation("excompressum.config.rabbitChance")
                    .defineInRange("rabbitChance", 0.01f, 0.002f, 1f);

            horseBaitChance = builder
                    .comment("The chance (per second) that a horse bait will result in a spawn.")
                    .translation("excompressum.config.horseChance")
                    .defineInRange("horseChance", 0.01f, 0.002f, 1f);

            donkeyBaitChance = builder
                    .comment("The chance (per second) that a donkey bait will result in a spawn.")
                    .translation("excompressum.config.donkeyChance")
                    .defineInRange("donkeyChance", 0.01f, 0.002f, 1f);

            parrotBaitChance = builder
                    .comment("The chance (per second) that a parrot bait will result in a spawn.")
                    .translation("excompressum.config.parrotChance")
                    .defineInRange("parrotChance", 0.01f, 0.002f, 1f);

            polarBearBaitChance = builder
                    .comment("The chance (per second) that a polar bear bait will result in a spawn.")
                    .translation("excompressum.config.polarBearChance")
                    .defineInRange("polarBearChance", 0.01f, 0.002f, 1f);

            llamaBaitChance = builder
                    .comment("The chance (per second) that a llama bait will result in a spawn.")
                    .translation("excompressum.config.llamaChance")
                    .defineInRange("llamaChance", 0.01f, 0.002f, 1f);

            childBaitChance = builder
                    .comment("The chance (per second) that an animal spawned from a bait will result in a child.")
                    .translation("excompressum.config.childChance")
                    .defineInRange("childChance", 0.5f, 0.0f, 1f);

            builder.pop();

            builder.push("compressedMobs");

            compressedMobChance = builder
                    .comment("The chance for mobs to spawn as Compressed Mobs. Set to 0 to disable.")
                    .translation("excompressum.config.compressedMobChance")
                    .defineInRange("compressedMobChance", 0.01f, 0f, 1f);

            compressedMobSize = builder
                    .comment("The amount of mobs that will spawn upon death of a compressed mob.")
                    .translation("excompressum.config.compressedMobSize")
                    .defineInRange("compressedMobSize", 9, 1, Integer.MAX_VALUE);

            compressedMobAllowedMobs = builder
                    .comment("A list of entity registry names that can spawn as compressed entities.")
                    .translation("excompressum.config.compressedMobAllowedMobs")
                    .defineList("compressedMobAllowedMobs", Lists.newArrayList(
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
                            "minecraft:ghast"), it -> it instanceof String);

            builder.pop();

            builder.push("tools");

			allowChickenStickCreation = builder
					.comment("If true, hitting a chicken with a stick will turn it into an Angry Chicken, which will drop a Chicken Stick when killed.")
					.translation("excompressum.config.allowChickenStickCreation")
					.define("allowChickenStickCreation", true);

			chickenStickSoundChance = builder
					.comment("The chance for the chicken stick to make sounds when breaking blocks. Set to 0 to disable.")
					.translation("excompressum.config.chickenStickSoundChance")
					.defineInRange("chickenStickSoundChance", 0.2f, 0f, 1f);

			chickenStickSpawnChance = builder
					.comment("The chance for the chicken stick to spawn a chicken. Set to 0 to disable.")
					.translation("excompressum.config.chickenStickSpawnChance")
					.defineInRange("chickenStickSpawnChance", 0.008f, 0f, 1f);

			chickenStickSounds = builder
					.comment("The sound names the chicken stick will randomly play.")
					.translation("excompressum.config.chickenStickSounds")
					.defineList("chickenStickSounds", Lists.newArrayList(
							"entity.chicken.ambient",
							"entity.chicken.hurt",
							"entity.chicken.egg",
							"entity.chicken.step"), it -> it instanceof String);

			compressedCrookDurabilityMultiplier = builder
					.comment("The multiplier applied to the Compressed Crook's durability (based on the normal wooden crook)")
					.translation("excompressum.config.compressedCrookDurabilityMultiplier")
					.defineInRange("compressedCrookDurabilityMultiplier", 2f, Float.MIN_VALUE, Float.MAX_VALUE);

			compressedCrookSpeedMultiplier = builder
					.comment("The multiplier applied to the Compressed Crook's speed (based on the normal wooden crook)")
					.translation("excompressum.config.compressedCrookSpeedMultiplier")
					.defineInRange("compressedCrookSpeedMultiplier", 4f, Float.MIN_VALUE, Float.MAX_VALUE);

            builder.pop();

            builder.push("compat");

			enableSmashingModifier = builder
					.comment("If set to true, adding a double compressed diamond hammer will add the Smashing II modifier to a Tinkers Construct tool, which allows smashing of compressed blocks.")
					.translation("excompressum.config.enableSmashingModifier")
					.define("enableSmashingModifier", true);

			enableCompressingModifier = builder
					.comment("If set to true, adding an auto compressor will add the Compressing modifier to a Tinkers Construct tool, which will automatically compress hammered compressed blocks.")
					.translation("excompressum.config.enableCompressingModifier")
					.define("enableCompressingModifier", true);

			enableEvolvedOrechid = builder
					.comment("Setting this to false will disable the Evolved Orechid.")
					.translation("excompressum.config.enableEvolvedOrechid")
					.define("enableEvolvedOrechid", true);

			disableVanillaOrechid = builder
					.comment("If set to true, Botania's Orechid will not show up in the lexicon and will not be craftable.")
					.translation("excompressum.config.disableVanillaOrechid")
					.define("disableVanillaOrechid", false);

			manaSieveCost = builder
					.comment("The mana cost of the Mana Sieve per Tick.")
					.translation("excompressum.config.manaSieveCost")
					.defineInRange("manaSieveCost", 1, 0, Integer.MAX_VALUE);

			evolvedOrechidCost = builder
					.comment("The mana cost of the Evolved Orechid. GoG Orechid is 700, vanilla Orechid is 17500.")
					.translation("excompressum.config.evolvedOrechidCost")
					.defineInRange("evolvedOrechidCost", 700, 0, Integer.MAX_VALUE);

			evolvedOrechidDelay = builder
					.comment("The ore generation delay for the Evolved Orechid in ticks. GoG Orechid is 2, vanilla Orechid is 100.")
					.translation("excompressum.config.evolvedOrechidDelay")
					.defineInRange("evolvedOrechidDelay", 2, 0, Integer.MAX_VALUE);

            builder.pop();

            builder.push("automation");

			autoHammerEnergy = builder
					.comment("The energy cost of the auto hammer per tick.")
					.translation("excompressum.config.autoHammerEnergy")
					.defineInRange("autoHammerEnergy", 40, 0, Integer.MAX_VALUE);

			autoHammerSpeed = builder
					.comment("The speed at which the auto hammer will smash stuff.")
					.translation("excompressum.config.autoHammerSpeed")
					.defineInRange("autoHammerSpeed", 0.0075f, Float.MIN_VALUE, Float.MAX_VALUE);

			autoHammerDecay = builder
					.comment("The chance for the upgrade hammers in an auto hammer to lose durability (per operation)")
					.translation("excompressum.config.autoHammerDecay")
					.defineInRange("autoHammerDecay", 0.5f, 0f, 1f);

			autoCompressedHammerEnergy = builder
					.comment("The energy cost of the auto compressed hammer per tick.")
					.translation("excompressum.config.autoCompressedHammerEnergy")
					.defineInRange("autoCompressedHammerEnergy", 120, 0, Integer.MAX_VALUE);

			autoCompressedHammerSpeed = builder
					.comment("The speed at which the auto compressed hammer will smash stuff.")
					.translation("excompressum.config.autoCompressedHammerSpeed")
					.defineInRange("autoCompressedHammerSpeed", 0.00375f, Float.MIN_VALUE, Float.MAX_VALUE);

			autoSieveEnergy = builder
					.comment("The energy cost of the auto sieve per tick.")
					.translation("excompressum.config.autoSieveEnergy")
					.defineInRange("autoSieveEnergy", 40, 0, Integer.MAX_VALUE);

			autoSieveSpeed = builder
					.comment("The speed at which the auto sieve will sift stuff.")
					.translation("excompressum.config.autoSieveSpeed")
					.defineInRange("autoSieveSpeed", 0.0075f, Float.MIN_VALUE, Float.MAX_VALUE);

			autoHeavySieveEnergy = builder
					.comment("The energy cost of the auto heavy sieve per tick.")
					.translation("excompressum.config.autoHeavySieveEnergy")
					.defineInRange("autoHeavySieveEnergy", 120, 0, Integer.MAX_VALUE);

			autoHeavySieveSpeed = builder
					.comment("The speed at which the auto heavy sieve will sift stuff.")
					.translation("excompressum.config.autoHeavySieveSpeed")
					.defineInRange("autoHeavySieveSpeed", 0.00375f, Float.MIN_VALUE, Float.MAX_VALUE);

			autoCompressorEnergy = builder
					.comment("The energy cost of the auto compressor per tick.")
					.translation("excompressum.config.autoCompressorEnergy")
					.defineInRange("autoCompressorEnergy", 5, 0, Integer.MAX_VALUE);

			autoCompressorSpeed = builder
					.comment("The speed at which the auto compressor will compress stuff.")
					.translation("excompressum.config.autoCompressorSpeed")
					.defineInRange("autoCompressorSpeed", 0.1f, Float.MIN_VALUE, Float.MAX_VALUE);

			allowHeavySieveAutomation = builder
					.comment("Set this to true if you want to allow automation of the heavy sieve through fake players (i.e. Autonomous Activator)")
					.translation("excompressum.config.allowHeavySieveAutomation")
					.define("allowHeavySieveAutomation", false);

			heavySieveClicksPerSecond = builder
					.comment("The maximum amount of clicks per second on a heavy sieve. Clicks above this will be ignored.")
					.translation("excompressum.config.heavySieveClicksPerSecond")
					.defineInRange("heavySieveClicksPerSecond", 6, 1, Integer.MAX_VALUE);

			woodenCrucibleSpeed = builder
					.comment("The rate at which the wooden crucible extracts water. Measured in liquid per second.")
					.translation("excompressum.config.woodenCrucibleSpeed")
					.defineInRange("woodenCrucibleSpeed", 8, 1, Integer.MAX_VALUE);

            builder.pop();
        }
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue skipAutoSieveSkins;
        public final ForgeConfigSpec.BooleanValue showRegistryWarnings;
        public final ForgeConfigSpec.BooleanValue disableParticles;

        public Client(ForgeConfigSpec.Builder builder) {
            skipAutoSieveSkins = builder
                    .comment("Set this to true to disable the loading of auto sieve skins from a remote server.")
                    .translation("excompressum.config.skipAutoSieveSkins")
                    .define("skipAutoSieveSkins", false);

            showRegistryWarnings = builder
                    .comment("Set this to true if you're a modpack dev to see Ex Compressum registry warnings in chat. Errors will always display.")
                    .translation("excompressum.config.showRegistryWarnings")
                    .define("showRegistryWarnings", false);

            disableParticles = builder
                    .comment("Setting this to true will disable particles from the Sieves and Auto Hammers from Ex Compressum.")
                    .translation("excompressum.config.disableParticles")
                    .define("disableParticles", false);
        }
    }

	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static final ForgeConfigSpec clientSpec;
	public static final Client CLIENT;

	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}


}
