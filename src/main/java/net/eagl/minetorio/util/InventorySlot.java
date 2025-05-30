package net.eagl.minetorio.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.function.Consumer;


public class InventorySlot {

    public static void addPlayerInventorySlots(Consumer<Slot> addSlotFunc, Inventory inventory, int index, int startX, int startY, int rowAmount, int columnAmount, int dx, int dy) {

        addBox(addSlotFunc, inventory, index, startX, startY, rowAmount, columnAmount, dx, dy);

    }

    public static void addHotbarSlots(Consumer<Slot> addSlotFunc, Inventory inventory, int index,  int startX, int startY, int columnAmount, int dx ) {

        addRow(addSlotFunc, inventory, index, startX, startY, columnAmount, dx);

    }

    public static void addHotbarAndPlayerInventorySlots(Consumer<Slot> addSlotFunc, Inventory inventory, int index, int startX, int startY, int rowAmount, int columnAmount, int dx, int dy, int distance){

        addHotbarSlots(addSlotFunc, inventory, index, startX, startY+distance, columnAmount, dx);
        addPlayerInventorySlots(addSlotFunc, inventory, index+columnAmount, startX, startY, rowAmount, columnAmount, dx, dy);

    }

    private static int addRow(Consumer<Slot> addSlotFunc, Inventory inventory, int index, int startX, int startY, int amount, int dx){

        for (int i = 0 ; i < amount ; i++) {
            addSlotFunc.accept(new Slot(inventory, index, startX, startY));
            startX += dx;
            index++;
        }
        return index;
    }

    private static void addBox(Consumer<Slot> addSlotFunc, Inventory inventory, int index, int startX, int startY, int rowAmount, int columnAmount, int dx, int dy){
        for (int j = 0 ; j < rowAmount ; j++) {
            index = addRow(addSlotFunc, inventory, index, startX, startY, columnAmount, dx);
            startY += dy;
        }
    }
}
