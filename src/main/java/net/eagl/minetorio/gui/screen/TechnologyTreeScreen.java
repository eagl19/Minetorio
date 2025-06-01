package net.eagl.minetorio.gui.screen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.TechnologyRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TechnologyTreeScreen extends Screen {
    private static final ResourceLocation NODE_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/tech.png");

    private final int treeOffsetX = 50;
    private final int treeOffsetY = 50;

    public TechnologyTreeScreen() {
        super(Component.literal("Technology Tree"));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

        // Draw connections
        for (Technology tech : TechnologyRegistry.getAll()) {
            int x1 = tech.getX() + treeOffsetX;
            int y1 = tech.getY() + treeOffsetY;

            for (String parentId : tech.getPrerequisites()) {
                Technology parent = TechnologyRegistry.get(parentId);
                if (parent != null) {
                    int x2 = parent.getX() + treeOffsetX;
                    int y2 = parent.getY() + treeOffsetY;
                    drawConnectionLine(guiGraphics, x1 + 16, y1 + 8, x2 + 16, y2 + 8, 0xFFFFFFFF);
                }
            }
        }

        // Draw nodes
        for (Technology tech : TechnologyRegistry.getAll()) {
            int x = tech.getX() + treeOffsetX;
            int y = tech.getY() + treeOffsetY;

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, NODE_TEXTURE);
            guiGraphics.blit(NODE_TEXTURE, x, y, 0, 0, 32, 16, 32, 16);
            guiGraphics.drawString(this.font, tech.getDisplayName(), x + 36, y + 4, 0xFFFFFF);

            // Tooltip on hover
            if (mouseX >= x && mouseX <= x + 32 && mouseY >= y && mouseY <= y + 16) {
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
            // Вертикальна лінія
            guiGraphics.vLine(x1, Math.min(y1, y2), Math.max(y1, y2), color);
        } else if (y1 == y2) {
            // Горизонтальна лінія
            guiGraphics.hLine(Math.min(x1, x2), Math.max(x1, x2), y1, color);
        } else {
            // "Г-подібна" лінія (спершу по горизонталі, потім по вертикалі)
            guiGraphics.hLine(Math.min(x1, x2), Math.max(x1, x2), y1, color);
            guiGraphics.vLine(x2, Math.min(y1, y2), Math.max(y1, y2), color);
        }
    }
}