package net.blay09.mods.excompressum;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.handler.CompressedEnemyHandler;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.registry.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

@Mod(modid = ExCompressum.MOD_ID, name = "Ex Compressum", dependencies = "required-after:exnihilo")
public class ExCompressum {

    public static final Logger logger = LogManager.getLogger();
    public static final String MOD_ID = "excompressum";

    @Mod.Instance
    public static ExCompressum instance;

    @SidedProxy(serverSide = "net.blay09.mods.excompressum.CommonProxy", clientSide = "net.blay09.mods.excompressum.client.ClientProxy")
    public static CommonProxy proxy;

    private Configuration config;

    public static float compressedCrookDurabilityMultiplier;
    public static float compressedCrookSpeedMultiplier;

    public static boolean allowHeavySieveAutomation;
    public static boolean woodenCrucibleBarrelRecipes;
    public static float woodenCrucibleSpeed;
    public static boolean woodenCrucibleFillFromRain;

    public static float baitWolfChance;
    public static float baitOcelotChance;
    public static float baitCowChance;
    public static float baitPigChance;
    public static float baitChickenChance;
    public static float baitSheepChance;
    public static float baitChildChance;

    public static float compressedMobChance;
    public static int compressedMobSize;
    public static List<String> compressedMobs = Lists.newArrayList();

    public static int autoCompressedHammerEnergy;
    public static float autoCompressedHammerSpeed;
    public static int autoCompressorEnergy;
    public static float autoCompressorSpeed;
    public static int autoHeavySieveEnergy;
    public static float autoHeavySieveSpeed;
    public static int autoSieveEnergy;
    public static float autoSieveSpeed;
    public static float autoSieveBookDecay;

    public static final ExCompressumCreativeTab creativeTab = new ExCompressumCreativeTab();

    public final List<IAddon> addons = Lists.newArrayList();

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        compressedCrookDurabilityMultiplier = config.getFloat("Compressed Crook Durability Multiplier", "general", 2f, 0.1f, 10f, "The multiplier applied to the Compressed Crook's durability (based on the normal wooden crook)");
        compressedCrookSpeedMultiplier = config.getFloat("Compressed Crook Speed Multiplier", "general", 4f, 0.1f, 10f, "The multiplier applied to the Compressed Crook's speed (based on the normal wooden crook)");
        allowHeavySieveAutomation = config.getBoolean("Allow Heavy Sieve Automation", "general", false, "Set this to true if you want to allow automation of the heavy sieve through fake players (i.e. Autonomous Activator)");
        woodenCrucibleBarrelRecipes = config.getBoolean("Allow Barrel Recipes in Wooden Crucible", "general", true, "Set this to true to have barrel recipes work in a Wooden Crucible (e.g. Water + Dust = Clay)");
        woodenCrucibleSpeed = config.getFloat("Wooden Crucible Speed", "general", 0.5f, 0.1f, 1f, "The speed at which the wooden crucible extracts water. 0.1 is equivalent to a torch below a crucible, 0.3 is equivalent to fire below a crucible.");
        woodenCrucibleFillFromRain = config.getBoolean("Allow Wooden Crucible filling from Rain", "general", true, "Set this to true to allow wooden crucibles to fill from rain.");

        baitWolfChance = config.getFloat("Wolf Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that a wolf bait will result in a wolf spawn.");
        baitOcelotChance = config.getFloat("Ocelot Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that an ocelot bait will result in an ocelot spawn.");
        baitCowChance = config.getFloat("Cow Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that a cow bait will result in a cow spawn.");
        baitPigChance = config.getFloat("Pig Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that a pig bait will result in a pig spawn.");
        baitChickenChance = config.getFloat("Chicken Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that a chicken bait will result in a chicken spawn.");
        baitSheepChance = config.getFloat("Sheep Bait Chance", "baits", 0.0005f, 0.0001f, 1f, "The chance (per tick) that a sheep bait will result in a sheep spawn.");
        baitChildChance = config.getFloat("Bait Child Chance", "baits", 0.5f, 0.0001f, 1f, "The chance that an animal spawned from a bait will result in a child.");

