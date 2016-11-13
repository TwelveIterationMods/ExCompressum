package net.blay09.mods.excompressum.compat.tconstruct;

import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.item.ItemDoubleCompressedDiamondHammer;
import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.BlockEvent;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.ModifyBuilder;
import tconstruct.library.tools.AbilityHelper;

import java.util.Collection;

@SuppressWarnings("unused")
public class TConstructAddon implements IAddon {

    private boolean enableModifiers;

    @Override
    public void loadConfig(Configuration config) {
        enableModifiers = config.getBoolean("Enable Smashing II Modifier", "compat.tconstruct", true, "If set to true, adding a double compressed diamond hammer will add the Smashing II modifier to a Tinkers Construct tool, which allows smashing of compressed blocks.");
    }

    @Override
    public void postInit() {
        if(enableModifiers) {
            ItemDoubleCompressedDiamondHammer.registerRecipes();
            ModifyBuilder.registerModifier(new ModSmashingII(new ItemStack[]{new ItemStack(ModItems.doubleCompressedDiamondHammer, 1, 0)}));
            TConstructRegistry.registerActiveToolMod(new ActiveSmashingMod());
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {}

    @SubscribeEvent
    public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        if(event.harvester == null) {
            return;
        }
        ItemStack itemStack = event.harvester.getHeldItem();
        if(itemStack == null) {
            return;
        }
        NBTTagCompound tags = itemStack.getTagCompound().getCompoundTag("InfiTool");
        if(tags.getBoolean(ModSmashingII.NAME)) {
            if(event.world.isRemote || event.isSilkTouching) {
                return;
            }
            Block block = event.world.getBlock(event.x, event.y, event.z);
            int metadata = event.world.getBlockMetadata(event.x, event.y, event.z);
            Collection<Smashable> rewards = CompressedHammerRegistry.getSmashables(block, metadata);
            if (rewards == null || rewards.isEmpty()) {
                rewards = HammerRegistry.getRewards(block, metadata);
                if(rewards == null || rewards.isEmpty()) {
                    return;
                }
            }
            event.drops.clear();
            event.dropChance = 1f;
            for (Smashable reward : rewards) {
                if (event.world.rand.nextFloat() <= reward.chance + (reward.luckMultiplier * event.fortuneLevel)) {
                    event.drops.add(new ItemStack(reward.item, 1, reward.meta));
                }
            }
            AbilityHelper.onBlockChanged(itemStack, event.world, block, event.x, event.y, event.z, event.harvester, AbilityHelper.random);
        }
    }
}
