package net.eagl.minetorio.worldgen.dimension;

import com.mojang.datafixers.util.Pair;
import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.worldgen.biome.MinetorioBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.List;
import java.util.OptionalLong;

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

    public static final ResourceKey<DimensionType> MINETORIO_DIM_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID,"minetorio_dim_type"));


    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(MINETORIO_DIM_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                1.0, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                0, // minY
                256, // height
                256, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                1.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(MinetorioBiomes.TEST_BIOME)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(Pair.of(
                                        Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(MinetorioBiomes.TEST_BIOME))                        ))),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(MinetorioDimensions.MINETORIO_DIM_DIM_TYPE), noiseBasedChunkGenerator);

        context.register(MINETORIO_DIM_KEY, stem);

        NoiseBasedChunkGenerator emptyBiomeChunkGen = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(MinetorioBiomes.EMPTY_BIOME)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));


        LevelStem emptyBiomeStem = new LevelStem(
                dimTypes.getOrThrow(MINETORIO_DIM_DIM_TYPE),
                emptyBiomeChunkGen);

        context.register(MINETORIO_DIM_EMPTY_KEY, emptyBiomeStem);

    }
}
