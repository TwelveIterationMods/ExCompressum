package net.blay09.mods.excompressum.client.gui;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.container.ContainerAutoSieve;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.tile.TileEntityAutoSieveBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiAutoSieve extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/autoSieve.png");
    private TileEntityAutoSieveBase tileEntity;

    public GuiAutoSieve(InventoryPlayer inventoryPlayer, TileEntityAutoSieveBase tileEntity) {
        super(new ContainerAutoSieve(inventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
        xSize = 176;
        ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (tileEntity.isProcessing()) {
            drawTexturedModalRect(guiLeft + 32, guiTop + 36, 176, 0, (int) (tileEntity.getProgress() * 15f), 15);
        }

        float energyPercentage = tileEntity.getEnergyPercentage();
        drawTexturedModalRect(guiLeft + 152, guiTop + 8 + (70 - (int) (energyPercentage * 70)), 176 + 15, 0, 16, (int) (energyPercentage * 70));
    }

    private static final List<String> tmpLines = Lists.newArrayList();
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (mouseX >= guiLeft + 152 && mouseX <= guiLeft + 167 && mouseY >= guiTop + 8 && mouseY <= guiTop + 77) {
            tmpLines.clear();
            tmpLines.add(tileEntity.getEnergyStored() + " RF");
            tmpLines.add(I18n.format("tooltip.excompressum:consumingEnergy", tileEntity.getEffectiveEnergy()));
            drawHoveringText(tmpLines, mouseX - guiLeft, mouseY - guiTop);
        }
        if(tileEntity.getMeshStack() == null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 300);
            drawRect(58, 16, 144, 71, 0x99000000);
            drawCenteredString(fontRendererObj, I18n.format("gui.excompressum:autoSieve.noMesh"), 101, 43 - fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFFFF);
            GlStateManager.popMatrix();
        } else if(!tileEntity.isCorrectSieveMesh()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 300);
            drawRect(58, 16, 144, 71, 0x99000000);
            drawCenteredString(fontRendererObj, I18n.format("gui.excompressum:autoSieve.incorrectMesh"), 101, 43 - fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFFFF);
            GlStateManager.popMatrix();
        }
    }

}
