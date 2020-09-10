package net.blay09.mods.excompressum.client.render;


import net.minecraft.block.BlockState;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

public class SievingParticle extends DiggingParticle {
	public SievingParticle(ClientWorld world, BlockPos pos, double x, double y, double z, float scale, BlockState state) {
		super(world, pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0f, 0f, 0f, state);
		setBlockPos(pos);

		particleScale = 0.2f * scale;
		maxAge = world.rand.nextInt(30) + 10;
		particleGravity = world.rand.nextFloat() * 0.25f;

		motionX = (world.rand.nextFloat() - 0.5f) * 0.025f * scale;
		motionY = 0f;
		motionZ = (world.rand.nextFloat() - 0.5f) * 0.025f * scale;
	}
}
