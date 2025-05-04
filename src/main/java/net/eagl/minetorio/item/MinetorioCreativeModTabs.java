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

    public static final RegistryObject<CreativeModeTab> MINETORIO_TAB = CREATIVE_MODE_TABS.register("minetorio_tab",
            ()-> CreativeModeTab.builder().icon( () -> new ItemStack(MinetorioItems.SAPPHIRE.get()))
                    .title(Component.translatable("creativetab.minetorio_tab"))
                    .displayItems((pParameters,pOutput) ->{
                        pOutput.accept(MinetorioItems.SAPPHIRE.get());
                        pOutput.accept(MinetorioItems.RAW_SAPPHIRE.get());
                        pOutput.accept(MinetorioItems.METAL_DETECTOR.get());
                        pOutput.accept(MinetorioItems.STRAWBERRY.get());
                        pOutput.accept(MinetorioItems.STRAWBERRY_SEEDS.get());
                        pOutput.accept(MinetorioItems.CORN.get());
                        pOutput.accept(MinetorioItems.CORN_SEEDS.get());
                        pOutput.accept(MinetorioItems.PINE_CONE.get());
                        pOutput.accept(MinetorioItems.SAPPHIRE_STAFF.get());

                        pOutput.accept(MinetorioItems.SAPPHIRE_SWORD.get());
                        pOutput.accept(MinetorioItems.SAPPHIRE_PICKAXE.get());
                        pOutput.accept(MinetorioItems.SAPPHIRE_AXE.get());
                        pOutput.accept(MinetorioItems.SAPPHIRE_SHOVEL.get());
                        pOutput.accept(MinetorioItems.SAPPHIRE_HOE.get());

                        pOutput.accept(MinetorioItems.BAR_BRAWL_MUSIC_DISC.get());

                        pOutput.accept(MinetorioItems.SAPPHIRE_HELMET.get());
                        pOutput.accept(MinetorioItems.SAPPHIRE_CHESTPLATE.get());
                        pOutput.accept(MinetorioItems.SAPPHIRE_LEGGINGS.get());
                        pOutput.accept(MinetorioItems.SAPPHIRE_BOOTS.get());

                        pOutput.accept(MinetorioBlocks.SAPPHIRE_BLOCK.get());
                        pOutput.accept(MinetorioBlocks.RAW_SAPPHIRE_BLOCK.get());
                        pOutput.accept(MinetorioBlocks.SAPPHIRE_ORE.get());
                        pOutput.accept(MinetorioBlocks.DEEPSLATE_SAPPHIRE_ORE.get());
                        pOutput.accept(MinetorioBlocks.NETHER_SAPPHIRE_ORE.get());
                        pOutput.accept(MinetorioBlocks.END_STONE_SAPPHIRE_ORE.get());
                        pOutput.accept(MinetorioBlocks.DUPLICATOR_BLOCK.get());
                        pOutput.accept(MinetorioBlocks.SOUND_BLOCK.get());

                        pOutput.accept(MinetorioBlocks.SAPPHIRE_BUTTON.get());
                        pOutput.accept(MinetorioBlocks.SAPPHIRE_DOOR.get());
                        pOutput.accept(MinetorioBlocks.SAPPHIRE_FENCE.get());
                        pOutput.accept(MinetorioBlocks.SAPPHIRE_FENCE_GATE.get());
                        pOutput.accept(MinetorioBlocks.SAPPHIRE_PRESSURE_PLATE.get());
                        pOutput.accept(MinetorioBlocks.SAPPHIRE_SLAB.get());
                        pOutput.accept(MinetorioBlocks.SAPPHIRE_STAIRS.get());
                        pOutput.accept(MinetorioBlocks.SAPPHIRE_TRAPDOOR.get());
                        pOutput.accept(MinetorioBlocks.SAPPHIRE_WALL.get());

                        pOutput.accept(MinetorioBlocks.CATMINT.get());
                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
