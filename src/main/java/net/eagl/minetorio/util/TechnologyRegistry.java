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
        register(new Technology("logistics", Component.literal("Logistics"), List.of(), Collections.singletonList(new ItemStack(MinetorioItems.FLASK_RED.get(),100)), false, 0,0));
        register(new Technology("automation", Component.literal("Automation"), List.of("logistics"), Collections.singletonList(new ItemStack(MinetorioItems.FLASK_RED.get(),200)), false, 100,100));
    }
}

