package net.blay09.mods.excompressum;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.platform.event.callback.ServerLifecycleCallback;
import net.blay09.mods.excompressum.api.ExCompressumAPI;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.hudinfo.ModHudInfo;
import net.blay09.mods.excompressum.component.ModComponents;
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
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class ExCompressum {

    public static final String MOD_ID = "excompressum";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static Supplier<CommonProxy> proxy = Balm.<CommonProxy>sidedProxy("net.blay09.mods.excompressum.CommonProxy",
            "net.blay09.mods.excompressum.client.ClientProxy").buildLazily();

    public static void initialize(BalmRegistrars registrars) {
        ExCompressumAPI.__setupAPI(new InternalMethodsImpl());

        ExCompressumConfig.initialize();
        ExRegistries.initialize();
        registrars.dataComponentTypes(ModComponents::initialize);
        registrars.blocks(ModBlocks::initialize);
        registrars.blockEntityTypes(ModBlockEntities::initialize);
        registrars.entityTypes(ModEntities::initialize);
        registrars.items(ModItems::initialize);
        registrars.creativeModeTabs(ModItems::initialize);
        registrars.menuTypes(ModMenus::initialize);
        ModLoot.initialize(Balm.lootModifiers());
        registrars.recipeTypes(ModRecipeTypes::initialize);

        ServerLifecycleCallback.Started.EVENT.register(server -> {
            initializeAddons();
        });

        Balm.config().onConfigAvailable(ExCompressumConfig.class, config -> AutoSieveSkinRegistry.load());

        HammerSpeedHandler.initialize();
        CompressedEnemyHandler.initialize();
        CrookPushHandler.initialize();
        ChickenStickHandler.initialize();

        ModHudInfo.initialize(Balm.modSupport().hudInfo());
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void  initializeAddons() {
        Balm.initializeIfLoaded(Compat.EXNIHILO_SEQUENTIA, "net.blay09.mods.excompressum.neoforge.compat.exnihilosequentia.ExNihiloSequentiaAddon");
        Balm.initializeIfLoaded(Compat.EX_DEORUM, "net.blay09.mods.excompressum.neoforge.compat.exdeorum.ExDeorumAddon");
        Balm.initializeIfLoaded(Compat.FABRICAE_EX_NIHILO, "net.blay09.mods.excompressum.fabric.compat.fabricaeexnihilo.FabricaeExNihiloAddon");
    }
}
