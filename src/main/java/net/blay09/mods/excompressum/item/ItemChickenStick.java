package net.blay09.mods.excompressum.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.blay09.mods.excompressum.registry.ChickenStickRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

import java.util.Set;

public class ItemChickenStick extends ItemTool {

    public static final Set<Block> blocksEffectiveAgainst = Sets.newHashSet();

    public ItemChickenStick() {
        super(0f, ToolMaterial.EMERALD, blocksEffectiveAgainst);
        setUnlocalizedName(ExCompressum.MOD_ID + ":chicken_stick");
        setTextureName(ExCompressum.MOD_ID + ":chicken_stick");
        setMaxDamage(0);
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase attacker, EntityLivingBase target) {
        playChickenSound(attacker.worldObj, (int) attacker.posX, (int) attacker.posY, (int) attacker.posZ);
        return super.hitEntity(itemStack, attacker, target);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        playChickenSound(world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
        return super.onItemRightClick(itemStack, world, entityPlayer);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entityLiving) {
        playChickenSound(world, x, y, z);
        if(world.rand.nextFloat() <= ExCompressum.chickenStickSpawnChance) {
            EntityChicken entityChicken = new EntityChicken(world);
            entityChicken.setPosition(x, y, z);
            world.spawnEntityInWorld(entityChicken);
        }
        return super.onBlockDestroyed(itemStack, world, block, x, y, z, entityLiving);
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
        if(world.rand.nextFloat() <= ExCompressum.chickenStickSoundChance) {
            String soundName = null;
            if(ExCompressum.chickenStickSounds.length > 0) {
                soundName = ExCompressum.chickenStickSounds[world.rand.nextInt(ExCompressum.chickenStickSounds.length)];
            }
            if(soundName != null) {
                world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, soundName, 1f, world.rand.nextFloat() * 0.1f + 0.9f);
            }
        }
    }
}
