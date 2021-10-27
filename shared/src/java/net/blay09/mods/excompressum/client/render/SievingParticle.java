package net.blay09.mods.excompressum.client.render;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SievingParticle extends TerrainParticle {
	public SievingParticle(ClientLevel level, BlockPos pos, double x, double y, double z, float scale, BlockState state) {
		super(level, pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0f, 0f, 0f, state);

		quadSize = 0.2f * scale;
		lifetime = level.random.nextInt(30) + 10;
		gravity = level.random.nextFloat() * 0.25f;

		xd = (level.random.nextFloat() - 0.5f) * 0.025f * scale;
		yd = 0f;
		zd = (level.random.nextFloat() - 0.5f) * 0.025f * scale;
	}
}
