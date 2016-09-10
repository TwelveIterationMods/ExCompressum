package net.blay09.mods.excompressum.tile;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.AutomationConfig;
import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

// TODO needs particles
public abstract class TileEntityAutoSieveBase extends TileEntityBase implements ITickable {

	// TODO need mesh slot

	private static final int UPDATE_INTERVAL = 20;

	private DefaultItemHandler itemHandler = new DefaultItemHandler(this, 22) {
		@Override
		public boolean isItemValid(int slot, ItemStack itemStack) {
			if(slot == 0) {
				return isRegistered(itemStack);
			} else if(slot == 21) {
				return isValidBook(itemStack);
			}
			return true;
		}
	};
	private RangedWrapper itemHandlerInput = new RangedWrapper(itemHandler, 0, 1);
	private RangedWrapper itemHandlerOutput = new RangedWrapper(itemHandler, 1, 21);
	private RangedWrapper itemHandlerUpgrade = new RangedWrapper(itemHandler, 21, 22);

	private ItemHandlerAutomation itemHandlerAutomation = new ItemHandlerAutomation(itemHandler) {
		@Override
		public boolean canExtractItem(int slot, int amount) {
			return slot >= 1 && slot <= 20;
		}
	};

	private ItemStack currentStack;
	private GameProfile customSkin;

	private int ticksSinceSync;
	protected boolean isDirty;

	private float progress;

	private float speedBoost = 1f;
	private int speedBoostTicks;

	private float armAngle;

