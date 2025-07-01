package net.eagl.minetorio.gui.screen;

import net.eagl.minetorio.gui.menu.PatternsCollectorMenu;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.server.PatternCollectorTeleport;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;


public class PatternsCollectorScreen extends AbstractContainerScreen<PatternsCollectorMenu> {

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/teleporter.png");

    public PatternsCollectorScreen(PatternsCollectorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 222;
        this.imageWidth = 176;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics,pMouseX,pMouseY);

    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {

    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.setColor(1f, 1f, 1f, 1f);
        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);


    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if(isHovering(71,53,35,35, pMouseX, pMouseY)){
            MinetorioNetwork.CHANNEL.sendToServer(new PatternCollectorTeleport(menu.getBlockEntity().getBlockPos()));
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
