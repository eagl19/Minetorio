package net.eagl.minetorio.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Technology {
    private final String id;
    private final Component displayName;
    private final List<String> prerequisites;
    private final List<ItemStack> cost;
    private final boolean hidden;

    private final int x, y;
    public int getX() { return x; }
    public int getY() { return y; }

    public Technology(String id, Component displayName, List<String> prerequisites, List<ItemStack> cost, boolean hidden, int x, int y) {
        this.id = id;
        this.displayName = displayName;
        this.prerequisites = prerequisites;
        this.cost = cost;
        this.hidden = hidden;
        this.x = x;
        this.y = y;
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

