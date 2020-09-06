package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemBatZapper extends Item {

    public static final String name = "bat_zapper";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ItemBatZapper(Item.Properties properties) {
        super(properties.maxDamage(ItemTier.STONE.getMaxUses()).maxStackSize(1));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        // Debug code for free energy
//        TileEntity tileEntity = world.getTileEntity(pos);
//        if(tileEntity != null) {
//            IEnergyStorage energy = tileEntity.getCapability(CapabilityEnergy.ENERGY, null);
//            if (energy != null) {
//                energy.receiveEnergy(Integer.MAX_VALUE, false);
//            }
//        }
        return zapBatter(context.getWorld(), context.getPlayer(), context.getItem(), context.getPos(), context.getHand()).getType();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        return zapBatter(world, player, player.getHeldItem(hand), player.getPosition(), hand);
    }

    private ActionResult<ItemStack> zapBatter(World world, @Nullable PlayerEntity player, ItemStack itemStack, BlockPos pos, Hand hand) {
        world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1f, world.rand.nextFloat() * 0.1f + 0.9f, false);
        if(player != null) {
            player.swingArm(hand);
        }
        if(!world.isRemote) {
            final int range = 5;
            for (Object obj : world.getEntitiesWithinAABB(BatEntity.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range))) {
                BatEntity entity = (BatEntity) obj;
                entity.attackEntityFrom(DamageSource.causePlayerDamage(player), Float.MAX_VALUE);
            }
        }
        itemStack.damageItem(1, player);
        return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
    }

}
