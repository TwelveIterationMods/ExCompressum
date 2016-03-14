package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.blay09.mods.excompressum.tile.TileEntityBait;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class BaitDataProvider implements IWailaDataProvider {

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
        if(accessor.getTileEntity() instanceof TileEntityBait) {
            TileEntityBait tileEntityBait = (TileEntityBait) accessor.getTileEntity();
            TileEntityBait.EnvironmentalCondition environmentalStatus = tileEntityBait.checkSpawnConditions(true);
            if(environmentalStatus == TileEntityBait.EnvironmentalCondition.CanSpawn) {
                list.add(StatCollector.translateToLocal("waila.excompressum:baitTooClose"));
                list.add(StatCollector.translateToLocal("waila.excompressum:baitTooClose2"));
            } else {
                list.add("\u00a7c" + StatCollector.translateToLocal(environmentalStatus.langKey));
            }
        }
        return list;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return list;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayer, TileEntity tileEntity, NBTTagCompound tagCompound, World world, int x, int y, int z) {
        if(tileEntity != null) {
            tileEntity.writeToNBT(tagCompound);
        }
        return tagCompound;
    }

}
