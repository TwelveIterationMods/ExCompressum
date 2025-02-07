package net.blay09.mods.excompressum.config;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;
import net.blay09.mods.balm.api.config.Config;
import net.blay09.mods.balm.api.config.ExpectedType;
import net.blay09.mods.excompressum.ExCompressum;

import java.util.List;

@Config(ExCompressum.MOD_ID)
public class ExCompressumConfigData implements BalmConfigData {

    public General general = new General();
    public Baits baits = new Baits();
    public CompressedMobs compressedMobs = new CompressedMobs();
    public Tools tools = new Tools();
    public Automation automation = new Automation();
    public Client client = new Client();

    public static class General {
        @Comment("The amount of times the heavy sieve should roll for compressed entries. For example, a value of 7 means for every compressed gravel you only get the equivalent drops of 7 sifted gravel blocks (2 loss). A value of 9 would therefore mean no loss.")
        public int heavySieveDefaultRolls = 7;
    }

    public static class Baits {
        @Comment("The chance (per second) that a wolf bait will result in a spawn.")
        public double wolfBaitChance = 0.01f;

        @Comment("The chance (per second) that a ocelot bait will result in a spawn.")
        public double ocelotBaitChance = 0.01f;

        @Comment("The chance (per second) that a cow bait will result in a spawn.")
        public double cowBaitChance = 0.01f;

        @Comment("The chance (per second) that a pig bait will result in a spawn.")
        public double pigBaitChance = 0.01f;

        @Comment("The chance (per second) that a chicken bait will result in a spawn.")
        public double chickenBaitChance = 0.01f;

        @Comment("The chance (per second) that a sheep bait will result in a spawn.")
        public double sheepBaitChance = 0.01f;

        @Comment("The chance (per second) that a squid bait will result in a spawn.")
        public double squidBaitChance = 0.01f;

        @Comment("The chance (per second) that a rabbit bait will result in a spawn.")
        public double rabbitBaitChance = 0.01f;

        @Comment("The chance (per second) that a mooshroom bait will result in a spawn.")
        public double horseBaitChance = 0.01f;

        @Comment("The chance (per second) that a donkey bait will result in a spawn.")
        public double donkeyBaitChance = 0.01f;

        @Comment("The chance (per second) that a parrot bait will result in a spawn.")
        public double parrotBaitChance = 0.01f;

        @Comment("The chance (per second) that a polar bear bait will result in a spawn.")
        public double polarBearBaitChance = 0.01f;

        @Comment("The chance (per second) that a llama bait will result in a spawn.")
        public double llamaBaitChance = 0.01f;

        @Comment("The chance (per second) that a cat bait will result in a spawn.")
        public double catBaitChance = 0.01f;

        @Comment("The chance (per second) that a fox bait will result in a spawn.")
        public double foxBaitChance = 0.01f;

        @Comment("The chance (per second) that a turtle bait will result in a spawn.")
        public double turtleBaitChance = 0.01f;

        @Comment("The chance (per second) that a mooshroom bait will result in a spawn.")
        public double mooshroomBaitChance = 0.01f;

        @Comment("The chance (per second) that an animal spawned from a bait will result in a child.")
        public double childBaitChance = 0.5f;
    }

    public static class CompressedMobs {
        @Comment("The chance for mobs to spawn as Compressed Mobs. Set to 0 to disable.")
        public double compressedMobChance = 0.01f;

        @Comment("The amount of mobs that will spawn upon death of a compressed mob.")
        public int compressedMobSize = 9;

        @Comment("Should the allowed mobs list be treated as a blacklist instead of a whitelist?")
        public boolean compressedMobAllowedMobsIsBlacklist = false;

