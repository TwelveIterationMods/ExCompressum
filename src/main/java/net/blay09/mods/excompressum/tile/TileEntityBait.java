package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityBait extends TileEntity {

    private EntityItem renderItemMain;
    private EntityItem renderItemSub;

    @Override
    public void updateEntity() {
        super.updateEntity();
        int metadata = getBlockMetadata();
        if(renderItemMain == null) {
            renderItemMain = new EntityItem(worldObj);
            renderItemMain.setEntityItemStack(getBaitDisplayItem(metadata, 0));
        }
        if(renderItemSub == null) {
            renderItemSub = new EntityItem(worldObj);
            renderItemSub.setEntityItemStack(getBaitDisplayItem(metadata, 1));
        }
        if(!worldObj.isRemote && worldObj.rand.nextFloat() <= getBaitChance(metadata)) {
            float range = 24f;
            if(worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xCoord - range, yCoord - range, zCoord - range, xCoord + range, yCoord + range, zCoord + range)).isEmpty()) {
                EntityLiving entityLiving = getBaitEntityLiving(worldObj, metadata);
                if (entityLiving != null) {
                    entityLiving.setPosition(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                    worldObj.spawnEntityInWorld(entityLiving);
                }
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            }
        }
    }

    private static ItemStack getBaitDisplayItem(int metadata, int i) {
        switch(metadata) {
            case 0: return i == 0 ? new ItemStack(Items.beef) : new ItemStack(Items.bone);
            case 1: return i == 0 ? new ItemStack(Items.gunpowder) : new ItemStack(Items.fish);
        }
        return null;
    }

    private static EntityLiving getBaitEntityLiving(World world, int metadata) {
        switch(metadata) {
            case 0: return new EntityWolf(world);
            case 1: return new EntityOcelot(world);
        }
        return null;
    }

    private float getBaitChance(int metadata) {
        switch(metadata) {
            case 0: return ExCompressum.baitWolfChance;
            case 1: return ExCompressum.baitOcelotChance;
        }
        return 0;
    }

    public EntityItem getRenderItem(int i) {
        return i == 0 ? renderItemMain : renderItemSub;
    }
}
