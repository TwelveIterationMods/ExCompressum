package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.blay09.mods.excompressum.compat.botania.BotaniaCompat;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.ManaSieveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ManaSieveBlock extends AutoSieveBaseBlock {

    public static final String name = "mana_sieve";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ManaSieveBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL)
                .noOcclusion()
                .isValidSpawn((a, b, c, d) -> false)
                .isRedstoneConductor((a, b, c) -> false)
                .isSuffocating((a, b, c) -> false)
                .isViewBlocking((a, b, c) -> false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ManaSieveBlockEntity(pos, state);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
        super.animateTick(state, level, pos, rand);

        if (!state.getValue(UGLY)) {
            if (level.getBlockEntity(pos) instanceof AbstractAutoSieveBlockEntity autoSieve && autoSieve.shouldAnimate()) {
                float posX = pos.getX() + 0.4f + rand.nextFloat() * 0.2f;
                float posY = pos.getY() + 0.35f + rand.nextFloat() * 0.25f;
                float posZ = pos.getZ() + 0.4f + rand.nextFloat() * 0.2f;
                float speed = 0.01f;
                Direction facing = state.getValue(FACING).getClockWise();
                float motionX = rand.nextFloat() * speed * facing.getStepX();
                float motionY = (1f - rand.nextFloat() * 1.5f) * speed;
                float motionZ = rand.nextFloat() * speed * facing.getStepZ();

                ParticleOptions particle = BotaniaCompat.getManaParticle();
                level.addParticle(particle, posX, posY, posZ, motionX, motionY, motionZ);
            }
        }
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return ModBlockEntities.manaSieve.get();
    }


}
