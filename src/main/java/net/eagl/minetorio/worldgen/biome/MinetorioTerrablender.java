package net.eagl.minetorio.worldgen.biome;

import net.eagl.minetorio.Minetorio;
import terrablender.api.Regions;

public class MinetorioTerrablender {
    public static void registerBiomes() {
        Regions.register(new MinetorioOverworldRegion(Minetorio.resourceLocation("overworld"), 5));
    }
}
