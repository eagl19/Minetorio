package net.eagl.minetorio.worldgen.dimension.custom;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.data.MinetorioDimensionSavedData;
import net.eagl.minetorio.data.PlayerSettings;
import net.eagl.minetorio.data.PlayerWorldSettingsData;
import net.eagl.minetorio.util.DimensionCreator;
import net.eagl.minetorio.worldgen.dimension.MinetorioDimensionTypes;
import net.eagl.minetorio.worldgen.structure.Rooms3x3;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.dimension.LevelStem;

import commoble.infiniverse.api.InfiniverseAPI;

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
        DimensionCreator dimensionCreator = new DimensionCreator(server);
        dimensionCreator.setNoise(false);
        dimensionCreator.setDimType(MinetorioDimensionTypes.MINETORIO_DIM_TYPE);
        dimensionCreator.getBiome().add(Biomes.THE_VOID);
        return dimensionCreator.flatDimension();
    }

    public static void teleportToDimension(ServerPlayer player, String dimId, DimensionCreator dimCreator) {

        String dimIdd = dimId + "_" + player.getUUID().toString().replace("-", "");

        ResourceKey<Level> dimKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, dimIdd));

        if(dimCreator.isNoise()) {
            InfiniverseAPI.get().getOrCreateLevel(player.server, dimKey, dimCreator::noiseDimension);
        }else {
            InfiniverseAPI.get().getOrCreateLevel(player.server, dimKey, dimCreator::flatDimension);
        }

        ServerLevel level = player.server.getLevel(dimKey);
        if (level != null) {
            PlayerWorldSettingsData data = PlayerWorldSettingsData.get(level);
            PlayerSettings settings = data.getOrCreate(player.getUUID());
            settings.getAllowedMobs().clear();
            settings.getAllowedMobs().addAll(dimCreator.getEntityType());
            settings.setInitialized(true);
            data.setDirty();
            BlockPos safePos = findSafeGround(level, 0, 0, level.getMinBuildHeight(), level.getMaxBuildHeight());

            if (safePos != null) {
                player.teleportTo(level,
                        safePos.getX() + 0.5,
                        safePos.getY(),
                        safePos.getZ() + 0.5,
                        player.getYRot(),
                        player.getXRot());
            } else {
                player.teleportTo(level, 0.5, 100.0, 0.5, player.getYRot(), player.getXRot());
            }
        }
    }

    public static BlockPos findSafeGround(ServerLevel level, int x, int z, int minY, int maxY) {
        for (int y = maxY; y >= minY; y--) {
            BlockPos pos = new BlockPos(x, y, z);
            BlockPos above = pos.above();
            BlockPos twoAbove = above.above();

            if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                    && level.getBlockState(above).isAir()
                    && level.getBlockState(twoAbove).isAir()) {
                return above;
            }
        }
        return null;
    }

}

