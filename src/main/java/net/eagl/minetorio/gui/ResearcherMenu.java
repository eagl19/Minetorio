package net.eagl.minetorio.gui;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.eagl.minetorio.util.InventorySlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
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
                this.addSlot(new SlotItemHandler(container, i, 8 + i * 18, 12));
            }
            this.addSlot(new SlotItemHandler(container, 9, 80, 37));

            this.data = new SimpleContainerData(4); // [0] — енергія, [1] — макс. енергія, [2] - поточне вивчення, [3] - вивчення

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

        int hotbarStart = 0;
        int hotbarEnd = 9;
        int playerInventoryStart = 9;
        int playerInventoryEnd = 36;
        int researcherStart = 36;
        int researcherEnd = 46;

        if (index >= researcherStart && index < researcherEnd) {
            // From Researcher to Player: try main inventory first, then hotbar
            if (!moveItemStackTo(originalStack, playerInventoryStart, playerInventoryEnd, false) &&
                    !moveItemStackTo(originalStack, hotbarStart, hotbarEnd, false)) {
                return ItemStack.EMPTY;
            }
        }
        else if (index >= playerInventoryStart && index < playerInventoryEnd) {
            // From Player Inventory to Hotbar
            if (!moveItemStackTo(originalStack, hotbarStart, hotbarEnd, false)) {
                return ItemStack.EMPTY;
            }
        }
        else if (index >= hotbarStart && index < hotbarEnd) {
            // From Hotbar to Player Inventory
            if (!moveItemStackTo(originalStack, playerInventoryStart, playerInventoryEnd, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (slot instanceof PatternSlot) {
            return ItemStack.EMPTY;
        }

        if (originalStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return copy;
    }




    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(access, pPlayer, MinetorioBlocks.RESEARCHER.get());
    }
}
