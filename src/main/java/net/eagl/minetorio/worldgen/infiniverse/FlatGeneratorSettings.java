package net.eagl.minetorio.worldgen.infiniverse;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.*;

public class FlatGeneratorSettings {

    private final List<FlatLayerInfo> layers = new ArrayList<>();
    private final List<ResourceKey<StructureSet>> structureSetKeys = new ArrayList<>();
    private Holder<Biome> biome = null;

    public FlatGeneratorSettings addLayer(int height, Block block) {
        layers.add(new FlatLayerInfo(height, block));
        return this;
    }

    public FlatGeneratorSettings setBiome(Holder<Biome> biome) {
        this.biome = biome;
        return this;
    }

    public FlatGeneratorSettings addStructureSet(ResourceKey<StructureSet> key) {
        structureSetKeys.add(key);
        return this;
    }

    public FlatLevelGeneratorSettings build(RegistryAccess access) {
        Registry<StructureSet> structureSetRegistry = access.registryOrThrow(Registries.STRUCTURE_SET);
        Registry<PlacedFeature> featureRegistry = access.registryOrThrow(Registries.PLACED_FEATURE);

        List<Holder<PlacedFeature>> lakes = List.of(
                featureRegistry.getHolderOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND),
                featureRegistry.getHolderOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE)
        );

        List<Holder<StructureSet>> holders = new ArrayList<>();
        for (ResourceKey<StructureSet> key : structureSetKeys) {
            holders.add(structureSetRegistry.getHolderOrThrow(key));
        }

        Optional<HolderSet<StructureSet>> structureOverrides = holders.isEmpty()
                ? Optional.empty()
                : Optional.of(HolderSet.direct(holders));

        FlatLevelGeneratorSettings settings = new FlatLevelGeneratorSettings(
                structureOverrides,
                Objects.requireNonNull(biome, "Biome must be set before building"),
                lakes
        );

        settings.getLayersInfo().clear();
        if(!layers.isEmpty()) {
            settings.getLayersInfo().addAll(layers);
        }
        settings.updateLayers();
        return settings;
    }
}
