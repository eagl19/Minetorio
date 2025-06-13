package net.eagl.minetorio.util;

import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.util.enums.FlaskColor;

import java.util.*;

public class TechnologyRegistry {
    private static final Map<String, Technology> TECHNOLOGIES = new HashMap<>();

    public static void register(Technology tech) {
        if (TECHNOLOGIES.containsKey(tech.getId())) {
            throw new IllegalArgumentException("Technology ID already registered: " + tech.getId());
        }
        TECHNOLOGIES.put(tech.getId(), tech);
    }

    public static Technology get(String id) {
        return TECHNOLOGIES.getOrDefault(id, Technology.EMPTY);
    }

    public static Collection<Technology> getAll() {
        return TECHNOLOGIES.values();
    }

    public static void init() {
        register(
                TechnologyBuilder.create("void")
                        .pattern(MinetorioItems.PATTERN_VOID.get())
                        .flasks(new FlasksField.Builder()
                                .set(FlaskColor.RED,1)
                                .build())
                        .duration(100)
                        .tier(3)
                        .pos(10, 10)
                        .build()
        );
        register(
                TechnologyBuilder.create("fire")
                        .pattern(MinetorioItems.PATTERN_FIRE.get())
                        .requires("void")
                        .flasks(new FlasksField.Builder()
                                .set(FlaskColor.RED, 3)
                                .set(FlaskColor.GREEN, 2)
                                .set(FlaskColor.BLACK, 1)
                                .build())
                        .duration(500)
                        .tier(3)
                        .pos(160, 10)
                        .build()
        );
        register(
                TechnologyBuilder.create("water")
                        .pattern(MinetorioItems.PATTERN_WATER.get())
                        .requires("void")
                        .flasks(new FlasksField.Builder()
                                .set(FlaskColor.RED, 3)
                                .set(FlaskColor.GREEN, 2)
                                .set(FlaskColor.BLACK, 1)
                                .build())
                        .duration(15000)
                        .tier(3)
                        .pos(160, 100)
                        .build()
        );
        register(
                TechnologyBuilder.create("air")
                        .pattern(MinetorioItems.PATTERN_AIR.get())
                        .requires("void")
                        .flasks(new FlasksField.Builder()
                                .set(FlaskColor.RED, 3)
                                .set(FlaskColor.GREEN, 2)
                                .set(FlaskColor.BLACK, 1)
                                .build())
                        .duration(15000)
                        .tier(3)
                        .pos(160, 190)
                        .build()
        );
        register(
                TechnologyBuilder.create("earth")
                        .pattern(MinetorioItems.PATTERN_EARTH.get())
                        .requires("void")
                        .flasks(new FlasksField.Builder()
                                .set(FlaskColor.RED, 3)
                                .set(FlaskColor.GREEN, 2)
                                .set(FlaskColor.BLACK, 1)
                                .build())
                        .duration(15000)
                        .tier(3)
                        .pos(160, 280)
                        .build()
        );
        register(
                TechnologyBuilder.create("cloud")
                        .pattern(MinetorioItems.PATTERN_CLOUD.get())
                        .requires("fire","water")
                        .flasks(new FlasksField.Builder()
                                .set(FlaskColor.RED, 12)
                                .set(FlaskColor.GREEN, 11)
                                .set(FlaskColor.BLACK, 10)
                                .set(FlaskColor.PURPLE, 9)
                                .set(FlaskColor.PINK, 8)
                                .set(FlaskColor.WHITE, 7)
                                .set(FlaskColor.BLUE, 6)
                                .set(FlaskColor.YELLOW, 5)
                                .set(FlaskColor.BROWN, 4)
                                .set(FlaskColor.CYAN, 3)
                                .set(FlaskColor.ORANGE, 2)
                                .set(FlaskColor.GRAY, 1)
                                .build())
                        .duration(400000)
                        .tier(3)
                        .pos(310, 60)
                        .build()
        );
        register(
                TechnologyBuilder.create("snowflake")
                        .pattern(MinetorioItems.PATTERN_SNOWFLAKE.get())
                        .requires("cloud")
                        .flasks(new FlasksField.Builder()
                                .set(FlaskColor.RED, 31)
                                .set(FlaskColor.GREEN, 21)
                                .set(FlaskColor.BLACK, 10)
                                .set(FlaskColor.PURPLE, 9)
                                .set(FlaskColor.PINK, 8)
                                .set(FlaskColor.WHITE, 7)
                                .set(FlaskColor.BLUE, 6)
                                .set(FlaskColor.YELLOW, 5)
                                .set(FlaskColor.BROWN, 4)
                                .set(FlaskColor.CYAN, 3)
                                .set(FlaskColor.ORANGE, 2)
                                .set(FlaskColor.GRAY, 1)
                                .build())
                        .duration(13000)
                        .tier(5)
                        .pos(450, 60)
                        .build()
        );
        register(
                TechnologyBuilder.create("snow")
                        .pattern(MinetorioItems.PATTERN_SNOW.get())
                        .requires("snowflake")
                        .flasks(new FlasksField.Builder()
                                .set(FlaskColor.RED, 31)
                                .set(FlaskColor.GREEN, 21)
                                .set(FlaskColor.BLACK, 10)
                                .set(FlaskColor.PURPLE, 9)
                                .set(FlaskColor.PINK, 8)
                                .set(FlaskColor.WHITE, 7)
                                .set(FlaskColor.BLUE, 6)
                                .set(FlaskColor.YELLOW, 5)
                                .set(FlaskColor.BROWN, 4)
                                .set(FlaskColor.CYAN, 3)
                                .set(FlaskColor.ORANGE, 2)
                                .set(FlaskColor.GRAY, 1)
                                .build())
                        .duration(13000)
                        .tier(5)
                        .pos(610, 60)
                        .build()
        );
        register(
                TechnologyBuilder.create("sun")
                        .pattern(MinetorioItems.PATTERN_SUN.get())
                        .requires("fire")
                        .flasks(new FlasksField.Builder()
                                .set(FlaskColor.RED, 3)
                                .set(FlaskColor.GREEN, 2)
                                .set(FlaskColor.BLACK, 1)
                                .build())
                        .duration(10000)
                        .tier(3)
                        .pos(310, -30)
                        .build()
        );
    }
}

