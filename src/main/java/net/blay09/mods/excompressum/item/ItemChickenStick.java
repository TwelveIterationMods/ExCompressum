package net.blay09.mods.excompressum.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ItemChickenStick extends ItemTool {

    public static final Set<Block> blocksEffectiveAgainst = Sets.newHashSet();

    public ItemChickenStick() {
        super(0f, 0f, ToolMaterial.DIAMOND, blocksEffectiveAgainst);
        setRegistryName("chicken_stick");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
        setMaxDamage(0);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        String chickenStickName = ChickenStickRegistry.getChickenStickName();
        return chickenStickName != null ? chickenStickName : super.getItemStackDisplayName(itemStack);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        if(ChickenStickRegistry.getChickenStickName() != null) {
            list.add(TextFormatting.GRAY + "(Chicken Stick)"); // TODO i18n
        }
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase attacker, EntityLivingBase target) {
        playChickenSound(attacker.worldObj, new BlockPos(attacker.posX, attacker.posY, attacker.posZ));
        return super.hitEntity(itemStack, attacker, target);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        playChickenSound(world, new BlockPos(player.posX, player.posY, player.posZ));
        player.swingArm(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemStack, BlockPos pos, EntityPlayer player) {
        World world = player.worldObj;
        if(world.isRemote || player instanceof FakePlayer) {
            return false;
        }
        IBlockState state = world.getBlockState(pos);
        if (!ChickenStickRegistry.isValidBlock(state)) {
            return false;
        }
        Collection<ItemStack> rewards = ExRegistro.rollHammerRewards(state, 0, 0f, world.rand);
        if (rewards.isEmpty()) {
            return false;
        }
        for (ItemStack rewardStack : rewards) {
            EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(rewardStack.getItem(), 1, rewardStack.getMetadata()));
            double motion = 0.05;
            entityItem.motionX = world.rand.nextGaussian() * motion;
            entityItem.motionY = 0.2;
            entityItem.motionZ = world.rand.nextGaussian() * motion;
            world.spawnEntityInWorld(entityItem);
        }
        world.setBlockToAir(pos);
        playChickenSound(world, pos);
        if(world.rand.nextFloat() <= ChickenStickRegistry.chickenStickSpawnChance) {
            EntityChicken entityChicken = new EntityChicken(world);
            entityChicken.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            world.spawnEntityInWorld(entityChicken);
        }
        return true;
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return ChickenStickRegistry.isValidBlock(state);
    }

    // TODO what is the new getDigSpeed?
    /*@Override
    public float getDigSpeed(ItemStack item, Block block, int meta) {
        if ((ChickenStickRegistry.isValidBlock(block, meta)) && block.getHarvestLevel(meta) <= toolMaterial.getHarvestLevel()) {
            return efficiencyOnProperMaterial * 0.75f;
        }
        return 0.8f;
    }*/

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return HashMultimap.create();
    }

    private void playChickenSound(World world, BlockPos pos) {
        if(world.rand.nextFloat() <= ChickenStickRegistry.chickenStickSoundChance) {
            String soundName = null;
            if(ChickenStickRegistry.chickenStickSounds.length > 0) {
                soundName = ChickenStickRegistry.chickenStickSounds[world.rand.nextInt(ChickenStickRegistry.chickenStickSounds.length)];
            }
            if(soundName != null) {
                SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName));
                world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, soundEvent, SoundCategory.PLAYERS, 1f, world.rand.nextFloat() * 0.1f + 0.9f, false);
            }
        }
    }
}
