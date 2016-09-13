package net.blay09.mods.excompressum.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleSieve extends ParticleDigging {
	public ParticleSieve(World world, BlockPos pos, double x, double y, double z, float scale, IBlockState state) {
		super(world, pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0f, 0f, 0f, state);
		setBlockPos(pos);

		particleScale = 0.2f * scale;
		particleMaxAge = world.rand.nextInt(30) + 10;
		particleGravity = world.rand.nextFloat() * 0.25f;

		motionX = (world.rand.nextFloat() - 0.5f) * 0.025f * scale;
		motionY = 0f;
		motionZ = (world.rand.nextFloat() - 0.5f) * 0.025f * scale;
	}
}
