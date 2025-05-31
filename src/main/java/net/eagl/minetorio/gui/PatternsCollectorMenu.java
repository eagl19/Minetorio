package net.eagl.minetorio.gui;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.gui.slot.PatternSlot;
import net.eagl.minetorio.handler.PatternItemsHandler;
import net.eagl.minetorio.util.InventorySlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PatternsCollectorMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;

    public PatternsCollectorMenu(int id, Inventory playerInventory, BlockEntity entity) {

        super(MinetorioMenus.PATTERNS_COLLECTOR_MENU.get(), id);
        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());
        PatternItemsHandler patternItemsHandler = new PatternItemsHandler();

        InventorySlot.addHotbarAndPlayerInventorySlots(this::addSlot, playerInventory, 0, 8, 140, 3, 9, 18, 18, 48);

        int startX = 8;
        int startY = 18;
        for (int i = 0; i < patternItemsHandler.getSlots(); i++) {
            int x = startX + (i % 9) * 18;
            int y = startY + (i / 9) * 18;
            this.addSlot(new PatternSlot(patternItemsHandler, i, x, y));

        }

    }

    public PatternsCollectorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        Slot slot = this.slots.get(pIndex);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack originalStack = slot.getItem();
        ItemStack copy = originalStack.copy();


        int playerInventoryStart = 0;
        int playerInventoryEnd =27;
        int hotbarEnd = 36;


        if (pIndex > hotbarEnd) {
            return ItemStack.EMPTY;
        }

        if (pIndex < playerInventoryEnd) {
            if (!moveItemStackTo(originalStack, playerInventoryEnd, hotbarEnd, false)) {
                return ItemStack.EMPTY;
            }
        }
        else if (pIndex < hotbarEnd) {
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
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, MinetorioBlocks.PATTERNS_COLLECTOR.get());
    }
}
