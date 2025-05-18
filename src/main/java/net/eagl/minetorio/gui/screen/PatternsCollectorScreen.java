package net.eagl.minetorio.gui.screen;


import net.eagl.minetorio.gui.PatternsCollectorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class PatternsCollectorScreen extends AbstractContainerScreen<PatternsCollectorMenu> {


    public PatternsCollectorScreen(PatternsCollectorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {


    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

        // Встановити колір (білий для текстур)
        pGuiGraphics.setColor(1f, 1f, 1f, 1f);

        // Вказати текстуру фону GUI
        pGuiGraphics.blit(
                getGuiTexture(),
                leftPos, topPos,
                0, 0,
                imageWidth, imageHeight
        );
    }

    private ResourceLocation getGuiTexture() {
        return ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/patterns_collector.png");
    }
}
