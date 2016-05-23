package net.blay09.mods.excompressum.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ChickenStickRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ItemChickenStick extends ItemTool {

    public static final Set<Block> blocksEffectiveAgainst = Sets.newHashSet();

    public ItemChickenStick() {
        super(0f, ToolMaterial.EMERALD, blocksEffectiveAgainst);
        setUnlocalizedName(ExCompressum.MOD_ID + ":chicken_stick");
        setTextureName(ExCompressum.MOD_ID + ":chicken_stick");
        setCreativeTab(ExCompressum.creativeTab);
        setMaxDamage(0);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        return ChickenStickRegistry.getChickenStickName() != null ? ChickenStickRegistry.getChickenStickName() : super.getItemStackDisplayName(itemStack);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        if(ChickenStickRegistry.getChickenStickName() != null) {
            list.add(EnumChatFormatting.GRAY + "(Chicken Stick)");
        }
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase attacker, EntityLivingBase target) {
        playChickenSound(attacker.worldObj, (int) attacker.posX, (int) attacker.posY, (int) attacker.posZ);
        return super.hitEntity(itemStack, attacker, target);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        playChickenSound(world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
        entityPlayer.swingItem();
        return super.onItemRightClick(itemStack, world, entityPlayer);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemStack, int x, int y, int z, EntityPlayer entityPlayer) {
        World world = entityPlayer.worldObj;
        if(world.isRemote || entityPlayer instanceof FakePlayer) {
            return false;
        }
        Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        if (!ChickenStickRegistry.isValidBlock(block, metadata)) {
            return false;
        }
        Collection<Smashable> rewards = HammerRegistry.getRewards(block, metadata);
        if (rewards == null || rewards.isEmpty()) {
            return false;
        }
        for (Smashable reward : rewards) {
            if (world.rand.nextFloat() <= reward.chance) {
                EntityItem entityItem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, new ItemStack(reward.item, 1, reward.meta));
                double motion = 0.05;
                entityItem.motionX = world.rand.nextGaussian() * motion;
                entityItem.motionY = 0.2;
                entityItem.motionZ = world.rand.nextGaussian() * motion;
                world.spawnEntityInWorld(entityItem);
            }
        }
        world.setBlockToAir(x, y, z);
        playChickenSound(world, x, y, z);
        if(!world.isRemote && world.rand.nextFloat() <= ChickenStickRegistry.chickenStickSpawnChance) {
            EntityChicken entityChicken = new EntityChicken(world);
            entityChicken.setPosition(x, y, z);
            world.spawnEntityInWorld(entityChicken);
        }
        return true;
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack itemStack) {
        return ChickenStickRegistry.isValidBlock(block, 0);
    }

    @Override
    public float getDigSpeed(ItemStack item, Block block, int meta) {
        if ((ChickenStickRegistry.isValidBlock(block, meta)) && block.getHarvestLevel(meta) <= toolMaterial.getHarvestLevel()) {
            return efficiencyOnProperMaterial * 0.75f;
        }
        return 0.8f;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public Multimap getItemAttributeModifiers() {
        return HashMultimap.create(); // reset damage value as set from tool
    }

    private void playChickenSound(World world, int x, int y, int z) {
        if(world.rand.nextFloat() <= ChickenStickRegistry.chickenStickSoundChance) {
            String soundName = null;
            if(ChickenStickRegistry.chickenStickSounds.length > 0) {
                soundName = ChickenStickRegistry.chickenStickSounds[world.rand.nextInt(ChickenStickRegistry.chickenStickSounds.length)];
            }
            if(soundName != null) {
                world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, soundName, 1f, world.rand.nextFloat() * 0.1f + 0.9f);
            }
        }
    }
}
