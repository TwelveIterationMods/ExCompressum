package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBatZapper extends ItemCompressum {

    public ItemBatZapper() {
        setRegistryName("bat_zapper");
        setUnlocalizedName(getRegistryNameString());
        setMaxDamage(ToolMaterial.STONE.getMaxUses());
        setMaxStackSize(1);
        setCreativeTab(ExCompressum.creativeTab);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return zapBatter(world, player, player.getHeldItem(hand), (int) player.posX, (int) player.posY, (int) player.posZ, hand).getType();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        return zapBatter(world, player, player.getHeldItem(hand), (int) player.posX, (int) player.posY, (int) player.posZ, hand);
    }

    public ActionResult<ItemStack> zapBatter(World world, EntityPlayer entityPlayer, ItemStack itemStack, int x, int y, int z, EnumHand hand) {
        world.playSound(x, y, z, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1f, world.rand.nextFloat() * 0.1f + 0.9f, false);
        entityPlayer.swingArm(hand);
        if(!world.isRemote) {
            final int range = 5;
            for (Object obj : world.getEntitiesWithinAABB(EntityBat.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range))) {
                EntityBat entity = (EntityBat) obj;
                entity.attackEntityFrom(DamageSource.causePlayerDamage(entityPlayer), Float.MAX_VALUE);
            }
        }
        itemStack.damageItem(1, entityPlayer);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

}
