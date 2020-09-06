package net.blay09.mods.excompressum.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.container.AutoCompressorContainer;
import net.blay09.mods.excompressum.tile.AutoCompressorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class AutoCompressorScreen extends ContainerScreen<AutoCompressorContainer> {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/auto_compressor.png");

    public AutoCompressorScreen(AutoCompressorContainer container, PlayerInventory inv, ITextComponent title) {
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
        AutoCompressorTileEntity tileEntity = container.getTileEntity();
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);

        if (tileEntity.isProcessing()) {
            blit(matrixStack, guiLeft + 69, guiTop + 9, 176, 0, (int) (tileEntity.getProgress() * 15f), 14);
            blit(matrixStack, guiLeft + 69, guiTop + 36, 176, 0, (int) (tileEntity.getProgress() * 15f), 14);
            blit(matrixStack, guiLeft + 69, guiTop + 63, 176, 0, (int) (tileEntity.getProgress() * 15f), 14);
        }
        if (tileEntity.isDisabledByRedstone()) {
            blit(matrixStack, guiLeft + 72, guiTop + 24, 176, 14, 15, 16);
            blit(matrixStack, guiLeft + 72, guiTop + 51, 176, 14, 15, 16);
        }
        float energyPercentage = tileEntity.getEnergyPercentage();
        blit(matrixStack, guiLeft + 152, guiTop + 8 + (70 - (int) (energyPercentage * 70)), 176 + 15, 0, 16, (int) (energyPercentage * 70));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        AutoCompressorTileEntity tileEntity = container.getTileEntity();
        if (mouseX >= guiLeft + 152 && mouseX <= guiLeft + 167 && mouseY >= guiTop + 8 && mouseY <= guiTop + 77) {
            List<ITextComponent> tmpLines = new ArrayList<>();
            tmpLines.add(new TranslationTextComponent("tooltip.excompressum.consumingEnergyValue", tileEntity.getEnergyStorage().getEnergyStored());
            tmpLines.add(new TranslationTextComponent("tooltip.excompressum:consumingEnergy", tileEntity.getEffectiveEnergy()));
            func_243308_b(matrixStack, tmpLines, mouseX - guiLeft, mouseY - guiTop);
        }
    }

}
