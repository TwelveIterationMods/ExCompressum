package net.blay09.mods.excompressum.client.render;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class HammeringParticle extends TerrainParticle {
    public HammeringParticle(ClientLevel world, BlockPos pos, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState state) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, state, pos);

        setParticleSpeed(velocityX, velocityY, velocityZ);
        setLifetime(10);
    }
}
