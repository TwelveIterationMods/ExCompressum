package net.blay09.mods.excompressum;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

import java.util.Set;

public class ItemChickenStick extends ItemTool {

    public static final Set<Block> blocksEffectiveAgainst = Sets.newHashSet();

    protected ItemChickenStick() {
        super(0f, ToolMaterial.EMERALD, blocksEffectiveAgainst);
        setUnlocalizedName("chicken_stick");
        setMaxDamage(0);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entityLiving) {
        if(world.rand.nextFloat() <= ExCompressum.chickenStickSoundChance) {
            world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "mob.chicken.say", 1f, world.rand.nextFloat() * 0.1f + 0.9f);
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
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon("excompressum:chicken_stick");
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public Multimap getItemAttributeModifiers() {
        return HashMultimap.create(); // reset damage value as set from tool
    }
}
