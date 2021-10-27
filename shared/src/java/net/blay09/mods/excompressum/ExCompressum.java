package net.blay09.mods.excompressum;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.api.ExCompressumAPI;
import net.blay09.mods.excompressum.client.ClientProxy;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.loot.ModLoot;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.NihilisticNihiloProvider;
import net.blay09.mods.excompressum.registry.autosieveskin.AutoSieveSkinRegistry;
import net.minecraftforge.fml.DistExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExCompressum {

    public static final String MOD_ID = "excompressum";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static CommonProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> CommonProxy::new);

    public static void initialize() {
        ExCompressumAPI.__setupAPI(new InternalMethodsImpl());

        ExCompressumConfig.initialize();

        Balm.initializeIfLoaded(Compat.EXNIHILO_OMNIA, "net.blay09.mods.excompressum.compat.exnihiloomnia.ExNihiloOmniaAddon");
        Balm.initializeIfLoaded(Compat.EXNIHILO_ADSCENSIO, "net.blay09.mods.excompressum.compat.exnihiloadscensio.ExNihiloAdscensioAddon");
        Balm.initializeIfLoaded(Compat.EXNIHILO_CREATIO, "net.blay09.mods.excompressum.compat.exnihilocreatio.ExNihiloCreatioAddon");
        Balm.initializeIfLoaded(Compat.EXNIHILO_SEQUENTIA, "net.blay09.mods.excompressum.compat.exnihilosequentia.ExNihiloSequentiaAddon");

        if (ExNihilo.instance == null) {
            ExCompressum.logger.warn("No Ex Nihilo mod installed - many things will be disabled.");
            ExNihilo.instance = new NihilisticNihiloProvider();
        }

        //Balm.initializeIfLoaded(Compat.TCONSTRUCT, "net.blay09.mods.excompressum.compat.tconstruct.TConstructAddon");
        Balm.initializeIfLoaded(Compat.PATCHOULI, "net.blay09.mods.excompressum.compat.patchouli.PatchouliAddon");

        ModLoot.registerLootEntries();

        AutoSieveSkinRegistry.load();
    }

}
