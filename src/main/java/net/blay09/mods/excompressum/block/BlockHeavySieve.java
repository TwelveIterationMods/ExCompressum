package net.blay09.mods.excompressum.block;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.blocks.tileentities.TileEntitySieve;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.registry.HeavySieveRegistry;
import net.blay09.mods.excompressum.tile.TileEntityHeavySieve;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class BlockHeavySieve extends BlockContainer {

    public static IIcon meshIcon;

    public BlockHeavySieve() {
        super(Material.wood);
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
        setBlockName(ExCompressum.MOD_ID + ":heavy_sieve");
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = Blocks.log.getIcon(0, 0);
        meshIcon = iconRegister.registerIcon(ExCompressum.MOD_ID + ":heavy_sieve_mesh");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for(int i = 0; i < 6; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
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
        return new TileEntityHeavySieve();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        if (entityPlayer == null) {
            return false;
        }
        PlayerInteractEvent event = new PlayerInteractEvent(entityPlayer, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, x, y, z, side, world);
        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY || event.useBlock == Event.Result.DENY) {
            return false;
        }

        TileEntityHeavySieve tileEntity = (TileEntityHeavySieve) world.getTileEntity(x, y, z);

        ItemStack heldItem = entityPlayer.getCurrentEquippedItem();
        if (tileEntity.getMode() == TileEntitySieve.SieveMode.EMPTY && heldItem != null) {
            if (HeavySieveRegistry.isRegistered(Block.getBlockFromItem(heldItem.getItem()), heldItem.getItemDamage())) {
                tileEntity.addSievable(heldItem);
                if (!entityPlayer.capabilities.isCreativeMode) {
                    heldItem.stackSize--;
                }
            }
        } else {
            if (world.isRemote) {
                tileEntity.processContents(entityPlayer.capabilities.isCreativeMode);
            } else {
                if (tileEntity.getMode() != TileEntitySieve.SieveMode.EMPTY) {
                    if (ExCompressum.allowHeavySieveAutomation || !(entityPlayer instanceof FakePlayer)) {
                        tileEntity.processContents(entityPlayer.capabilities.isCreativeMode);
                    }
                }
            }
        }

        return true;
    }

    public static void registerRecipes(Configuration config) {
        if (config.getBoolean("Heavy Sieve", "blocks", true, "If set to false, the recipe for the heavy sieve will be disabled.")) {
            Item itemSilkMesh = GameRegistry.findItem("exnihilo", "mesh");
            if (itemSilkMesh != null) {
                GameRegistry.addRecipe(new ItemStack(ModItems.heavySilkMesh), "##", "##", '#', itemSilkMesh);
            }
            for (int i = 0; i < 4; i++) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.heavySieve, 1, i), "pmp", "pmp", "s s", 'p', new ItemStack(Blocks.log, 1, i), 'm', ModItems.heavySilkMesh, 's', "stickWood"));
            }
            for (int i = 0; i < 2; i++) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.heavySieve, 1, 4 + i), "pmp", "pmp", "s s", 'p', new ItemStack(Blocks.log2, 1, i), 'm', ModItems.heavySilkMesh, 's', "stickWood"));
            }
        }
    }
}
