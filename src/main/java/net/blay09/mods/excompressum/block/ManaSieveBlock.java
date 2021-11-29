package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.botania.BotaniaCompat;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntityBase;
import net.blay09.mods.excompressum.tile.ManaSieveTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.particles.IParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ManaSieveBlock extends AutoSieveBaseBlock {

    public static final String name = "mana_sieve";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ManaSieveBlock() {
        super(Properties.create(Material.IRON)
                .notSolid()
                .setAllowsSpawn((a, b, c, d) -> false)
                .setOpaque((a, b, c) -> false)
                .setSuffocates((a, b, c) -> false)
                .setBlocksVision((a, b, c) -> false));
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return BotaniaCompat.createManaSieveTileEntity();
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.animateTick(stateIn, worldIn, pos, rand);

        if (!stateIn.get(UGLY)) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof AutoSieveTileEntityBase && ((AutoSieveTileEntityBase) tileEntity).shouldAnimate()) {
                float posX = pos.getX() + 0.4f + rand.nextFloat() * 0.2f;
                float posY = pos.getY() + 0.35f + rand.nextFloat() * 0.25f;
                float posZ = pos.getZ() + 0.4f + rand.nextFloat() * 0.2f;
                float speed = 0.01f;
                Direction facing = stateIn.get(FACING).rotateY();
                float motionX = rand.nextFloat() * speed * facing.getXOffset();
                float motionY = (1f - rand.nextFloat() * 1.5f) * speed;
                float motionZ = rand.nextFloat() * speed * facing.getZOffset();

                IParticleData particle = BotaniaCompat.getManaParticle();
                worldIn.addOptionalParticle(particle, posX, posY, posZ, motionX, motionY, motionZ);
            }
        }
    }
}
