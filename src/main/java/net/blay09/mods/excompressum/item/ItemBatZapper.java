package net.blay09.mods.excompressum.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModItems;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemBatZapper extends Item {

    public ItemBatZapper() {
        setUnlocalizedName(ExCompressum.MOD_ID + ":bat_zapper");
        setTextureName(ExCompressum.MOD_ID + ":bat_zapper");
        setCreativeTab(ExCompressum.creativeTab);
        setMaxDamage(ToolMaterial.STONE.getMaxUses());
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        zapBatter(world, entityPlayer, itemStack, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        zapBatter(world, entityPlayer, itemStack, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
        return itemStack;
    }

    public void zapBatter(World world, EntityPlayer entityPlayer, ItemStack itemStack, int x, int y, int z) {
        world.playSoundEffect(x, y, z, "fire.ignite", 1f, world.rand.nextFloat() * 0.1f + 0.9f);
        entityPlayer.swingItem();
        if(!world.isRemote) {
            final int range = 5;
            for (Object obj : world.getEntitiesWithinAABB(EntityBat.class, AxisAlignedBB.getBoundingBox(x - range, y - range, z - range, x + range, y + range, z + range))) {
                EntityBat entity = (EntityBat) obj;
                entity.attackEntityFrom(DamageSource.causePlayerDamage(entityPlayer), Float.MAX_VALUE);
            }
        }
        itemStack.damageItem(1, entityPlayer);
    }

    public static void registerRecipes(Configuration config) {
        if(config.getBoolean("Bat Zapper", "items", true, "If set to false, the recipe for the bat zapper will be disabled.")) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.batZapper), " RG", " SR", "S  ", 'R', Items.redstone, 'G', Items.glowstone_dust, 'S', "stickWood"));
        }
    }
}
