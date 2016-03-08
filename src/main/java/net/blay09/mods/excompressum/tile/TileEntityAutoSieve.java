package net.blay09.mods.excompressum.tile;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exnihilo.particles.ParticleSieve;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.SiftingResult;
import exnihilo.utils.ItemInfo;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collection;

@Optional.Interface(modid = "CoFHCore", iface = "cofh.api.energy.IEnergyHandler", striprefs = true)
public class TileEntityAutoSieve extends TileEntity implements ISidedInventory, IEnergyHandler {

    private static final int UPDATE_INTERVAL = 20;

    private final EnergyStorage storage = new EnergyStorage(64000);
    private ItemStack[] inventory = new ItemStack[getSizeInventory()];
    private ItemStack currentStack;

    private GameProfile customSkin;
    private boolean spawnParticles;
    private int ticksSinceUpdate;
    private boolean isDirty;
    private float progress;

    private float speedBoost = 1f;
    private int speedBoostTicks;

    @Override
    public void updateEntity() {
        if (worldObj.isRemote && spawnParticles) {
            spawnFX();
        }

        if (speedBoostTicks > 0) {
            speedBoostTicks--;
            if (speedBoostTicks <= 0) {
                speedBoost = 1f;
            }
        }

        ticksSinceUpdate++;
        if (ticksSinceUpdate > UPDATE_INTERVAL) {
            spawnParticles = false;
            if (isDirty) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                isDirty = false;
            }
            ticksSinceUpdate = 0;
        }
        int effectiveEnergy = getEffectiveEnergy();
        if (storage.getEnergyStored() >= effectiveEnergy) {
            if (currentStack == null) {
                if (inventory[0] != null && isRegistered(inventory[0])) {
                    boolean foundSpace = false;
                    for (int i = 1; i < inventory.length; i++) {
                        if (inventory[i] == null) {
                            foundSpace = true;
                        }
                    }
                    if (!foundSpace) {
                        return;
                    }
                    currentStack = inventory[0].splitStack(1);
                    if (inventory[0].stackSize == 0) {
                        inventory[0] = null;
                    }
                    storage.extractEnergy(effectiveEnergy, false);
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    progress = 0f;
                }
            } else {
                storage.extractEnergy(effectiveEnergy, false);
                progress += getEffectiveSpeed();
                isDirty = true;
                if (progress >= 1) {
                    if (!worldObj.isRemote) {
                        Collection<SiftingResult> rewards = getSiftingOutput(currentStack);
                        if (rewards != null && !rewards.isEmpty()) {
                            for (SiftingResult reward : rewards) {
                                if (worldObj.rand.nextInt((int) Math.max(1f, reward.rarity / getEffectiveLuck())) == 0) {
                                    ItemStack rewardStack = new ItemStack(reward.item, 1, reward.meta);
                                    if (!addItemToOutput(rewardStack)) {
                                        EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, rewardStack);
                                        double motion = 0.05;
                                        entityItem.motionX = worldObj.rand.nextGaussian() * motion;
                                        entityItem.motionY = 0.2;
                                        entityItem.motionZ = worldObj.rand.nextGaussian() * motion;
                                        worldObj.spawnEntityInWorld(entityItem);
                                    }
                                }
                            }
                            degradeBook();
                        }
                    }
                    progress = 0f;
                    currentStack = null;
                } else {
                    spawnParticles = true;
                }
            }
        }
    }

    private boolean addItemToOutput(ItemStack itemStack) {
        int firstEmptySlot = -1;
        for (int i = 1; i < getSizeInventory(); i++) {
            if (inventory[i] == null) {
                if (firstEmptySlot == -1) {
                    firstEmptySlot = i;
                }
            } else {
                if (inventory[i].stackSize + itemStack.stackSize < inventory[i].getMaxStackSize() && inventory[i].isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(inventory[i], itemStack)) {
                    inventory[i].stackSize += itemStack.stackSize;
                    return true;
                }
            }
        }
        if (firstEmptySlot != -1) {
            inventory[firstEmptySlot] = itemStack;
            return true;
        }
        return false;
    }

    public void degradeBook() {
        if (inventory[21] != null && worldObj.rand.nextFloat() <= ExCompressum.autoSieveBookDecay) {
            NBTTagList tagList = inventory[21].getEnchantmentTagList();
            if (tagList != null) {
                for (int i = 0; i < tagList.tagCount(); ++i) {
                    short id = tagList.getCompoundTagAt(i).getShort("id");
                    if(id != Enchantment.fortune.effectId && id != Enchantment.efficiency.effectId) {
                        continue;
                    }
                    int level = tagList.getCompoundTagAt(i).getShort("lvl") - 1;
                    if(level <= 0) {
                        tagList.removeTag(i);
                    } else {
                        tagList.getCompoundTagAt(i).setShort("lvl", (short) level);
                    }
                    break;
                }
                if(tagList.tagCount() == 0) {
                    inventory[21] = new ItemStack(Items.book);
                }
            }
        }
    }

    public int getEffectiveEnergy() {
        return ExCompressum.autoSieveEnergy;
    }

    public float getEffectiveSpeed() {
        return ExCompressum.autoSieveSpeed * getSpeedBoost();
    }

    public float getEffectiveLuck() {
        if (inventory[21] != null) {
            return 1f + EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, inventory[21]);
        }
        return 1f;
    }

    public boolean isRegistered(ItemStack itemStack) {
        return SieveRegistry.registered(Block.getBlockFromItem(itemStack.getItem()), itemStack.getItemDamage());
    }

    public boolean isValidBook(ItemStack itemStack) {
        return itemStack.getItem() == Items.enchanted_book && (EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, inventory[21]) > 0 || EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, inventory[21]) > 0);
    }

    public Collection<SiftingResult> getSiftingOutput(ItemStack itemStack) {
        return SieveRegistry.getSiftingOutput(new ItemInfo(itemStack));
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        currentStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("CurrentStack"));
        progress = tagCompound.getFloat("Progress");
        spawnParticles = tagCompound.getBoolean("Particles");
        storage.readFromNBT(tagCompound);
        if (tagCompound.hasKey("CustomSkin")) {
            customSkin = NBTUtil.func_152459_a(tagCompound.getCompoundTag("CustomSkin"));
            ExCompressum.proxy.preloadSkin(customSkin);
        }
        NBTTagList items = tagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound itemCompound = items.getCompoundTagAt(i);
            int slot = itemCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(itemCompound);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if (currentStack != null) {
            tagCompound.setTag("CurrentStack", currentStack.writeToNBT(new NBTTagCompound()));
        }
        tagCompound.setFloat("Progress", progress);
        tagCompound.setBoolean("Particles", spawnParticles);
        if (customSkin != null) {
            NBTTagCompound customSkinTag = new NBTTagCompound();
            NBTUtil.func_152460_a(customSkinTag, customSkin);
            tagCompound.setTag("CustomSkin", customSkinTag);
        }
        storage.writeToNBT(tagCompound);
        NBTTagList items = new NBTTagList();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                itemCompound.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(itemCompound);
                items.appendTag(itemCompound);
            }
        }
        tagCompound.setTag("Items", items);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, blockMetadata, tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    @SideOnly(Side.CLIENT)
    private void spawnFX() {
        if (currentStack != null) {
            IIcon icon = currentStack.getIconIndex();
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

    @Override
    public int receiveEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection side, int maxExtract, boolean simulate) {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection side) {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection side) {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection side) {
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        return isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
        return slot >= 1 && slot <= 20;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return (slot == 0 && isRegistered(itemStack)) || (slot == 21 && isValidBook(itemStack));
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public int getSizeInventory() {
        return 22;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (inventory[slot] != null) {
            if (inventory[slot].stackSize <= amount) {
                ItemStack itemStack = inventory[slot];
                inventory[slot] = null;
                return itemStack;
            }
            ItemStack itemStack = inventory[slot].splitStack(amount);
            if (inventory[slot].stackSize == 0) {
                inventory[slot] = null;
            }
            return itemStack;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        inventory[slot] = itemStack;
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
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityPlayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64;
    }

    public void setEnergyStored(int energyStored) {
        storage.setEnergyStored(energyStored);
    }

    public boolean isProcessing() {
        return progress > 0f;
    }

    public float getProgress() {
        return progress;
    }

    public float getEnergyPercentage() {
        return (float) storage.getEnergyStored() / (float) storage.getMaxEnergyStored();
    }

    public ItemStack getCurrentStack() {
        return currentStack;
    }

    public void setCustomSkin(GameProfile customSkin) {
        this.customSkin = customSkin;
        grabProfile();
        isDirty = true;
        markDirty();
    }

    public GameProfile getCustomSkin() {
        return customSkin;
    }

    private void grabProfile() {
        if (!worldObj.isRemote && customSkin != null && !StringUtils.isNullOrEmpty(customSkin.getName())) {
            if (!customSkin.isComplete() || !customSkin.getProperties().containsKey("textures")) {
                GameProfile gameProfile = MinecraftServer.getServer().func_152358_ax().func_152655_a(customSkin.getName());
                if (gameProfile != null) {
                    Property property = Iterables.getFirst(gameProfile.getProperties().get("textures"), null);
                    if (property == null) {
                        gameProfile = MinecraftServer.getServer().func_147130_as().fillProfileProperties(gameProfile, true);
                    }
                    customSkin = gameProfile;
                    isDirty = true;
                    markDirty();
                }
            }
        }
    }

    public boolean isActive() {
        return spawnParticles;
    }

    public float getSpeedBoost() {
        float activeSpeedBost = speedBoost;
        if (inventory[21] != null) {
            activeSpeedBost += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, inventory[21]);
        }
        return activeSpeedBost;
    }

    public void setSpeedBoost(int speedBoostTicks, float speedBoost) {
        this.speedBoostTicks = speedBoostTicks;
        this.speedBoost = speedBoost;
    }
}
