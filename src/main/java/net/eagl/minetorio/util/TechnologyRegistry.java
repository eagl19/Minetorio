package net.eagl.minetorio.util;

import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class TechnologyRegistry {
    private static final Map<String, Technology> TECHNOLOGIES = new HashMap<>();

    public static void register(Technology tech) {
        TECHNOLOGIES.put(tech.id(), tech);
    }

    public static Technology get(String id) {
        return TECHNOLOGIES.get(id);
    }

    public static Collection<Technology> getAll() {
        return TECHNOLOGIES.values();
    }

    public static void init() {
        register(
                new Technology(
                        "void",
                        MinetorioItems.PATTERN_VOID.get(),
                        List.of(),
                        List.of(
                                new ItemStack(MinetorioItems.FLASK_RED.get(),1)
                        ),
                        1000,
                        3,
                        false,
                        10,
                        10
                )
        );
        register(
                new Technology(
                        "fire",
                        MinetorioItems.PATTERN_FIRE.get(),
                        List.of("void"),
                        List.of(
                                new ItemStack(MinetorioItems.FLASK_RED.get(), 3),
                                new ItemStack(MinetorioItems.FLASK_GREEN.get(), 2),
                                new ItemStack(MinetorioItems.FLASK_BLACK.get(), 1)
                        ),
                        10000,
                        3,
                        false,
                        160,
                        100
                )
        );
        register(
                new Technology(
                        "water",
                        MinetorioItems.PATTERN_WATER.get(),
                        List.of("void"),
                        List.of(
                                new ItemStack(MinetorioItems.FLASK_RED.get(), 3),
                                new ItemStack(MinetorioItems.FLASK_GREEN.get(), 2),
                                new ItemStack(MinetorioItems.FLASK_BLACK.get(), 1)
                        ),
                        15000,
                        3,
                        false,
                        160,
                        10
                )
        );
        register(
                new Technology(
                        "cloud",
                        MinetorioItems.PATTERN_CLOUD.get(),
                        List.of("fire","water"),
                        List.of(
                                new ItemStack(MinetorioItems.FLASK_RED.get(), 12),
                                new ItemStack(MinetorioItems.FLASK_GREEN.get(), 11),
                                new ItemStack(MinetorioItems.FLASK_BLACK.get(), 10),
                                new ItemStack(MinetorioItems.FLASK_PURPLE.get(), 9),
                                new ItemStack(MinetorioItems.FLASK_PINK.get(), 8),
                                new ItemStack(MinetorioItems.FLASK_WHITE.get(), 7),
                                new ItemStack(MinetorioItems.FLASK_BLUE.get(), 6),
                                new ItemStack(MinetorioItems.FLASK_YELLOW.get(), 5),
                                new ItemStack(MinetorioItems.FLASK_BROWN.get(), 4),
                                new ItemStack(MinetorioItems.FLASK_CYAN.get(), 3),
                                new ItemStack(MinetorioItems.FLASK_ORANGE.get(), 2),
                                new ItemStack(MinetorioItems.FLASK_GRAY.get(), 1)
                        ),
                        400000,
                        3,
                        false,
                        310,
                        60
                )
        );
        register(
                new Technology(
                        "snowflake",
                        MinetorioItems.PATTERN_SNOWFLAKE.get(),
                        List.of("cloud"),
                        List.of(
                                new ItemStack(MinetorioItems.FLASK_RED.get(), 31),
                                new ItemStack(MinetorioItems.FLASK_GREEN.get(), 21),
                                new ItemStack(MinetorioItems.FLASK_BLACK.get(), 10),
                                new ItemStack(MinetorioItems.FLASK_PURPLE.get(), 9),
                                new ItemStack(MinetorioItems.FLASK_PINK.get(), 8),
                                new ItemStack(MinetorioItems.FLASK_GREEN.get(), 7),
                                new ItemStack(MinetorioItems.FLASK_GREEN.get(), 6),
                                new ItemStack(MinetorioItems.FLASK_GREEN.get(), 5),
                                new ItemStack(MinetorioItems.FLASK_GREEN.get(), 4),
                                new ItemStack(MinetorioItems.FLASK_GREEN.get(), 3),
                                new ItemStack(MinetorioItems.FLASK_GREEN.get(), 2),
                                new ItemStack(MinetorioItems.FLASK_BLACK.get(), 1)
                        ),
                        13000,
                        5,
                        false,
                        310,
                        150
                )
        );
    }
}

