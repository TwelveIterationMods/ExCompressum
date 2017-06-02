package net.blay09.mods.excompressum.compat.tconstruct;

import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.compat.IAddon;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;

public class TConstructAddon implements IAddon {

    private boolean enableModifiers;

    @Override
    public void loadConfig(Configuration config) {
        enableModifiers = config.getBoolean("Enable Smashing II Modifier", "compat.tconstruct", true, "If set to true, adding a double compressed diamond hammer will add the Smashing II modifier to a Tinkers Construct tool, which allows smashing of compressed blocks.");
    }

    @Override
    public void init() {

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
    @SideOnly(Side.CLIENT)
    public void clientInit() {

    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {}
}
