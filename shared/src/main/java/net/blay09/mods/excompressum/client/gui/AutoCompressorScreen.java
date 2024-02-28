package net.blay09.mods.excompressum.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.menu.AutoCompressorMenu;
import net.blay09.mods.excompressum.block.entity.AutoCompressorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        AutoCompressorBlockEntity tileEntity = menu.getAutoCompressor();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        guiGraphics.blit(texture, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        if (tileEntity.isProcessing()) {
            guiGraphics.blit(texture, leftPos + 69, topPos + 9, 176, 0, (int) (tileEntity.getProgress() * 15f), 14);
            guiGraphics.blit(texture, leftPos + 69, topPos + 36, 176, 0, (int) (tileEntity.getProgress() * 15f), 14);
            guiGraphics.blit(texture, leftPos + 69, topPos + 63, 176, 0, (int) (tileEntity.getProgress() * 15f), 14);
        }
        if (tileEntity.isDisabledByRedstone()) {
            guiGraphics.blit(texture, leftPos + 72, topPos + 24, 176, 14, 15, 16);
            guiGraphics.blit(texture, leftPos + 72, topPos + 51, 176, 14, 15, 16);
        }
        float energyPercentage = tileEntity.getEnergyPercentage();
        guiGraphics.blit(texture, leftPos + 152, topPos + 8 + (70 - (int) (energyPercentage * 70)), 176 + 15, 0, 16, (int) (energyPercentage * 70));
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (mouseX >= leftPos + 152 && mouseX <= leftPos + 167 && mouseY >= topPos + 8 && mouseY <= topPos + 77) {
            AutoCompressorBlockEntity blockEntity = menu.getAutoCompressor();
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("excompressum.tooltip.energyStored", blockEntity.getEnergyStorage().getEnergy()));
            tooltip.add(Component.translatable("excompressum.tooltip.consumingEnergy", blockEntity.getEffectiveEnergy()));
            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, tooltip, mouseX - leftPos, mouseY - topPos);
        }
    }

}
