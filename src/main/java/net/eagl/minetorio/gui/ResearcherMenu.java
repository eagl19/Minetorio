package net.eagl.minetorio.gui;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.eagl.minetorio.gui.slot.FlaskSlot;
import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.ResearchListSyncServerPacket;
import net.eagl.minetorio.network.ResearcherSyncToClientPacket;
import net.eagl.minetorio.util.InventorySlot;
import net.eagl.minetorio.util.Technology;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ResearcherMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final ResearcherBlockEntity be;

    private final ContainerData data;

    public ResearcherMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(MinetorioMenus.RESEARCHER_MENU.get(), id);

        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());

        InventorySlot.addHotbarAndPlayerInventorySlots(this::addSlot, playerInventory, 0, 8, 140, 3, 9, 18, 18, 58);

        if (entity instanceof ResearcherBlockEntity researcherEntity) {

            be = researcherEntity;

            ItemStackHandler container = Objects.requireNonNull(researcherEntity.getItemStackHandler(), "Researcher container is null");

            this.addSlot(new FlaskSlot(container, 0,  8,  98, MinetorioItems.FLASK_RED.get()));
            this.addSlot(new FlaskSlot(container, 1,  26, 98, MinetorioItems.FLASK_GREEN.get()));
            this.addSlot(new FlaskSlot(container, 2,  44, 98, MinetorioItems.FLASK_BLACK.get()));
            this.addSlot(new FlaskSlot(container, 3,  62, 98, MinetorioItems.FLASK_PURPLE.get()));
            this.addSlot(new FlaskSlot(container, 4,  80, 98, MinetorioItems.FLASK_PINK.get()));
            this.addSlot(new FlaskSlot(container, 5,  98, 98, MinetorioItems.FLASK_WHITE.get()));

            this.addSlot(new FlaskSlot(container, 6,  8,  116, MinetorioItems.FLASK_BLUE.get()));
            this.addSlot(new FlaskSlot(container, 7,  26, 116, MinetorioItems.FLASK_YELLOW.get()));
            this.addSlot(new FlaskSlot(container, 8,  44, 116, MinetorioItems.FLASK_BROWN.get()));
            this.addSlot(new FlaskSlot(container, 9,  62, 116, MinetorioItems.FLASK_CYAN.get()));
            this.addSlot(new FlaskSlot(container, 10, 80, 116, MinetorioItems.FLASK_ORANGE.get()));
            this.addSlot(new FlaskSlot(container, 11, 98, 116, MinetorioItems.FLASK_GRAY.get()));


            this.data = researcherEntity.getContainerData();
            addDataSlots(this.data);
            data.set(6, 7500);
            data.set(7, 10000);

        } else {
            throw new IllegalStateException("Invalid block entity for ResearcherMenu");
        }


    }


    public ContainerData getData() {
        return this.data;
    }

    public ResearcherMenu (int id, Inventory inv, FriendlyByteBuf extraData){
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }


    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack originalStack = slot.getItem();
        ItemStack copy = originalStack.copy();

        if(slot instanceof FlaskSlot){
            return ItemStack.EMPTY;
        }

         if (index >= 9 && index < 36) {
            if (isFlaskItem(originalStack.getItem())) {
                if (!moveItemStackTo(originalStack, 36, 48, false) &&
                        !moveItemStackTo(originalStack, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!moveItemStackTo(originalStack, 0, 9, false)) {
                return ItemStack.EMPTY;
            }
        }
        else if (index >= 0 && index < 9) {
            if (isFlaskItem(originalStack.getItem())) {
                if (!moveItemStackTo(originalStack, 36, 48, false) &&
                        !moveItemStackTo(originalStack, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!moveItemStackTo(originalStack, 9, 36, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (originalStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return copy;
    }

    private boolean isFlaskItem(Item item) {
        return item.getDescriptionId().contains("flask_");
    }


    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(access, pPlayer, MinetorioBlocks.RESEARCHER.get());
    }

    public  int getMaxEnergyStorage(){
        return data.get(1);
    }

    public int getEnergy(){
        return data.get(0);
    }

    public int getWater() {
        return data.get(2);
    }
    public int getMaxWaterStorage(){
        return data.get(3);
    }
    public int getLava(){
        return  data.get(4);
    }
    public int getMaxLavaStorage(){
        return  data.get(5);
    }
    public  List<Technology> getTechList(){
        return be.getTechList();
    }
    public void setTechList(List<Technology> pList){
        be.setTechList(pList);
    }

    @Override
    public void removed(@NotNull Player pPlayer) {
        super.removed(pPlayer);
        if (pPlayer.level().isClientSide) {
            MinetorioNetwork.CHANNEL.sendToServer(new ResearchListSyncServerPacket(be.getBlockPos(), be.getTechList()));
        }
    }
}
