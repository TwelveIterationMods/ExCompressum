package net.blay09.mods.excompressum.tile;


import net.minecraft.block.BlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseTileEntity extends TileEntity {
	public boolean isUseableByPlayer(PlayerEntity entityPlayer) {
		return world.getTileEntity(pos) == this && entityPlayer.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
	}

	@Override
	public void readFromNBT(CompoundNBT tagCompound) {
		super.readFromNBT(tagCompound);
		readFromNBTSynced(tagCompound, false);
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT tagCompound) {
		super.writeToNBT(tagCompound);
		writeToNBTSynced(tagCompound, false);
		return tagCompound;
	}

	@Override
	public final CompoundNBT getUpdateTag() {
		return writeToNBT(new CompoundNBT());
	}

	@Override
	public final void handleUpdateTag(CompoundNBT tag) {
		readFromNBT(tag);
	}

	@Override
	public final SUpdateTileEntityPacket getUpdatePacket() {
		if(!hasUpdatePacket()) {
			return null;
		}
		CompoundNBT tagCompound = new CompoundNBT();
		writeToNBTSynced(tagCompound, true);
		return new SUpdateTileEntityPacket(pos, getBlockMetadata(), tagCompound);
	}

	@Override
	public final void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		readFromNBTSynced(pkt.getNbtCompound(), true);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	protected abstract void writeToNBTSynced(CompoundNBT tagCompound, boolean isSync);
	protected abstract void readFromNBTSynced(CompoundNBT tagCompound, boolean isSync);
	protected abstract boolean hasUpdatePacket();
}
