package net.eagl.minetorio.gui.screen;

import net.eagl.minetorio.gui.widget.ItemIconWidget;
import net.eagl.minetorio.gui.widget.RemovableItemIcon;
import net.eagl.minetorio.gui.ResearcherMenu;
import net.eagl.minetorio.gui.slot.FlaskSlot;
import net.eagl.minetorio.util.Technology;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ResearcherScreen extends AbstractContainerScreen<ResearcherMenu> {

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/researcher.png");

    private final Inventory playerInventory;

    public ResearcherScreen(ResearcherMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 222;
        this.imageWidth = 176;
        this.playerInventory = pPlayerInventory;
    }

    public void updateTechnologies() {
        this.clearWidgets();
        this.init();
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(new ItemIconWidget(
                leftPos + 8,
                topPos + 60,
                new ItemStack(getTechList().get(0).getDisplayIcon())
        ));

        for (int i = 1; i < 10; i++) {
            Technology currentTech = getTechList().get(i);
            int currentI = i;
            this.addRenderableWidget(new RemovableItemIcon(
                    leftPos - 10 + i * 18, topPos + 23,
                    new ItemStack(currentTech.getDisplayIcon()),
                    () -> openFlaskAction(currentI, currentTech),
                    () -> removeFlaskAction(currentI)
            ));
        }
    }

    private void removeFlaskAction(int currentTech) {
        if(!getTechList().get(currentTech).equals(Technology.EMPTY)){

            List<Technology> list = new ArrayList<>(menu.getTechList());

            list.set(currentTech,Technology.EMPTY);

            int size=list.size();
            if(currentTech + 1 < list.size()) {
                for (int i = currentTech +1; i < size; i++){
                    if(!list.get(i).equals(Technology.EMPTY) && !list.get(i).canLearn(list)){
                        list.set(i,Technology.EMPTY);
                    }
                }
            }
            list.sort((a, b) -> {
                if (a == Technology.EMPTY && b != Technology.EMPTY) return 1;
                if (a != Technology.EMPTY && b == Technology.EMPTY) return -1;
                return 0;
            });

            menu.setTechList(list);
            updateTechnologies();
        }
    }

    private void openFlaskAction(int tech, Technology currentTech) {

        Minecraft.getInstance().setScreen(new TechnologyTreeScreen(menu, this.playerInventory, this.title, currentTech, tech));

    }

    private List<Technology> getTechList() {
        return menu.getTechList();
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.setColor(1f, 1f, 1f, 1f);
        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);


        int energy = menu.getEnergy();
        int maxEnergy = menu.getMaxEnergyStorage();
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
        if(pMouseX > leftPos + 155 && pMouseX < leftPos + 167 && pMouseY > topPos + 44 && pMouseY < topPos + 134) {
            pGuiGraphics.renderTooltip(this.font, Component.literal(String.valueOf(energy)).withStyle(ChatFormatting.GOLD), pMouseX, pMouseY);
        }
        int water = menu.getWater();
        int maxWater = menu.getMaxWaterStorage();

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

        if(pMouseX > leftPos + 138 && pMouseX < leftPos + 150 && pMouseY > topPos + 44 && pMouseY < topPos + 134) {
            pGuiGraphics.renderTooltip(this.font, Component.literal(String.valueOf(water)).withStyle(ChatFormatting.AQUA), pMouseX, pMouseY);
        }

        int lava = menu.getLava();
        int maxLava = menu.getMaxLavaStorage();

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
        if(pMouseX > leftPos + 121 && pMouseX < leftPos + 133 && pMouseY > topPos + 44 && pMouseY < topPos + 134) {
            pGuiGraphics.renderTooltip(this.font, Component.literal(String.valueOf(lava)).withStyle(ChatFormatting.RED), pMouseX, pMouseY);
        }


        int learn = menu.getLearn();
        int maxLearn = menu.getMaxLearn();

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
        if(pMouseX > leftPos + 8 && pMouseX < leftPos + 168 && pMouseY > topPos + 7 && pMouseY < topPos + 19) {
            double progress = 0;
            if (maxLearn > 0) {
                progress = (double) learn / maxLearn * 100;
            }
            DecimalFormat df = new DecimalFormat("0.00");
            String percentage = df.format(progress) + "%";
            pGuiGraphics.renderTooltip(this.font, Component.literal(percentage).withStyle(ChatFormatting.GREEN), pMouseX, pMouseY);
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
