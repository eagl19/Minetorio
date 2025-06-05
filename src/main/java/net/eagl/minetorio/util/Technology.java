package net.eagl.minetorio.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class Technology {
    private final String id;
    private final Item displayIcon;
    private final List<String> prerequisites;
    private final List<ItemStack> cost;
    private final boolean hidden;


    private final int x, y, time, count;


    public Technology(String id, Item displayIcon, List<String> prerequisites, List<ItemStack> cost, int time, int count, boolean hidden, int x, int y) {
        this.id = id;
        this.displayIcon = displayIcon;
        this.prerequisites = prerequisites;
        this.cost = cost;
        this.hidden = hidden;
        this.x = x;
        this.y = y;
        this.time = time;
        this.count = count;
    }

    public MutableComponent getBenefit(){
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(displayIcon);
        if (key == null) {
            return Component.empty();
        }
        return Component.translatable("pattern.minetorio." + key.getPath() + ".benefit").withStyle(ChatFormatting.BLUE);

    }

    public MutableComponent getTotalTimeAsString() {
        int totalSeconds = time * this.getCount()/ 20;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return Component.translatable("tooltip.minetorio.learn_time")
                .append(": ").withStyle(ChatFormatting.DARK_BLUE)
                .append(Component.translatable(String.format("%d:%02d:%02d", hours, minutes, seconds))
                        .withStyle(ChatFormatting.BLUE)
                );
    }

    public String getId() {
        return id;
    }

    public Component getDisplayName() {
        return displayIcon.getDescription();
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public Item getDisplayIcon(){
        return displayIcon;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getCount() { return count; }
    public int getTime() { return  time; }
    public List<ItemStack> getCost() {
        return cost;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Component getFormattedCost() {
        return Component.literal(
                cost.stream()
                        .map(s -> s.getCount() + "x " + s.getDisplayName().getString())
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("Free")
        );
    }
}

