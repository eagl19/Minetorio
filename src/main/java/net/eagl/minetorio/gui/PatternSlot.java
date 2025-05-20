package net.eagl.minetorio.gui;

import net.eagl.minetorio.client.ClientPatternsData;
import net.eagl.minetorio.gui.screen.PatternsCollectorScreen;
import net.minecraft.client.Minecraft;
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

    public boolean isLearned(String key) {
        return ClientPatternsData.isLearned(key);
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
