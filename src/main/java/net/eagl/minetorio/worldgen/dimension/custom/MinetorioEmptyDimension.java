package net.eagl.minetorio.worldgen.dimension.custom;

import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.biome.Biomes;

import java.util.List;
import java.util.Optional;

public class MinetorioEmptyDimension {
    public static LevelStem create(HolderGetter<Biome> biomes, HolderGetter<DimensionType> dimTypes, ResourceKey<DimensionType> dimTypeKey) {
        FlatLevelGeneratorSettings settings = new FlatLevelGeneratorSettings(
                Optional.empty(),
                biomes.getOrThrow(Biomes.THE_VOID),
                List.of()
        );
        settings.getLayersInfo().clear();
        settings.updateLayers();

        FlatLevelSource source = new FlatLevelSource(settings);

        return new LevelStem(
                dimTypes.getOrThrow(dimTypeKey),
                source
        );
    }
}
