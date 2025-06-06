package net.eagl.minetorio.gui.screen;

import net.eagl.minetorio.gui.RemovableItemIcon;
import net.eagl.minetorio.gui.ResearcherMenu;
import net.eagl.minetorio.gui.slot.FlaskSlot;
import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ResearcherScreen extends AbstractContainerScreen<ResearcherMenu> {

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/researcher.png");

    public ResearcherScreen(ResearcherMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 222;
        this.imageWidth = 176;
    }


    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new RemovableItemIcon(
                leftPos + 20, topPos + 40,
                new ItemStack(MinetorioItems.PATTERN_VOID.get()),
                () -> openFlaskAction(),
                () -> removeFlaskAction()
        ));
    }

    private void removeFlaskAction() {

    }

    private void openFlaskAction() {
        Minecraft.getInstance().setScreen(new TechnologyTreeScreen());
    }


    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.setColor(1f, 1f, 1f, 1f);
        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int energy = menu.getData().get(0);
        int maxEnergy = menu.getData().get(1);

        if (maxEnergy > 0 && energy > 0) {
            int barHeight = 90;
            int energyHeight = (int) ((float) energy / maxEnergy * barHeight);

            pGuiGraphics.blit(GUI_TEXTURE,
                    leftPos + 155,
                    topPos + 43 + (barHeight - energyHeight),
                    178,
                    43 + barHeight - energyHeight,
                    12,
                    energyHeight
            );
        }

        int water = menu.getData().get(2);
        int maxWater = menu.getData().get(3);

        if (maxWater > 0 && water > 0) {
            int barHeight = 90;
            int learnHeight = (int) ((float) water / maxWater * barHeight);

            pGuiGraphics.blit(GUI_TEXTURE,
                    leftPos + 138,
                    topPos + 43 + (barHeight - learnHeight),
                    204,
                    43 + barHeight - learnHeight,
                    12,
                    learnHeight
            );
        }
        int lava = menu.getData().get(4);
        int maxLava = menu.getData().get(5);

        if (maxLava > 0 && lava > 0) {
            int barHeight = 90;
            int learnHeight = (int) ((float) lava / maxLava * barHeight);

            pGuiGraphics.blit(GUI_TEXTURE,
                    leftPos + 121,
                    topPos + 43 + (barHeight - learnHeight),
                    191,
                    43 + barHeight - learnHeight,
                    12,
                    learnHeight
            );
        }

        int learn = menu.getData().get(6);
        int maxLearn = menu.getData().get(7);

        if (maxLearn > 0 && learn > 0) {
            int barWidth = 160;
            int learWidth = (int) ((float) learn / maxLearn * barWidth);

            pGuiGraphics.blit(GUI_TEXTURE,
                    leftPos + 8,
                    topPos + 7,
                    8,
                    225,
                    learWidth,
                    12
            );
        }

    }



    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {

        for (Slot slot : menu.slots) {
            if (slot instanceof FlaskSlot flask && !flask.isVisible()) {
               pGuiGraphics.renderItem(new ItemStack(flask.getFlask()),slot.x, slot.y);
                pGuiGraphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, 0x88000000);
            }
        }

    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);


    }
}
