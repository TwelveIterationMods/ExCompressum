package net.blay09.mods.excompressum.tile;

import exnihilo.registries.BarrelRecipeRegistry;
import exnihilo.utils.ItemInfo;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.WoodenCrucibleRegistry;
import net.blay09.mods.excompressum.registry.data.WoodenMeltable;
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

    private Fluid fluid;
    private int ticksSinceUpdate;
    private boolean isDirty;
    private WoodenMeltable currentMeltable;
    private float solidVolume;
    private float fluidVolume;

    public boolean addItem(ItemStack itemStack) {
        if (ExCompressum.woodenCrucibleBarrelRecipes && fluidVolume >= 1000) {
            ItemInfo itemInfo = BarrelRecipeRegistry.getOutput(new FluidStack(fluid, (int) fluidVolume), itemStack);
            if (itemInfo != null) {
                if(!worldObj.isRemote) {
                    fluidVolume -= 1000;
                    EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1, zCoord + 0.5, itemInfo.getStack());
                    entityItem.motionY = 0.2;
                    worldObj.spawnEntityInWorld(entityItem);
                    VanillaPacketHandler.sendTileEntityUpdate(this);
                }
                return true;
            }
        }
        WoodenMeltable meltable = WoodenCrucibleRegistry.getMeltable(itemStack);
        float capacityLeft = getCapacityLeft();
        if (meltable != null && capacityLeft > meltable.fluidStack.amount && (fluid == null || meltable.fluidStack.getFluid() == fluid)) {
            currentMeltable = meltable;
            fluid = meltable.fluidStack.getFluid();
            solidVolume = solidVolume + meltable.fluidStack.amount;
            VanillaPacketHandler.sendTileEntityUpdate(this);
            return true;
        }
        return false;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            float rainFall = worldObj.getBiomeGenForCoords(xCoord, zCoord).rainfall;
            if ((fluidVolume == 0 || fluid == FluidRegistry.WATER) && worldObj.isRaining() && yCoord >= worldObj.getTopSolidOrLiquidBlock(xCoord, zCoord) - 1 && rainFall > 0f && ExCompressum.woodenCrucibleFillFromRain) {
                fluidVolume = Math.min(MAX_FLUID, fluidVolume + rainFall);
                isDirty = true;
            }

            float speed = ExCompressum.woodenCrucibleSpeed;
            if (solidVolume > 0 && fluidVolume < MAX_FLUID) {
                fluidVolume = Math.min(MAX_FLUID, fluidVolume + Math.min(speed, solidVolume));
                solidVolume = Math.max(0, solidVolume - speed);
                isDirty = true;
            } else if (fluidVolume == 0 && fluid != null) {
                isDirty = true;
            }

            ticksSinceUpdate++;
            if (ticksSinceUpdate >= UPDATE_INTERVAL) {
                ticksSinceUpdate = 0;
                if (isDirty) {
                    VanillaPacketHandler.sendTileEntityUpdate(this);
                    isDirty = false;
                }
            }
        }
    }

    private float getCapacityLeft() {
        return MAX_FLUID - solidVolume;
    }

    public WoodenMeltable getCurrentMeltable() {
        return currentMeltable;
    }

    public boolean hasSolids() {
        return solidVolume > 0;
    }

    public float getFluidVolume() {
        return fluidVolume;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack fillStack, boolean doFill) {
        if(fillStack.getFluid().getTemperature() > 500) {
            return 0;
        }
        int capacityLeft = (int) (getCapacityLeft() - fluidVolume);
        if (!doFill) {
            if (fluid == null) {
                return fillStack.amount;
            } else {
                return fluid == fillStack.getFluid() ? Math.min(capacityLeft, fillStack.amount) : 0;
            }
        }
        int addedFluid = Math.min(capacityLeft, fillStack.amount);
        if (fluid == null || fluid == fillStack.getFluid()) {
            fluid = fillStack.getFluid();
            fluidVolume += addedFluid;
        } else {
            return 0;
        }
        isDirty = true;
        return addedFluid;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack drainStack, boolean doDrain) {
        if (fluid == null || fluid != drainStack.getFluid()) {
            return null;
        }
        FluidStack result = new FluidStack(drainStack, Math.min((int) fluidVolume, drainStack.amount));
        if (doDrain) {
            fluidVolume -= result.amount;
            isDirty = true;
        }
        return result;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (fluid == null) {
            return null;
        }
        FluidStack result = new FluidStack(fluid, Math.min((int) fluidVolume, maxDrain));
        if (doDrain) {
            fluidVolume -= result.amount;
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
        return new FluidTankInfo[]{new FluidTankInfo(new FluidStack(fluid != null ? fluid : FluidRegistry.WATER, (int) fluidVolume), MAX_FLUID)};
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
            WoodenMeltable meltable = WoodenCrucibleRegistry.getMeltable(itemStack);
            if (meltable != null && getCapacityLeft() >= meltable.fluidStack.amount && (fluid == null || meltable.fluidStack.getFluid() == fluid)) {
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
        solidVolume = tagCompound.getFloat("SolidVolume");
        fluid = FluidRegistry.getFluid(tagCompound.getString("FluidName"));
        fluidVolume = tagCompound.getFloat("FluidVolume");
        if (tagCompound.hasKey("Content")) {
            currentMeltable = WoodenCrucibleRegistry.getMeltable(ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Content")));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if (currentMeltable != null) {
            tagCompound.setTag("Content", currentMeltable.itemStack.writeToNBT(new NBTTagCompound()));
        }
        tagCompound.setFloat("SolidVolume", solidVolume);
        if (fluid != null) {
            tagCompound.setString("FluidName", fluid.getName());
        }
        tagCompound.setFloat("FluidVolume", fluidVolume);
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

    public float getSolidVolume() {
        return solidVolume;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public boolean hasFluids() {
        return fluid != null && fluidVolume > 0;
    }
}
