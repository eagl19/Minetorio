package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.worldgen.biome.MinetorioBiomes;
import net.eagl.minetorio.worldgen.dimension.MinetorioDimensions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;


import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MinetorioWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, MinetorioDimensions::bootstrapType)
            //.add(Registries.CONFIGURED_FEATURE, MinetorioConfiguredFeatures::bootstrap)
            //.add(Registries.PLACED_FEATURE, MinetorioPlacedFeatures::bootstrap)
            //.add(ForgeRegistries.Keys.BIOME_MODIFIERS, MinetorioBiomeModifiers::bootstrap)
            .add(Registries.BIOME, MinetorioBiomes::boostrap)
            .add(Registries.LEVEL_STEM, MinetorioDimensions::bootstrapStem);

    public MinetorioWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Minetorio.MOD_ID));
    }
}
