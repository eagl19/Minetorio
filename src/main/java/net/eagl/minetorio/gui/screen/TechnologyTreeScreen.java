package net.eagl.minetorio.gui.screen;


import com.mojang.blaze3d.systems.RenderSystem;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.TechnologyRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TechnologyTreeScreen extends Screen {
    private static final ResourceLocation NODE_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/tech.png");



    public TechnologyTreeScreen() {
        super(Component.literal("Technology Tree"));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

        for (Technology tech : TechnologyRegistry.getAll()) {
            int x1 = tech.getX();
            int y1 = tech.getY();

            for (String parentId : tech.getPrerequisites()) {
                Technology parent = TechnologyRegistry.get(parentId);
                if (parent != null) {
                    int x2 = parent.getX();
                    int y2 = parent.getY();
                    drawConnectionLine(guiGraphics, x1 + 64, y1 + 32, x2 + 64, y2 + 32, 0xFFFFFFFF);
                }
            }
        }


        for (Technology tech : TechnologyRegistry.getAll()) {
            int x = tech.getX();
            int y = tech.getY();

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, NODE_TEXTURE);
            guiGraphics.blit(NODE_TEXTURE, x, y, 0, 0, 128, 64, 128, 64);


            guiGraphics.renderItem(new ItemStack(tech.getDisplayIcon()), x +1, y + 1);


            guiGraphics.pose().pushPose();

            float scale = 0.75f;  // 75% від початкового розміру
            guiGraphics.pose().scale(scale, scale, 1f);



            int costStartX = (int) ((x + 1) / scale);
            int costY = (int) ((y + 16)/scale);

            int time = tech.getTime();
            int count = tech.getCount();

            int iconOffsetX = 0;

            for (ItemStack baseCost : tech.getCost()) {

                // Рендер іконки
                guiGraphics.renderItem(baseCost, costStartX + iconOffsetX, costY);

                // Рендер кількості поверх іконки
                guiGraphics.renderItemDecorations(this.font, baseCost, costStartX + iconOffsetX, costY);

                iconOffsetX += 14;
            }
            guiGraphics.drawString(this.font, Component.literal(" x "+timeToString(time) +" x "+ count), costStartX + iconOffsetX, costY+3, 0xFFFFFF);

            guiGraphics.pose().popPose();

            if (mouseX >= x && mouseX <= x + 128 && mouseY >= y && mouseY <= y + 64) {
                guiGraphics.renderComponentTooltip(this.font, List.of(
                        tech.getDisplayName(),
                        Component.literal("Cost: " + tech.getFormattedCost().getString())
                ), mouseX, mouseY);
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void drawConnectionLine(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int color) {
        if (x1 == x2) {
            guiGraphics.vLine(x1, Math.min(y1, y2), Math.max(y1, y2), color);
        } else if (y1 == y2) {
            guiGraphics.hLine(Math.min(x1, x2), Math.max(x1, x2), y1, color);
        } else {
            guiGraphics.hLine(Math.min(x1, x2), Math.max(x1, x2), y1, color);
            guiGraphics.vLine(x2, Math.min(y1, y2), Math.max(y1, y2), color);
        }
    }

    private String timeToString(int time){

        int totalSeconds = time / 20;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        StringBuilder sb = new StringBuilder();

        if (hours > 0) {
            sb.append(String.format("%d:", hours));
        }
        sb.append(String.format("%d:", minutes));
        sb.append(String.format("%d", seconds));

        return sb.toString().trim();
    }
}