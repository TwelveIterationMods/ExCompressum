package net.blay09.mods.excompressum.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.tile.TileEntityBait;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;
import java.util.Random;

public class BlockBait extends BlockContainer {

    public BlockBait() {
        super(Material.ground);
        setHardness(0.1f);
        setCreativeTab(ExCompressum.creativeTab);
        setBlockBounds(0f, 0f, 0f, 1f, 0.1f, 1f);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
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
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i <= 6; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityBait();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        TileEntityBait.EnvironmentalCondition environmentStatus = ((TileEntityBait) world.getTileEntity(x, y, z)).checkSpawnConditions(true);
        if(entityPlayer != null && !world.isRemote) {
            IChatComponent chatComponent = new ChatComponentTranslation(environmentStatus.langKey);
            chatComponent.getChatStyle().setColor(environmentStatus != TileEntityBait.EnvironmentalCondition.CanSpawn ? EnumChatFormatting.RED : EnumChatFormatting.GREEN);
            entityPlayer.addChatComponentMessage(chatComponent);
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityPlayer, ItemStack itemStack) {
        if(entityPlayer instanceof EntityPlayer) {
            TileEntityBait.EnvironmentalCondition environmentStatus = ((TileEntityBait) world.getTileEntity(x, y, z)).checkSpawnConditions(true);
            if (!world.isRemote) {
                IChatComponent chatComponent = new ChatComponentTranslation(environmentStatus.langKey);
                chatComponent.getChatStyle().setColor(environmentStatus != TileEntityBait.EnvironmentalCondition.CanSpawn ? EnumChatFormatting.RED : EnumChatFormatting.GREEN);
                ((EntityPlayer) entityPlayer).addChatComponentMessage(chatComponent);
            }
        }
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        TileEntityBait tileEntity = (TileEntityBait) world.getTileEntity(x, y, z);
        if(tileEntity.checkSpawnConditions(false) == TileEntityBait.EnvironmentalCondition.CanSpawn) {
            if (random.nextFloat() <= 0.2f) {
                world.spawnParticle("smoke", x + random.nextFloat(), y + random.nextFloat() * 0.5f, z + random.nextFloat(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = Blocks.quartz_block.getIcon(0, 0);
    }

    public static void registerRecipes(Configuration config) {
        if (config.getBoolean("Wolf Bait", "blocks", true, "If set to false, the recipe for the wolf bait will be disabled.")) {
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 0), Items.bone, Items.beef);
        }
        if (config.getBoolean("Ocelot Bait", "blocks", true, "If set to false, the recipe for the ocelot bait will be disabled.")) {
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 1), Items.gunpowder, new ItemStack(Items.fish, 1, OreDictionary.WILDCARD_VALUE));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.bait, 1, 1), Items.gunpowder, "listAllfishraw")); // Pam's Fishies
        }
        if (config.getBoolean("Cow Bait", "blocks", true, "If set to false, the recipe for the cow bait will be disabled.")) {
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 2), Items.wheat, Items.wheat);
        }
        if (config.getBoolean("Pig Bait", "blocks", true, "If set to false, the recipe for the pig bait will be disabled.")) {
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 3), Items.carrot, Items.carrot);
        }
        if (config.getBoolean("Chicken Bait", "blocks", true, "If set to false, the recipe for the chicken bait will be disabled.")) {
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 4), Items.wheat_seeds, Items.wheat_seeds);
        }
        if (config.getBoolean("Sheep Bait", "blocks", true, "If set to false, the recipe for the sheep bait will be disabled.")) {
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 5), GameRegistry.findItem("exnihilo", "seed_grass"), Items.wheat);
        }
        if (config.getBoolean("Squid Bait", "blocks", false, "If set to false, the recipe for the squid bait will be disabled.")) {
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bait, 1, 6), Items.fish, Items.fish);
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.bait, 1, 6), "listAllfishraw", "listAllfishraw")); // Pam's Fishies
        }
    }

}
