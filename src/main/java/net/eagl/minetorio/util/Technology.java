package net.eagl.minetorio.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Technology {
    private final String id;
    private final Component displayName;
    private final Item displayIcon;
    private final List<String> prerequisites;
    private final List<ItemStack> cost;
    private final boolean hidden;


    private final int x, y, time, count;


    public Technology(String id, Component displayName, Item displayIcon, List<String> prerequisites, List<ItemStack> cost, int time, int count, boolean hidden, int x, int y) {
        this.id = id;
        this.displayName = displayName;
        this.displayIcon = displayIcon;
        this.prerequisites = prerequisites;
        this.cost = cost;
        this.hidden = hidden;
        this.x = x;
        this.y = y;
        this.time = time;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public Component getDisplayName() {
        return displayName;
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

