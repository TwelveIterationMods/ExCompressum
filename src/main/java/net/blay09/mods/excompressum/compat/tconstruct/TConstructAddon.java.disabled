package net.blay09.mods.excompressum.compat.tconstruct;

import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.compat.IAddon;
import net.minecraft.item.Item;
import slimeknights.tconstruct.library.TinkerRegistry;

public class TConstructAddon implements IAddon {
    @Override
    public void postInit() {
        if(ModConfig.compat.enableSmashingModifier) {
            ModSmashingII smashingII = new ModSmashingII();
            smashingII.addItem(ModItems.doubleCompressedDiamondHammer);
        }

        if(ModConfig.compat.enableCompressingModifier) {
            ModCompressing compressing = new ModCompressing();
            compressing.addItem(Item.getItemFromBlock(ModBlocks.autoCompressor));
        }
    }
}
