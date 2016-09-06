package net.blay09.mods.excompressum.compat.tconstruct;

import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.IAddon;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;

@SuppressWarnings("unused")
public class TConstructAddon implements IAddon {

    private boolean enableModifiers;

    @Override
    public void loadConfig(Configuration config) {
        enableModifiers = config.getBoolean("Enable Smashing II Modifier", "compat.tconstruct", true, "If set to true, adding a double compressed diamond hammer will add the Smashing II modifier to a Tinkers Construct tool, which allows smashing of compressed blocks.");
    }

    @Override
    public void postInit() {
        if(enableModifiers) {
            ModSmashingII smashingII = new ModSmashingII();
            smashingII.addItem(ModItems.doubleCompressedDiamondHammer);
            TinkerRegistry.registerModifier(smashingII);
            GameRegistry.addRecipe(new ItemStack(ModItems.doubleCompressedDiamondHammer), "##", "##", '#', ModItems.compressedHammerDiamond);
        }
    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {}
}
