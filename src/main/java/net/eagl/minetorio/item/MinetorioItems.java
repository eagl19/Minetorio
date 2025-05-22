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



    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