        autoCompressedHammerSpeed = config.getFloat("Auto Compressed Hammer Speed", "general", 0.005f, 0.0001f, 0.1f, "The speed at which the auto compressed hammer will smash stuff.");
        autoCompressedHammerEnergy = config.getInt("Auto Compressed Hammer Cost", "general", 120, 0, 100000, "The energy cost of the auto compressed hammer per tick.");
        autoHeavySieveSpeed = config.getFloat("Auto Heavy Sieve Speed", "general", 0.005f, 0.0001f, 0.1f, "The speed at which the auto heavy sieve will sift stuff.");
        autoHeavySieveEnergy = config.getInt("Auto Heavy Sieve Cost", "general", 80, 0, 100000, "The energy cost of the auto heavy sieve per tick.");
        autoSieveSpeed = config.getFloat("Auto Sieve Speed", "general", 0.005f, 0.0001f, 0.1f, "The speed at which the auto sieve will sift stuff.");
        autoSieveEnergy = config.getInt("Auto Sieve Cost", "general", 40, 0, 100000, "The energy cost of the auto sieve per tick.");
        autoCompressorSpeed = config.getFloat("Auto Compressor Speed", "general", 0.1f, 0.0001f, 1f, "The speed at which the auto compressor will compress stuff.");
        autoCompressorEnergy = config.getInt("Auto Compressor Cost", "general", 5, 0, 100000, "The energy cost of the auto compressor per tick.");
        autoSieveBookDecay = config.getFloat("Auto Sieve Book Decay", "general", 0.003f, 0.0001f, 0.1f, "The chance for the enchantment on books in a sieve to lose a level (per operation)");

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
                "Pig"
        }, "A list of entity names that can spawn as compressed entities."));

        ModItems.init();
        ModBlocks.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new CompressedEnemyHandler());

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLInterModComms.sendMessage("Waila", "register", "net.blay09.mods.excompressum.compat.waila.WailaProvider.register");
    }

    @Mod.EventHandler
    @SuppressWarnings("unused unchecked")
    public void postInit(FMLPostInitializationEvent event) {
        ModItems.registerRecipes(config);
        ModBlocks.registerRecipes(config);

        boolean easyMode = config.getBoolean("Easy Mode", "general", false, "Set this to true to enable easy-mode, which makes compressed smashables work for normal hammers instead.");
        CompressedHammerRegistry.load(config, easyMode);
        ChickenStickRegistry.load(config);
        HeavySieveRegistry.load(config);
        CompressedRecipeRegistry.reload();
        WoodenCrucibleRegistry.load(config);
        AutoSieveSkinRegistry.load();

        boolean isLegacyMineTweaker = true;
        try {
            Class mtClass = Class.forName("minetweaker.MineTweakerImplementationAPI");
            mtClass.getMethod("onPostReload", Class.forName("minetweaker.util.IEventHandler"));
            isLegacyMineTweaker = false;
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodException ignored) {}
        if(isLegacyMineTweaker) {
            registerAddon(event, "MineTweaker3", "net.blay09.mods.excompressum.compat.minetweaker.MineTweakerAddonLegacy");
        } else {
            registerAddon(event, "MineTweaker3", "net.blay09.mods.excompressum.compat.minetweaker.MineTweakerAddon");
        }

        registerAddon(event, "Botania", "net.blay09.mods.excompressum.compat.botania.BotaniaAddon");
        registerAddon(event, "TConstruct", "net.blay09.mods.excompressum.compat.tconstruct.TConstructAddon");
        registerAddon(event, "IguanaTweaksTConstruct", "net.blay09.mods.excompressum.compat.iguanatweakstconstruct.IguanaTweaksTConstructAddon");

        for(IAddon addon : addons) {
            addon.loadConfig(config);
            addon.postInit();
        }

        config.save();
    }

    private void registerAddon(FMLPostInitializationEvent event, String modid, String className) {
        IAddon addon = (IAddon) event.buildSoftDependProxy(modid, className);
        if(addon != null) {
            addons.add(addon);
        }
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void serverStarted(FMLServerStartedEvent event) {
        for(IAddon addon : addons) {
            addon.serverStarted(event);
        }
    }

}