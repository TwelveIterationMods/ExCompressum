package net.blay09.mods.excompressum.tile;

import cpw.mods.fml.common.Optional;
import exnihilo.registries.helpers.Smashable;
import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.minecraft.item.ItemStack;

import java.util.Collection;

@Optional.Interface(modid = "CoFHCore", iface = "cofh.api.energy.IEnergyHandler", striprefs = true)
public class TileEntityAutoCompressedHammer extends TileEntityAutoHammer {

    @Override
    protected boolean isRegistered(ItemStack itemStack) {
        return CompressedHammerRegistry.isRegistered(itemStack);
    }

    @Override
    protected Collection<Smashable> getSmashables(ItemStack itemStack) {
        return CompressedHammerRegistry.getSmashables(itemStack);
    }

}
