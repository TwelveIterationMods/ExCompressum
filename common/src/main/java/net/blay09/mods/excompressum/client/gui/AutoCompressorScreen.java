package net.blay09.mods.excompressum.client.gui;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.menu.AutoCompressorMenu;
import net.blay09.mods.excompressum.block.entity.AutoCompressorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class AutoCompressorScreen extends AbstractContainerScreen<AutoCompressorMenu> {

    private static final Identifier texture = Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "textures/gui/auto_compressor.png");

    public AutoCompressorScreen(AutoCompressorMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        AutoCompressorBlockEntity tileEntity = menu.getAutoCompressor();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        if (tileEntity.isProcessing()) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 69, topPos + 9, 176, 0, (int) (tileEntity.getProgress() * 15f), 14, 256, 256);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 69, topPos + 36, 176, 0, (int) (tileEntity.getProgress() * 15f), 14, 256, 256);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 69, topPos + 63, 176, 0, (int) (tileEntity.getProgress() * 15f), 14, 256, 256);
        }
        if (tileEntity.isDisabledByRedstone()) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 72, topPos + 24, 176, 14, 15, 16, 256, 256);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 72, topPos + 51, 176, 14, 15, 16, 256, 256);
        }
        float energyPercentage = tileEntity.getEnergyPercentage();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 152, topPos + 8 + (70 - (int) (energyPercentage * 70)), 176 + 15, 0, 16, (int) (energyPercentage * 70), 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (mouseX >= leftPos + 152 && mouseX <= leftPos + 167 && mouseY >= topPos + 8 && mouseY <= topPos + 77) {
            AutoCompressorBlockEntity blockEntity = menu.getAutoCompressor();
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("tooltip.excompressum.energyStored", blockEntity.getEnergyStorage().getEnergy()));
            tooltip.add(Component.translatable("tooltip.excompressum.consumingEnergy", blockEntity.getEffectiveEnergy()));
            guiGraphics.setComponentTooltipForNextFrame(Minecraft.getInstance().font, tooltip, mouseX - leftPos, mouseY - topPos);
        }
    }

}
