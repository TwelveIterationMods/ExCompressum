package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Collection;

// TODO needs particles
public class TileHeavySieve extends TileEntity implements ITickable {

    private static final int MAX_CLICKS_PER_SECOND = 6;
    private static final float PROCESSING_INTERVAL = 0.075f;
    private static final int UPDATE_INTERVAL = 20;

    private ItemStack meshStack;
    private ItemStack currentStack;

    private float progress;
    private int clicksSinceSync;

    private boolean isDirty;
    private int ticksSinceSync;

    public boolean addSiftable(EntityPlayer player, ItemStack itemStack) {
        if(currentStack != null || meshStack == null || !HeavySieveRegistry.isSiftable(itemStack)) {
            return false;
        }
        currentStack = player.capabilities.isCreativeMode ? ItemHandlerHelper.copyStackWithSize(itemStack, 1) : itemStack.splitStack(1);
        progress = 0f;
        VanillaPacketHandler.sendTileEntityUpdate(this);
        return true;
    }

    @Override
    public void update() {
        ticksSinceSync++;
        if (ticksSinceSync >= UPDATE_INTERVAL) {
            ticksSinceSync = 0;
            clicksSinceSync = 0;

            if (isDirty) {
                VanillaPacketHandler.sendTileEntityUpdate(this);
                isDirty = false;
            }
        }
    }

    public void processContents(EntityPlayer player) {
        if(currentStack != null && meshStack != null) {
            if (player.capabilities.isCreativeMode) {
                progress = 1f;
            } else {
                clicksSinceSync++;
                if (clicksSinceSync <= MAX_CLICKS_PER_SECOND) {
                    progress = Math.min(1f, progress + PROCESSING_INTERVAL);
                }
            }
            if (progress >= 1f) {
                if (!worldObj.isRemote) {
                    SieveMeshRegistryEntry sieveMesh = getSieveMesh();
                    if(sieveMesh != null) {
                        Collection<ItemStack> rewards = HeavySieveRegistry.rollSieveRewards(currentStack, sieveMesh, 0f, worldObj.rand);
                        for (ItemStack itemStack : rewards) {
                            worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, itemStack));
                        }
                    } else {
                        worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, currentStack));
                    }
                    currentStack = null;
                    if(ExRegistro.doMeshesHaveDurability() && sieveMesh != null) {
                        if(!sieveMesh.isHeavy()) {
                            // TODO this should probably play a broken sound and show particles
                            meshStack = null;
                        } else {
                            meshStack.damageItem(1, player);
                            if (meshStack.stackSize == 0) {
                                // TODO this should probably play a broken sound and show particles
                                meshStack = null;
                            }
                        }
                    }
                    progress = 0f;
                    VanillaPacketHandler.sendTileEntityUpdate(this);
                }
            }
            isDirty = true;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        currentStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Content"));
        meshStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Mesh"));
        progress = tagCompound.getFloat("Progress");
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

    @Nullable
    public SieveMeshRegistryEntry getSieveMesh() {
        if(meshStack != null) {
            return SieveMeshRegistry.getEntry(meshStack);
        }
        return null;
    }

    public void setMeshStack(ItemStack meshStack) {
        this.meshStack = meshStack;
        isDirty = true;
    }
}
