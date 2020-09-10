package net.blay09.mods.excompressum.client.render;


import net.minecraft.block.BlockState;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

public class HammeringParticle extends DiggingParticle {
	public HammeringParticle(ClientWorld world, BlockPos pos, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState state) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, state);

		motionX = velocityX;
		motionY = velocityY;
		motionZ = velocityZ;

		this.maxAge = 10;

		setBlockPos(pos);
	}
}
