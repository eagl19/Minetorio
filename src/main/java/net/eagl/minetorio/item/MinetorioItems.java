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

    public static final RegistryObject<Item> INFINITY = ITEMS.register("infinity",
            ()-> new Item(new Item.Properties()));

    public static final RegistryObject<Item> VOID = ITEMS.register("void",
            ()-> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
