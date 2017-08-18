package net.blay09.mods.excompressum;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.api.ExCompressumAPI;
import net.blay09.mods.excompressum.block.BlockCompressed;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.entity.EntityAngryChicken;
import net.blay09.mods.excompressum.handler.ChickenStickHandler;
import net.blay09.mods.excompressum.handler.CompressedCrookHandler;
import net.blay09.mods.excompressum.handler.CompressedEnemyHandler;
import net.blay09.mods.excompressum.handler.CompressedHammerHandler;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.handler.HammerHandler;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.AbstractRegistry;
import net.blay09.mods.excompressum.registry.AutoSieveSkinRegistry;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.NihilisticNihiloProvider;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID)
@Mod(modid = ExCompressum.MOD_ID, name = "Ex Compressum", dependencies = "after:exnihiloomnia;after:exnihiloadscensio;after:exnihilocreatio;", acceptedMinecraftVersions = "[1.12]")
public class ExCompressum {

	public static final String MOD_ID = "excompressum";
	public static final Logger logger = LogManager.getLogger(MOD_ID);

	@Mod.Instance
	public static ExCompressum instance;

	@SidedProxy(serverSide = "net.blay09.mods.excompressum.CommonProxy", clientSide = "net.blay09.mods.excompressum.client.ClientProxy")
	public static CommonProxy proxy;

	public static File configDir;

	public static final ExCompressumCreativeTab creativeTab = new ExCompressumCreativeTab();

	public final List<IAddon> addons = Lists.newArrayList();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		//noinspection deprecation
		ExCompressumAPI.__internalMethods = new InternalMethods();

		configDir = new File(event.getModConfigurationDirectory(), "ExCompressum");
		if (!configDir.exists() && !configDir.mkdirs()) {
			throw new RuntimeException("Couldn't create Ex Compressum configuration directory");
		}

		ModBlocks.registerTileEntities();

		EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "angry_chicken"), EntityAngryChicken.class, "AngryChicken", 0, ExCompressum.instance, 64, 10, true);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new HammerHandler());
		MinecraftForge.EVENT_BUS.register(new CompressedHammerHandler());
		MinecraftForge.EVENT_BUS.register(new CompressedCrookHandler());
		MinecraftForge.EVENT_BUS.register(new CompressedEnemyHandler());
		MinecraftForge.EVENT_BUS.register(new ChickenStickHandler());

		registerAddon(Compat.BOTANIA, "net.blay09.mods.excompressum.compat.botania.BotaniaAddon");
		registerAddon(Compat.EXNIHILO_OMNIA, "net.blay09.mods.excompressum.compat.exnihiloomnia.ExNihiloOmniaAddon");
//		registerAddon(Compat.EXNIHILO_ADSCENSIO, "net.blay09.mods.excompressum.compat.exnihiloadscensio.ExNihiloAdscensioAddon");
		registerAddon(Compat.EXNIHILO_CREATIO, "net.blay09.mods.excompressum.compat.exnihilocreatio.ExNihiloCreatioAddon");
		if (ExRegistro.instance == null) {
			ExCompressum.logger.warn("No Ex Nihilo mod installed - many things will be disabled. Why would you run Ex Compressum without Ex Nihilo? Pfft.");
			ExRegistro.instance = new NihilisticNihiloProvider();
		}

		MinecraftForge.EVENT_BUS.register(proxy);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		OreDictionary.registerOre("dustWood", new ItemStack(ModItems.woodChipping));
		OreDictionary.registerOre("compressed1xDust", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.DUST.ordinal()));
		OreDictionary.registerOre("compressed1xCobblestone", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.COBBLESTONE.ordinal()));
		OreDictionary.registerOre("compressed1xGravel", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.GRAVEL.ordinal()));
		OreDictionary.registerOre("compressed1xSand", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SAND.ordinal()));
		OreDictionary.registerOre("compressed1xDirt", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.DIRT.ordinal()));
		OreDictionary.registerOre("compressed1xFlint", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.FLINT.ordinal()));
		OreDictionary.registerOre("compressed1xEnderGravel", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.ENDER_GRAVEL.ordinal()));
		OreDictionary.registerOre("compressed1xNetherGravel", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.NETHER_GRAVEL.ordinal()));
		OreDictionary.registerOre("compressed1xSoulsand", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SOUL_SAND.ordinal()));
		OreDictionary.registerOre("compressed1xNetherrack", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.NETHERRACK.ordinal()));
		OreDictionary.registerOre("compressed1xEndStone", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.END_STONE.ordinal()));

		FMLInterModComms.sendFunctionMessage(Compat.THEONEPROBE, "getTheOneProbe", "net.blay09.mods.excompressum.compat.top.TheOneProbeAddon");
		FMLInterModComms.sendMessage(Compat.WAILA, "register", "net.blay09.mods.excompressum.compat.waila.WailaProvider.register");

		for (IAddon addon : addons) {
			addon.init();
		}

		ChickenStickRegistry.INSTANCE.load(configDir);
		WoodenCrucibleRegistry.INSTANCE.load(configDir);
		CompressedHammerRegistry.INSTANCE.load(configDir);
		HeavySieveRegistry.INSTANCE.load(configDir);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		CompressedRecipeRegistry.reload();

		SieveMeshRegistry.registerDefaults();
		AutoSieveSkinRegistry.load();

		registerAddon(Compat.CRAFTTWEAKER, "net.blay09.mods.excompressum.compat.minetweaker.MineTweakerAddon");
		registerAddon(Compat.TCONSTRUCT, "net.blay09.mods.excompressum.compat.tconstruct.TConstructAddon");

		for (IAddon addon : addons) {
			addon.postInit();
		}
	}

	private Optional<?> buildSoftDependProxy(String modId, String className) {
		if (Loader.isModLoaded(modId)) {
			try {
				Class<?> clz = Class.forName(className, true, Loader.instance().getModClassLoader());
				return Optional.ofNullable(clz.newInstance());
			} catch (Exception e) {
				return Optional.empty();
			}
		}
		return Optional.empty();
	}

	private void registerAddon(String modid, String className) {
		Optional<?> optional = buildSoftDependProxy(modid, className);
		optional.ifPresent(o -> addons.add((IAddon) o));
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandExCompressum());
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (AbstractRegistry.registryErrors.size() > 0) {
			event.player.sendMessage(new TextComponentString(TextFormatting.RED + "There were errors loading the Ex Compressum registries:"));
			TextFormatting lastFormatting = TextFormatting.WHITE;
			for (String error : AbstractRegistry.registryErrors) {
				event.player.sendMessage(new TextComponentString(lastFormatting + "* " + error));
				lastFormatting = lastFormatting == TextFormatting.GRAY ? TextFormatting.WHITE : TextFormatting.GRAY;
			}
		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		ModBlocks.register(event.getRegistry());

		for (IAddon addon : instance.addons) {
			addon.registerBlocks(event.getRegistry());
		}
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		ModBlocks.registerItemBlocks(event.getRegistry());
		ModItems.register(event.getRegistry());

		for (IAddon addon : instance.addons) {
			addon.registerItems(event.getRegistry());
		}
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		proxy.registerModels();
		ModBlocks.registerModels();
		ModItems.registerModels();

		for (IAddon addon : instance.addons) {
			addon.registerModels();
		}
	}
}