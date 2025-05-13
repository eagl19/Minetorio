package net.eagl.minetorio.worldgen.biome;

import net.eagl.minetorio.Minetorio;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Regions;

public class MinetorioTerrablender {
    public static void registerBiomes() {
        Regions.register(new MinetorioOverworldRegion(ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID,"overworld"), 5));
    }
}
