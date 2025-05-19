package net.eagl.minetorio.gui;

import net.eagl.minetorio.handler.PatternItemsHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class PatternSlot extends SlotItemHandler {
    private final boolean learned;

    public PatternSlot(IItemHandler handler, int index, int x, int y, boolean learned) {
        super(handler, index, x, y);
        this.learned = learned;
    }

    @Override
    public boolean isActive() {
        return learned; // слот неактивний, якщо не вивчено
    }

    @Override
    public boolean mayPickup(Player player) {
        return false; // не дозволяємо забирати
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false; // не дозволяємо вставляти
    }
}
