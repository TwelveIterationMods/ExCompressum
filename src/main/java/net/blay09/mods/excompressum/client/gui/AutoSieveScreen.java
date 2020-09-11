package net.blay09.mods.excompressum.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.container.AutoSieveContainer;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class AutoSieveScreen extends ContainerScreen<AutoSieveContainer> {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/auto_sieve.png");

    public AutoSieveScreen(AutoSieveContainer container, PlayerInventory inv, ITextComponent title) {
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
        Minecraft.getInstance().getTextureManager().bindTexture(getBackgroundTexture());
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);

        AutoSieveTileEntityBase tileEntity = container.getTileEntity();
        if (tileEntity.isProcessing()) {
            blit(matrixStack, guiLeft + 32, guiTop + 36, 176, 0, (int) (tileEntity.getProgress() * 15f), 14);
        }
        if (tileEntity.isDisabledByRedstone()) {
            blit(matrixStack, guiLeft + 34, guiTop + 52, 176, 14, 15, 16);
        }

        renderEnergyBar(matrixStack);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        // Render No Mesh / Incorrect Mesh overlay
        AutoSieveTileEntityBase tileEntity = container.getTileEntity();
        if (tileEntity.getMeshStack().isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0, 0, 300);
            fill(matrixStack, 58, 16, 144, 71, 0x99000000);
            drawCenteredString(matrixStack, font, I18n.format("gui.excompressum:autoSieve.noMesh"), 101, 43 - font.FONT_HEIGHT / 2, 0xFFFFFFFF);
            matrixStack.pop();
        } else if (!tileEntity.isCorrectSieveMesh()) {
            matrixStack.push();
            matrixStack.translate(0, 0, 300);
            fill(matrixStack, 58, 16, 144, 71, 0x99000000);
            drawCenteredString(matrixStack, font, I18n.format("gui.excompressum:autoSieve.incorrectMesh"), 101, 43 - font.FONT_HEIGHT / 2, 0xFFFFFFFF);
            matrixStack.pop();
        }

        renderPowerTooltip(matrixStack, mouseX, mouseY);
    }

    protected ResourceLocation getBackgroundTexture() {
        return texture;
    }

    protected void renderEnergyBar(MatrixStack matrixStack) {
        AutoSieveTileEntityBase tileEntity = container.getTileEntity();
        float energyPercentage = tileEntity.getEnergyPercentage();
        blit(matrixStack, guiLeft + 152, guiTop + 8 + (70 - (int) (energyPercentage * 70)), 176 + 15, 0, 16, (int) (energyPercentage * 70));
    }

    protected void renderPowerTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (mouseX >= guiLeft + 152 && mouseX <= guiLeft + 167 && mouseY >= guiTop + 8 && mouseY <= guiTop + 77) {
            AutoSieveTileEntityBase tileEntity = container.getTileEntity();
            List<ITextComponent> tmpLines = new ArrayList<>();
            tmpLines.add(new TranslationTextComponent("tooltip.excompressum.consumingEnergyValue", tileEntity.getEnergyStored()));
            tmpLines.add(new TranslationTextComponent("tooltip.excompressum:consumingEnergy", tileEntity.getEffectiveEnergy()));
            func_243308_b(matrixStack, tmpLines, mouseX - guiLeft, mouseY - guiTop); // TODO duplicate code for all machines
        }
    }

}
