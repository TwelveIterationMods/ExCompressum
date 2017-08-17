package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.handler.ChickenStickHandler;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
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

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;

public class ItemChickenStick extends ItemTool {

    public static final String name = "chicken_stick";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ItemChickenStick() {
        super(0f, 0f, ToolMaterial.DIAMOND, new HashSet<>());
        setUnlocalizedName(registryName.toString());
        setCreativeTab(ExCompressum.creativeTab);
        setMaxDamage(0);
        damageVsEntity = 0f;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        String chickenStickName = ChickenStickHandler.chickenStickName;
        return chickenStickName != null ? chickenStickName : super.getItemStackDisplayName(itemStack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        if(ChickenStickHandler.chickenStickName != null) {
            tooltip.add(TextFormatting.GRAY + I18n.format("item.excompressum:chicken_stick.name"));
        }
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase attacker, EntityLivingBase target) {
        playChickenSound(attacker.world, new BlockPos(attacker.posX, attacker.posY, attacker.posZ));
        return super.hitEntity(itemStack, attacker, target);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        playChickenSound(world, new BlockPos(player.posX, player.posY, player.posZ));
        player.swingArm(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return ChickenStickRegistry.isHammerable(state);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if ((ChickenStickRegistry.isHammerable(state))) {
            if(isAngry(stack)) {
                return efficiencyOnProperMaterial * 1.5f;
            }
            return efficiencyOnProperMaterial;
        }
        return 0.8f;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    public void playChickenSound(World world, BlockPos pos) {
        if(world.rand.nextFloat() <= ModConfig.tools.chickenStickSoundChance) {
            ResourceLocation location = null;
            if(ModConfig.tools.chickenStickSounds.length > 0) {
                location = new ResourceLocation(ModConfig.tools.chickenStickSounds[world.rand.nextInt(ModConfig.tools.chickenStickSounds.length)]);
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

    @Override
    public boolean hasEffect(ItemStack stack) {
        return isAngry(stack);
    }

    public boolean isAngry(ItemStack itemStack) {
        return itemStack.getTagCompound() != null && itemStack.getTagCompound().getBoolean("IsAngry");
    }

}
