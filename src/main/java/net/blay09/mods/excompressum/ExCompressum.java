package net.blay09.mods.excompressum;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.entity.EntityAngryChicken;
import net.blay09.mods.excompressum.handler.ChickenStickHandler;
import net.blay09.mods.excompressum.handler.CompressedEnemyHandler;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.registry.*;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.registry.crucible.WoodenCrucibleRegistry;
import net.blay09.mods.excompressum.registry.hammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.sieve.HeavySieveRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(modid = ExCompressum.MOD_ID, name = "Ex Compressum")
public class ExCompressum {

    public static final Logger logger = LogManager.getLogger();
    public static final String MOD_ID = "excompressum";

    @Mod.Instance
    public static ExCompressum instance;

    @SidedProxy(serverSide = "net.blay09.mods.excompressum.CommonProxy", clientSide = "net.blay09.mods.excompressum.client.ClientProxy")
    public static CommonProxy proxy;

    private Configuration config;

    public static final ExCompressumCreativeTab creativeTab = new ExCompressumCreativeTab();

    public final List<IAddon> addons = Lists.newArrayList();

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        ExCompressumConfig.load(config);

        ModItems.init();
        ModBlocks.init();

        EntityRegistry.registerModEntity(EntityAngryChicken.class, "AngryChicken", 0, this, 64, 5, true);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new CompressedEnemyHandler());
        MinecraftForge.EVENT_BUS.register(new ChickenStickHandler());

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLInterModComms.sendMessage("Waila", "register", "net.blay09.mods.excompressum.compat.waila.WailaProvider.register");

        proxy.init(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused unchecked")
    public void postInit(FMLPostInitializationEvent event) {
        registerAddon(event, Compat.EXNIHILOOMNIA, "net.blay09.mods.excompressum.compat.exnihiloomnia.ExNihiloOmniaAddon");

        if(ExRegistro.instance == null) {
            ExCompressum.logger.warn("No Ex Nihilo mod installed - many things will be disabled. Why would you run Ex Compressum without Ex Nihilo? Pfft.");
            ExRegistro.instance = new NihilisticNihiloProvider();
        }

        ModRecipes.init(config);

        CompressedHammerRegistry.loadFromConfig(config);
        ChickenStickRegistry.load(config);
        HeavySieveRegistry.loadFromConfig(config);
        CompressedRecipeRegistry.reload();
        WoodenCrucibleRegistry.load(config);
        AutoSieveSkinRegistry.load();

        registerAddon(event, "MineTweaker3", "net.blay09.mods.excompressum.compat.minetweaker.MineTweakerAddon");
        //registerAddon(event, Compat.BOTANIA, "net.blay09.mods.excompressum.compat.botania.BotaniaAddon"); // TODO Botania requires addons to register stuff in preInit because stupid, fix this later
        registerAddon(event, "TConstruct", "net.blay09.mods.excompressum.compat.tconstruct.TConstructAddon");

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
    @SuppressWarnings("unused")
    public void serverStarted(FMLServerStartedEvent event) {
        for(IAddon addon : addons) {
            addon.serverStarted(event);
        }
    }

}