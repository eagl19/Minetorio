package net.eagl.minetorio.gui;

import net.eagl.minetorio.client.ClientPatternsData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class PatternSlot extends SlotItemHandler {


    public PatternSlot(IItemHandler handler, int index, int x, int y) {
        super(handler, index, x, y);
    }

    public boolean isLearned() {
        ResourceLocation key=ForgeRegistries.ITEMS.getKey(this.getItem().getItem());
        if (key == null) return false;
        return ClientPatternsData.isLearned(key.toString());
    }

    @Override
    public boolean isActive() {
        return false;
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