        @ExpectedType(String.class)
        @Comment("A list of entity registry names that can spawn as compressed entities.")
        public List<String> compressedMobAllowedMobs = Lists.newArrayList(
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
                "minecraft:bee",
                "minecraft:chicken",
                "minecraft:sheep",
                "minecraft:cow",
                "minecraft:mooshroom",
                "minecraft:pig",
                "minecraft:ghast",
                "minecraft:drowned",
                "minecraft:elder_guardian",
                "minecraft:endermite",
                "minecraft:cat",
                "minecraft:evoker",
                "minecraft:husk",
                "minecraft:hoglin",
                "minecraft:guardian",
                "minecraft:piglin",
                "minecraft:piglin_brute",
                "minecraft:pillager",
                "minecraft:shulker",
                "minecraft:stray",
                "minecraft:vindicator",
                "minecraft:wither_skeleton",
                "minecraft:zoglin",
                "minecraft:zombie_villager",
                "minecraft:zombified_piglin");
    }

    public static class Tools {
        @Comment("If true, hitting a chicken with a stick will turn it into an Angry Chicken, which will drop a Chicken Stick when killed.")
        public boolean allowChickenStickCreation = true;

        @Comment("The chance for the chicken stick to make sounds when breaking blocks. Set to 0 to disable.")
        public double chickenStickSoundChance = 0.2f;

        @Comment("The chance for the chicken stick to spawn a chicken. Set to 0 to disable.")
        public double chickenStickSpawnChance = 0.008f;

        @ExpectedType(String.class)
        @Comment("The sound names the chicken stick will randomly play.")
        public List<String> chickenStickSounds = Lists.newArrayList(
                "entity.chicken.ambient",
                "entity.chicken.hurt",
                "entity.chicken.egg",
                "entity.chicken.step");

        @Comment("The multiplier applied to the Compressed Crook's durability (based on the normal wooden crook)")
        public double compressedCrookDurabilityMultiplier = 2f;

        @Comment("The multiplier applied to the Compressed Crook's speed (based on the normal wooden crook)")
        public double compressedCrookSpeedMultiplier = 4f;
    }

    public static class Automation {
        @Comment("The energy cost of the auto hammer per tick.")
        public int autoHammerEnergy = 40;

        @Comment("The speed at which the auto hammer will smash stuff.")
        public double autoHammerSpeed = 0.008f;

        @Comment("The chance for the upgrade hammers in an auto hammer to lose durability (per operation)")
        public double autoHammerDecay = 0.5f;

        @Comment("The energy cost of the auto compressed hammer per tick.")
        public int autoCompressedHammerEnergy = 120;

        @Comment("The speed at which the auto compressed hammer will smash stuff.")
        public double autoCompressedHammerSpeed = 0.0038f;

        @Comment("The energy cost of the auto sieve per tick.")
        public int autoSieveEnergy = 40;

        @Comment("The energy cost of the auto heavy sieve per tick.")
        public int autoHeavySieveEnergy = 120;

        @Comment("The speed at which the auto heavy sieve will sift stuff.")
        public double autoHeavySieveSpeed = 0.00375f;

        @Comment("The speed at which the auto sieve will sift stuff.")
        public double autoSieveSpeed = 0.0075f;

        @Comment("The energy cost of the auto compressor per tick.")
        public int autoCompressorEnergy = 5;

        @Comment("The speed at which the auto compressor will compress stuff.")
        public double autoCompressorSpeed = 0.1f;

        @Comment("Set this to true if you want to allow automation of the heavy sieve through fake players (i.e. Autonomous Activator)")
        public boolean allowHeavySieveAutomation = false;

        @Comment("The maximum amount of clicks per second on a heavy sieve. Clicks above this will be ignored.")
        public int heavySieveClicksPerSecond = 6;

        @Comment("The rate at which the wooden crucible extracts water. Measured in liquid per second.")
        public int woodenCrucibleSpeed = 8;

        @Comment("Set this to true if you want to allow right-clicking Auto Sieves with Food to give them speed boosts")
        public boolean allowAutoSieveFoodSpeedBoosts = true;
    }

    public static class Client {
        @Comment("Set this to true to disable the loading of auto sieve skins from a remote server.")
        public boolean skipAutoSieveSkins = false;

        @Comment("Setting this to true will disable particles from the Sieves and Auto Hammers from Ex Compressum.")
        public boolean disableParticles = false;
    }

}
