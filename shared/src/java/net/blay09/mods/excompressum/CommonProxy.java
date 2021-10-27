package net.blay09.mods.excompressum;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

public class CommonProxy {
    public void preloadSkin(GameProfile customSkin) {
    }

    public void spawnCrushParticles(Level level, BlockPos pos, BlockState state) {
    }

    public void spawnAutoSieveParticles(Level level, BlockPos pos, BlockState emitterState, BlockState particleState, int particleCount) {
    }

    public void spawnHeavySieveParticles(Level level, BlockPos pos, BlockState particleState, int particleCount) {
    }

    public LootTables getLootTableManager() {
        return ServerLifecycleHooks.getCurrentServer().getLootTables();
    }

    public RecipeManager getRecipeManager(@Nullable Level level) {
        if (level != null && level.getServer() != null) {
            return level.getServer().getRecipeManager();
        }

        return ServerLifecycleHooks.getCurrentServer().getRecipeManager();
    }
}
