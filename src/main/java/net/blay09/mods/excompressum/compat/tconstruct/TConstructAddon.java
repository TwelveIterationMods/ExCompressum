package net.blay09.mods.excompressum.compat.tconstruct;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.ItemStack;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.client.TConstructClientRegistry;
import tconstruct.library.crafting.ModifyBuilder;

@SuppressWarnings("unused")
public class TConstructAddon {

    public TConstructAddon() {
        if(ExCompressum.tconstructModifier) {
            ModifyBuilder.registerModifier(new ModSmashingII(new ItemStack[]{new ItemStack(ExCompressum.doubleCompressedDiamondHammer, 1, 0)}, 0));
            TConstructRegistry.registerActiveToolMod(new ActiveSmashingMod());
        }
    }

}
