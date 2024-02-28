package net.blay09.mods.excompressum;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.excompressum.api.ExCompressumAPI;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.entity.ModEntities;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.item.ModTags;
import net.blay09.mods.excompressum.loot.ModLoot;
import net.blay09.mods.excompressum.menu.ModMenus;
import net.blay09.mods.excompressum.registry.autosieveskin.AutoSieveSkinRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExCompressum {

    public static final String MOD_ID = "excompressum";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static SidedProxy<CommonProxy> proxy = Balm.sidedProxy("net.blay09.mods.excompressum.CommonProxy", "net.blay09.mods.excompressum.client.ClientProxy");

    public static void initialize() {
        ExCompressumAPI.__setupAPI(new InternalMethodsImpl());

        ExCompressumConfig.initialize();
        ModBlocks.initialize(Balm.getBlocks());
        ModBlockEntities.initialize(Balm.getBlockEntities());
        ModEntities.initialize(Balm.getEntities());
        ModItems.initialize(Balm.getItems());
        ModMenus.initialize(Balm.getMenus());
        ModTags.initialize(Balm.getRegistries());
        ModLoot.initialize(Balm.getLootTables());

        Balm.initializeIfLoaded(Compat.EXNIHILO_SEQUENTIA, "net.blay09.mods.excompressum.compat.exnihilosequentia.ExNihiloSequentiaAddon");

        AutoSieveSkinRegistry.load();
    }

}
