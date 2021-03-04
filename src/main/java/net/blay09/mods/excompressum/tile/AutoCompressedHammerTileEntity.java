package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.jei.LootTableUtils;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import java.util.Collection;
import java.util.Random;

public class AutoCompressedHammerTileEntity extends AutoHammerTileEntity {

    public AutoCompressedHammerTileEntity() {
        super(ModTileEntities.autoCompressedHammer);
    }

    @Override
    public int getEffectiveEnergy() {
        return ExCompressumConfig.COMMON.autoCompressedHammerEnergy.get();
    }

    @Override
    public float getEffectiveSpeed() {
        return (float) (ExCompressumConfig.COMMON.autoCompressedHammerSpeed.get() * getSpeedMultiplier());
    }

    @Override
    public boolean isRegistered(ItemStack itemStack) {
        RecipeManager recipeManager = ExCompressum.proxy.getRecipeManager(world);
        return ExRegistries.getCompressedHammerRegistry().isHammerable(recipeManager, itemStack);
    }

    @Override
    public Collection<ItemStack> rollHammerRewards(ItemStack itemStack, ItemStack toolItem, Random rand) {
        LootContext lootContext = LootTableUtils.buildLootContext(((ServerWorld) world), itemStack, rand);
        return CompressedHammerRegistry.rollHammerRewards(world, lootContext, itemStack);
    }

    @Override
    public boolean isHammerUpgrade(ItemStack itemStack) {
        if (itemStack.getItem() == ModItems.compressedHammerDiamond) {
            return true;
        }
        if (itemStack.getItem() == Compat.TCONSTRUCT_HAMMER) {
            CompoundNBT tagCompound = itemStack.getTag();
            if (tagCompound != null) {
                ListNBT traits = tagCompound.getList("Traits", Constants.NBT.TAG_STRING);
                for (INBT tag : traits) {
                    if (tag.getString().equalsIgnoreCase(Compat.TCONSTRUCT_TRAIT_SMASHINGII)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.excompressum.auto_compressed_hammer");
    }
}
