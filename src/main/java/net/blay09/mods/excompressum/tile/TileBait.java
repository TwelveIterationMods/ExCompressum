package net.blay09.mods.excompressum.tile;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.registry.ItemAndMetadata;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Collection;

public class TileBait extends TileEntity implements ITickable {

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
    public void update() {
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
                if (worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range)).isEmpty()) {
                    EntityLiving entityLiving = getBaitEntityLiving(worldObj, metadata);
                    if (entityLiving != null) {
                        if (entityLiving instanceof EntityAgeable && worldObj.rand.nextFloat() <= ExCompressumConfig.baitChildChance) {
                            ((EntityAgeable) entityLiving).setGrowingAge(-24000);
                        }
                        entityLiving.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                        worldObj.spawnEntityInWorld(entityLiving);
                    }
                    worldObj.setBlockToAir(pos);
                }
            }
        }
    }

    private static ItemStack getBaitDisplayItem(int metadata, int i) {
        switch(metadata) {
            case 0: return i == 0 ? new ItemStack(Items.BEEF) : new ItemStack(Items.BONE);
            case 1: return i == 0 ? new ItemStack(Items.GUNPOWDER) : new ItemStack(Items.FISH);
            case 2: return new ItemStack(Items.WHEAT);
            case 3: return new ItemStack(Items.CARROT);
            case 4: return new ItemStack(Items.WHEAT_SEEDS);
            case 5: return i == 0 ? new ItemStack(GameRegistry.findItem("exnihilo" ,"seed_grass")) : new ItemStack(Items.WHEAT); // TODO generify
            case 6: return new ItemStack(Items.FISH);
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
            case 6: return new EntitySquid(world);
        }
        return null;
    }

    private float getBaitChance(int metadata) {
        switch(metadata) {
            case 0: return ExCompressumConfig.baitWolfChance;
            case 1: return ExCompressumConfig.baitOcelotChance;
            case 2: return ExCompressumConfig.baitCowChance;
            case 3: return ExCompressumConfig.baitPigChance;
            case 4: return ExCompressumConfig.baitChickenChance;
            case 5: return ExCompressumConfig.baitSheepChance;
            case 6: return ExCompressumConfig.baitSquidChance;
        }
        return 0;
    }

    @Nullable
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
            for(int x = pos.getX() - range; x < pos.getX() + range; x++) {
                for(int y = pos.getY() - rangeVertical; y < pos.getY() + rangeVertical; y++) {
                    for(int z = pos.getZ() - range; z < pos.getZ() + range; z++) {
                        BlockPos testPos = new BlockPos(x, y, z);
                        IBlockState state = worldObj.getBlockState(testPos);
                        if(state.getBlock() == ModBlocks.bait) {
                            countBait++;
                        } else if(state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER) {
                            foundWater = true;
                        }
                        if(envBlocks.contains(state)) { // TODO again, make sure this is safe to do with IBlockState
                            countEnvBlocks++;
                        }/* else if (envBlocks.contains(new ItemAndMetadata(block, OreDictionary.WILDCARD_VALUE))) { // TODO yeah RIP
                            countEnvBlocks++;
                        }*/
                    }
                }
            }
            if(!foundWater) {
                environmentStatus = EnvironmentalCondition.NoWater;
            } else if(countBait > MAX_BAITS_IN_AREA) {
                environmentStatus = EnvironmentalCondition.NearbyBait;
            } else if(countEnvBlocks < MIN_ENV_IN_AREA) {
                environmentStatus = EnvironmentalCondition.WrongEnv;
            } else if(worldObj.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(pos.getX() - range * 2, pos.getY() - rangeVertical, pos.getZ() - range * 2, pos.getX() + range * 2, pos.getY() + rangeVertical, pos.getZ() + range * 2)).size() > MAX_ANIMALS_IN_AREA) {
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
            envBlockMap.put(i, new ItemAndMetadata(Blocks.GRASS, 0));
            for (int j = 0; j <= 2; j++) envBlockMap.put(i, new ItemAndMetadata(Blocks.SAPLING, j));
            for (int j = 1; j <= 2; j++) envBlockMap.put(i, new ItemAndMetadata(Blocks.TALLGRASS, j));
            for (int j = 0; j <= 2; j++) envBlockMap.put(i, new ItemAndMetadata(Blocks.LOG, j));
            for (int j = 0; j <= 1; j++) envBlockMap.put(i, new ItemAndMetadata(Blocks.LOG2, j));
        }

        // Ocelot
        envBlockMap.put(1, new ItemAndMetadata(Blocks.LOG, 3));
        envBlockMap.put(1, new ItemAndMetadata(Blocks.VINE, OreDictionary.WILDCARD_VALUE));
        envBlockMap.put(1, new ItemAndMetadata(Blocks.WATERLILY, 0));
        envBlockMap.put(1, new ItemAndMetadata(Blocks.SAPLING, 3));

        // Squid
        envBlockMap.put(6, new ItemAndMetadata(Blocks.WATER, OreDictionary.WILDCARD_VALUE));
        envBlockMap.put(6, new ItemAndMetadata(Blocks.FLOWING_WATER, OreDictionary.WILDCARD_VALUE));
    }

}
