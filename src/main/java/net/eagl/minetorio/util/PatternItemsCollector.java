package net.eagl.minetorio.util;

import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PatternItemsCollector {
    private static final Logger LOGGER = LogManager.getLogger();
    private static Set<Item> patternItems = null;

    public static Set<Item> getPatternItems() {
        if (patternItems == null) {
            Set<Item> result = new HashSet<>();
            try {
                Field[] fields = MinetorioItems.class.getDeclaredFields();

                for (Field field : fields) {
                    if (field.getName().startsWith("PATTERN_")) {
                        field.setAccessible(true);
                        Object obj = field.get(null);

                        if (obj instanceof RegistryObject<?> registryObject) {
                            Object item = registryObject.get();
                            if (item instanceof Item) {
                                result.add((Item) item);
                            }
                        } else if (obj instanceof Item) {
                            result.add((Item) obj);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to access PATTERN_ items via reflection in MinetorioItems", e);
            }
            patternItems = Collections.unmodifiableSet(result);
        }
        return patternItems;
    }
}
