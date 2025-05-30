package net.eagl.minetorio.gui.screen;

import net.eagl.minetorio.gui.ResearcherMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ResearcherScreen extends AbstractContainerScreen<ResearcherMenu> {

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/researcher.png");

    public ResearcherScreen(ResearcherMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 256;
        this.imageWidth = 176;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.setColor(1f, 1f, 1f, 1f);
        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int energy = menu.getData().get(0);
        int maxEnergy = menu.getData().get(1);

        if (maxEnergy > 0 && energy > 0) {
            int barHeight = 100;
            int energyHeight = (int) ((float) energy / maxEnergy * barHeight);

            pGuiGraphics.blit(GUI_TEXTURE,
                    leftPos + 155,
                    topPos + 33 + (barHeight - energyHeight),
                    178,
                    33 + barHeight - energyHeight,
                    12,
                    energyHeight
            );
        }

        int learn = menu.getData().get(2);
        int maxLearn = menu.getData().get(3);

        if (maxLearn > 0 && learn > 0) {
            int barHeight = 100;
            int learnHeight = (int) ((float) learn / maxLearn * barHeight);

            pGuiGraphics.blit(GUI_TEXTURE,
                    leftPos + 10,
                    topPos + 33 + (barHeight - learnHeight),
                    193,
                    33 + barHeight - learnHeight,
                    12,
                    learnHeight
            );
        }

    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {

    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
