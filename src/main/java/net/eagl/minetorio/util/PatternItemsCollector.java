package net.eagl.minetorio.util;

import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class PatternItemsCollector {
    private static final Logger LOGGER = LogManager.getLogger();
    private static Set<Item> patternItems = null;

    public static Set<Item> getPatternItems() {
        if (patternItems == null) {
            patternItems = new HashSet<>();
            try {
                // Отримуємо всі поля класу MinetorioItems
                Field[] fields = MinetorioItems.class.getDeclaredFields();

                for (Field field : fields) {
                    if (field.getName().startsWith("PATTERN_")) {
                        // Поля у MinetorioItems, судячи з твого коду, це Lazy<Item>, тому дістанемо get()
                        field.setAccessible(true);
                        Object obj = field.get(null); // статичне поле, тому null

                        if (obj instanceof net.minecraftforge.registries.RegistryObject<?>) {
                            // RegistryObject<Item> - викликаємо get()
                            Object item = ((net.minecraftforge.registries.RegistryObject<?>) obj).get();
                            if (item instanceof Item) {
                                patternItems.add((Item) item);
                            }
                        } else if (obj instanceof Item) {
                            patternItems.add((Item) obj);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to access PATTERN_ items via reflection in MinetorioItems", e);
            }
        }
        return patternItems;
    }
}
