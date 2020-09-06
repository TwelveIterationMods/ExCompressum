package net.blay09.mods.excompressum.client.render;


import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleAutoHammer extends ParticleDigging {
	public ParticleAutoHammer(World world, BlockPos pos, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState state) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, state);

		motionX = velocityX;
		motionY = velocityY;
		motionZ = velocityZ;

		particleMaxAge = 10;

		setBlockPos(pos);
	}
}
