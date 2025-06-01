package net.eagl.minetorio.util;

import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class TechnologyRegistry {
    private static final Map<String, Technology> TECHNOLOGIES = new HashMap<>();

    public static void register(Technology tech) {
        TECHNOLOGIES.put(tech.getId(), tech);
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
                        Component.literal("Void"),
                        MinetorioItems.PATTERN_VOID.get(),
                        List.of(),
                        List.of(
                                new ItemStack(MinetorioItems.FLASK_RED.get(),1)
                        ),
                        1000,
                        3,
                        false,
                        0,
                        0
                )
        );
        register(
                new Technology(
                        "fire",
                        Component.literal("Fire"),
                        MinetorioItems.PATTERN_FIRE.get(),
                        List.of("void"),
                        List.of(
                                new ItemStack(MinetorioItems.FLASK_RED.get(), 3),
                                new ItemStack(MinetorioItems.FLASK_GREEN.get(), 2),
                                new ItemStack(MinetorioItems.FLASK_BLACK.get(), 1)
                        ),
                        1000,
                        3,
                        false,
                        200,
                        100
                )
        );
        register(
                new Technology(
                        "water",
                        Component.literal("Water"),
                        MinetorioItems.PATTERN_WATER.get(),
                        List.of("void"),
                        List.of(
                                new ItemStack(MinetorioItems.FLASK_RED.get(), 3),
                                new ItemStack(MinetorioItems.FLASK_GREEN.get(), 2),
                                new ItemStack(MinetorioItems.FLASK_BLACK.get(), 1)
                        ),
                        1000,
                        3,
                        false,
                        200,
                        0
                )
        );
    }
}

