package net.eagl.minetorio.util;

import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.util.enums.FlaskColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;

public class FlasksField {
    private final EnumMap<FlaskColor, Integer> flasks;

    public static final FlasksField EMPTY = new Builder().build();

    public FlasksField(EnumMap<FlaskColor, Integer> flasks) {
        this.flasks = new EnumMap<>(flasks);
    }

    public void setFlask(FlaskColor color, int amount) {
        flasks.put(color, amount);
    }

    public int getFlaskAmount(FlaskColor color) {
        return flasks.getOrDefault(color, 0);
    }

    public static ItemStack getFlask(FlaskColor color) {
        return new ItemStack(getFlaskItemByColor(color), 0);
    }

    public static Item getFlaskItemByColor(FlaskColor color) {
        return switch (color) {
            case RED -> MinetorioItems.FLASK_RED.get();
            case GREEN -> MinetorioItems.FLASK_GREEN.get();
            case BLACK -> MinetorioItems.FLASK_BLACK.get();
            case PURPLE -> MinetorioItems.FLASK_PURPLE.get();
            case PINK -> MinetorioItems.FLASK_PINK.get();
            case WHITE -> MinetorioItems.FLASK_WHITE.get();
            case BLUE -> MinetorioItems.FLASK_BLUE.get();
            case YELLOW -> MinetorioItems.FLASK_YELLOW.get();
            case BROWN -> MinetorioItems.FLASK_BROWN.get();
            case CYAN -> MinetorioItems.FLASK_CYAN.get();
            case ORANGE -> MinetorioItems.FLASK_ORANGE.get();
            case GRAY -> MinetorioItems.FLASK_GRAY.get();
        };
    }

    public EnumMap<FlaskColor, Integer> getAll() {
        return flasks;
    }

    public int size() {
        return FlaskColor.values().length;
    }

    public boolean isEmpty() {
        return flasks.values().stream().allMatch(v -> v == 0);
    }

    public FlasksField add(FlasksField other) {
        Builder builder = new Builder();
        for (FlaskColor color : FlaskColor.values()) {
            int sum = this.getFlaskAmount(color) + other.getFlaskAmount(color);
            builder.set(color, sum);
        }
        return builder.build();
    }

    public static class Builder {
        private final EnumMap<FlaskColor, Integer> flasks = new EnumMap<>(FlaskColor.class);

        public Builder() {
            for (FlaskColor color : FlaskColor.values()) {
                flasks.put(color, 0);
            }
        }

        public Builder set(FlaskColor color, int amount) {
            flasks.put(color, amount);
            return this;
        }

        public Builder setAll(int amount) {
            for (FlaskColor color : FlaskColor.values()) {
                flasks.put(color, amount);
            }
            return this;
        }

        public FlasksField build() {
            return new FlasksField(flasks);
        }
    }
}
