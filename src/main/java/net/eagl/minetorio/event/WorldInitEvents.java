package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.GlowingBedrockBlock;
import net.eagl.minetorio.block.custom.GlowingBedrockBlockState;
import net.eagl.minetorio.block.entity.PortalBlockEntity;
import net.eagl.minetorio.data.MinetorioDimensionSavedData;
import net.eagl.minetorio.worldgen.dimension.MinetorioDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class WorldInitEvents {

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        if (!level.dimension().equals(MinetorioDimensions.MINETORIO_DIM_EMPTY_LEVEL_KEY)) return;

        MinetorioDimensionSavedData data = MinetorioDimensionSavedData.get(level);

        if (!data.isInitialized()) {

            spawnRoom(level, new BlockPos(0, 100, 0));

            patternCollectorRoom(level, new BlockPos(1000,100,1000));

            data.markInitialized();
        }
    }

    private static void setBlockPortalBlock(ServerLevel level, BlockPos blockPos, BlockPos teleportPos){

        level.setBlockAndUpdate(blockPos, MinetorioBlocks.PORTAL.get().defaultBlockState());
        BlockEntity be = level.getBlockEntity(blockPos);

        if (be instanceof PortalBlockEntity portalBE) {
            portalBE.setTeleportTarget(teleportPos);

        }

    }

    private static  void  patternCollectorRoom(ServerLevel level, BlockPos blockPos){

        final List<BlockPos> TELEPORT_POSITIONS = List.of(
                new BlockPos(1000, 100, 1000),//TODO замінити
                new BlockPos(1000, 100, 1000),//TODO замінити
                new BlockPos(1000, 100, 1000),//TODO замінити
                new BlockPos(1000, 100, 1000)//TODO замінити
        );
        level.setBlockAndUpdate(blockPos.offset(0, 2, 0), MinetorioBlocks.PATTERNS_COLLECTOR.get().defaultBlockState());

        for (int dx = 10; dx >= -10; dx--) {
            for (int dz = 10; dz >= -10; dz--) {
                level.setBlockAndUpdate(blockPos.offset(dx, -1, dz), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx, 17, dz), Blocks.BARRIER.defaultBlockState());
            }
        }
        for (int dx = 8; dx >= -8; dx--) {
            for (int dy = 0; dy <= 16; dy++) {
                level.setBlockAndUpdate(blockPos.offset(dx, dy, 10),  Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx, dy, -10), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(10, dy, dx),   Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(-10, dy, dx),  Blocks.BARRIER.defaultBlockState());

                setBlockPortalBlock(level, blockPos.offset(dx,dy,9),    TELEPORT_POSITIONS.get(0));
                setBlockPortalBlock(level, blockPos.offset(dx,dy,-9),   TELEPORT_POSITIONS.get(1));
                setBlockPortalBlock(level, blockPos.offset(9,  dy, dx), TELEPORT_POSITIONS.get(2));
                setBlockPortalBlock(level, blockPos.offset(-9, dy, dx), TELEPORT_POSITIONS.get(3));

            }

        }

    }

    private static void spawnRoom(ServerLevel level, BlockPos blockPos){

        BlockState glowingBedrock = MinetorioBlocks.GLOWING_BEDROCK.get().defaultBlockState().setValue(GlowingBedrockBlock.STATE, GlowingBedrockBlockState.BEDROCK);

        final List<BlockPos> TELEPORT_POSITIONS = List.of(
                new BlockPos(1000, 100, 1000),
                new BlockPos(1000, 100, 1000),//TODO замінити
                new BlockPos(1000, 100, 1000),//TODO замінити
                new BlockPos(1000, 100, 1000)//TODO замінити
        );

        for (int dx = 10; dx >= -10; dx--) {
            for (int dz = 10; dz >= -10; dz--) {
                level.setBlockAndUpdate(blockPos.offset(dx, -1, dz), glowingBedrock);
                level.setBlockAndUpdate(blockPos.offset(dx,  0, dz), Blocks.AIR.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx,  1, dz), Blocks.AIR.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx,  2, dz), Blocks.AIR.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx,  3, dz), Blocks.AIR.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx,  4, dz), Blocks.BARRIER.defaultBlockState());
            }
        }

        for(int dx = 9; dx >= -8; dx-- ) {
            for (int dy=0; dy<=3; dy++) {
                level.setBlockAndUpdate(blockPos.offset(dx, dy,  10), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx, dy, -10), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(10, dy, dx),  Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(-10, dy, dx), Blocks.BARRIER.defaultBlockState());

                setBlockPortalBlock(level, blockPos.offset(dx, dy, 9),  TELEPORT_POSITIONS.get(0));
                setBlockPortalBlock(level, blockPos.offset(dx, dy, -9), TELEPORT_POSITIONS.get(1));
                setBlockPortalBlock(level, blockPos.offset(9, dy, dx),  TELEPORT_POSITIONS.get(2));
                setBlockPortalBlock(level, blockPos.offset(-9, dy, dx), TELEPORT_POSITIONS.get(3));

            }
        }
    }
}

