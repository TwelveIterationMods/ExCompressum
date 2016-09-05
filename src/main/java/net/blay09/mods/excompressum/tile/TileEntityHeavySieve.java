package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.data.SiftingResult;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;

public class TileEntityHeavySieve extends TileEntity implements ITickable {

    private static final float MIN_RENDER_CAPACITY = 0.70f;
    private static final float MAX_RENDER_CAPACITY = 0.9f;
    private static final float PROCESSING_INTERVAL = 0.075f;
    private static final int UPDATE_INTERVAL = 20;

    private ItemStack content;
    private TileEntitySieve.SieveMode mode = TileEntitySieve.SieveMode.EMPTY;
    private boolean isDirty;
    private boolean spawnParticles;
    private float volume;
    private int ticksSinceUpdate;
    private int clicksSinceUpdate;

    public TileEntitySieve.SieveMode getMode() {
        return mode;
    }

    public void addSievable(ItemStack content) {
        this.content = content;
        mode = TileEntitySieve.SieveMode.FILLED;
        volume = 1f;
        VanillaPacketHandler.sendTileEntityUpdate(this);
    }

    @Override
    public void update() {
        if (worldObj.isRemote && spawnParticles) {
            spawnFX();
        }

        ticksSinceUpdate++;
        if (ticksSinceUpdate >= UPDATE_INTERVAL) {
            ticksSinceUpdate = 0;
            clicksSinceUpdate = 0;

            spawnParticles = false;

            if (isDirty) {
                VanillaPacketHandler.sendTileEntityUpdate(this);
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
                Collection<SiftingResult> rewards = HeavySieveRegistry.getSiftingOutput(content);
                for (SiftingResult reward : rewards) {
                    if (worldObj.rand.nextInt(reward.getRarity()) == 0) {
                        EntityItem entityItem = new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, new ItemStack(reward.getItem(), 1, reward.getMetadata()));
                        double motionScale = 0.05;
                        entityItem.motionX = worldObj.rand.nextGaussian() * motionScale;
                        entityItem.motionY = 0.2;
                        entityItem.motionZ = worldObj.rand.nextGaussian() * motionScale;
                        worldObj.spawnEntityInWorld(entityItem);
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
            IIcon icon = content.getIconIndex();
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

    public float getAdjustedVolume() {
        float capacity = MAX_RENDER_CAPACITY - MIN_RENDER_CAPACITY;
        float adjusted = volume * capacity;
        adjusted += MIN_RENDER_CAPACITY;
        return adjusted;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        mode = tagCompound.getInteger("Mode") == 1 ? TileEntitySieve.SieveMode.FILLED : TileEntitySieve.SieveMode.EMPTY;
        content = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Content"));
        volume = tagCompound.getFloat("Volume");
        spawnParticles = tagCompound.getBoolean("Particles");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("Mode", mode.value);
        if(content != null) {
            tagCompound.setTag("Content", content.writeToNBT(new NBTTagCompound()));
        }
        tagCompound.setFloat("Volume", volume);
        tagCompound.setBoolean("Particles", spawnParticles);
        return tagCompound;
    }

    // TODO fix te syncing

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    public ItemStack getContent() {
        return content;
    }

    public float getVolumeLeft() {
        return volume;
    }
}
