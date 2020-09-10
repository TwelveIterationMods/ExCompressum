package net.blay09.mods.excompressum.tile;


import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class BaseTileEntity extends TileEntity {

    public BaseTileEntity(TileEntityType<?> type) {
        super(type);
    }

    public boolean isUsableByPlayer(PlayerEntity entityPlayer) {
        return world != null
                && world.getTileEntity(pos) == this
                && entityPlayer.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }

    @Override
    public void read(BlockState state, CompoundNBT tagCompound) {
        super.read(state, tagCompound);
        readFromNBTSynced(tagCompound, false);
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        writeToNBTSynced(tagCompound, false);
        return tagCompound;
    }

    @Override
    public final CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public final void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    @Override
    public final SUpdateTileEntityPacket getUpdatePacket() {
        if (!hasUpdatePacket()) {
            return null;
        }

        CompoundNBT tagCompound = new CompoundNBT();
        writeToNBTSynced(tagCompound, true);
        return new SUpdateTileEntityPacket(pos, 0, tagCompound);
    }

    @Override
    public final void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        readFromNBTSynced(pkt.getNbtCompound(), true);
    }

    protected abstract void writeToNBTSynced(CompoundNBT tagCompound, boolean isSync);

    protected abstract void readFromNBTSynced(CompoundNBT tagCompound, boolean isSync);

    protected abstract boolean hasUpdatePacket();
}
