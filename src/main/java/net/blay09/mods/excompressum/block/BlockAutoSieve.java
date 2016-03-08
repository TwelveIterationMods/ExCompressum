package net.blay09.mods.excompressum.block;

import cofh.api.block.IDismantleable;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.blocks.BlockSieve;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.registry.AutoSieveSkinRegistry;
import net.blay09.mods.excompressum.tile.TileEntityAutoSieve;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

@Optional.Interface(modid = "CoFHCore", iface = "cofh.api.block.IDismantleable", striprefs = true)
public class BlockAutoSieve extends BlockContainer implements IDismantleable {

    public BlockAutoSieve() {
        super(Material.iron);
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
        setBlockName(ExCompressum.MOD_ID + ":auto_sieve");
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
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityAutoSieve();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = entityPlayer.getHeldItem();
        if(heldItem != null) {
            TileEntityAutoSieve tileEntity = (TileEntityAutoSieve) world.getTileEntity(x, y, z);
            if(heldItem.getItem() instanceof ItemFood) {
                if(tileEntity.getSpeedBoost() <= 1f) {
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
                if(!entityPlayer.capabilities.isCreativeMode) {
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
            if (itemStack.stackTagCompound.hasKey("EnergyStored")) {
                tileEntity.setEnergyStored(itemStack.stackTagCompound.getInteger("EnergyStored"));
            }
            if (itemStack.stackTagCompound.hasKey("CustomSkin")) {
                tileEntity.setCustomSkin(NBTUtil.func_152459_a(itemStack.stackTagCompound.getCompoundTag("CustomSkin")));
                useRandomSkin = false;
            }
        }
        if(!world.isRemote && useRandomSkin) {
            tileEntity.setCustomSkin(new GameProfile(null, AutoSieveSkinRegistry.getRandomSkin()));
        }
        super.onBlockPlacedBy(world, x, y, z, player, itemStack);
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer entityPlayer, World world, int x, int y, int z, boolean returnDrops) {
        TileEntityAutoSieve tileEntity = (TileEntityAutoSieve) world.getTileEntity(x, y, z);
        ItemStack itemStack = new ItemStack(this);
        if (itemStack.stackTagCompound == null) {
            itemStack.stackTagCompound = new NBTTagCompound();
        }
        itemStack.stackTagCompound.setInteger("EnergyStored", tileEntity.getEnergyStored(null));
        NBTTagCompound customSkinTag = new NBTTagCompound();
        NBTUtil.func_152460_a(customSkinTag, tileEntity.getCustomSkin());
        itemStack.stackTagCompound.setTag("CustomSkin", customSkinTag);

        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(itemStack);
        world.setBlockToAir(x, y, z);
        if (!returnDrops) {
            dropBlockAsItem(world, x, y, z, itemStack);
        }
        return drops;
    }

    @Override
    public boolean canDismantle(EntityPlayer entityPlayer, World world, int x, int y, int z) {
        return true;
    }

    public static void registerRecipes(Configuration config) {
        if (Loader.isModLoaded("CoFHCore")) {
            if (config.getBoolean("Auto Sieve", "blocks", true, "Set this to false to disable the recipe for the auto sieve.")) {
                ItemStack sieve = new ItemStack(GameRegistry.findBlock("exnihilo", "sifting_table"), 1, OreDictionary.WILDCARD_VALUE);
                GameRegistry.addRecipe(new ItemStack(ModBlocks.autoSieve), "BGB", "GSG", "BHB", 'B', "blockIron", 'S', sieve, 'G', "paneGlass");
            }
        }
    }
}
