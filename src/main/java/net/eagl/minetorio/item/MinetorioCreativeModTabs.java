package net.eagl.minetorio.item;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MinetorioCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Minetorio.MOD_ID);

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab> MINETORIO_TAB = CREATIVE_MODE_TABS.register("minetorio_tab",
            ()-> CreativeModeTab.builder().icon( () -> new ItemStack(MinetorioItems.SAPPHIRE.get()))
                    .title(Component.translatable("creativetab.minetorio_tab"))
                    .displayItems((pParameters,pOutput) ->{
                        pOutput.accept(MinetorioItems.SAPPHIRE.get());
                        pOutput.accept(MinetorioItems.INFINITY.get());
                        pOutput.accept(MinetorioItems.VOID.get());

                        pOutput.accept(MinetorioBlocks.GLOWING_BEDROCK.get());


                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}