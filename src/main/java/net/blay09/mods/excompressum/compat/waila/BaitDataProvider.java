package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.blay09.mods.excompressum.tile.TileEntityBait;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
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
                list.add(I18n.format("waila.excompressum:baitTooClose"));
                list.add(I18n.format("waila.excompressum:baitTooClose2"));
            } else {
                list.add(TextFormatting.RED + I18n.format(environmentalStatus.langKey));
            }
        }
        return list;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return list;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tileEntity, NBTTagCompound tagCompound, World world, BlockPos pos) {
        if(tileEntity != null) {
            tileEntity.writeToNBT(tagCompound);
        }
        return tagCompound;
    }

}
