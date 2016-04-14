package net.blay09.mods.excompressum.block;

import com.mojang.authlib.GameProfile;
import exnihilo.blocks.BlockSieve;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.registry.AutoSieveSkinRegistry;
import net.blay09.mods.excompressum.tile.TileEntityAutoSieve;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class BlockAutoSieve extends BlockContainer {

	protected BlockAutoSieve(Material material) {
		super(material);
		setCreativeTab(ExCompressum.creativeTab);
		setHardness(2f);
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		return BlockSieve.meshIcon;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = entityPlayer.getHeldItem();
		if (heldItem != null) {
			TileEntityAutoSieve tileEntity = (TileEntityAutoSieve) world.getTileEntity(x, y, z);
			if (heldItem.getItem() instanceof ItemFood) {
				if (tileEntity.getSpeedBoost() <= 1f) {
					tileEntity.setSpeedBoost((int) (((ItemFood) heldItem.getItem()).func_150906_h(heldItem) * 640), Math.max(1f, ((ItemFood) heldItem.getItem()).func_150905_g(heldItem) * 0.75f));
					if (!entityPlayer.capabilities.isCreativeMode) {
						heldItem.stackSize--;
					}
					if (!world.isRemote) {
						world.playAuxSFX(2005, x, y, z, 0);
					}
				}
				return true;
			} else if (heldItem.getItem() == Items.name_tag && heldItem.hasDisplayName()) {
				tileEntity.setCustomSkin(new GameProfile(null, heldItem.getDisplayName()));
				if (!entityPlayer.capabilities.isCreativeMode) {
					heldItem.stackSize--;
				}
				return true;
			}
		}
		entityPlayer.openGui(ExCompressum.instance, GuiHandler.GUI_AUTO_SIEVE, world, x, y, z);
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
		IInventory tileEntity = (IInventory) world.getTileEntity(x, y, z);
		for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
			if (tileEntity.getStackInSlot(i) != null) {
				EntityItem entityItem = new EntityItem(world, x, y, z, tileEntity.getStackInSlot(i));
				double motion = 0.05;
				entityItem.motionX = world.rand.nextGaussian() * motion;
				entityItem.motionY = 0.2;
				entityItem.motionZ = world.rand.nextGaussian() * motion;
				world.spawnEntityInWorld(entityItem);
			}
		}
		if (((TileEntityAutoSieve) tileEntity).getCurrentStack() != null) {
			EntityItem entityItem = new EntityItem(world, x, y, z, ((TileEntityAutoSieve) tileEntity).getCurrentStack());
			double motion = 0.05;
			entityItem.motionX = world.rand.nextGaussian() * motion;
			entityItem.motionY = 0.2;
			entityItem.motionZ = world.rand.nextGaussian() * motion;
			world.spawnEntityInWorld(entityItem);
		}
		super.breakBlock(world, x, y, z, block, metadata);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack) {
		TileEntityAutoSieve tileEntity = (TileEntityAutoSieve) world.getTileEntity(x, y, z);
		boolean useRandomSkin = true;
		if (itemStack.stackTagCompound != null) {
			if (itemStack.stackTagCompound.hasKey("CustomSkin")) {
				tileEntity.setCustomSkin(NBTUtil.func_152459_a(itemStack.stackTagCompound.getCompoundTag("CustomSkin")));
				useRandomSkin = false;
			}
		}
		if (!world.isRemote && useRandomSkin) {
			tileEntity.setCustomSkin(new GameProfile(null, AutoSieveSkinRegistry.getRandomSkin()));
		}
		int facing = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if (facing == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}
		if (facing == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}
		if (facing == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}
		if (facing == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
		super.onBlockPlacedBy(world, x, y, z, player, itemStack);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
		return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
	}
}
