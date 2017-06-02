package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.tile.TileHeavySieve;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class HeavySieveDataProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return ItemStack.EMPTY;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return list;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if(accessor.getTileEntity() instanceof TileHeavySieve) {
            TileHeavySieve tileEntity = (TileHeavySieve) accessor.getTileEntity();
            if(tileEntity.getProgress() > 0f) {
                list.add(I18n.format("waila.excompressum:sieveProgress", (int) (tileEntity.getProgress() * 100) + "%"));
            }
            ItemStack meshStack = tileEntity.getMeshStack();
            if (!meshStack.isEmpty()) {
                if(ExRegistro.doMeshesHaveDurability()) {
                    list.add(I18n.format("waila.excompressum:sieveMesh", meshStack.getDisplayName(), meshStack.getMaxDamage() - meshStack.getItemDamage(), meshStack.getMaxDamage()));
                } else {
                    list.add(meshStack.getDisplayName());
                }
            } else {
                list.add(I18n.format("waila.excompressum:sieveNoMesh"));
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
