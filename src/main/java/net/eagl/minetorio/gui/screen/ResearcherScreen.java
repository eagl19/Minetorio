package net.eagl.minetorio.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eagl.minetorio.gui.widget.ButtonWidget;
import net.eagl.minetorio.gui.widget.ItemIconWidget;
import net.eagl.minetorio.gui.widget.RemovableItemWidget;
import net.eagl.minetorio.gui.ResearcherMenu;
import net.eagl.minetorio.gui.slot.FlaskSlot;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.server.RemoveResearcherPlanPacket;
import net.eagl.minetorio.network.server.ResearcherButtonPacket;
import net.eagl.minetorio.util.Technology;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ResearcherScreen extends AbstractContainerScreen<ResearcherMenu> {

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/researcher.png");
    private static final ResourceLocation RESEARCH_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/button_learn.png");

    private List<FormattedCharSequence> lines;
    private int scrollOffset = 0;
    private final int maxVisibleLines = 6;
    private final int lineHeight = 10;

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
        List<Technology> techList = new ArrayList<>(getTechList());
        this.addRenderableWidget(new ItemIconWidget(
                leftPos + 26,
                topPos + 44,
                new ItemStack(techList.get(0).getDisplayIcon()),
                () -> openFlaskAction(0, techList.get(0))
        ));
        for (int i = 1; i < 10; i++) {
            Technology currentTech = techList.get(i);
            int currentI = i;
            if(currentTech.equals(Technology.EMPTY)){
                this.addRenderableWidget(new ItemIconWidget(
                        leftPos - 10 + i * 18, topPos + 23,
                        new ItemStack(currentTech.getDisplayIcon()),
                        () -> openFlaskAction(currentI, currentTech)
                ));
            }else {
                this.addRenderableWidget(new RemovableItemWidget(
                        leftPos - 10 + i * 18, topPos + 23,
                        new ItemStack(currentTech.getDisplayIcon()),
                        () -> openFlaskAction(currentI, currentTech),
                        () -> removeFlaskAction(currentI)
                ));
            }
        }
        this.addRenderableWidget( new ButtonWidget(
                leftPos + 8, topPos + 69, 59, 19,
                RESEARCH_TEXTURE,
                Component.translatable("tooltip.minetorio.researcher_gui.button_learn").withStyle(ChatFormatting.GREEN),
                this::buttonClick)
        );
        if(!menu.getBlockEntity().getResearchPlan().getFirst().equals(Technology.EMPTY)) {
            lines = font.split(menu.getBlockEntity().getResearchPlan().getFirst().getBenefit(),90);
        }else{
            lines = List.of();
        }

    }

    private void buttonClick(){
        if(menu.getLearn() == menu.getMaxLearn()) {
            MinetorioNetwork.CHANNEL.sendToServer(new ResearcherButtonPacket(0));
        }
    }

    private void removeFlaskAction(int currentTech) {
        MinetorioNetwork.CHANNEL.sendToServer(new RemoveResearcherPlanPacket(
                menu.getBlockEntity().getBlockPos(), currentTech));
    }

    private void openFlaskAction(int techIndex, Technology currentTech) {

        Minecraft.getInstance().setScreen(new TechnologyTreeScreen(menu, this.playerInventory, this.title, currentTech, techIndex));

    }

    private List<Technology> getTechList() {
        return menu.getTechList();
    }

    private void drawScrollbar(GuiGraphics guiGraphics, int totalLines, int offset) {
        int x = 104;
        int y = 0;
        int maxScroll = Math.max(0, totalLines - maxVisibleLines);
        int scrollAreaHeight = maxVisibleLines * lineHeight;
        int barHeight = Math.max(10, scrollAreaHeight * maxVisibleLines / totalLines);
        int barY = y + (scrollAreaHeight - barHeight) * offset / Math.max(1, maxScroll);

        guiGraphics.fill(x, y, x + 4, y + scrollAreaHeight, 0xFFAAAAAA);
        guiGraphics.fill(x, barY, x + 4, barY + barHeight, 0xFF555555);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.setColor(1f, 1f, 1f, 1f);
        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        pGuiGraphics.pose().pushPose();

        int startX = leftPos + 70;
        int startY = topPos + 44;
        pGuiGraphics.pose().translate(startX, startY, 0);
        pGuiGraphics.pose().scale(0.5f, 0.5f, 1.0f);
        RenderSystem.setShaderColor(0.6f, 0.6f, 0.6f, 1.0f);
        for (int i = 0; i < maxVisibleLines; i++) {
            int lineIndex = scrollOffset + i;
            if (lineIndex >= lines.size()) break;
            pGuiGraphics.drawString(font, lines.get(lineIndex), 0,  i * lineHeight, 0x000000, false);
        }

        if(lines.size() > maxVisibleLines) {
            drawScrollbar(pGuiGraphics, lines.size(), scrollOffset);
        }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        pGuiGraphics.pose().popPose();

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
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int maxScroll = Math.max(0, lines.size() - maxVisibleLines);
        scrollOffset = Mth.clamp(scrollOffset - (int) delta, 0, maxScroll);
        return true;
    }
}
