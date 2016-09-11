package net.blay09.mods.excompressum;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.entity.EntityAngryChicken;
import net.blay09.mods.excompressum.handler.ChickenStickHandler;
import net.blay09.mods.excompressum.handler.CompressedEnemyHandler;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.*;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistry;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Mod(modid = ExCompressum.MOD_ID, name = "Ex Compressum", dependencies = "after:exnihiloomnia;after:exnihiloadscensio")
@SuppressWarnings("unused")
public class ExCompressum {

    public static final Logger logger = LogManager.getLogger();
    public static final String MOD_ID = "excompressum";

    @Mod.Instance
    public static ExCompressum instance;

    @SidedProxy(serverSide = "net.blay09.mods.excompressum.CommonProxy", clientSide = "net.blay09.mods.excompressum.client.ClientProxy")
    public static CommonProxy proxy;

    public static File configDir;
    private Configuration config;

    public static final ExCompressumCreativeTab creativeTab = new ExCompressumCreativeTab();

    public final List<IAddon> addons = Lists.newArrayList();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "ExCompressum");
        if(!configDir.exists() && !configDir.mkdirs()) {
            throw new RuntimeException("Couldn't create Ex Compressum configuration directory");
        }
        config = new Configuration(new File(configDir, "ExCompressum.cfg"));
        config.load();

        ExCompressumConfig.load(config);

        ModItems.init();
        ModBlocks.init();

        EntityRegistry.registerModEntity(EntityAngryChicken.class, "AngryChicken", 0, this, 64, 5, true);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CompressedEnemyHandler());
        MinecraftForge.EVENT_BUS.register(new ChickenStickHandler());

        // Botania is a special snowflake that requires addon initialization during preInit
        if(Loader.isModLoaded(Compat.BOTANIA)) {
            try {
                Class<?> clazz = Class.forName("net.blay09.mods.excompressum.compat.botania.BotaniaAddon");
                addons.add((IAddon) clazz.getConstructor(Configuration.class).newInstance(config));
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                ExCompressum.logger.error("Failed to load Botania addon: {}", e);
            }
        }

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLInterModComms.sendMessage(Compat.WAILA, "register", "net.blay09.mods.excompressum.compat.waila.WailaProvider.register");

        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        registerAddon(event, Compat.EXNIHILO_OMNIA, "net.blay09.mods.excompressum.compat.exnihiloomnia.ExNihiloOmniaAddon");
        registerAddon(event, Compat.EXNIHILO_ADSCENSIO, "net.blay09.mods.excompressum.compat.exnihiloadscensio.ExNihiloAdscensioAddon");

        if(ExRegistro.instance == null) {
            ExCompressum.logger.warn("No Ex Nihilo mod installed - many things will be disabled. Why would you run Ex Compressum without Ex Nihilo? Pfft.");
            ExRegistro.instance = new NihilisticNihiloProvider();
        }

        ChickenStickRegistry.INSTANCE.load(configDir);
        WoodenCrucibleRegistry.INSTANCE.load(configDir);
        CompressedHammerRegistry.INSTANCE.load(configDir);
        HeavySieveRegistry.INSTANCE.load(configDir);

        ModRecipes.init(config);
        CompressedRecipeRegistry.reload();

        SieveMeshRegistry.registerDefaults();
        AutoSieveSkinRegistry.load();

        registerAddon(event, Compat.MINETWEAKER, "net.blay09.mods.excompressum.compat.minetweaker.MineTweakerAddon");
        registerAddon(event, Compat.TCONSTRUCT, "net.blay09.mods.excompressum.compat.tconstruct.TConstructAddon");

        for(IAddon addon : addons) {
            addon.loadConfig(config);
            addon.postInit();
        }

        if(config.hasChanged()) {
            config.save();
        }

        proxy.postInit(event);
    }

    private void registerAddon(FMLPostInitializationEvent event, String modid, String className) {
        Optional<?> optional = event.buildSoftDependProxy(modid, className);
        if(optional.isPresent()) {
            addons.add((IAddon) optional.get());
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandExCompressum());
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        for(IAddon addon : addons) {
            addon.serverStarted(event);
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if(AbstractRegistry.registryErrors.size() > 0) {
            event.player.addChatComponentMessage(new TextComponentString(TextFormatting.RED + "There were errors loading the Ex Compressum registries:"));
            TextFormatting lastFormatting = TextFormatting.WHITE;
            for(String error : AbstractRegistry.registryErrors) {
                event.player.addChatMessage(new TextComponentString(lastFormatting + "* " + error));
                lastFormatting = lastFormatting == TextFormatting.GRAY ? TextFormatting.WHITE : TextFormatting.GRAY;
            }
        }
    }

}