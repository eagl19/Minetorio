package net.eagl.minetorio.util;

import net.eagl.minetorio.item.MinetorioItems;

import java.util.*;

public class TechnologyRegistry {
    private static final Map<String, Technology> TECHNOLOGIES = new HashMap<>();

    public static void register(Technology tech) {
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
                new Technology(
                        "void",
                        MinetorioItems.PATTERN_VOID.get(),
                        List.of(),
                        new FlasksField(1,0,0,0,0,0,0,0,0,0,0,0),
                        1000,
                        3,
                        10,
                        10
                )
        );
        register(
                new Technology(
                        "fire",
                        MinetorioItems.PATTERN_FIRE.get(),
                        List.of("void"),
                        new FlasksField(3,2,1,0,0,0,0,0,0,0,0,0),
                        10000,
                        3,
                        160,
                        10
                )
        );
        register(
                new Technology(
                        "water",
                        MinetorioItems.PATTERN_WATER.get(),
                        List.of("void"),
                        new FlasksField(3,2,1,0,0,0,0,0,0,0,0,0),
                        15000,
                        3,
                        160,
                        100
                )
        );
        register(
                new Technology(
                        "air",
                        MinetorioItems.PATTERN_AIR.get(),
                        List.of("void"),
                        new FlasksField(3,2,1,0,0,0,0,0,0,0,0,0),
                        15000,
                        3,
                        160,
                        190
                )
        );
        register(
                new Technology(
                        "earth",
                        MinetorioItems.PATTERN_EARTH.get(),
                        List.of("void"),
                        new FlasksField(3,2,1,0,0,0,0,0,0,0,0,0),
                        15000,
                        3,
                        160,
                        280
                )
        );
        register(
                new Technology(
                        "cloud",
                        MinetorioItems.PATTERN_CLOUD.get(),
                        List.of("fire","water"),
                        new FlasksField(12,11,10,9,8,7,6,5,4,3,2,1),
                        400000,
                        3,
                        310,
                        60
                )
        );
        register(
                new Technology(
                        "snowflake",
                        MinetorioItems.PATTERN_SNOWFLAKE.get(),
                        List.of("cloud"),
                        new FlasksField(31,21,10,9,8,7,6,5,4,3,2,1),
                        13000,
                        5,
                        450,
                        60
                )
        );
        register(
                new Technology(
                        "snow",
                        MinetorioItems.PATTERN_SNOW.get(),
                        List.of("snowflake"),
                        new FlasksField(31,21,10,9,8,7,6,5,4,3,2,1),
                        13000,
                        5,
                        610,
                        60
                )
        );
        register(
                new Technology(
                        "sun",
                        MinetorioItems.PATTERN_SUN.get(),
                        List.of("fire"),
                        new FlasksField(3,2,1,0,0,0,0,0,0,0,0,0),
                        10000,
                        3,
                        310,
                        -30
                )
        );
    }
}

