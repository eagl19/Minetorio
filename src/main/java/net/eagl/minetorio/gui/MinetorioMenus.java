package net.eagl.minetorio.gui;

import net.eagl.minetorio.Minetorio;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class MinetorioMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Minetorio.MOD_ID);

    public static final RegistryObject<MenuType<PatternsCollectorMenu>> PATTERNS_COLLECTOR_MENU =
            MENUS.register("patterns_collector_menu",
                    () -> IForgeMenuType.create(PatternsCollectorMenu::new));

    public static final RegistryObject<MenuType<ResearcherMenu>> RESEARCHER_MENU =
            MENUS.register("researcher_menu",
                    () -> IForgeMenuType.create(ResearcherMenu::new));

    public static final RegistryObject<MenuType<WaterGeneratorMenu>> WATER_GENERATOR_MENU =
            MENUS.register("water_generator_menu",
                    () -> IForgeMenuType.create(WaterGeneratorMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
