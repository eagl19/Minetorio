package net.eagl.minetorio.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class DimensionCreator {
    private ResourceKey<DimensionType> dimType;
    private final List<ResourceKey<Biome>> biome = List.of(Biomes.PLAINS);
    private List<EntityType<?>> entityType;
    private long time;

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
}
