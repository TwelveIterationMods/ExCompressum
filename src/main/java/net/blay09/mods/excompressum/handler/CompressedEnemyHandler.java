package net.blay09.mods.excompressum.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class CompressedEnemyHandler {

    @SubscribeEvent
    public void onSpawnEntity(EntityJoinWorldEvent event) {
        if(event.entity instanceof EntityCreature && event.entity.worldObj.rand.nextFloat() <= ExCompressum.compressedMobChance) {
            String entityName = EntityList.getEntityString(event.entity);
            if(ExCompressum.compressedMobs.contains(entityName)) {
                if (!event.entity.getEntityData().getCompoundTag("ExCompressum").hasKey("NoCompress") && !event.entity.getEntityData().getCompoundTag("ExCompressum").hasKey("Compressed")) {
                    ((EntityCreature) event.entity).setAlwaysRenderNameTag(true);
                    ((EntityCreature) event.entity).setCustomNameTag("Compressed " + event.entity.getCommandSenderName());
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    tagCompound.setBoolean("Compressed", true);
                    event.entity.getEntityData().setTag("ExCompressum", tagCompound);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if(!event.entity.worldObj.isRemote && event.entity.getEntityData().getCompoundTag("ExCompressum").hasKey("Compressed")) {
            if(event.entity instanceof EntityCreature) {
                if(event.source.getEntity() instanceof EntityPlayer && !(event.source.getEntity() instanceof FakePlayer)) {
                    if(EnchantmentHelper.getSilkTouchModifier((EntityLivingBase) event.source.getEntity())) {
                        return;
                    }
                    int entityId = EntityList.getEntityID(event.entity);
                    for(int i = 0; i < ExCompressum.compressedMobSize; i++) {
                        EntityLivingBase entity = (EntityLivingBase) EntityList.createEntityByID(entityId, event.entity.worldObj);
                        NBTTagCompound tagCompound = new NBTTagCompound();
                        tagCompound.setBoolean("NoCompress", true);
                        entity.getEntityData().setTag("ExCompressum", tagCompound);
                        entity.setPositionAndUpdate(event.entity.posX, event.entity.posY + 1, event.entity.posZ);
                        double motion = 0.01;
                        entity.motionX = (event.entity.worldObj.rand.nextGaussian() - 0.5) * motion;
                        entity.motionY = 0;
                        entity.motionZ = (event.entity.worldObj.rand.nextGaussian() - 0.5) * motion;
                        event.entity.worldObj.spawnEntityInWorld(entity);
                    }
                }
            }
        }
    }

}
