package net.blay09.mods.excompressum.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.menu.AutoSieveMenu;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class AutoSieveScreen extends AbstractContainerScreen<AutoSieveMenu> {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/auto_sieve.png");

    public AutoSieveScreen(AutoSieveMenu container, Inventory inv, Component title) {
        super(container, inv, title);
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
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, getBackgroundTexture());
        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        AbstractAutoSieveBlockEntity tileEntity = menu.getAutoSieve();
        if (tileEntity.isProcessing()) {
            blit(poseStack, leftPos + 32, topPos + 36, 176, 0, (int) (tileEntity.getProgress() * 15f), 14);
        }
        if (tileEntity.isDisabledByRedstone()) {
            blit(poseStack, leftPos + 34, topPos + 52, 176, 14, 15, 16);
        }

        renderEnergyBar(poseStack);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        // Render No Mesh / Incorrect Mesh overlay
        AbstractAutoSieveBlockEntity blockEntity = menu.getAutoSieve();
        if (blockEntity.getMeshStack().isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0, 0, 300);
            fill(poseStack, 58, 16, 144, 71, 0x99000000);
            drawCenteredString(poseStack, font, I18n.get("excompressum.gui.autoSieve.noMesh"), 101, 43 - font.lineHeight / 2, 0xFFFFFFFF);
            poseStack.popPose();
        } else if (!blockEntity.isCorrectSieveMesh()) {
            poseStack.pushPose();
            poseStack.translate(0, 0, 300);
            fill(poseStack, 58, 16, 144, 71, 0x99000000);
            drawCenteredString(poseStack, font, I18n.get("excompressum.gui.autoSieve.incorrectMesh"), 101, 43 - font.lineHeight / 2, 0xFFFFFFFF);
            poseStack.popPose();
        }

        renderPowerTooltip(poseStack, mouseX, mouseY);
    }

    protected ResourceLocation getBackgroundTexture() {
        return texture;
    }

    protected void renderEnergyBar(PoseStack poseStack) {
        AbstractAutoSieveBlockEntity tileEntity = menu.getAutoSieve();
        float energyPercentage = tileEntity.getEnergyPercentage();
        blit(poseStack, leftPos + 152, topPos + 8 + (70 - (int) (energyPercentage * 70)), 176 + 15, 0, 16, (int) (energyPercentage * 70));
    }

    protected void renderPowerTooltip(PoseStack poseStack, int mouseX, int mouseY) {
        if (mouseX >= leftPos + 152 && mouseX <= leftPos + 167 && mouseY >= topPos + 8 && mouseY <= topPos + 77) {
            AbstractAutoSieveBlockEntity blockEntity = menu.getAutoSieve();
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Messages.lang("tooltip.energyStored", blockEntity.getEnergyStored()));
            tooltip.add(Messages.lang("tooltip.consumingEnergy", blockEntity.getEffectiveEnergy()));
            renderComponentTooltip(poseStack, tooltip, mouseX - leftPos, mouseY - topPos);
        }
    }

}
