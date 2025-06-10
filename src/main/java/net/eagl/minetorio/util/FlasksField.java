package net.eagl.minetorio.util;

import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.util.enums.FlaskColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;


public class FlasksField {
    private final EnumMap<FlaskColor, Integer> flasks;
    public static final FlasksField EMPTY = new FlasksField(
            0, 0, 0,
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
    );
    public FlasksField(int red, int green, int black,
                       int purple, int pink, int white,
                       int blue, int yellow, int brown,
                       int cyan, int orange, int gray) {
        this.flasks = new EnumMap<>(FlaskColor.class);
        flasks.put(FlaskColor.RED, red);
        flasks.put(FlaskColor.GREEN, green);
        flasks.put(FlaskColor.BLACK, black);
        flasks.put(FlaskColor.PURPLE, purple);
        flasks.put(FlaskColor.PINK, pink);
        flasks.put(FlaskColor.WHITE, white);
        flasks.put(FlaskColor.BLUE, blue);
        flasks.put(FlaskColor.YELLOW, yellow);
        flasks.put(FlaskColor.BROWN, brown);
        flasks.put(FlaskColor.CYAN, cyan);
        flasks.put(FlaskColor.ORANGE, orange);
        flasks.put(FlaskColor.GRAY, gray);
    }

    public void setFlask(FlaskColor color, int amount) {
        flasks.put(color, amount);
    }

    public int getFlaskAmount(FlaskColor color) {
        return flasks.getOrDefault(color, 0);
    }

    public ItemStack getFlask(FlaskColor color){
        Item item;
        switch (color) {
            case RED -> item = MinetorioItems.FLASK_RED.get();
            case GREEN -> item = MinetorioItems.FLASK_GREEN.get();
            case BLACK -> item = MinetorioItems.FLASK_BLACK.get();
            case PURPLE -> item = MinetorioItems.FLASK_PURPLE.get();
            case PINK -> item = MinetorioItems.FLASK_PINK.get();
            case WHITE -> item = MinetorioItems.FLASK_WHITE.get();
            case BLUE -> item = MinetorioItems.FLASK_BLUE.get();
            case YELLOW -> item = MinetorioItems.FLASK_YELLOW.get();
            case BROWN -> item = MinetorioItems.FLASK_BROWN.get();
            case CYAN -> item = MinetorioItems.FLASK_CYAN.get();
            case ORANGE -> item = MinetorioItems.FLASK_ORANGE.get();
            case GRAY -> item = MinetorioItems.FLASK_GRAY.get();
            default -> throw new IllegalArgumentException("Unknown flask color: " + color);
        }
        return new ItemStack(item);
    }

    public EnumMap<FlaskColor, Integer> getAll() {
        return flasks;
    }

    public int size() {
        return 12;
    }
}

