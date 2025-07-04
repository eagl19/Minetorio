package net.eagl.minetorio.util;

import com.mojang.datafixers.util.Pair;
import net.eagl.minetorio.datagen.world.MinetorioBiomes;
import net.eagl.minetorio.worldgen.dimension.MinetorioDimensionTypes;
import net.eagl.minetorio.worldgen.infiniverse.FlatGeneratorSettings;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;

import java.util.ArrayList;
import java.util.List;

public class DimensionCreator {
    private ResourceKey<DimensionType> dimType;
    private final List<ResourceKey<Biome>> biome = new ArrayList<>();
    private final List<EntityType<?>> entityType = new ArrayList<>();
    private final FlatGeneratorSettings flatSettings = new FlatGeneratorSettings();
    private boolean isNoise = false;
    private long time;
    private final MinecraftServer server;

    public DimensionCreator(MinecraftServer server) {
        this.server = server;
    }

    public LevelStem flatDimension() {
        RegistryAccess access = server.registryAccess();
        Registry<Biome> biomeRegistry = access.registryOrThrow(Registries.BIOME);
        flatSettings.setBiome(getBiomeHolder(biomeRegistry));
        Holder<DimensionType> dimTypeFlat = access.registryOrThrow(Registries.DIMENSION_TYPE)
                .getHolderOrThrow(dimType);
        return new LevelStem(dimTypeFlat, new FlatLevelSource(flatSettings.build(access)));
    }

    public LevelStem noiseDimension(){
        RegistryAccess access = server.registryAccess();

        Registry<Biome> biomeRegistry = access.registryOrThrow(Registries.BIOME);
        Registry<DimensionType> dimTypeRegistry = access.registryOrThrow(Registries.DIMENSION_TYPE);
        Registry<NoiseGeneratorSettings> noiseSettingsRegistry = access.registryOrThrow(Registries.NOISE_SETTINGS);

        Holder<DimensionType> dimTypeNoise = dimTypeRegistry.getHolderOrThrow(dimType);

        Holder<NoiseGeneratorSettings> noiseSettings = noiseSettingsRegistry
                .getHolderOrThrow(NoiseGeneratorSettings.AMPLIFIED);


        NoiseBasedChunkGenerator generator = new NoiseBasedChunkGenerator(
                getBiomeSource(biomeRegistry),
                noiseSettings
        );

        return new LevelStem(dimTypeNoise, generator);
    }

    public FlatGeneratorSettings getFlatSettings() {
        return flatSettings;
    }

    public List<ResourceKey<Biome>> getBiome() {
        return biome;
    }

    public Holder<Biome> getBiomeHolder(Registry<Biome> biomeRegistry){
        return biomeRegistry.getHolderOrThrow(biome.get(0));
    }

    public BiomeSource getBiomeSource(Registry<Biome> biomeRegistry) {
        Climate.ParameterPoint climatePoint = new Climate.ParameterPoint(
                Climate.Parameter.point(0),
                Climate.Parameter.point(0),
                Climate.Parameter.point(0),
                Climate.Parameter.point(0),
                Climate.Parameter.point(0),
                Climate.Parameter.point(0),
                0
        );

        List<Pair<Climate.ParameterPoint, Holder<Biome>>> entries = new ArrayList<>();

        for (ResourceKey<Biome> biomeResourceKey : biome) {
            entries.add(Pair.of(climatePoint, biomeRegistry.getHolderOrThrow(biomeResourceKey)));
        }

        return MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(entries));
    }

    public List<EntityType<?>> getEntityType(){
        return entityType;
    }

    public long getTime() {
        return time;
    }

    public ResourceKey<DimensionType> getDimType() {
        return dimType;
    }

    public void setDimType(ResourceKey<DimensionType> dimType){
        this.dimType = dimType;
    }
    public boolean isNoise() {
        return isNoise;
    }

    public void setNoise(boolean noise) {
        isNoise = noise;
    }
}
