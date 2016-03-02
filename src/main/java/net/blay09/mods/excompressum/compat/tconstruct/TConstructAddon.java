package net.blay09.mods.excompressum.compat.tconstruct;

import cpw.mods.fml.common.event.FMLServerStartedEvent;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.item.ItemDoubleCompressedDiamondHammer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.ModifyBuilder;

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
            ItemDoubleCompressedDiamondHammer.registerRecipes();
            ModifyBuilder.registerModifier(new ModSmashingII(new ItemStack[]{new ItemStack(ModItems.doubleCompressedDiamondHammer, 1, 0)}));
            TConstructRegistry.registerActiveToolMod(new ActiveSmashingMod());
        }
    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {}
}
