package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collection;

// TODO infinite sifting!
// TODO Waila durability is wrong!
// TODO use up mesh durability!
public class TileHeavySieve extends TileEntity implements ITickable {

    private static final float PROCESSING_INTERVAL = 0.075f;
    private static final int UPDATE_INTERVAL = 20;

    private ItemStack meshStack;
    private ItemStack currentStack;
    private boolean isDirty;
    private boolean spawnParticles;
    private float progress;
    private int ticksSinceUpdate;
    private int clicksSinceUpdate;

    public void addSiftable(ItemStack itemStack) {
        currentStack = itemStack;
        progress = 0f;
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
        if(currentStack != null) {
            if (creative) {
                progress = 1f;
            } else {
                clicksSinceUpdate++;
                if (clicksSinceUpdate <= 6) {
                    progress += PROCESSING_INTERVAL;
                }
            }
            if (progress >= 1f) {
                if (!worldObj.isRemote) {
                    Collection<ItemStack> rewards = HeavySieveRegistry.rollSieveRewards(currentStack, 0f, worldObj.rand);
                    for (ItemStack itemStack : rewards) {
                        worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, itemStack));
                    }
                }
            } else {
                spawnParticles = true;
            }
            isDirty = true;
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnFX() {
        if (currentStack != null) {
            for (int i = 0; i < 4; i++) {
                // TODO fix the particlebobs
                /*ParticleSieve particle = new ParticleSieve(worldObj,
                        xCoord + 0.8 * worldObj.rand.nextFloat() + 0.15,
                        yCoord + 0.69,
                        zCoord + 0.8 * worldObj.rand.nextFloat() + 0.15,
                        0, 0, 0, icon);
                Minecraft.getMinecraft().effectRenderer.addEffect(particle);*/
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        currentStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Content"));
        meshStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Mesh"));
        progress = tagCompound.getFloat("Progress");
        spawnParticles = tagCompound.getBoolean("Particles");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if(currentStack != null) {
            tagCompound.setTag("Content", currentStack.writeToNBT(new NBTTagCompound()));
        }
        if(meshStack != null) {
            tagCompound.setTag("Mesh", meshStack.writeToNBT(new NBTTagCompound()));
        }
        tagCompound.setFloat("Progress", progress);
        tagCompound.setBoolean("Particles", spawnParticles);
        return tagCompound;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    @Nullable
    public ItemStack getCurrentStack() {
        return currentStack;
    }

    @Nullable
    public ItemStack getMeshStack() {
        return meshStack;
    }

    public float getProgress() {
        return progress;
    }

    public void setMeshStack(ItemStack meshStack) {
        this.meshStack = meshStack;
        isDirty = true;
    }
}
