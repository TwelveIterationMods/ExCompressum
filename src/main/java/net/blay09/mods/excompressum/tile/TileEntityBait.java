package net.blay09.mods.excompressum.tile;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.registry.data.ItemAndMetadata;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;

public class TileEntityBait extends TileEntity {

    private static final int ENVIRONMENTAL_CHECK_INTERVAL = 20 * 10;
    private static final int MAX_BAITS_IN_AREA = 2;
    private static final int MIN_ENV_IN_AREA = 10;
    private static final int MAX_ANIMALS_IN_AREA = 2;

    public enum EnvironmentalCondition {
        CanSpawn("info.excompressum:baitCanSpawn"),
        NearbyBait("info.excompressum:baitNearbyBait"),
        WrongEnv("info.excompressum:baitWrongEnv"),
        NearbyAnimal("info.excompressum:baitNearbyAnimal"),
        NoWater("info.excompressum:baitNoWater");

        public final String langKey;

        EnvironmentalCondition(String langKey) {
            this.langKey = langKey;
        }
    }

    private static final Multimap<Integer, ItemAndMetadata> envBlockMap = ArrayListMultimap.create();

    private EntityItem renderItemMain;
    private EntityItem renderItemSub;
    private EnvironmentalCondition environmentStatus;
    private int ticksSinceEnvironmentalCheck;

    @Override
    public void updateEntity() {
        super.updateEntity();
        ticksSinceEnvironmentalCheck++;
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
            if(checkSpawnConditions(true) == EnvironmentalCondition.CanSpawn) {
                float range = 24f;
                if (worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xCoord - range, yCoord - range, zCoord - range, xCoord + range, yCoord + range, zCoord + range)).isEmpty()) {
                    EntityLiving entityLiving = getBaitEntityLiving(worldObj, metadata);
                    if (entityLiving != null) {
                        if (entityLiving instanceof EntityAgeable && worldObj.rand.nextFloat() <= ExCompressum.baitChildChance) {
                            ((EntityAgeable) entityLiving).setGrowingAge(-24000);
                        }
                        entityLiving.setPosition(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                        worldObj.spawnEntityInWorld(entityLiving);
                    }
                    worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                }
            }
        }
    }

    private static ItemStack getBaitDisplayItem(int metadata, int i) {
        switch(metadata) {
            case 0: return i == 0 ? new ItemStack(Items.beef) : new ItemStack(Items.bone);
            case 1: return i == 0 ? new ItemStack(Items.gunpowder) : new ItemStack(Items.fish);
            case 2: return new ItemStack(Items.wheat);
            case 3: return new ItemStack(Items.carrot);
            case 4: return new ItemStack(Items.wheat_seeds);
            case 5: return i == 0 ? GameRegistry.findItemStack("exnihilo" ,"seed_grass", 1) : new ItemStack(Items.wheat);
        }
        return null;
    }

    private static EntityLiving getBaitEntityLiving(World world, int metadata) {
        switch(metadata) {
            case 0: return new EntityWolf(world);
            case 1: return new EntityOcelot(world);
            case 2: return new EntityCow(world);
            case 3: return new EntityPig(world);
            case 4: return new EntityChicken(world);
            case 5: return new EntitySheep(world);
        }
        return null;
    }

    private float getBaitChance(int metadata) {
        switch(metadata) {
            case 0: return ExCompressum.baitWolfChance;
            case 1: return ExCompressum.baitOcelotChance;
            case 2: return ExCompressum.baitCowChance;
            case 3: return ExCompressum.baitPigChance;
            case 4: return ExCompressum.baitChickenChance;
            case 5: return ExCompressum.baitSheepChance;
        }
        return 0;
    }

    public EntityItem getRenderItem(int i) {
        return i == 0 ? renderItemMain : renderItemSub;
    }

    public EnvironmentalCondition checkSpawnConditions(boolean checkNow) {
        if(checkNow || ticksSinceEnvironmentalCheck > ENVIRONMENTAL_CHECK_INTERVAL) {
            int metadata = getBlockMetadata();
            Collection<ItemAndMetadata> envBlocks = envBlockMap.get(metadata);
            populateEnvBlocks();
            final int range = 5;
            final int rangeVertical = 3;
            int countBait = 0;
            int countEnvBlocks = 0;
            boolean foundWater = false;
            for(int x = xCoord - range; x < xCoord + range; x++) {
                for(int y = yCoord - rangeVertical; y < yCoord + rangeVertical; y++) {
                    for(int z = zCoord - range; z < zCoord + range; z++) {
                        Block block = worldObj.getBlock(x, y, z);
                        if(block == ModBlocks.bait) {
                            countBait++;
                        } else if(block == Blocks.water || block == Blocks.flowing_water) {
                            foundWater = true;
                        } else if(envBlocks.contains(new ItemAndMetadata(block, worldObj.getBlockMetadata(x, y, z)))) {
                            countEnvBlocks++; } else if (envBlocks.contains(new ItemAndMetadata(block, OreDictionary.WILDCARD_VALUE))) {
                            countEnvBlocks++;
                        }
                    }
                }
            }
            if(!foundWater) {
                environmentStatus = EnvironmentalCondition.NoWater;
            } else if(countBait > MAX_BAITS_IN_AREA) {
                environmentStatus = EnvironmentalCondition.NearbyBait;
            } else if(countEnvBlocks < MIN_ENV_IN_AREA) {
                environmentStatus = EnvironmentalCondition.WrongEnv;
            } else if(worldObj.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(xCoord - range * 2, yCoord - rangeVertical, zCoord - range * 2, xCoord + range * 2, yCoord + rangeVertical, zCoord + range * 2)).size() > MAX_ANIMALS_IN_AREA) {
                environmentStatus = EnvironmentalCondition.NearbyAnimal;
            } else {
                environmentStatus = EnvironmentalCondition.CanSpawn;
            }
            ticksSinceEnvironmentalCheck = 0;
        }

        return environmentStatus;
    }

    private static void populateEnvBlocks() {
        if(envBlockMap.size() > 0) {
            return;
        }

        // Wolf, Cow, Pig, Chicken, Sheep
        for(int i = 0; i <= 5; i++) {
            if(i == 1) {
                continue;
            }
            envBlockMap.put(i, new ItemAndMetadata(Blocks.grass, 0));
            for (int j = 0; j <= 2; j++) envBlockMap.put(i, new ItemAndMetadata(Blocks.sapling, j));
            for (int j = 1; j <= 2; j++) envBlockMap.put(i, new ItemAndMetadata(Blocks.tallgrass, j));
            for (int j = 0; j <= 2; j++) envBlockMap.put(i, new ItemAndMetadata(Blocks.log, j));
            for (int j = 0; j <= 1; j++) envBlockMap.put(i, new ItemAndMetadata(Blocks.log2, j));
        }

        // Ocelot
        envBlockMap.put(1, new ItemAndMetadata(Blocks.log, 3));
        envBlockMap.put(1, new ItemAndMetadata(Blocks.vine, OreDictionary.WILDCARD_VALUE));
        envBlockMap.put(1, new ItemAndMetadata(Blocks.waterlily, 0));
        envBlockMap.put(1, new ItemAndMetadata(Blocks.sapling, 3));
    }
}
