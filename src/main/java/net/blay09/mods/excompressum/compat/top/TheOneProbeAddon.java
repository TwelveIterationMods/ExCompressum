package net.blay09.mods.excompressum.compat.top;

import com.google.common.base.Function;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.*;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.tile.*;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.InterModComms;

import javax.annotation.Nullable;

public class TheOneProbeAddon  {

    public static void register() {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", TopInitializer::new);
    }

    public static class TopInitializer implements Function<ITheOneProbe, Void> {
        @Nullable
        @Override
        public Void apply(@Nullable ITheOneProbe top) {
            if (top != null) {
                top.registerProvider(new ProbeInfoProvider());
            }
            return null;
        }
    }

    public static class ProbeInfoProvider implements IProbeInfoProvider {
        @Override
        public String getID() {
            return ExCompressum.MOD_ID;
        }

        @Override
        public void addProbeInfo(ProbeMode mode, IProbeInfo info, PlayerEntity playerEntity, World world, BlockState state, IProbeHitData data) {
            if (state.getBlock() instanceof AutoSieveBlock) {
                AutoSieveTileEntityBase tileEntity = tryGetTileEntity(world, data.getPos(), AutoSieveTileEntityBase.class);
                if (tileEntity != null) {
                    addAutoSieveInfo(tileEntity, mode, info);
                }
            } else if (state.getBlock() instanceof AutoHammerBlock) {
                AutoHammerTileEntity tileEntity = tryGetTileEntity(world, data.getPos(), AutoHammerTileEntity.class);
                if (tileEntity != null) {
                    addAutoHammerInfo(tileEntity, mode, info);
                }
            } else if (state.getBlock() instanceof BaitBlock) {
                BaitTileEntity tileEntity = tryGetTileEntity(world, data.getPos(), BaitTileEntity.class);
                if (tileEntity != null) {
                    addBaitInfo(tileEntity, mode, info);
                }
            } else if (state.getBlock() instanceof WoodenCrucibleBlock) {
                WoodenCrucibleTileEntity tileEntity = tryGetTileEntity(world, data.getPos(), WoodenCrucibleTileEntity.class);
                if (tileEntity != null) {
                    addWoodenCrucibleInfo(tileEntity, mode, info);
                }
            } else if (state.getBlock() instanceof HeavySieveBlock) {
                HeavySieveTileEntity tileEntity = tryGetTileEntity(world, data.getPos(), HeavySieveTileEntity.class);
                if (tileEntity != null) {
                    addHeavySieveInfo(tileEntity, mode, info);
                }
            }
        }

        private void addAutoSieveInfo(AutoSieveTileEntityBase tileEntity, ProbeMode mode, IProbeInfo info) {
            if (tileEntity.getCustomSkin() != null) {
                info.text(new TranslationTextComponent("excompressum.tooltip.sieveSkin", tileEntity.getCustomSkin().getName()));
            }
            if (tileEntity.getFoodBoost() > 1f) {
                info.text(new TranslationTextComponent("excompressum.tooltip.speedBoost", tileEntity.getFoodBoost()));
            }
            if (tileEntity.getEffectiveLuck() > 1) {
                info.text(new TranslationTextComponent("excompressum.tooltip.luckBonus", tileEntity.getEffectiveLuck() - 1));
            }
        }

        private void addAutoHammerInfo(AutoHammerTileEntity tileEntity, ProbeMode mode, IProbeInfo info) {
            if (tileEntity.getEffectiveLuck() > 1) {
                info.text(new TranslationTextComponent("excompressum.tooltip.luckBonus", tileEntity.getEffectiveLuck() - 1));
            }
        }

        private void addBaitInfo(BaitTileEntity tileEntity, ProbeMode mode, IProbeInfo info) {
            EnvironmentalCondition environmentalStatus = tileEntity.checkSpawnConditions(true);
            if (environmentalStatus == EnvironmentalCondition.CanSpawn) {
                info.text(new TranslationTextComponent("excompressum.tooltip.baitTooClose"));
                info.text(new TranslationTextComponent("excompressum.tooltip.baitTooClose2"));
            } else {
                TranslationTextComponent statusText = new TranslationTextComponent(environmentalStatus.langKey);
                statusText.mergeStyle(TextFormatting.RED);
                info.text(statusText);
            }
        }

        private void addWoodenCrucibleInfo(WoodenCrucibleTileEntity tileEntity, ProbeMode mode, IProbeInfo info) {
            if (tileEntity.getSolidVolume() > 0f) {
                info.text(new TranslationTextComponent("excompressum.tooltip.solidVolume", (int) tileEntity.getSolidVolume()));
            }
            if (tileEntity.getFluidTank().getFluidAmount() > 0f) {
                info.text(new TranslationTextComponent("excompressum.tooltip.fluidVolume", (int) tileEntity.getFluidTank().getFluidAmount()));
            }
        }

        private void addHeavySieveInfo(HeavySieveTileEntity tileEntity, ProbeMode mode, IProbeInfo info) {
            if(tileEntity.getProgress() > 0f) {
                info.text(new TranslationTextComponent("excompressum.tooltip.sieveProgress", (int) (tileEntity.getProgress() * 100) + "%"));
            }
            ItemStack meshStack = tileEntity.getMeshStack();
            if (!meshStack.isEmpty()) {
                if(ExNihilo.getInstance().doMeshesHaveDurability()) {
                    info.text(new TranslationTextComponent("excompressum.tooltip.sieveMesh", meshStack.getDisplayName(), meshStack.getMaxDamage() - meshStack.getDamage(), meshStack.getMaxDamage()));
                } else {
                    info.text(meshStack.getDisplayName());
                }
            } else {
                info.text(new TranslationTextComponent("excompressum.tooltip.sieveNoMesh"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T extends TileEntity> T tryGetTileEntity(World world, BlockPos pos, Class<T> tileClass) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileClass.isAssignableFrom(tileEntity.getClass())) {
            return (T) tileEntity;
        }
        return null;
    }

}
