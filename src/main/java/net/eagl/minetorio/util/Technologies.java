package net.eagl.minetorio.util;

import net.eagl.minetorio.item.MinetorioItems;

import java.util.Collections;

public class Technologies {
    public static Technology EMPTY;
    public static Technology VOID;
    public static Technology FIRE;
    public static Technology WATER;
    public static Technology AIR;
    public static Technology EARTH;
    public static Technology CLOUD;
    public static Technology SNOWFLAKE;
    public static Technology SNOW;
    public static Technology SUN;

    public static void init() {
        EMPTY = new Technology(
                "empty",
                MinetorioItems.PATTERN_EMPTY.get(),
                Collections.emptyList(),
                FlasksField.EMPTY,
                0,
                0,
                0,
                0
        );

        VOID = TechnologyRegistry.get("void");
        FIRE = TechnologyRegistry.get("fire");
        WATER = TechnologyRegistry.get("water");
        AIR = TechnologyRegistry.get("air");
        EARTH = TechnologyRegistry.get("earth");
        CLOUD = TechnologyRegistry.get("cloud");
        SNOWFLAKE = TechnologyRegistry.get("snowflake");
        SNOW = TechnologyRegistry.get("snow");
        SUN = TechnologyRegistry.get("sun");
    }
}
