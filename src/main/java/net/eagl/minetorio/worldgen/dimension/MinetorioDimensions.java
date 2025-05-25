package net.eagl.minetorio.worldgen.dimension;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.worldgen.dimension.custom.MinetorioEmptyDimension;
import net.eagl.minetorio.worldgen.dimension.custom.MinetorioTestDimension;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;


public class MinetorioDimensions {
    public static final ResourceKey<LevelStem> MINETORIO_DIM_KEY = ResourceKey.create(
            Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID,"minetorio_dim"));

    public static final ResourceKey<LevelStem> MINETORIO_DIM_EMPTY_KEY = ResourceKey.create(
            Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, "minetorio_dim_empty")
    );


    public static final ResourceKey<Level> MINETORIO_DIM_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID,"minetorio_dim"));

    public static final ResourceKey<Level> MINETORIO_DIM_EMPTY_LEVEL_KEY = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, "minetorio_dim_empty")
    );

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        MinetorioDimensionTypes.bootstrap(context);
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<NoiseGeneratorSettings> noise = context.lookup(Registries.NOISE_SETTINGS);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);

        context.register(MINETORIO_DIM_KEY,
                MinetorioTestDimension.create(biomes, noise, dimTypes, MinetorioDimensionTypes.MINETORIO_DIM_TYPE));

        context.register(MINETORIO_DIM_EMPTY_KEY,
                MinetorioEmptyDimension.create(biomes, dimTypes, MinetorioDimensionTypes.MINETORIO_DIM_TYPE));
    }
}
