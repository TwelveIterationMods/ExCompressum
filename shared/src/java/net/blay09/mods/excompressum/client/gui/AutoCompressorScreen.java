package net.blay09.mods.excompressum.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.menu.AutoCompressorMenu;
import net.blay09.mods.excompressum.block.entity.AutoCompressorBlockEntity;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class AutoCompressorScreen extends AbstractContainerScreen<AutoCompressorMenu> {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/auto_compressor.png");

    public AutoCompressorScreen(AutoCompressorMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        AutoCompressorBlockEntity tileEntity = menu.getTileEntity();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, texture);
        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        if (tileEntity.isProcessing()) {
            blit(poseStack, leftPos + 69, topPos + 9, 176, 0, (int) (tileEntity.getProgress() * 15f), 14);
            blit(poseStack, leftPos + 69, topPos + 36, 176, 0, (int) (tileEntity.getProgress() * 15f), 14);
            blit(poseStack, leftPos + 69, topPos + 63, 176, 0, (int) (tileEntity.getProgress() * 15f), 14);
        }
        if (tileEntity.isDisabledByRedstone()) {
            blit(poseStack, leftPos + 72, topPos + 24, 176, 14, 15, 16);
            blit(poseStack, leftPos + 72, topPos + 51, 176, 14, 15, 16);
        }
        float energyPercentage = tileEntity.getEnergyPercentage();
        blit(poseStack, leftPos + 152, topPos + 8 + (70 - (int) (energyPercentage * 70)), 176 + 15, 0, 16, (int) (energyPercentage * 70));
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        if (mouseX >= leftPos + 152 && mouseX <= leftPos + 167 && mouseY >= topPos + 8 && mouseY <= topPos + 77) {
            AutoCompressorBlockEntity blockEntity = menu.getTileEntity();
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Messages.lang("tooltip.energyStored", blockEntity.getEnergyStorage().getEnergy()));
            tooltip.add(Messages.lang("tooltip.consumingEnergy", blockEntity.getEffectiveEnergy()));
            renderComponentTooltip(poseStack, tooltip, mouseX - leftPos, mouseY - topPos);
        }
    }

}
