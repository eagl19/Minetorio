package net.eagl.minetorio.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.List;

public class DimensionCreator {
    private ResourceKey<DimensionType> dimType;
    private ResourceKey<Biome> biome;

    public Holder<Biome> getBiome(Registry<Biome> biomeRegistry){
        return biomeRegistry.getHolderOrThrow(biome);
    }

    public BiomeSource getBiomeSource(Registry<Biome> biomeRegistry) {
        Holder<Biome> testBiome = getBiome(biomeRegistry);
        Climate.ParameterPoint climatePoint = new Climate.ParameterPoint(
                Climate.Parameter.point(0),
                Climate.Parameter.point(0),
                Climate.Parameter.point(0),
                Climate.Parameter.point(0),
                Climate.Parameter.point(0),
                Climate.Parameter.point(0),
                0
        );

        List<Pair<Climate.ParameterPoint, Holder<Biome>>> entries = List.of(
                Pair.of(climatePoint, testBiome)
        );

        return MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(entries));
    }


    public ResourceKey<DimensionType> getDimType() {
        return dimType;
    }
}
