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

    public static ItemStack getFlask(FlaskColor color){
        return new ItemStack(getFlaskItemByColor(color),0);
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
        return 12;
    }
}

