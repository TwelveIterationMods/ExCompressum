package net.blay09.mods.excompressum.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exnihilo.blocks.tileentities.TileEntitySieve;
import exnihilo.particles.ParticleSieve;
import exnihilo.registries.helpers.SiftingResult;
import net.blay09.mods.excompressum.registry.HeavySieveRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import java.util.Collection;

public class TileEntityHeavySieve extends TileEntity {

    private static final float MIN_RENDER_CAPACITY = 0.70f;
    private static final float MAX_RENDER_CAPACITY = 0.9f;
    private static final float PROCESSING_INTERVAL = 0.075f;
    private static final int UPDATE_INTERVAL = 20;

    private Block content;
    private int contentMeta = 0;
    private TileEntitySieve.SieveMode mode = TileEntitySieve.SieveMode.EMPTY;
    private boolean isDirty;
    private boolean spawnParticles;
    private float volume;
    private int ticksSinceUpdate;
    private int clicksSinceUpdate;

    public TileEntitySieve.SieveMode getMode() {
        return mode;
    }

    public void addSievable(Block block, int metadata) {
        this.content = block;
        this.contentMeta = metadata;

        this.mode = TileEntitySieve.SieveMode.FILLED;

        volume = 1f;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote && spawnParticles) {
            spawnFX();
        }

        ticksSinceUpdate++;
        if (ticksSinceUpdate >= UPDATE_INTERVAL) {
            ticksSinceUpdate = 0;
            clicksSinceUpdate = 0;

            spawnParticles = false;

            if (isDirty) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                isDirty = false;
            }
        }
    }

    public void processContents(boolean creative) {
        if (creative) {
            volume = 0f;
        } else {
            clicksSinceUpdate++;
            if (clicksSinceUpdate <= 6) {
                volume -= PROCESSING_INTERVAL;
            }
        }
        if (volume <= 0) {
            mode = TileEntitySieve.SieveMode.EMPTY;
            if (!worldObj.isRemote) {
                Collection<SiftingResult> rewards = HeavySieveRegistry.getSiftingOutput(content, contentMeta);
                if (rewards != null) {
                    for (SiftingResult reward : rewards) {
                        if (worldObj.rand.nextInt(reward.rarity) == 0) {
                            EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, new ItemStack(reward.item, 1, reward.meta));
                            double motionScale = 0.05;
                            entityItem.motionX = worldObj.rand.nextGaussian() * motionScale;
                            entityItem.motionY = 0.2;
                            entityItem.motionZ = worldObj.rand.nextGaussian() * motionScale;
                            worldObj.spawnEntityInWorld(entityItem);
                        }
                    }
                }
            }
        } else {
            spawnParticles = true;
        }
        isDirty = true;
    }

    @SideOnly(Side.CLIENT)
    private void spawnFX() {
        if (content != null) {
            IIcon icon = content.getIcon(0, contentMeta);
            for (int i = 0; i < 4; i++) {
                ParticleSieve particle = new ParticleSieve(worldObj,
                        xCoord + 0.8 * worldObj.rand.nextFloat() + 0.15,
                        yCoord + 0.69,
                        zCoord + 0.8 * worldObj.rand.nextFloat() + 0.15,
                        0, 0, 0, icon);
                Minecraft.getMinecraft().effectRenderer.addEffect(particle);
            }
        }
    }

    public int getContentMeta() {
        return contentMeta;
    }

    public Block getContentBlock() {
        return content;
    }

    public float getAdjustedVolume() {
        float capacity = MAX_RENDER_CAPACITY - MIN_RENDER_CAPACITY;
        float adjusted = volume * capacity;
        adjusted += MIN_RENDER_CAPACITY;
        return adjusted;
    }
}
