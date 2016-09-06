package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.crucible.WoodenCrucibleRegistry;
import net.blay09.mods.excompressum.registry.crucible.WoodenMeltable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class TileWoodenCrucible extends TileEntity implements ITickable {

    public static final int MAX_FLUID = Fluid.BUCKET_VOLUME;
    private static final int UPDATE_INTERVAL = 10;

    private IItemHandler itemHandler = new IItemHandler() {
        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        @Nullable
        public ItemStack getStackInSlot(int slot) {
            return null;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            ItemStack tryStack = stack.copy();
            if(addItem(tryStack)) {
                return tryStack;
            }
            return stack;
        }

        @Override
        @Nullable
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return null;
        }
    };
    private FluidTank fluidTank = new FluidTank(MAX_FLUID) {
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return fluid.getFluid().getTemperature() > 500;
        }
    };

    private int ticksSinceUpdate;
    private boolean isDirty;
    private WoodenMeltable currentMeltable;
    private float solidVolume;
    private float fluidProgress;
    private FluidStack fillingFluid;

    public boolean addItem(ItemStack itemStack) {
        // Make clay if possible
        if (ExCompressumConfig.woodenCrucibleMakesClay && fluidTank.getFluidAmount() >= Fluid.BUCKET_VOLUME) {
            if(ExRegistro.isNihiloBlock(itemStack, ExNihiloProvider.NihiloBlocks.DUST)) {
                if(!worldObj.isRemote) {
                    FluidStack drained = fluidTank.drain(Fluid.BUCKET_VOLUME, true);
                    if(drained == null || drained.amount < Fluid.BUCKET_VOLUME) {
                        return false;
                    }
                    EntityItem entityItem = new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, new ItemStack(Blocks.CLAY));
                    entityItem.motionY = 0.2;
                    worldObj.spawnEntityInWorld(entityItem);
                    VanillaPacketHandler.sendTileEntityUpdate(this);
                }
                return true;
            }
        }

        // Add meltable to the crucible
        WoodenMeltable meltable = WoodenCrucibleRegistry.getMeltable(itemStack);
        float capacityLeft = getCapacityLeft();
        Fluid lockFluid = getFluid();
        if (meltable != null && capacityLeft > meltable.fluidStack.amount && (lockFluid == null || meltable.fluidStack.getFluid() == lockFluid)) {
            currentMeltable = meltable;
            if(lockFluid == null) {
                fillingFluid = new FluidStack(meltable.fluidStack.getFluid(), 0);
                fluidTank.setFluid(fillingFluid.copy());
            }
            solidVolume = solidVolume + meltable.fluidStack.amount;
            VanillaPacketHandler.sendTileEntityUpdate(this);
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            Fluid lockFluid = getFluid();
            float rainFall = worldObj.getBiome(pos).getRainfall();
            if ((lockFluid == null || lockFluid == FluidRegistry.WATER) && worldObj.isRaining() && pos.getY() >= worldObj.getTopSolidOrLiquidBlock(pos).getY() - 1 && rainFall > 0f && ExCompressumConfig.woodenCrucibleFillFromRain) {
                if(lockFluid == null) {
                    fillingFluid = new FluidStack(FluidRegistry.WATER, 0);
                    fluidTank.setFluid(fillingFluid.copy());
                }
                fluidProgress += rainFall;
                isDirty = true;
            }

            float speed = ExCompressumConfig.woodenCrucibleSpeed;
            if (solidVolume > 0 && fluidProgress < MAX_FLUID) {
                fluidProgress += Math.min(speed, solidVolume);
                solidVolume = Math.max(0, solidVolume - speed);
                isDirty = true;
            } else if (fluidProgress == 0 && lockFluid != null) {
                isDirty = true;
            }

            if(fluidProgress >= 1f) {
                fillingFluid.amount = (int) fluidProgress;
                fluidProgress -= fluidTank.fill(fillingFluid, true);
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

    public float getFluidProgress() {
        return fluidProgress;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        solidVolume = tagCompound.getFloat("SolidVolume");
        fluidProgress = tagCompound.getFloat("FluidProgress");
        fluidTank.readFromNBT(tagCompound.getCompoundTag("FluidTank"));
        Fluid lockFluid = getFluid();
        if(lockFluid != null) {
            fillingFluid = new FluidStack(lockFluid, 0);
        }
        if (tagCompound.hasKey("Content")) {
            currentMeltable = WoodenCrucibleRegistry.getMeltable(ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Content")));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if (currentMeltable != null) {
            tagCompound.setTag("Content", currentMeltable.itemStack.writeToNBT(new NBTTagCompound()));
        }
        tagCompound.setFloat("SolidVolume", solidVolume);
        tagCompound.setFloat("FluidProgress", fluidProgress);
        tagCompound.setTag("FluidTank", fluidTank.writeToNBT(new NBTTagCompound()));
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
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    public float getSolidVolume() {
        return solidVolume;
    }

    @Nullable
    public Fluid getFluid() {
        return fluidTank.getFluid() != null ? fluidTank.getFluid().getFluid() : null; // yeah this is beautiful
    }

    public boolean hasFluids() {
        return fluidTank.getFluidAmount() > 0;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) fluidTank;
        }
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandler;
        }
        return super.getCapability(capability, facing);
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }
}
