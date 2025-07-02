package net.eagl.minetorio.datagen.world;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.datagen.world.biome.TestBiome;
import net.eagl.minetorio.datagen.world.biome.VoidBiome;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;

public class MinetorioBiomes {
    public static final ResourceKey<Biome> TEST_BIOME = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID,"test_biome"));

    public static final ResourceKey<Biome> VOID_BIOME = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID,"void_biome"));

    public static void boostrap(BootstapContext<Biome> context) {
        context.register(TEST_BIOME, TestBiome.create(context));
        context.register(VOID_BIOME, VoidBiome.create(context));

    }



}