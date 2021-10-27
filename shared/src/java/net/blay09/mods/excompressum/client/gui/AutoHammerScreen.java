package net.blay09.mods.excompressum.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.menu.AutoHammerMenu;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class AutoHammerScreen extends AbstractContainerScreen<AutoHammerMenu> {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/auto_hammer.png");

    public AutoHammerScreen(AutoHammerMenu container, Inventory inventory, Component title) {
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
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, texture);
        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        AutoHammerBlockEntity blockEntity = menu.getAutoHammer();

        if (blockEntity.isProcessing()) {
            blit(poseStack, leftPos + 32, topPos + 36, 176, 0, (int) (blockEntity.getProgress() * 15f), 14);
        }
        if (blockEntity.isDisabledByRedstone()) {
            blit(poseStack, leftPos + 44, topPos + 48, 176, 14, 15, 16);
        }

        float energyPercentage = blockEntity.getEnergyPercentage();
        blit(poseStack, leftPos + 152, topPos + 8 + (70 - (int) (energyPercentage * 70)), 176 + 15, 0, 16, (int) (energyPercentage * 70));
    }


    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        if (mouseX >= leftPos + 152 && mouseX <= leftPos + 167 && mouseY >= topPos + 8 && mouseY <= topPos + 77) {
            AutoHammerBlockEntity blockEntity = menu.getAutoHammer();
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Messages.lang("tooltip.energyStored", blockEntity.getEnergyStorage().getEnergy()));
            tooltip.add(Messages.lang("tooltip.consumingEnergy", blockEntity.getEffectiveEnergy()));
            renderComponentTooltip(poseStack, tooltip, mouseX - leftPos, mouseY - topPos);
        }
    }

}
