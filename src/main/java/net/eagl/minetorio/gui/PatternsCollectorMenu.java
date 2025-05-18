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
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PatternsCollectorMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final PatternItemsHandler patternItemsHandler;

    private static final int INVENTORY_ROWS = 3;
    private static final int INVENTORY_COLUMNS = 9;
    private static final int HOTBAR_SIZE = 9;


    public PatternsCollectorMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(MinetorioMenus.PATTERNS_COLLECTOR_MENU.get(), id);
        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());
        this.patternItemsHandler = new PatternItemsHandler();

        // Додати слоти основного інвентаря (3 ряди по 9 слотів)
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


        int hotbarY = inventoryStartY + INVENTORY_ROWS * 18 + 4;
        for (int col = 0; col < HOTBAR_SIZE; col++) {
            this.addSlot(new Slot(playerInventory, col, inventoryStartX + col * 18, hotbarY));
        }

        int startX = 8;
        int startY = 18;
        for (int i = 0; i < patternItemsHandler.getSlots(); i++) {
            int x = startX + (i % 9) * 18;  // 9 слотів у ряд
            int y = startY + (i / 9) * 18;
            this.addSlot(new SlotItemHandler(patternItemsHandler, i, x, y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false; // заборонити ставити предмети
                }

                @Override
                public boolean mayPickup(Player player) {
                    return false; // заборонити забирати предмети
                }
            });
        }
    }

    public PatternsCollectorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        // тут логіка для переміщення стеків між слотами
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, MinetorioBlocks.PATTERNS_COLLECTOR.get());
    }
}
