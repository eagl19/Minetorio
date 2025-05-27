package net.eagl.minetorio.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.function.Consumer;

public class InventorySlotHelper {

    public static void addPlayerInventorySlots(Consumer<Slot> addSlotFunc, Inventory inventory, int startX, int startY) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int index = col + row * 9 + 9;
                int x = startX + col * 18;
                int y = startY + row * 18;
                addSlotFunc.accept(new Slot(inventory, index, x, y));
            }
        }
    }

    public static void addHotbarSlots(Consumer<Slot> addSlotFunc, Inventory inventory, int startX, int startY) {
        for (int col = 0; col < 9; col++) {
            int x = startX + col * 18;
            addSlotFunc.accept(new Slot(inventory, col, x, startY));
        }
    }
}
