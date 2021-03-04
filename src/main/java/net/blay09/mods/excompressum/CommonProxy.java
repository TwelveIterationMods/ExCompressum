package net.blay09.mods.excompressum;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

public class CommonProxy {
    public void preloadSkin(GameProfile customSkin) {
    }

    public void spawnCrushParticles(World world, BlockPos pos, BlockState state) {
    }

    public void spawnAutoSieveParticles(World world, BlockPos pos, BlockState emitterState, BlockState particleState, int particleCount) {
    }

    public void spawnHeavySieveParticles(World world, BlockPos pos, BlockState particleState, int particleCount) {
    }

    public LootTableManager getLootTableManager() {
        return ServerLifecycleHooks.getCurrentServer().getLootTableManager();
    }

    public RecipeManager getRecipeManager(@Nullable World world) {
        if (world != null && world.getServer() != null) {
            return world.getServer().getRecipeManager();
        }
        return ServerLifecycleHooks.getCurrentServer().getRecipeManager();
    }
}
