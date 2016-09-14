package net.blay09.mods.excompressum.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityBase extends TileEntity {
	public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
		return worldObj.getTileEntity(pos) == this && entityPlayer.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
	}

	@Override
	public final void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		readFromNBTSynced(tagCompound, false);
	}

	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		writeToNBTSynced(tagCompound, false);
		return tagCompound;
	}

	@Override
	public final NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public final void handleUpdateTag(NBTTagCompound tag) {
		readFromNBT(tag);
	}

	@Override
	public final SPacketUpdateTileEntity getUpdatePacket() {
		if(!hasUpdatePacket()) {
			return null;
		}
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBTSynced(tagCompound, true);
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tagCompound);
	}

	@Override
	public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBTSynced(pkt.getNbtCompound(), true);
	}

	protected abstract void writeToNBTSynced(NBTTagCompound tagCompound, boolean isSync);
	protected abstract void readFromNBTSynced(NBTTagCompound tagCompound, boolean isSync);
	protected abstract boolean hasUpdatePacket();
}
