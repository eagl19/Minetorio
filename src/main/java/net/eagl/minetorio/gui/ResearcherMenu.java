package net.eagl.minetorio.gui;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.eagl.minetorio.gui.slot.FlaskSlot;
import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.OpenTechnologyTreeScreenPacket;
import net.eagl.minetorio.util.InventorySlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ResearcherMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final SimpleContainerData data;


    public ResearcherMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(MinetorioMenus.RESEARCHER_MENU.get(), id);
        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());

        InventorySlot.addHotbarAndPlayerInventorySlots(this::addSlot, playerInventory, 0, 8, 140, 3, 9, 18, 18, 58);

        if (entity instanceof ResearcherBlockEntity researcherEntity) {
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


            this.data = new SimpleContainerData(8); // [0] — енергія, [1] — макс. енергія, [2] - поточне вивчення, [3] - вивчення

            addDataSlots(data);
            updateDataValues(researcherEntity);

        } else {
            throw new IllegalStateException("Invalid block entity for ResearcherMenu");
        }


    }

    public ContainerData getData() {
        return this.data;
    }

    private void updateDataValues(ResearcherBlockEntity blockEntity) {
        //data.set(0, blockEntity.getEnergyStorage().getEnergyStored());
        //data.set(1, blockEntity.getEnergyStorage().getMaxEnergyStored());
        data.set(0, 5000);
        data.set(1, 10000);
        data.set(2, 5000);
        data.set(3, 10000);
        data.set(4, 5000);
        data.set(5, 10000);
        data.set(6, 7500);
        data.set(7, 10000);
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

    @Override
    public void clicked(int slotId, int dragType, @NotNull ClickType clickType, @NotNull Player player) {
        if (slotId >= 36 && slotId <= 45) {
            if (!player.level().isClientSide) {
                MinetorioNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                        new OpenTechnologyTreeScreenPacket()
                );
            }
            return;
        }
        super.clicked(slotId, dragType, clickType, player);
    }

}
