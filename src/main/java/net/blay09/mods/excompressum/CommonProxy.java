package net.blay09.mods.excompressum;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommonProxy {
    public void preloadSkin(GameProfile customSkin) {
    }

    public void spawnCrushParticles(World world, BlockPos pos, BlockState state) {
    }

    public void spawnAutoSieveParticles(World world, BlockPos pos, BlockState emitterState, BlockState particleState, int particleCount) {}
    public void spawnHeavySieveParticles(World world, BlockPos pos, BlockState particleState, int particleCount) {}
}
