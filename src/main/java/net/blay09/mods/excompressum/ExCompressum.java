package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.api.ExCompressumAPI;
import net.blay09.mods.excompressum.client.ClientProxy;
import net.blay09.mods.excompressum.client.ModRenderers;
import net.blay09.mods.excompressum.client.ModScreens;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.top.TheOneProbeAddon;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.loot.ModLoot;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.NihilisticNihiloProvider;
import net.blay09.mods.excompressum.registry.autosieveskin.AutoSieveSkinRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import novamachina.exnihilosequentia.common.compat.top.CompatTOP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ExCompressum.MOD_ID)
public class ExCompressum {

    public static final String MOD_ID = "excompressum";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static CommonProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> CommonProxy::new);

    public static final ExCompressumCreativeTab itemGroup = new ExCompressumCreativeTab();

    public ExCompressum() {
        ExCompressumAPI.__setupAPI(new InternalMethodsImpl());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ExCompressumConfig.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ExCompressumConfig.clientSpec);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imc);

        MinecraftForge.EVENT_BUS.register(proxy);

        AutoSieveSkinRegistry.load();
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        buildSoftDependProxy(Compat.EXNIHILO_OMNIA, "net.blay09.mods.excompressum.compat.exnihiloomnia.ExNihiloOmniaAddon");
        buildSoftDependProxy(Compat.EXNIHILO_ADSCENSIO, "net.blay09.mods.excompressum.compat.exnihiloadscensio.ExNihiloAdscensioAddon");
        buildSoftDependProxy(Compat.EXNIHILO_CREATIO, "net.blay09.mods.excompressum.compat.exnihilocreatio.ExNihiloCreatioAddon");
        buildSoftDependProxy(Compat.EXNIHILO_SEQUENTIA, "net.blay09.mods.excompressum.compat.exnihilosequentia.ExNihiloSequentiaAddon");

        if (ExNihilo.instance == null) {
            ExCompressum.logger.warn("No Ex Nihilo mod installed - many things will be disabled.");
            ExNihilo.instance = new NihilisticNihiloProvider();
        }

        //buildSoftDependProxy(Compat.TCONSTRUCT, "net.blay09.mods.excompressum.compat.tconstruct.TConstructAddon");
        buildSoftDependProxy(Compat.PATCHOULI, "net.blay09.mods.excompressum.compat.patchouli.PatchouliAddon");

        ModLoot.registerLootEntries();
    }

    private void setupClient(FMLClientSetupEvent event) {
        ModScreens.register();
        ModRenderers.register();
    }

    private void imc(InterModEnqueueEvent event) {
        if(ModList.get().isLoaded("theoneprobe")) {
            TheOneProbeAddon.register();
        }
    }

    private static void buildSoftDependProxy(String modId, String className) {
        if (ModList.get().isLoaded(modId)) {
            try {
                Class.forName(className).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                logger.error("Failed to load ExCompressum compat for mod id {}: ", modId, e);
            }
        }
    }


}
