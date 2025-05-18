package net.eagl.minetorio.handler;

import net.eagl.minetorio.util.PatternItemsCollector;
import net.minecraftforge.items.IItemHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ArrayList;

public class PatternItemsHandler implements IItemHandler {

    private final List<Item> patternItems;

    public PatternItemsHandler() {
        this.patternItems = new ArrayList<>(PatternItemsCollector.getPatternItems());
    }

    @Override
    public int getSlots() {
        return patternItems.size();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= patternItems.size()) return ItemStack.EMPTY;
        return new ItemStack(patternItems.get(slot), 1);
    }

    // Нижче забороняємо змінювати слоти
    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack; // не дозволяємо вставляти предмети
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY; // не дозволяємо витягати предмети
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false; // не дозволяємо ставити предмети
    }
}