package net.eagl.minetorio.gui.screen;

import net.eagl.minetorio.gui.menu.LavaGeneratorMenu;
import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.server.GeneratorInitializePacket;
import net.eagl.minetorio.network.server.RemoveConsumersPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class LavaGeneratorScreen extends AbstractFluidGeneratorScreen<LavaGeneratorMenu> {

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/lava_generator.png");

    private final Inventory playerInventory;

    public LavaGeneratorScreen(LavaGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, GUI_TEXTURE);
        this.imageHeight = 222;
        this.imageWidth = 176;
        this.playerInventory = pPlayerInventory;
    }

    @Override
    protected int getEnergy() {
        return menu.getEnergy();
    }

    @Override
    protected int getMaxEnergy() {
        return menu.getMaxEnergyStorage();
    }

    @Override
    protected int getFluid() {
        return menu.getFluid();
    }

    @Override
    protected int getMaxFluid() {
        return menu.getMaxFluidStorage();
    }

    @Override
    protected int getProduce() {
        return menu.getProduce();
    }

    @Override
    protected int getMaxProduce() {
        return menu.getMaxProduce();
    }

    @Override
    protected List<BlockPos> getGeneratorConsumers() {
        return menu.getFluidTargets().getConsumers();
    }

    @Override
    protected ItemStack getItemFromBlockPos(BlockPos pos) {
        return menu.getItemFromBlockPos(pos);
    }

    @Override
    protected ItemStack geUpdateIcon() {
        return new ItemStack(menu.getTech().getDisplayIcon());
    }

    @Override
    protected void sendOpenPacket() {
        MinetorioNetwork.CHANNEL.sendToServer(new GeneratorInitializePacket(menu.getGeneratorBlockEntity().getBlockPos()));
        Minecraft.getInstance().setScreen(new ConsumerListScreen(menu, this.playerInventory, this.title,
                Component.translatable("tooltip.minetorio.lava_generator.consumers").withStyle(ChatFormatting.DARK_RED)));
    }

    @Override
    protected void sendRemovePacket(int index) {
        MinetorioNetwork.CHANNEL.sendToServer(new RemoveConsumersPacket(menu.getGeneratorBlockEntity().getBlockPos(), index));
    }

    @Override
    protected ItemStack getPatternIcon() {
        return new ItemStack(MinetorioItems.PATTERN_FIRE.get());
    }

    @Override
    protected ItemStack getConsumerPatternIcon() {
        return new ItemStack(MinetorioItems.PATTERN_LAVA_CONSUMER.get());
    }

}
