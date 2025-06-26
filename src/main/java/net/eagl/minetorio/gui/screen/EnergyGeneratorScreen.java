package net.eagl.minetorio.gui.screen;

import net.eagl.minetorio.gui.menu.EnergyGeneratorMenu;
import net.eagl.minetorio.gui.slot.FlaskSlot;
import net.eagl.minetorio.gui.widget.ItemIconWidget;
import net.eagl.minetorio.gui.widget.RemovableItemWidget;
import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.server.GeneratorInitializePacket;
import net.eagl.minetorio.network.server.RemoveConsumersPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class EnergyGeneratorScreen extends AbstractContainerScreen<EnergyGeneratorMenu> implements IGeneratorScreen {

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/energy_generator.png");
    private final Inventory playerInventory;

    public EnergyGeneratorScreen(EnergyGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 222;
        this.imageWidth = 176;
        this.playerInventory = pPlayerInventory;
    }

    @Override
    protected void init() {
        super.init();
        update();
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.setColor(1f, 1f, 1f, 1f);
        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int energy = menu.getEnergy();
        int maxEnergy = menu.getMaxEnergy();
        if (maxEnergy > 0 && energy > 0) {
            int barHeight = 128;
            int height = (int) ((float) energy / maxEnergy * barHeight);
            pGuiGraphics.blit(GUI_TEXTURE, leftPos + 155, topPos + 5 + (barHeight - height), 178, 5 + barHeight - height, 12, height);
        }
        if(pMouseX > leftPos + 155 && pMouseX < leftPos + 167 && pMouseY > topPos + 5 && pMouseY < topPos + 133) {
            pGuiGraphics.renderTooltip(this.font, Component.literal(String.valueOf(energy)).withStyle(ChatFormatting.GOLD), pMouseX, pMouseY);
        }

        int produce = menu.getMaxProduce() - menu.getProduce();
        if (menu.getMaxProduce() > 0 && produce > 0) {
            int barWidth = 121;
            int width = (int) ((float) produce / menu.getMaxProduce() * barWidth);
            pGuiGraphics.blit(GUI_TEXTURE, leftPos + 28, topPos + 56, 28, 225, width, 24);
        }
        if(pMouseX > leftPos + 28 && pMouseX < leftPos + 149 && pMouseY > topPos + 56 && pMouseY < topPos + 80) {
            double percent = (double) produce / menu.getMaxProduce() * 100;
            DecimalFormat df = new DecimalFormat("0.00");
            pGuiGraphics.renderTooltip(this.font, Component.literal(df.format(percent) + "%").withStyle(ChatFormatting.GREEN), pMouseX, pMouseY);
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        for (Slot slot : menu.slots) {
            if (slot instanceof FlaskSlot flask && flask.isVisible()) {
                pGuiGraphics.renderItem(new ItemStack(flask.getFlask()),slot.x, slot.y);
                pGuiGraphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, 0x88000000);

                if (this.isHovering(slot.x, slot.y, 16, 16, pMouseX, pMouseY)) {
                    pGuiGraphics.renderTooltip(font, flask.getFlask().getDefaultInstance().getHoverName(), pMouseX - leftPos, pMouseY - topPos);
                }
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    public void update() {

        this.clearWidgets();

        this.addRenderableWidget(new ItemIconWidget(
                leftPos + 8 , topPos + 7,
                new ItemStack(MinetorioItems.PATTERN_SUN.get()),
                null
        ));

        List<BlockPos> list = menu.getTargets().getConsumers();

        for(int i = 0; i < 7; i++){
            if(i < list.size()) {
                int finalI = i;
                this.addRenderableWidget(new RemovableItemWidget(
                        leftPos + 26 + i * 18, topPos + 7,
                        menu.getItemFromBlockPos(list.get(i)),
                        this::openAction,
                        () -> removeAction(finalI)
                ));
            }else {
                this.addRenderableWidget(new ItemIconWidget(
                        leftPos + 26 + i * 18, topPos + 7,
                        new ItemStack(MinetorioItems.PATTERN_ENERGY_CONSUMER.get()),
                        this::openAction
                ));
            }
        }

        this.addRenderableWidget(new ItemIconWidget(
                leftPos + 8 , topPos + 117,
                new ItemStack(menu.getTech().getDisplayIcon()),
                null
        ));

        for(int i = 0; i < 7; i++){
            if( i + 7 < list.size()) {
                int finalI = i;
                this.addRenderableWidget(new RemovableItemWidget(
                        leftPos + 26 + i * 18, topPos + 118,
                        menu.getItemFromBlockPos(list.get(i + 7)),
                        this::openAction,
                        () -> removeAction(finalI + 7)
                ));
            }else {
                this.addRenderableWidget(new ItemIconWidget(
                        leftPos + 26 + i * 18, topPos + 118,
                        new ItemStack(MinetorioItems.PATTERN_ENERGY_CONSUMER.get()),
                        this::openAction
                ));
            }
        }
    }

    private void openAction(){
        MinetorioNetwork.CHANNEL.sendToServer(new GeneratorInitializePacket(menu.getBlockEntity().getBlockPos()));
        Minecraft.getInstance().setScreen(new ConsumerListScreen<>(menu, this.playerInventory, this.title,
                Component.translatable("tooltip.minetorio.energy_generator.consumers").withStyle(ChatFormatting.DARK_AQUA)));
    }

    private void removeAction(int index){
        MinetorioNetwork.CHANNEL.sendToServer(new RemoveConsumersPacket(menu.getBlockEntity().getBlockPos(), index));
    }
}
