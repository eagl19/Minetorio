package net.eagl.minetorio.util;

import net.eagl.minetorio.Minetorio;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MinetorioTags {

    public static class Blocks{
        public static final TagKey<Block> METAL_DETECTOR_VALUABLES = tag("metal_detector_valuables");
        public static final TagKey<Block> ORES = tag("ores");
        public static final TagKey<Block> NEEDS_SAPPHIRE_TOOL = tag("needs_sapphire_tool");


        private static TagKey<Block> tag(String name){
            return TagKey.create(Registries.BLOCK, Minetorio.resourceLocation(name));
        }

    }

    public  static class Items{
        public static final TagKey<Item> FOODS = tag("foods");
        public static final TagKey<Item> FUELS = tag("fuels");

        private static TagKey<Item> tag(String name){
            return TagKey.create(Registries.ITEM, Minetorio.resourceLocation(name));
        }
    }
}
