package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public class ItemBatZapper extends Item {

    public static final String name = "bat_zapper";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ItemBatZapper(Item.Properties properties) {
        super(properties.maxDamage(ItemTier.STONE.getMaxUses()).maxStackSize(1));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        // Debug code for free energy when in creative
        if (context.getPlayer() != null && context.getPlayer().abilities.isCreativeMode) {
            TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
            if (tileEntity != null) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(energyStorage -> energyStorage.receiveEnergy(Integer.MAX_VALUE, false));
            }
        }

        return zapBatter(context.getWorld(), context.getPlayer(), context.getItem(), context.getPos(), context.getHand()).getType();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        return zapBatter(world, player, player.getHeldItem(hand), player.getPosition(), hand);
    }

    private ActionResult<ItemStack> zapBatter(World world, PlayerEntity player, ItemStack itemStack, BlockPos pos, Hand hand) {
        world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1f, world.rand.nextFloat() * 0.1f + 0.9f);
        player.swingArm(hand);

        if (!world.isRemote) {
            final int range = 5;
            for (Object obj : world.getEntitiesWithinAABB(BatEntity.class, new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range))) {
                BatEntity entity = (BatEntity) obj;
                entity.attackEntityFrom(DamageSource.causePlayerDamage(player), Float.MAX_VALUE);
            }
        }

        itemStack.damageItem(1, player, it -> {
        });
        return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
    }

}
