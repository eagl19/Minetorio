package net.eagl.minetorio.item;

import net.eagl.minetorio.Minetorio;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MinetorioItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Minetorio.MOD_ID);


    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire",
            ()-> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PATTERN_INFINITY = ITEMS.register("pattern_infinity",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_VOID = ITEMS.register("pattern_void",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_CLOUD = ITEMS.register("pattern_cloud",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_FIRE = ITEMS.register("pattern_fire",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_WATER = ITEMS.register("pattern_water",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_EARTH = ITEMS.register("pattern_earth",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_AIR = ITEMS.register("pattern_air",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_SUN = ITEMS.register("pattern_sun",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_RAIN = ITEMS.register("pattern_rain",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_SNOWFLAKE = ITEMS.register("pattern_snowflake",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_SNOW = ITEMS.register("pattern_snow",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_LIGHTNING = ITEMS.register("pattern_lightning",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_BATTERY = ITEMS.register("pattern_battery",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_RESEARCH_BOOK = ITEMS.register("pattern_research_book",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_MINETORIO = ITEMS.register("pattern_minetorio",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PATTERN_EMPTY = ITEMS.register("pattern_empty",
            ()-> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FLASK_BLACK = ITEMS.register("flask_black",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLASK_BLUE = ITEMS.register("flask_blue",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLASK_BROWN = ITEMS.register("flask_brown",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLASK_CYAN = ITEMS.register("flask_cyan",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLASK_GRAY = ITEMS.register("flask_gray",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLASK_GREEN = ITEMS.register("flask_green",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLASK_ORANGE = ITEMS.register("flask_orange",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLASK_PINK = ITEMS.register("flask_pink",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLASK_PURPLE = ITEMS.register("flask_purple",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLASK_RED = ITEMS.register("flask_red",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLASK_WHITE = ITEMS.register("flask_white",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLASK_YELLOW = ITEMS.register("flask_yellow",
            ()-> new Item(new Item.Properties()));



    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
