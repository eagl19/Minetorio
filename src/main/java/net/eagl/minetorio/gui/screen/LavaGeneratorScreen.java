package net.eagl.minetorio.gui.screen;

import net.eagl.minetorio.gui.LavaGeneratorMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class LavaGeneratorScreen extends AbstractContainerScreen<LavaGeneratorMenu> {

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/lava_generator.png");

    public LavaGeneratorScreen(LavaGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 222;
        this.imageWidth = 176;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.setColor(1f, 1f, 1f, 1f);
        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int energy = menu.getEnergy();
        int maxEnergy = menu.getMaxEnergyStorage();
        if (maxEnergy > 0 && energy > 0) {
            int barHeight = 128;
            int energyHeight = (int) ((float) energy / maxEnergy * barHeight);

            pGuiGraphics.blit(GUI_TEXTURE,
                    leftPos + 155,
                    topPos + 5 + (barHeight - energyHeight),
                    178,
                    5 + barHeight - energyHeight,
                    12,
                    energyHeight
            );
        }
        if(pMouseX > leftPos + 155 && pMouseX < leftPos + 167 && pMouseY > topPos + 5 && pMouseY < topPos + 133) {
            pGuiGraphics.renderTooltip(this.font, Component.literal(String.valueOf(energy)).withStyle(ChatFormatting.GOLD), pMouseX, pMouseY);
        }
        int lava = menu.getLava();
        int maxLava = menu.getMaxLavaStorage();

        if (maxLava > 0 && lava > 0) {
            int barHeight = 128;
            int learnHeight = (int) ((float) lava / maxLava * barHeight);

            pGuiGraphics.blit(GUI_TEXTURE,
                    leftPos + 138,
                    topPos + 5 + (barHeight - learnHeight),
                    192,
                    5 + barHeight - learnHeight,
                    12,
                    learnHeight
            );
        }

        if(pMouseX > leftPos + 138 && pMouseX < leftPos + 150 && pMouseY > topPos + 5 && pMouseY < topPos + 133) {
            pGuiGraphics.renderTooltip(this.font, Component.literal(String.valueOf(lava)).withStyle(ChatFormatting.GOLD), pMouseX, pMouseY);
        }


        int maxProduce = menu.getMaxProduce();
        int produce = maxProduce - menu.getProduce();

        if (maxProduce > 0 && produce > 0) {
            int barWidth = 102;
            int learWidth = (int) ((float) produce / maxProduce * barWidth);

            pGuiGraphics.blit(GUI_TEXTURE,
                    leftPos + 28,
                    topPos + 56,
                    28,
                    225,
                    learWidth,
                    24
            );
        }
        if(pMouseX > leftPos + 28 && pMouseX < leftPos + 130 && pMouseY > topPos + 56 && pMouseY < topPos + 80) {
            double progress = 0;
            if (maxProduce > 0) {
                progress = (double) produce / maxProduce * 100;
            }
            DecimalFormat df = new DecimalFormat("0.00");
            String percentage = df.format(progress) + "%";
            pGuiGraphics.renderTooltip(this.font, Component.literal(percentage).withStyle(ChatFormatting.GREEN), pMouseX, pMouseY);
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {

    }
}
