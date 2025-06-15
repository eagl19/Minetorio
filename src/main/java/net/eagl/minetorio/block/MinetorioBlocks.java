package net.eagl.minetorio.block;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.custom.*;
import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class MinetorioBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Minetorio.MOD_ID);

    public static final RegistryObject<Block> GLOWING_BEDROCK = registerBlock("glowing_bedrock",
             GlowingBedrockBlock::new);

    public static final RegistryObject<Block> PATTERNS_COLLECTOR = registerBlock("patterns_collector",
            PatternsCollectorBlock::new);

    public static final RegistryObject<Block> PORTAL = registerBlock("portal",
            PortalBlock::new);

    public static final RegistryObject<Block> RESEARCHER = registerBlock("researcher",
            ResearchBlock::new);

    public static final RegistryObject<Block> STATIC_WATER_BLOCK = BLOCKS.register("static_water_block",
            () -> new StaticFluidBlock(BlockBehaviour.Properties.copy(Blocks.WATER)
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()));

    public static final RegistryObject<Block> STATIC_LAVA_BLOCK = BLOCKS.register("static_lava_block",
            () -> new StaticFluidBlock(BlockBehaviour.Properties.copy(Blocks.LAVA)
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name,toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){

        return MinetorioItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}