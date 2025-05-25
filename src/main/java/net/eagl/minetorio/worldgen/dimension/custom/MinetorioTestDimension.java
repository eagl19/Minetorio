package net.eagl.minetorio.worldgen.dimension.custom;

import com.mojang.datafixers.util.Pair;
import net.eagl.minetorio.worldgen.biome.MinetorioBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.biome.Climate;

import java.util.List;

public class MinetorioTestDimension {
    public static LevelStem create(HolderGetter<Biome> biomes, HolderGetter<NoiseGeneratorSettings> noiseSettings, HolderGetter<DimensionType> dimTypes, ResourceKey<DimensionType> dimTypeKey) {
        NoiseBasedChunkGenerator generator = new NoiseBasedChunkGenerator(
                net.minecraft.world.level.biome.MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(Pair.of(
                                Climate.parameters(0, 0, 0, 0, 0, 0, 0),
                                biomes.getOrThrow(MinetorioBiomes.TEST_BIOME)
                        )))
                ),
                noiseSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED)
        );

        return new LevelStem(
                dimTypes.getOrThrow(dimTypeKey),
                generator
        );
    }
}

