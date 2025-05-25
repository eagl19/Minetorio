package net.eagl.minetorio.handler;

import net.eagl.minetorio.util.PatternItemsCollector;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PatternItemsHandler implements IItemHandlerModifiable {

    private final LinkedHashMap<Item, Boolean> patternItems; // Зберігаємо порядок

    public PatternItemsHandler() {

        Set<Item> items = PatternItemsCollector.getPatternItems();

        List<Item> sortedItems = items.stream()
                .sorted(Comparator.comparing(item -> item.getName(ItemStack.EMPTY).getString()))
                .toList();

        this.patternItems = new LinkedHashMap<>();
        for (Item item : sortedItems) {
            patternItems.put(item, false);
        }
    }

    public static PatternInfo getPatternInfo(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            return new PatternInfo("", "");
        }

        Item item = itemStack.getItem();
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);

        if (key == null) {
            return new PatternInfo("", "");
        }

        String benefitKey = "pattern.minetorio." + key.getPath() + ".benefit";
        String howToLearnKey = "pattern.minetorio." + key.getPath() + ".how_to_learn";

        return new PatternInfo(benefitKey, howToLearnKey);
    }

    @Override
    public int getSlots() {
        return patternItems.size();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= patternItems.size()) return ItemStack.EMPTY;

        Item item = getItemByIndex(slot);
        boolean learned = patternItems.get(item);

        ItemStack stack = new ItemStack(item);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean("Learned", learned);
        return stack;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack; // не дозволяємо вставку
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY; // не дозволяємо витяг
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false; // не дозволяємо ставити предмети
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {

    }


    public void setLearned(Item item, boolean learned) {
        if (patternItems.containsKey(item)) {
            patternItems.put(item, learned);
        }
    }

    public boolean isLearned(Item item) {
        return patternItems.getOrDefault(item, false);
    }

    public @NotNull Item getItemByIndex(int index) {
        return new ArrayList<>(patternItems.keySet()).get(index);
    }

    public @NotNull List<Item> getAllItems() {
        return new ArrayList<>(patternItems.keySet());
    }

    public void setLearnedByIndex(int index, boolean learned) {
        if (index >= 0 && index < getSlots()) {
            Item item = getItemByIndex(index);
            patternItems.put(item, learned);
        }
    }
}

