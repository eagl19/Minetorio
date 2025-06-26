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

                        pOutput.accept(MinetorioItems.COOLING_CORE.get());
                        pOutput.accept(MinetorioItems.DEW_COLLECTOR.get());
                        pOutput.accept(MinetorioItems.ENCHANTED_PIPE.get());
                        pOutput.accept(MinetorioItems.FILTERED_NOZZLE.get());
                        pOutput.accept(MinetorioItems.HYDRO_CATALYST.get());

                        pOutput.accept(MinetorioItems.ABYSSAL_STONE.get());
                        pOutput.accept(MinetorioItems.BLAZE_CRYSTAL.get());
                        pOutput.accept(MinetorioItems.INFERNAL_CATALYST.get());
                        pOutput.accept(MinetorioItems.MAGMATIC_CORE.get());
                        pOutput.accept(MinetorioItems.VOLCANO_ESSENCE.get());

                        pOutput.accept(MinetorioItems.BATTERY_BUSTER.get());
                        pOutput.accept(MinetorioItems.MICRO_INVERTER.get());
                        pOutput.accept(MinetorioItems.NANO_ENCHANTER.get());
                        pOutput.accept(MinetorioItems.PHOTON_AMPLIFIER.get());
                        pOutput.accept(MinetorioItems.QUARTZ_LENS.get());

                        pOutput.accept(MinetorioItems.PATTERN_AIR.get());
                        pOutput.accept(MinetorioItems.PATTERN_BATTERY.get());
                        pOutput.accept(MinetorioItems.PATTERN_CLOUD.get());
                        pOutput.accept(MinetorioItems.PATTERN_EARTH.get());
                        pOutput.accept(MinetorioItems.PATTERN_EMPTY.get());
                        pOutput.accept(MinetorioItems.PATTERN_FIRE.get());
                        pOutput.accept(MinetorioItems.PATTERN_INFINITY.get());
                        pOutput.accept(MinetorioItems.PATTERN_LIGHTNING.get());
                        pOutput.accept(MinetorioItems.PATTERN_MINETORIO.get());
                        pOutput.accept(MinetorioItems.PATTERN_RAIN.get());
                        pOutput.accept(MinetorioItems.PATTERN_RESEARCH_BOOK.get());
                        pOutput.accept(MinetorioItems.PATTERN_SNOW.get());
                        pOutput.accept(MinetorioItems.PATTERN_SNOWFLAKE.get());
                        pOutput.accept(MinetorioItems.PATTERN_SUN.get());
                        pOutput.accept(MinetorioItems.PATTERN_VOID.get());
                        pOutput.accept(MinetorioItems.PATTERN_WATER.get());
                        pOutput.accept(MinetorioItems.PATTERN_WATER_CONSUMER.get());
                        pOutput.accept(MinetorioItems.PATTERN_LAVA_CONSUMER.get());
                        pOutput.accept(MinetorioItems.PATTERN_ENERGY_CONSUMER.get());


                        pOutput.accept(MinetorioItems.FLASK_BLACK.get());
                        pOutput.accept(MinetorioItems.FLASK_BLUE.get());
                        pOutput.accept(MinetorioItems.FLASK_BROWN.get());
                        pOutput.accept(MinetorioItems.FLASK_CYAN.get());
                        pOutput.accept(MinetorioItems.FLASK_GRAY.get());
                        pOutput.accept(MinetorioItems.FLASK_GREEN.get());
                        pOutput.accept(MinetorioItems.FLASK_ORANGE.get());
                        pOutput.accept(MinetorioItems.FLASK_PINK.get());
                        pOutput.accept(MinetorioItems.FLASK_PURPLE.get());
                        pOutput.accept(MinetorioItems.FLASK_RED.get());
                        pOutput.accept(MinetorioItems.FLASK_WHITE.get());
                        pOutput.accept(MinetorioItems.FLASK_YELLOW.get());


                        pOutput.accept(MinetorioBlocks.GLOWING_BEDROCK.get());
                        pOutput.accept(MinetorioBlocks.PATTERNS_COLLECTOR.get());
                        pOutput.accept(MinetorioBlocks.PORTAL.get());
                        pOutput.accept(MinetorioBlocks.RESEARCHER.get());

                        pOutput.accept(MinetorioBlocks.LAVA_GENERATOR.get());
                        pOutput.accept(MinetorioBlocks.WATER_GENERATOR.get());
                        pOutput.accept(MinetorioBlocks.ENERGY_GENERATOR.get());


                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}