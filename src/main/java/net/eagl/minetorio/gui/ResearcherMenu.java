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

            for (int i = 0; i < 9; i++) {
                this.addSlot(new SlotItemHandler(container, i, 8 + i * 18, 23));
            }
            this.addSlot(new SlotItemHandler(container, 9, 8, 60));


            this.addSlot(new FlaskSlot(container, 10, 8,  98, MinetorioItems.FLASK_RED.get()));
            this.addSlot(new FlaskSlot(container, 11, 26, 98, MinetorioItems.FLASK_GREEN.get()));
            this.addSlot(new FlaskSlot(container, 12, 44, 98, MinetorioItems.FLASK_BLACK.get()));
            this.addSlot(new FlaskSlot(container, 13, 62, 98, MinetorioItems.FLASK_PURPLE.get()));
            this.addSlot(new FlaskSlot(container, 14, 80, 98, MinetorioItems.FLASK_PINK.get()));
            this.addSlot(new FlaskSlot(container, 15, 98, 98, MinetorioItems.FLASK_WHITE.get()));

            this.addSlot(new FlaskSlot(container, 16, 8,  116, MinetorioItems.FLASK_BLUE.get()));
            this.addSlot(new FlaskSlot(container, 17, 26, 116, MinetorioItems.FLASK_YELLOW.get()));
            this.addSlot(new FlaskSlot(container, 18, 44, 116, MinetorioItems.FLASK_BROWN.get()));
            this.addSlot(new FlaskSlot(container, 19, 62, 116, MinetorioItems.FLASK_CYAN.get()));
            this.addSlot(new FlaskSlot(container, 20, 80, 116, MinetorioItems.FLASK_RED.get()));
            this.addSlot(new FlaskSlot(container, 21, 98, 116, MinetorioItems.FLASK_RED.get()));


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

        if (index >= 36 && index < 46) {
            // From Researcher to Player: try main inventory first, then hotbar
            if (!moveItemStackTo(originalStack, 9, 36, false) &&
                    !moveItemStackTo(originalStack, 0, 9, false)) {
                return ItemStack.EMPTY;
            }
        }
        else if (index >= 9 && index < 36) {
            if (isFlaskItem(originalStack.getItem())) {
                if (!moveItemStackTo(originalStack, 46, 58, false) &&
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
                if (!moveItemStackTo(originalStack, 46, 58, false) &&
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
