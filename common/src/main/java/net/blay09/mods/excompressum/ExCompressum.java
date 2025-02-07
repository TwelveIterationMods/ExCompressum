package net.blay09.mods.excompressum;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Function3;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.excompressum.api.ExCompressumAPI;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.entity.ModEntities;
import net.blay09.mods.excompressum.handler.ChickenStickHandler;
import net.blay09.mods.excompressum.handler.CompressedEnemyHandler;
import net.blay09.mods.excompressum.handler.CrookPushHandler;
import net.blay09.mods.excompressum.handler.HammerSpeedHandler;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.loot.ModLoot;
import net.blay09.mods.excompressum.menu.ModMenus;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.blay09.mods.excompressum.registry.autosieveskin.AutoSieveSkinRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExCompressum {

    public static final String MOD_ID = "excompressum";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static SidedProxy<CommonProxy> proxy = Balm.sidedProxy("net.blay09.mods.excompressum.CommonProxy", "net.blay09.mods.excompressum.client.ClientProxy");
    public static Function3<Gson, ResourceLocation, JsonElement, LootTable> lootTableLoader;

    public static void initialize() {
        ExCompressumAPI.__setupAPI(new InternalMethodsImpl());

        ExCompressumConfig.initialize();
        ExRegistries.initialize();
        ModBlocks.initialize(Balm.getBlocks());
        ModBlockEntities.initialize(Balm.getBlockEntities());
        ModEntities.initialize(Balm.getEntities());
        ModItems.initialize(Balm.getItems());
        ModMenus.initialize(Balm.getMenus());
        ModLoot.initialize(Balm.getLootTables());
        ModRecipeTypes.initialize(Balm.getRecipes());

        Balm.initializeIfLoaded(Compat.EXNIHILO_SEQUENTIA, "net.blay09.mods.excompressum.forge.compat.exnihilosequentia.ExNihiloSequentiaAddon");
        Balm.initializeIfLoaded(Compat.EX_DEORUM, "net.blay09.mods.excompressum.forge.compat.exdeorum.ExDeorumAddon");
        Balm.initializeIfLoaded(Compat.FABRICAE_EX_NIHILO, "net.blay09.mods.excompressum.fabric.compat.fabricaeexnihilo.FabricaeExNihiloAddon");

        AutoSieveSkinRegistry.load();
        HammerSpeedHandler.initialize();
        CompressedEnemyHandler.initialize();
        CrookPushHandler.initialize();
        ChickenStickHandler.initialize();
    }

}
