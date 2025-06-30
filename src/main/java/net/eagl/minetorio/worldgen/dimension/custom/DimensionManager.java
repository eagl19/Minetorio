package net.eagl.minetorio.worldgen.dimension.custom;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.data.MinetorioDimensionSavedData;
import net.eagl.minetorio.worldgen.dimension.MinetorioDimensionTypes;
import net.eagl.minetorio.worldgen.structure.Rooms3x3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.core.Holder;

import commoble.infiniverse.api.InfiniverseAPI;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DimensionManager {

    public static ResourceKey<Level> getOrCreatePlayerDimension(MinecraftServer server, UUID playerUUID) {
        String dimId = "dim_" + playerUUID.toString().replace("-", "");
        ResourceKey<Level> dimKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, dimId));

        InfiniverseAPI.get().getOrCreateLevel(server, dimKey, () -> createVoidDimension(server));

        return dimKey;
    }

    public static void teleportToPlayerDimension(ServerPlayer player) {
        MinecraftServer server = player.server;
        ResourceKey<Level> dimKey = getOrCreatePlayerDimension(server, player.getUUID());

        ServerLevel level = server.getLevel(dimKey);
        if (level != null) {

            MinetorioDimensionSavedData data = MinetorioDimensionSavedData.get(level);
            if (!data.isInitialized()) {
                Rooms3x3.create(level, new BlockPos(0,100,0));
                data.markInitialized();
            }

            BlockPos spawnPos = new BlockPos(0, 100, 0);
            player.setRespawnPosition(dimKey, spawnPos, 0.0F, true, false);

            player.teleportTo(level, 0.5, 100.0, 0.5, player.getYRot(), player.getXRot());


        }
    }

    public static LevelStem createVoidDimension(MinecraftServer server) {
        RegistryAccess access = server.registryAccess();
        Registry<Biome> biomeRegistry = access.registryOrThrow(Registries.BIOME);
        Holder<Biome> biome = biomeRegistry.getHolderOrThrow(Biomes.THE_VOID);

        Holder<DimensionType> dimType = access.registryOrThrow(Registries.DIMENSION_TYPE)
                .getHolderOrThrow(MinetorioDimensionTypes.MINETORIO_DIM_TYPE);

        FlatLevelGeneratorSettings flatSettings = new FlatLevelGeneratorSettings(
                Optional.empty(),
                biome,
                List.of()
        );
        flatSettings.getLayersInfo().clear();
        flatSettings.updateLayers();

        ChunkGenerator generator = new FlatLevelSource(flatSettings);

        return new LevelStem(dimType, generator);
    }
}

