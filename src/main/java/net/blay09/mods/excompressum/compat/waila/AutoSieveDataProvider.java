package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.blay09.mods.excompressum.tile.TileAutoSieveBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class AutoSieveDataProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return list;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if(accessor.getTileEntity() instanceof TileAutoSieveBase) {
            TileAutoSieveBase tileEntity = (TileAutoSieveBase) accessor.getTileEntity();
            if(tileEntity.getCustomSkin() != null) {
                list.add(I18n.format("waila.excompressum:sieveSkin", tileEntity.getCustomSkin().getName()));
            }
            if(tileEntity.getSpeedMultiplier() > 1f) {
                list.add(I18n.format("waila.excompressum:speedBoost", tileEntity.getFoodBoost()));
            }
            if(tileEntity.getEffectiveLuck() > 1) {
                list.add(I18n.format("waila.excompressum:luckBonus", tileEntity.getEffectiveLuck() - 1));
            }
        }
        return list;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return list;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayer, TileEntity tileEntity, NBTTagCompound tagCompound, World world, BlockPos pos) {
        if(tileEntity != null) {
            tileEntity.writeToNBT(tagCompound);
        }
        return tagCompound;
    }

}
