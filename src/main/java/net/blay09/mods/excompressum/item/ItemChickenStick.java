package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ToolsConfig;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class ItemChickenStick extends ItemTool {

    public ItemChickenStick() {
        super(0f, 0f, ToolMaterial.DIAMOND, new HashSet<Block>());
        setRegistryName("chicken_stick");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
        setMaxDamage(0);
        damageVsEntity = 0f;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        String chickenStickName = ToolsConfig.getChickenStickName();
        return chickenStickName != null ? chickenStickName : super.getItemStackDisplayName(itemStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean flag) {
        if(ToolsConfig.getChickenStickName() != null) {
            list.add(TextFormatting.GRAY + I18n.format("item.excompressum:chicken_stick.name"));
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
    public boolean onBlockDestroyed(ItemStack itemStack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if(!world.isRemote && !(entityLiving instanceof FakePlayer) && ChickenStickRegistry.isHammerable(state)) {
            Collection<ItemStack> rewards = ExRegistro.rollHammerRewards(state, 0, 0f, world.rand);
            for (ItemStack rewardStack : rewards) {
                world.spawnEntityInWorld(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, rewardStack));
            }
            world.setBlockToAir(pos);
            if (world.rand.nextFloat() <= ToolsConfig.chickenStickSpawnChance) {
                EntityChicken entityChicken = new EntityChicken(world);
                entityChicken.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                world.spawnEntityInWorld(entityChicken);
            }
        }
        playChickenSound(world, pos);
        return true;
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return ChickenStickRegistry.isHammerable(state);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if ((ChickenStickRegistry.isHammerable(state))) {
            return efficiencyOnProperMaterial;
        }
        return 0.8f;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    private void playChickenSound(World world, BlockPos pos) {
        if(world.rand.nextFloat() <= ToolsConfig.chickenStickSoundChance) {
            ResourceLocation location = null;
            if(ToolsConfig.chickenStickSounds.length > 0) {
                location = ToolsConfig.chickenStickSounds[world.rand.nextInt(ToolsConfig.chickenStickSounds.length)];
            }
            if(location != null) {
                SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(location);
                if(soundEvent != null) {
                    world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, soundEvent, SoundCategory.PLAYERS, 1f, world.rand.nextFloat() * 0.1f + 0.9f, false);
                } else {
                    ExCompressum.logger.warn("Chicken Stick tried to play a sound that does not exist: {}", location);
                }
            }
        }
    }
}
