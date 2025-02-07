package net.blay09.mods.excompressum.client;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import net.blay09.mods.excompressum.CommonProxy;
import net.blay09.mods.excompressum.client.render.HammeringParticle;
import net.blay09.mods.excompressum.client.render.SievingParticle;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import java.util.Set;

public class ClientProxy extends CommonProxy {

    private final Set<GameProfile> skinRequested = Sets.newHashSet();

    @Override
    public void preloadSkin(GameProfile customSkin) {
        if (!skinRequested.contains(customSkin)) {
            Minecraft.getInstance().getSkinManager().registerSkins(customSkin, (typeIn, location, profileTexture) -> {
            }, true);
            skinRequested.add(customSkin);
        }
    }

    @Override
    public void spawnCrushParticles(Level level, BlockPos pos, BlockState particleState) {
        if (!ExCompressumConfig.getActive().client.disableParticles) {
            for (int i = 0; i < 10; i++) {
                Minecraft.getInstance().particleEngine.add(new HammeringParticle((ClientLevel) level, pos, pos.getX() + 0.7f, pos.getY() + 0.3f, pos.getZ() + 0.5f, (-level.random.nextDouble() + 0.2f) / 9, 0.2f, (level.random.nextDouble() - 0.5) / 9, particleState));
            }
        }
    }

    @Override
    public void spawnAutoSieveParticles(Level level, BlockPos pos, BlockState emitterState, BlockState particleState, int particleCount) {
        float offsetX = 0;
        float offsetZ = 0;
        if (emitterState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            switch (emitterState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                case WEST -> offsetZ -= 0.125f;
                case EAST -> offsetZ += 0.125f;
                case NORTH -> offsetX += 0.125f;
                case SOUTH -> offsetX -= 0.125f;
                default -> {
                }
            }
        }

        spawnSieveParticles(level, pos, particleState, particleCount, new Vec3(offsetX, 0.2f, offsetZ), 0.5f);
    }

    public void spawnHeavySieveParticles(Level level, BlockPos pos, BlockState particleState, int particleCount) {
        spawnSieveParticles(level, pos, particleState, particleCount, new Vec3(0f, 0.4f, 0f), 1f);
    }

    private void spawnSieveParticles(Level level, BlockPos pos, BlockState particleState, int particleCount, Vec3 particleOffset, float scale) {
        // Do not render sieve particles if particles are disabled in the config.
        if (ExCompressumConfig.getActive().client.disableParticles) {
            return;
        }

        // Do not render sieve particles if minimal particles are configured.
        ParticleStatus particleStatus = Minecraft.getInstance().options.particles().get();
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
            float particleScale = 0.25f * scale;
            double particleX = 0.5f + particleOffset.x() + level.random.nextFloat() * spread - min;
            double particleY = particleOffset.y();
            double particleZ = 0.5f + particleOffset.z() + level.random.nextFloat() * spread - min;
            Minecraft.getInstance().particleEngine.add(new SievingParticle((ClientLevel) level, pos, particleX, particleY, particleZ, particleScale, particleState));
        }
    }

    /**
     * @deprecated Can't remove yet because we need it in ChickenStickItem isCorrectToolForDrops/getDestroySpeed where we don't have level access. maybe we can use ExRegistries instead
     */
    @Override
    @Deprecated
    public RecipeManager getRecipeManager(@Nullable Level level) {
        if (level == null) {
            level = Minecraft.getInstance().level;
        }

        if (level == null) {
            return new RecipeManager();
        }

        return level.getRecipeManager();
    }
}
