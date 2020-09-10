package net.blay09.mods.excompressum.client;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import net.blay09.mods.excompressum.CommonProxy;
import net.blay09.mods.excompressum.client.render.HammeringParticle;
import net.blay09.mods.excompressum.client.render.SievingParticle;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
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
        if (!ModConfig.client.disableParticles) {
            for (int i = 0; i < 10; i++) {
                Minecraft.getInstance().particles.addEffect(new HammeringParticle((ClientWorld) world, pos, pos.getX() + 0.7f, pos.getY() + 0.3f, pos.getZ() + 0.5f, (-world.rand.nextDouble() + 0.2f) / 9, 0.2f, (world.rand.nextDouble() - 0.5) / 9, particleState));
            }
        }
    }

    @Override
    public void spawnSieveParticles(World world, BlockPos pos, BlockState particleState, int particleCount) {
        // Do not render sieve particles if particles are disabled in the config.
        if (ModConfig.client.disableParticles) {
            return;
        }

        // Do not render sieve particles if minimal particles are configured.
        ParticleStatus particleStatus = Minecraft.getInstance().gameSettings.particles;
        if (particleStatus == ParticleStatus.MINIMAL) {
            return;
        }

        // Lower the amount of particles and skip some of them if decreated particles are configured.
        int actualParticleCount = particleCount;
        if (particleStatus == ParticleStatus.DECREASED) {
            float half = actualParticleCount / 2f;
            if (half < 1f && Math.random() <= 0.5f) {
                return;
            }

            actualParticleCount = (int) Math.ceil(half);
        }

        for (int i = 0; i < actualParticleCount; i++) {
            double particleX = 0.5 + world.rand.nextFloat() * 0.4 - 0.2;
            double particleZ = 0.5 + world.rand.nextFloat() * 0.4 - 0.2;
            if (particleState.hasProperty(HorizontalBlock.HORIZONTAL_FACING)) {
                switch (particleState.get(HorizontalBlock.HORIZONTAL_FACING)) {
                    case WEST:
                        particleZ -= 0.125f;
                        break;
                    case EAST:
                        particleZ += 0.125f;
                        break;
                    case NORTH:
                        particleX += 0.125f;
                        break;
                    case SOUTH:
                        particleX -= 0.125f;
                        break;
                    default:
                        break;
                }
            }
            Minecraft.getInstance().particles.addEffect(new SievingParticle((ClientWorld) world, pos, particleX, 0.2, particleZ, 0.5f, particleState));
        }
    }


    @OnlyIn(Dist.CLIENT)
    public void spawnHeavySieveParticles() {
        BlockState state = StupidUtils.getStateFromItemStack(currentStack);
        if (state != null) {
            for (int i = 0; i < actualParticleCount; i++) {
                Minecraft.getInstance().effectRenderer.addEffect(new SievingParticle(world, pos, 0.5 + world.rand.nextFloat() * 0.8 - 0.4, 0.4, 0.5 + world.rand.nextFloat() * 0.8 - 0.4, 1f, state));
            }
        }
    }
}
