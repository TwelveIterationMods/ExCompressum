package net.blay09.mods.excompressum.compat.tconstruct;

import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.compat.IAddon;
import slimeknights.tconstruct.library.TinkerRegistry;

public class TConstructAddon implements IAddon {
    @Override
    public void postInit() {
        if(ModConfig.compat.enableModifiers) {
            ModSmashingII smashingII = new ModSmashingII();
            smashingII.addItem(ModItems.doubleCompressedDiamondHammer);
            TinkerRegistry.registerModifier(smashingII);
            //GameRegistry.addRecipe(new ItemStack(ModItems.doubleCompressedDiamondHammer), "##", "##", '#', ModItems.compressedHammerDiamond); TODO move to json
        }
    }
}
