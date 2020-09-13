package net.blay09.mods.excompressum.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.container.AutoHammerContainer;
import net.blay09.mods.excompressum.tile.AutoHammerTileEntity;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class AutoHammerScreen extends ContainerScreen<AutoHammerContainer> {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/auto_hammer.png");

    public AutoHammerScreen(AutoHammerContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        xSize = 176;
        ySize = 166;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        func_230459_a_(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);

        AutoHammerTileEntity tileEntity = container.getTileEntity();

        if (tileEntity.isProcessing()) {
            blit(matrixStack, guiLeft + 32, guiTop + 36, 176, 0, (int) (tileEntity.getProgress() * 15f), 14);
        }
        if (tileEntity.isDisabledByRedstone()) {
            blit(matrixStack, guiLeft + 44, guiTop + 48, 176, 14, 15, 16);
        }

        float energyPercentage = tileEntity.getEnergyPercentage();
        blit(matrixStack, guiLeft + 152, guiTop + 8 + (70 - (int) (energyPercentage * 70)), 176 + 15, 0, 16, (int) (energyPercentage * 70));
    }


    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (mouseX >= guiLeft + 152 && mouseX <= guiLeft + 167 && mouseY >= guiTop + 8 && mouseY <= guiTop + 77) {
            AutoHammerTileEntity tileEntity = container.getTileEntity();
            List<ITextComponent> tooltip = new ArrayList<>();
            tooltip.add(Messages.lang("tooltip.energyStored", tileEntity.getEnergyStorage().getEnergyStored()));
            tooltip.add(Messages.lang("tooltip.consumingEnergy", tileEntity.getEffectiveEnergy()));
            func_243308_b(matrixStack, tooltip, mouseX - guiLeft, mouseY - guiTop);
        }
    }

}
