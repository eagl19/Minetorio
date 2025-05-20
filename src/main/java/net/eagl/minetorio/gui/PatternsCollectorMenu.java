package net.eagl.minetorio.gui;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.handler.PatternItemsHandler;
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


    private static final int INVENTORY_ROWS = 3;
    private static final int INVENTORY_COLUMNS = 9;
    private static final int HOTBAR_SIZE = 9;


    public PatternsCollectorMenu(int id, Inventory playerInventory, BlockEntity entity) {

        super(MinetorioMenus.PATTERNS_COLLECTOR_MENU.get(), id);
        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());
        PatternItemsHandler patternItemsHandler = new PatternItemsHandler();

        // слоти основного інвентаря (3 ряди по 9 слотів)
        int inventoryStartX = 8;
        int inventoryStartY = 140;
        for (int row = 0; row < INVENTORY_ROWS; row++) {
            for (int col = 0; col < INVENTORY_COLUMNS; col++) {
                int slotIndex = col + row * INVENTORY_COLUMNS + HOTBAR_SIZE;
                int x = inventoryStartX + col * 18;
                int y = inventoryStartY + row * 18;
                this.addSlot(new Slot(playerInventory, slotIndex, x, y));
            }
        }

        // слоти хотбару (9 слотів)
        int hotbarY = inventoryStartY + INVENTORY_ROWS * 18 + 4;
        for (int col = 0; col < HOTBAR_SIZE; col++) {
            this.addSlot(new Slot(playerInventory, col, inventoryStartX + col * 18, hotbarY));
        }

        int startX = 8;
        int startY = 18;
        for (int i = 0; i < patternItemsHandler.getSlots(); i++) {
            int x = startX + (i % 9) * 18;  // 9 слотів у ряд
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
        int playerInventoryEnd =INVENTORY_COLUMNS*INVENTORY_ROWS; // 3 ряди по 9
        int hotbarEnd = playerInventoryEnd + 9;

        // Забороняємо взаємодію з read-only шаблонними слотами
        if (pIndex > hotbarEnd) {
            return ItemStack.EMPTY;
        }

        // Якщо клікнули в основний інвентар — намагаємось перемістити в хотбар
        if (pIndex < playerInventoryEnd) {
            if (!moveItemStackTo(originalStack, playerInventoryEnd, hotbarEnd, false)) {
                return ItemStack.EMPTY;
            }
        }
        // Якщо клікнули в хотбар — намагаємось перемістити в основний інвентар
        else if (pIndex < hotbarEnd) {
            if (!moveItemStackTo(originalStack, playerInventoryStart, playerInventoryEnd, false)) {
                return ItemStack.EMPTY;
            }
            // Забороняємо взаємодію з read-only шаблонними слотами
        }
        if (slot instanceof PatternSlot) {
            return ItemStack.EMPTY;
        }

        // Якщо стек повністю переміщено — очищаємо слот
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
