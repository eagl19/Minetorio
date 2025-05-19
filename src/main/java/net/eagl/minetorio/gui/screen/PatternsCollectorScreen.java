package net.eagl.minetorio.gui.screen;


import net.eagl.minetorio.gui.PatternSlot;
import net.eagl.minetorio.gui.PatternsCollectorMenu;
import net.eagl.minetorio.handler.PatternInfo;
import net.eagl.minetorio.handler.PatternItemsHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatternsCollectorScreen extends AbstractContainerScreen<PatternsCollectorMenu> {

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/patterns_collector.png");

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

    private boolean isHoveringSlot(Slot slot, int mouseX, int mouseY) {
        int x = leftPos + slot.x;
        int y = topPos + slot.y;
        return mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16;
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {

        for (Slot slot : menu.slots) {
            if (!(slot instanceof PatternSlot)) continue;
            if (!isHoveringSlot(slot, pMouseX, pMouseY)) continue;

            ItemStack stack = slot.getItem();
            if (stack.isEmpty()) continue;

            boolean learned = stack.getOrCreateTag().getBoolean("Learned");
            Component learnedStatus = learned
                    ? Component.translatable("tooltip.minetorio.learned").withStyle(ChatFormatting.GREEN)
                    : Component.translatable("tooltip.minetorio.unlearned").withStyle(ChatFormatting.RED);

            PatternInfo info = PatternItemsHandler.getPatternInfo(stack); // або static, або екземпляр
            List<Component> componentList = new ArrayList<>();
            componentList.add( stack.getHoverName().copy().withStyle(ChatFormatting.GRAY));
            componentList.add(Component.literal(""));
            componentList.add(Component.translatable("tooltip.minetorio.learn_status").append(": ").append(learnedStatus));
            componentList.add(Component.translatable("tooltip.minetorio.benefit").append(": ").append(Component.translatable(info.benefit()).withStyle(ChatFormatting.BLUE)));

            if(!learned){
                componentList.add(Component.translatable("tooltip.minetorio.how_to_learn").append(": ").append(Component.translatable(info.howToLearn()).withStyle(ChatFormatting.AQUA)));
            }

            pGuiGraphics.renderTooltip(
                    font,
                    componentList,
                    Optional.empty(),
                    pMouseX - leftPos,
                    pMouseY - topPos
            );
        }
    }


    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.setColor(1f, 1f, 1f, 1f);
        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        for (Slot slot : menu.slots) {
            if (slot instanceof PatternSlot) {
                int x = slot.x;
                int y = slot.y;
                ItemStack stack = slot.getItem();

                // Тут перевіряємо, чи предмет вивчений, наприклад через метод меню:
                boolean isLearned = slot.isActive(); // припустимо, ти так визначаєш

                if (stack.isEmpty()) continue;

                // Якщо предмет невивчений — задаємо сірий колір
                if (!isLearned) {
                    // Сірий колір, наприклад 50% яскравості
                    pGuiGraphics.setColor(0.5f, 0.5f, 0.5f, 1f);
                } else {
                    // Білий колір (звичайний)
                    pGuiGraphics.setColor(1f, 1f, 1f, 1f);
                }

                pGuiGraphics.renderItem(stack, leftPos + x, topPos + y);

                // Відновлюємо колір (щоб не впливати на наступні рендери)
                pGuiGraphics.setColor(1f, 1f, 1f, 1f);
            }
        }

    }

}
