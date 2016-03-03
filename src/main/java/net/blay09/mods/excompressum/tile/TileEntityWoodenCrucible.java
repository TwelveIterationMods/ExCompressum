package net.blay09.mods.excompressum.tile;

import exnihilo.blocks.tileentities.TileEntityCrucible;
import exnihilo.registries.BarrelRecipeRegistry;
import exnihilo.utils.ItemInfo;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.WoodenCrucibleRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityWoodenCrucible extends TileEntity implements IFluidHandler, ISidedInventory {

    public static final int MAX_FLUID = 1000;
    private static final int UPDATE_INTERVAL = 10;

    private TileEntityCrucible.CrucibleMode mode = TileEntityCrucible.CrucibleMode.EMPTY;
    private FluidStack fluidStack = new FluidStack(FluidRegistry.WATER, 0);
    private int ticksSinceUpdate;
    private boolean isDirty;
    private WoodenCrucibleRegistry.WoodenMeltable content;
    private int solidVolume;

    public boolean addItem(ItemStack itemStack) {
        if (ExCompressum.allowCrucibleBarrelRecipes && fluidStack.amount > 1000) {
            ItemInfo itemInfo = BarrelRecipeRegistry.getOutput(fluidStack, itemStack);
            if (itemInfo != null) {
                fluidStack.amount -= 1000;
                EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1, zCoord + 0.5, itemInfo.getStack());
                entityItem.motionY = 0.2;
                worldObj.spawnEntityInWorld(entityItem);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return true;
            }
        }
        WoodenCrucibleRegistry.WoodenMeltable meltable = WoodenCrucibleRegistry.getMeltable(itemStack);
        if (meltable != null && getCapacityLeft() >= meltable.fluidStack.amount && (mode == TileEntityCrucible.CrucibleMode.EMPTY || meltable.fluidStack.isFluidEqual(fluidStack))) {
            content = meltable;
            solidVolume += meltable.fluidStack.amount;
            mode = TileEntityCrucible.CrucibleMode.USED;
            if (!fluidStack.isFluidEqual(meltable.fluidStack)) {
                fluidStack = new FluidStack(meltable.fluidStack, 0);
            }
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            return true;
        }
        return false;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            int speed = ExCompressum.woodenCrucibleSpeed;
            if (solidVolume > 0) {
                fluidStack.amount += Math.min(speed, solidVolume);
                solidVolume = Math.max(0, solidVolume - speed);
                isDirty = true;
            } else if (solidVolume == 0 && fluidStack.amount == 0 && mode != TileEntityCrucible.CrucibleMode.EMPTY) {
                mode = TileEntityCrucible.CrucibleMode.EMPTY;
                isDirty = true;
            }

            ticksSinceUpdate++;
            if (ticksSinceUpdate >= UPDATE_INTERVAL) {
                ticksSinceUpdate = 0;
                if (isDirty) {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    isDirty = false;
                }
            }
        }
    }

    private float getCapacityLeft() {
        return MAX_FLUID - (solidVolume - fluidStack.amount);
    }

    public WoodenCrucibleRegistry.WoodenMeltable getContent() {
        return content;
    }

    public boolean hasSolids() {
        return solidVolume > 0;
    }

    public FluidStack getFluidStack() {
        return fluidStack;
    }

    public int getFluidVolume() {
        return fluidStack.amount;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack fillStack, boolean doFill) {
        int capacityLeft = (int) getCapacityLeft();
        if (!doFill) {
            if (mode == TileEntityCrucible.CrucibleMode.EMPTY) {
                return fillStack.amount;
            } else {
                return fluidStack.isFluidEqual(fillStack) ? Math.min(capacityLeft, fillStack.amount) : 0;
            }
        }
        int addedFluid = Math.min(capacityLeft, fillStack.amount);
        if (mode == TileEntityCrucible.CrucibleMode.EMPTY) {
            fluidStack = new FluidStack(fillStack, addedFluid);
        } else if (fluidStack.isFluidEqual(fillStack)) {
            fluidStack.amount += addedFluid;
        } else {
            return 0;
        }
        mode = TileEntityCrucible.CrucibleMode.USED;
        isDirty = true;
        return addedFluid;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack drainStack, boolean doDrain) {
        if (mode != TileEntityCrucible.CrucibleMode.USED || !fluidStack.isFluidEqual(drainStack)) {
            return null;
        }
        FluidStack result = new FluidStack(drainStack, Math.min(fluidStack.amount, drainStack.amount));
        if (doDrain) {
            fluidStack.amount -= result.amount;
            isDirty = true;
        }
        return result;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (mode != TileEntityCrucible.CrucibleMode.USED) {
            return null;
        }
        FluidStack result = new FluidStack(fluidStack, Math.min(fluidStack.amount, maxDrain));
        if (doDrain) {
            fluidStack.amount -= result.amount;
            isDirty = true;
        }
        return result;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{new FluidTankInfo(fluidStack, MAX_FLUID)};
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side == ForgeDirection.UP.ordinal()) {
            return new int[]{0};
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        if (side == 1 && slot == 1) {
            WoodenCrucibleRegistry.WoodenMeltable meltable = WoodenCrucibleRegistry.getMeltable(itemStack);
            if (meltable != null && getCapacityLeft() >= meltable.fluidStack.amount && (mode == TileEntityCrucible.CrucibleMode.EMPTY || meltable.fluidStack.isFluidEqual(fluidStack))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        addItem(itemStack);
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return false;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return WoodenCrucibleRegistry.isRegistered(itemStack);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        mode = tagCompound.getInteger("Mode") == 1 ? TileEntityCrucible.CrucibleMode.USED : TileEntityCrucible.CrucibleMode.EMPTY;
        solidVolume = tagCompound.getInteger("solidVolume");
        if (tagCompound.hasKey("Content")) {
            content = WoodenCrucibleRegistry.getMeltable(ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Content")));
        }
        fluidStack = FluidStack.loadFluidStackFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("Mode", mode.value);
        if (content != null) {
            tagCompound.setTag("Content", content.itemStack.writeToNBT(new NBTTagCompound()));
        }
        tagCompound.setInteger("solidVolume", solidVolume);
        fluidStack.writeToNBT(tagCompound);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, blockMetadata, tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
    }

    public int getSolidVolume() {
        return solidVolume;
    }
}
