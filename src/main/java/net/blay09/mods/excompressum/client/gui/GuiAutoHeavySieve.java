package net.blay09.mods.excompressum.client.gui;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.container.ContainerAutoCompressedHammer;
import net.blay09.mods.excompressum.container.ContainerAutoHeavySieve;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressedHammer;
import net.blay09.mods.excompressum.tile.TileEntityAutoHeavySieve;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiAutoHeavySieve extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/autoHeavySieve.png");
    private TileEntityAutoHeavySieve tileEntity;

    public GuiAutoHeavySieve(InventoryPlayer inventoryPlayer, TileEntityAutoHeavySieve tileEntity) {
        super(new ContainerAutoHeavySieve(inventoryPlayer, tileEntity));
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

        float energyPerc = tileEntity.getEnergyPercentage();
        drawTexturedModalRect(guiLeft + 152, guiTop + 8 + (70 - (int) (energyPerc * 70)), 176 + 15, 0, 16, (int) (energyPerc * 70));
    }

    private static final List<String> tmpLines = Lists.newArrayList();
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (mouseX >= guiLeft + 152 && mouseX <= guiLeft + 167 && mouseY >= guiTop + 8 && mouseY <= guiTop + 77) {
            tmpLines.clear();
            tmpLines.add(tileEntity.getEnergyStored(null) + " RF");
            tmpLines.add("Consuming " + tileEntity.getEffectiveEnergy() + " RF/t");
            func_146283_a(tmpLines, mouseX - guiLeft, mouseY - guiTop);
        }
    }

}
