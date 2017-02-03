package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.CompressedMobsConfig;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
public class CompressedEnemyHandler {

    private static final String COMPRESSED = "Compressed";
    private static final String NOCOMPRESS = "NoCompress";

    @SubscribeEvent
    public void onSpawnEntity(EntityJoinWorldEvent event) {
        if(!event.getWorld().isRemote && (event.getEntity() instanceof EntityCreature || event.getEntity() instanceof EntityGhast)) {
            String entityName = EntityList.getEntityString(event.getEntity());
            if(CompressedMobsConfig.compressedMobs.contains(entityName)) {
                if (event.getEntity().worldObj.rand.nextFloat() <= CompressedMobsConfig.compressedMobChance && !event.getEntity().getEntityData().getCompoundTag(ExCompressum.MOD_ID).hasKey(NOCOMPRESS) && !event.getEntity().getEntityData().getCompoundTag(ExCompressum.MOD_ID).hasKey(COMPRESSED)) {
                    event.getEntity().setAlwaysRenderNameTag(true);
                    event.getEntity().setCustomNameTag("Compressed " + event.getEntity().getName());
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    tagCompound.setBoolean(COMPRESSED, true);
                    event.getEntity().getEntityData().setTag(ExCompressum.MOD_ID, tagCompound);
                } else {
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    tagCompound.setBoolean(NOCOMPRESS, true);
                    event.getEntity().getEntityData().setTag(ExCompressum.MOD_ID, tagCompound);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if(!event.getEntity().worldObj.isRemote && event.getEntity().getEntityData().getCompoundTag(ExCompressum.MOD_ID).hasKey(COMPRESSED)) {
            if(event.getEntity() instanceof EntityCreature || event.getEntity() instanceof EntityGhast) {
                if(event.getSource().getEntity() instanceof EntityPlayer && !(event.getSource().getEntity() instanceof FakePlayer)) {
                    if(StupidUtils.hasSilkTouchModifier((EntityLivingBase) event.getSource().getEntity())) {
                        return;
                    }
                    int entityId = EntityList.getEntityID(event.getEntity());
                    for(int i = 0; i < CompressedMobsConfig.compressedMobSize; i++) {
                        EntityLivingBase entity = (EntityLivingBase) EntityList.createEntityByID(entityId, event.getEntity().worldObj);
                        if(entity == null) {
                            return;
                        }
                        if(((EntityLivingBase) event.getEntity()).isChild()) {
                            if(entity instanceof EntityZombie) {
                                ((EntityZombie) entity).setChild(true);
                            } else if(entity instanceof EntityAgeable && event.getEntity() instanceof EntityAgeable) {
                                ((EntityAgeable) entity).setGrowingAge(((EntityAgeable) event.getEntity()).getGrowingAge());
                            }
                        }
                        if(event.getEntity() instanceof EntitySkeleton && entity instanceof EntitySkeleton) {
                            ((EntitySkeleton) entity).setSkeletonType(((EntitySkeleton) event.getEntity()).getSkeletonType());
                        }
                        if(entity instanceof EntityPigZombie) {
                            entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                        } else if(entity instanceof EntitySkeleton) {
                            if(((EntitySkeleton) entity).getSkeletonType() == SkeletonType.NORMAL) {
                                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                            } else {
                                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                            }
                        }
                        NBTTagCompound tagCompound = new NBTTagCompound();
                        tagCompound.setBoolean(NOCOMPRESS, true);
                        entity.getEntityData().setTag(ExCompressum.MOD_ID, tagCompound);
                        entity.setLocationAndAngles(event.getEntity().posX, event.getEntity().posY + 1, event.getEntity().posZ, (float) Math.random(), (float) Math.random());
                        double motion = 0.01;
                        entity.motionX = (event.getEntity().worldObj.rand.nextGaussian() - 0.5) * motion;
                        entity.motionY = 0;
                        entity.motionZ = (event.getEntity().worldObj.rand.nextGaussian() - 0.5) * motion;
                        event.getEntity().worldObj.spawnEntityInWorld(entity);
                    }
                }
            }
        }
    }

}
