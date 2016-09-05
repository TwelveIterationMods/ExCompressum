package net.blay09.mods.excompressum.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.world.World;

public class ParticleSieve extends ParticleDigging {
	public ParticleSieve(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, IBlockState state) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, state);

		motionX = velocityX;
		motionY = velocityY;
		motionZ = velocityZ;
	}
}
