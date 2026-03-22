package net.blay09.mods.excompressum.client.gui;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.menu.AutoSieveMenu;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class AutoSieveScreen extends AbstractContainerScreen<AutoSieveMenu> {

    private static final Identifier texture = Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "textures/gui/auto_sieve.png");

    public AutoSieveScreen(AutoSieveMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        AbstractAutoSieveBlockEntity tileEntity = menu.getAutoSieve();
        if (tileEntity.isProcessing()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 32, topPos + 36, 176, 0, (int) (tileEntity.getProgress() * 15f), 14, 256, 256);
        }
        if (tileEntity.isDisabledByRedstone()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 34, topPos + 52, 176, 14, 15, 16, 256, 256);
        }

        renderEnergyBar(graphics);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        final var poseStack = guiGraphics.pose();
        // Render No Mesh / Incorrect Mesh overlay
        AbstractAutoSieveBlockEntity blockEntity = menu.getAutoSieve();
        if (blockEntity.getMeshStack().isEmpty()) {
            poseStack.pushMatrix();
            // TODO z 300
            guiGraphics.fill(58, 16, 144, 71, 0x99000000);
            guiGraphics.centeredText(font, I18n.get("gui.excompressum.auto_sieve.no_mesh"), 101, 43 - font.lineHeight / 2, 0xFFFFFFFF);
            poseStack.popMatrix();
        } else if (!blockEntity.isCorrectSieveMesh()) {
            poseStack.pushMatrix();
            // TODO z 300
            guiGraphics.fill(58, 16, 144, 71, 0x99000000);
            guiGraphics.centeredText(font, I18n.get("gui.excompressum.auto_sieve.incorrect_mesh"), 101, 43 - font.lineHeight / 2, 0xFFFFFFFF);
            poseStack.popMatrix();
        }

        renderPowerTooltip(guiGraphics, mouseX, mouseY);
    }

    protected void renderEnergyBar(GuiGraphicsExtractor guiGraphics) {
        AbstractAutoSieveBlockEntity tileEntity = menu.getAutoSieve();
        float energyPercentage = tileEntity.getEnergyPercentage();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 152, topPos + 8 + (70 - (int) (energyPercentage * 70)), 176 + 15, 0, 16, (int) (energyPercentage * 70), 256, 256);
    }

    protected void renderPowerTooltip(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        if (mouseX >= leftPos + 152 && mouseX <= leftPos + 167 && mouseY >= topPos + 8 && mouseY <= topPos + 77) {
            AbstractAutoSieveBlockEntity blockEntity = menu.getAutoSieve();
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("tooltip.excompressum.energyStored", blockEntity.getEnergyStored()));
            tooltip.add(Component.translatable("tooltip.excompressum.consumingEnergy", blockEntity.getEffectiveEnergy()));
            guiGraphics.setComponentTooltipForNextFrame(Minecraft.getInstance().font, tooltip, mouseX - leftPos, mouseY - topPos);
        }
    }

}