	@Override
	public void update() {
		if (speedBoostTicks > 0) {
			speedBoostTicks--;
			if (speedBoostTicks <= 0) {
				speedBoost = 1f;
			}
		}

		ticksSinceSync++;
		if (ticksSinceSync > UPDATE_INTERVAL) {
			if (isDirty) {
				VanillaPacketHandler.sendTileEntityUpdate(this);
				isDirty = false;
			}
			ticksSinceSync = 0;
		}

		int effectiveEnergy = getEffectiveEnergy();
		if (getEnergyStored() >= effectiveEnergy) {
			if (currentStack == null) {
				ItemStack inputStack = itemHandlerInput.getStackInSlot(0);
				if (inputStack != null && isRegistered(inputStack)) {
					boolean foundSpace = false;
					for (int i = 0; i < itemHandlerOutput.getSlots(); i++) {
						if (itemHandlerOutput.getStackInSlot(i) == null) {
							foundSpace = true;
						}
					}
					if (!foundSpace) {
						return;
					}
					currentStack = inputStack.splitStack(1);
					if (inputStack.stackSize == 0) {
						itemHandlerInput.setStackInSlot(0, null);
					}
					setEnergyStored(getEnergyStored() - effectiveEnergy);
					VanillaPacketHandler.sendTileEntityUpdate(this);
					progress = 0f;
				}
			} else {
				armAngle += 0.05f * (Math.max(1f, speedBoost / 2f));
				setEnergyStored(getEnergyStored() - effectiveEnergy);
				progress += getEffectiveSpeed();
				isDirty = true;
				if (progress >= 1) {
					if (!worldObj.isRemote) {
						Collection<ItemStack> rewards = rollSieveRewards(currentStack, getMeshLevel(), getEffectiveLuck(), worldObj.rand);
						for (ItemStack itemStack : rewards) {
							if (!addItemToOutput(itemStack)) {
								EntityItem entityItem = new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, itemStack);
								double motion = 0.05;
								entityItem.motionX = worldObj.rand.nextGaussian() * motion;
								entityItem.motionY = 0.2;
								entityItem.motionZ = worldObj.rand.nextGaussian() * motion;
								worldObj.spawnEntityInWorld(entityItem);
							}
						}
						degradeBook();
					}
					progress = 0f;
					currentStack = null;
				}
			}
		}
	}

	private boolean addItemToOutput(ItemStack itemStack) {
		int firstEmptySlot = -1;
		for (int i = 0; i < itemHandlerOutput.getSlots(); i++) {
			ItemStack slotStack = itemHandlerOutput.getStackInSlot(i);
			if (slotStack == null) {
				if (firstEmptySlot == -1) {
					firstEmptySlot = i;
				}
			} else {
				if (slotStack.stackSize + itemStack.stackSize <= slotStack.getMaxStackSize() && slotStack.isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(slotStack, itemStack)) {
					slotStack.stackSize += itemStack.stackSize;
					return true;
				}
			}
		}
		if (firstEmptySlot != -1) {
			itemHandlerOutput.setStackInSlot(firstEmptySlot, itemStack);
			return true;
		}
		return false;
	}

	private void degradeBook() {
		ItemStack upgradeStack = itemHandlerUpgrade.getStackInSlot(0);
		if (upgradeStack != null && worldObj.rand.nextFloat() <= AutomationConfig.autoSieveBookDecay) {
			NBTTagList tagList = getEnchantmentList(upgradeStack);
			if (tagList != null) {
				for (int i = 0; i < tagList.tagCount(); ++i) {
					short id = tagList.getCompoundTagAt(i).getShort("id");
					if (id != Enchantment.getEnchantmentID(Enchantments.FORTUNE) && id != Enchantment.getEnchantmentID(Enchantments.EFFICIENCY)) {
						continue;
					}
					int level = tagList.getCompoundTagAt(i).getShort("lvl") - 1;
					if (level <= 0) {
						tagList.removeTag(i);
					} else {
						tagList.getCompoundTagAt(i).setShort("lvl", (short) level);
					}
					break;
				}
				if (tagList.tagCount() == 0) {
					itemHandlerUpgrade.setStackInSlot(0, new ItemStack(Items.BOOK));
				}
			}
		}
	}

	public int getEffectiveEnergy() {
		return AutomationConfig.autoSieveEnergy;
	}

	public float getEffectiveSpeed() {
		return AutomationConfig.autoSieveSpeed * getSpeedBoost();
	}

	public float getEffectiveLuck() {
		ItemStack upgradeStack = itemHandlerUpgrade.getStackInSlot(0);
		if (upgradeStack != null) {
			return 1f + getEnchantmentLevel(Enchantments.FORTUNE, upgradeStack);
		}
		return 1f;
	}

	public boolean isRegistered(ItemStack itemStack) {
		return ExRegistro.isSiftable(itemStack);
	}

	public boolean isValidBook(ItemStack itemStack) {
		return itemStack.getItem() == Items.ENCHANTED_BOOK && (getEnchantmentLevel(Enchantments.FORTUNE, itemStack) > 0 || getEnchantmentLevel(Enchantments.EFFICIENCY, itemStack) > 0);
	}

	private static NBTTagList getEnchantmentList(ItemStack itemStack) {
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if(tagCompound == null) {
			return null;
		}
		if(tagCompound.hasKey("StoredEnchantments")) {
			return tagCompound.getTagList("StoredEnchantments", Constants.NBT.TAG_COMPOUND);
		}
		if(tagCompound.hasKey("ench")) {
			return tagCompound.getTagList("ench", Constants.NBT.TAG_COMPOUND);
		}
		return null;
	}

	private static int getEnchantmentLevel(Enchantment enchantment, ItemStack itemStack) {
		NBTTagList tagList = getEnchantmentList(itemStack);
		if (tagList == null) {
			return 0;
		}
		for (int i = 0; i < tagList.tagCount(); i++) {
			short id = tagList.getCompoundTagAt(i).getShort("id");
			short lvl = tagList.getCompoundTagAt(i).getShort("lvl");
			if (id == Enchantment.getEnchantmentID(enchantment)) {
				return lvl;
			}
		}
		return 0;
	}

	public Collection<ItemStack> rollSieveRewards(ItemStack itemStack, int meshLevel, float luck, Random rand) {
		return ExRegistro.rollSieveRewards(itemStack, meshLevel, luck, rand);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		readFromNBTSynced(tagCompound);
		itemHandler.deserializeNBT(tagCompound.getCompoundTag("ItemHandler"));
	}

	protected void readFromNBTSynced(NBTTagCompound tagCompound) {
		currentStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("CurrentStack"));
		progress = tagCompound.getFloat("Progress");
		if (tagCompound.hasKey("CustomSkin")) {
			customSkin = NBTUtil.readGameProfileFromNBT(tagCompound.getCompoundTag("CustomSkin"));
			if(customSkin != null) {
				ExCompressum.proxy.preloadSkin(customSkin);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		writeToNBTSynced(tagCompound);
		tagCompound.setTag("ItemHandler", itemHandler.serializeNBT());
		return tagCompound;
	}

	protected void writeToNBTSynced(NBTTagCompound tagCompound) {
		if (currentStack != null) {
			tagCompound.setTag("CurrentStack", currentStack.writeToNBT(new NBTTagCompound()));
		}
		tagCompound.setFloat("Progress", progress);
		if (customSkin != null) {
			NBTTagCompound customSkinTag = new NBTTagCompound();
			NBTUtil.writeGameProfile(customSkinTag, customSkin);
			tagCompound.setTag("CustomSkin", customSkinTag);
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBTSynced(tagCompound);
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBTSynced(pkt.getNbtCompound());
	}

	public abstract void setEnergyStored(int energyStored);

	public abstract int getMaxEnergyStored();

	public abstract int getEnergyStored();

	public float getEnergyPercentage() {
		return (float) getEnergyStored() / (float) getMaxEnergyStored();
	}

	public boolean isProcessing() {
		return progress > 0f;
	}

	public float getProgress() {
		return progress;
	}

	@Nullable
	public ItemStack getCurrentStack() {
		return currentStack;
	}

	public void setCustomSkin(GameProfile customSkin) {
		this.customSkin = customSkin;
		grabProfile();
		isDirty = true;
		markDirty();
	}

	@Nullable
	public GameProfile getCustomSkin() {
		return customSkin;
	}

	private void grabProfile() {
		try {
			if (!worldObj.isRemote && customSkin != null && !StringUtils.isNullOrEmpty(customSkin.getName())) {
				if (!customSkin.isComplete() || !customSkin.getProperties().containsKey("textures")) {
					GameProfile gameProfile = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache().getGameProfileForUsername(customSkin.getName());
					if (gameProfile != null) {
						Property property = Iterables.getFirst(gameProfile.getProperties().get("textures"), null);
						if (property == null) {
							gameProfile = FMLCommonHandler.instance().getMinecraftServerInstance().getMinecraftSessionService().fillProfileProperties(gameProfile, true);
						}
						customSkin = gameProfile;
						isDirty = true;
						markDirty();
					}
				}
			}
		} catch (ClassCastException ignored) {
			// This is really dumb
			// Vanilla's Yggdrasil can fail with a "com.google.gson.JsonPrimitive cannot be cast to com.google.gson.JsonObject" exception, likely their server was derping or whatever. I have no idea
			// And there doesn't seem to be safety checks for that in Vanilla code so I have to do it here.
		}
	}

	public float getSpeedBoost() {
		float activeSpeedBoost = speedBoost;
		ItemStack upgradeStack = itemHandlerUpgrade.getStackInSlot(0);
		if (upgradeStack != null) {
			activeSpeedBoost += getEnchantmentLevel(Enchantments.EFFICIENCY, upgradeStack);
		}
		return activeSpeedBoost;
	}

	public void setSpeedBoost(int speedBoostTicks, float speedBoost) {
		this.speedBoostTicks = speedBoostTicks;
		this.speedBoost = speedBoost;
		this.isDirty = true;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) itemHandlerAutomation;
		}
		return super.getCapability(capability, facing);
	}

	public DefaultItemHandler getItemHandler() {
		return itemHandler;
	}

	public int getMeshLevel() {
		return 0; // TODO Mesh Level impl
	}

	public float getArmAngle() {
		return armAngle;
	}
}
