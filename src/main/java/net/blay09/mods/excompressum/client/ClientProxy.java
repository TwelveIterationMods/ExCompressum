package net.blay09.mods.excompressum.client;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import net.blay09.mods.excompressum.CommonProxy;
import net.blay09.mods.excompressum.client.render.HammeringParticle;
import net.blay09.mods.excompressum.client.render.SievingParticle;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Set;

public class ClientProxy extends CommonProxy {

    private final Set<GameProfile> skinRequested = Sets.newHashSet();

    @Override
    public void preloadSkin(GameProfile customSkin) {
        if (!skinRequested.contains(customSkin)) {
            Minecraft.getInstance().getSkinManager().loadProfileTextures(customSkin, (typeIn, location, profileTexture) -> {
            }, true);
            skinRequested.add(customSkin);
        }
    }

    @Override
    public void spawnCrushParticles(World world, BlockPos pos, BlockState particleState) {
        if (!ExCompressumConfig.CLIENT.disableParticles.get()) {
            for (int i = 0; i < 10; i++) {
                Minecraft.getInstance().particles.addEffect(new HammeringParticle((ClientWorld) world, pos, pos.getX() + 0.7f, pos.getY() + 0.3f, pos.getZ() + 0.5f, (-world.rand.nextDouble() + 0.2f) / 9, 0.2f, (world.rand.nextDouble() - 0.5) / 9, particleState));
            }
        }
    }

    @Override
    public void spawnAutoSieveParticles(World world, BlockPos pos, BlockState emitterState, BlockState particleState, int particleCount) {
        float offsetX = 0;
        float offsetZ = 0;
        if (emitterState.hasProperty(HorizontalBlock.HORIZONTAL_FACING)) {
            switch (emitterState.get(HorizontalBlock.HORIZONTAL_FACING)) {
                case WEST:
                    offsetZ -= 0.125f;
                    break;
                case EAST:
                    offsetZ += 0.125f;
                    break;
                case NORTH:
                    offsetX += 0.125f;
                    break;
                case SOUTH:
                    offsetX -= 0.125f;
                    break;
                default:
                    break;
            }
        }

        spawnSieveParticles(world, pos, particleState, particleCount, new Vector3f(offsetX, 0.2f, offsetZ), 0.5f);
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnHeavySieveParticles(World world, BlockPos pos, BlockState particleState, int particleCount) {
        spawnSieveParticles(world, pos, particleState, particleCount, new Vector3f(0f, 0.4f, 0f), 1f);
    }

    private void spawnSieveParticles(World world, BlockPos pos, BlockState particleState, int particleCount, Vector3f particleOffset, float scale) {
        // Do not render sieve particles if particles are disabled in the config.
        if (ExCompressumConfig.CLIENT.disableParticles.get()) {
            return;
        }

        // Do not render sieve particles if minimal particles are configured.
        ParticleStatus particleStatus = Minecraft.getInstance().gameSettings.particles;
        if (particleStatus == ParticleStatus.MINIMAL) {
            return;
        }

        // Lower the amount of particles and skip some of them if decreased particles are configured.
        int actualParticleCount = particleCount;
        if (particleStatus == ParticleStatus.DECREASED) {
            float half = actualParticleCount / 2f;
            if (half < 1f && Math.random() <= 0.5f) {
                return;
            }

            actualParticleCount = (int) Math.ceil(half);
        }

        for (int i = 0; i < actualParticleCount; i++) {
            double spread = 0.8 * scale;
            double min = 0.4 * scale;
            double particleX = particleOffset.getX() + world.rand.nextFloat() * spread - min;
            double particleY = particleOffset.getY();
            double particleZ = particleOffset.getZ() + world.rand.nextFloat() * spread - min;
            Minecraft.getInstance().particles.addEffect(new SievingParticle((ClientWorld) world, pos, particleX, particleY, particleZ, scale, particleState));
        }
    }
}
