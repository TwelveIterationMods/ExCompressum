package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.fabric.provider.FabricBalmProviders;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricExCompressum implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initialize(ExCompressum.MOD_ID, ExCompressum::initialize);
    }
}
