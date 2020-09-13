package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.api.ExCompressumAPI;
import net.blay09.mods.excompressum.client.ClientProxy;
import net.blay09.mods.excompressum.client.ModRenderers;
import net.blay09.mods.excompressum.client.ModScreens;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.NihilisticNihiloProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ExCompressum.MOD_ID)
public class ExCompressum {

    public static final String MOD_ID = "excompressum";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static final ExCompressumCreativeTab itemGroup = new ExCompressumCreativeTab();

    public ExCompressum() {
        ExCompressumAPI.__setupAPI(new InternalMethodsImpl());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ExCompressumConfig.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ExCompressumConfig.clientSpec);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);

        MinecraftForge.EVENT_BUS.register(proxy);
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        buildSoftDependProxy(Compat.EXNIHILO_OMNIA, "net.blay09.mods.excompressum.compat.exnihiloomnia.ExNihiloOmniaAddon");
        buildSoftDependProxy(Compat.EXNIHILO_ADSCENSIO, "net.blay09.mods.excompressum.compat.exnihiloadscensio.ExNihiloAdscensioAddon");
        buildSoftDependProxy(Compat.EXNIHILO_CREATIO, "net.blay09.mods.excompressum.compat.exnihilocreatio.ExNihiloCreatioAddon");

        if (ExRegistro.instance == null) {
            ExCompressum.logger.warn("No Ex Nihilo mod installed - many things will be disabled.");
            ExRegistro.instance = new NihilisticNihiloProvider();
        }

        buildSoftDependProxy(Compat.BOTANIA, "net.blay09.mods.excompressum.compat.botania.BotaniaAddon");
        buildSoftDependProxy(Compat.TCONSTRUCT, "net.blay09.mods.excompressum.compat.tconstruct.TConstructAddon");
    }

    private void setupClient(FMLClientSetupEvent event) {
        ModScreens.register();
        ModRenderers.register();
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

    public static void registerOreDictionary() {
        /* TODO OreDictionary.registerOre("dustWood", new ItemStack(ModItems.woodChipping));
        OreDictionary.registerOre("compressed1xDust", new ItemStack(ModBlocks.compressedBlock, 1, CompressedBlockType.DUST.ordinal()));
        OreDictionary.registerOre("compressed1xCobblestone", new ItemStack(ModBlocks.compressedBlock, 1, CompressedBlockType.COBBLESTONE.ordinal()));
        OreDictionary.registerOre("compressed1xGravel", new ItemStack(ModBlocks.compressedBlock, 1, CompressedBlockType.GRAVEL.ordinal()));
        OreDictionary.registerOre("compressed1xSand", new ItemStack(ModBlocks.compressedBlock, 1, CompressedBlockType.SAND.ordinal()));
        OreDictionary.registerOre("compressed1xDirt", new ItemStack(ModBlocks.compressedBlock, 1, CompressedBlockType.DIRT.ordinal()));
        OreDictionary.registerOre("compressed1xFlint", new ItemStack(ModBlocks.compressedBlock, 1, CompressedBlockType.FLINT.ordinal()));
        OreDictionary.registerOre("compressed1xEnderGravel", new ItemStack(ModBlocks.compressedBlock, 1, CompressedBlockType.ENDER_GRAVEL.ordinal()));
        OreDictionary.registerOre("compressed1xNetherGravel", new ItemStack(ModBlocks.compressedBlock, 1, CompressedBlockType.NETHER_GRAVEL.ordinal()));
        OreDictionary.registerOre("compressed1xSoulsand", new ItemStack(ModBlocks.compressedBlock, 1, CompressedBlockType.SOUL_SAND.ordinal()));
        OreDictionary.registerOre("compressed1xNetherrack", new ItemStack(ModBlocks.compressedBlock, 1, CompressedBlockType.NETHERRACK.ordinal()));
        OreDictionary.registerOre("compressed1xEndStone", new ItemStack(ModBlocks.compressedBlock, 1, CompressedBlockType.END_STONE.ordinal())); */
    }

}
