package net.eagl.minetorio.worldgen.structure;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.GlowingBedrockBlock;
import net.eagl.minetorio.block.custom.GlowingBedrockBlockState;
import net.eagl.minetorio.block.entity.PortalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class Rooms3x3 {

    public static void create(ServerLevel pLevel, BlockPos pPosition) {


        rooms(pLevel, pPosition);
    }
    private static void setBlockPortalBlock(ServerLevel level, BlockPos blockPos, BlockPos teleportPos){

        level.setBlockAndUpdate(blockPos, MinetorioBlocks.PORTAL.get().defaultBlockState());
        BlockEntity be = level.getBlockEntity(blockPos);

        if (be instanceof PortalBlockEntity portalBE) {
            portalBE.setTeleportTarget(teleportPos);

        }
    }

    private static void rooms(ServerLevel level, BlockPos pPosition){

        List<BlockPos> ROOMS_POSITIONS = List.of(
        pPosition.offset(-1000, 0,-1000),  //TODO створити (0)  (0)(1)(2)  (-x, -z) (0x, -z) (+x, -z)
        pPosition.offset(0,     0,-1000),  //TODO створити (1)  (3)(4)(5)  (-x, 0z) (0x, 0z) (+x, 0z)
        pPosition.offset(1000,  0,-1000),  //TODO створити (2)  (6)(7)(8)  (-x, +z) (0x, +z) (+x, +z)
        pPosition.offset(-1000, 0,0),      //TODO створити (3)
        pPosition.offset(0,     0,0),      //spawn room (4)
        pPosition.offset(1000,  0,0),      //TODO створити (5)         ( ) (N) ( )
        pPosition.offset(-1000, 0,1000),   //TODO створити (6)         (W) ( ) (E)
        pPosition.offset(0,     0,1000),   //TODO створити (7)         ( ) (S) ( )
        pPosition.offset(1000,  0,1000)    //collector room (8)
        );

        BlockState glowingBedrock = MinetorioBlocks.GLOWING_BEDROCK.get().defaultBlockState().setValue(GlowingBedrockBlock.STATE, GlowingBedrockBlockState.BEDROCK);

        room(level, ROOMS_POSITIONS.get(0), Blocks.ICE.defaultBlockState(),                 ROOMS_POSITIONS.get(6), ROOMS_POSITIONS.get(3), ROOMS_POSITIONS.get(1), ROOMS_POSITIONS.get(2));// room (0)
        room(level, ROOMS_POSITIONS.get(1), Blocks.COPPER_BLOCK.defaultBlockState(),        ROOMS_POSITIONS.get(7), ROOMS_POSITIONS.get(4), ROOMS_POSITIONS.get(2), ROOMS_POSITIONS.get(0));// room (1)
        room(level, ROOMS_POSITIONS.get(2), Blocks.IRON_BLOCK.defaultBlockState(),          ROOMS_POSITIONS.get(8), ROOMS_POSITIONS.get(5), ROOMS_POSITIONS.get(0), ROOMS_POSITIONS.get(1));// room (1)
        room(level, ROOMS_POSITIONS.get(3), Blocks.COBBLED_DEEPSLATE.defaultBlockState(),   ROOMS_POSITIONS.get(0), ROOMS_POSITIONS.get(6), ROOMS_POSITIONS.get(4), ROOMS_POSITIONS.get(5));// room (1)
        room(level, ROOMS_POSITIONS.get(4), glowingBedrock,                                 ROOMS_POSITIONS.get(1), ROOMS_POSITIONS.get(7), ROOMS_POSITIONS.get(5), ROOMS_POSITIONS.get(3));//spawn room (4)
        room(level, ROOMS_POSITIONS.get(5), Blocks.ACACIA_PLANKS.defaultBlockState(),       ROOMS_POSITIONS.get(2), ROOMS_POSITIONS.get(8), ROOMS_POSITIONS.get(3), ROOMS_POSITIONS.get(4));// room (5)
        room(level, ROOMS_POSITIONS.get(6), Blocks.EMERALD_BLOCK.defaultBlockState(),       ROOMS_POSITIONS.get(3), ROOMS_POSITIONS.get(0), ROOMS_POSITIONS.get(7), ROOMS_POSITIONS.get(8));// room (6)
        room(level, ROOMS_POSITIONS.get(7), Blocks.DIAMOND_BLOCK.defaultBlockState(),       ROOMS_POSITIONS.get(4), ROOMS_POSITIONS.get(1), ROOMS_POSITIONS.get(8), ROOMS_POSITIONS.get(6));// room (7)

        // collector room (8)
        room(level, ROOMS_POSITIONS.get(8), Blocks.BARRIER.defaultBlockState(), ROOMS_POSITIONS.get(5), ROOMS_POSITIONS.get(2), ROOMS_POSITIONS.get(6), ROOMS_POSITIONS.get(7));
        level.setBlockAndUpdate(ROOMS_POSITIONS.get(8).offset(0, 2, 0), MinetorioBlocks.PATTERNS_COLLECTOR.get().defaultBlockState());

    }

    private static void room(ServerLevel level, BlockPos roomCenter, BlockState floor, BlockPos north, BlockPos south, BlockPos east, BlockPos west){

        for (int dx = 10; dx >= -10; dx--) {
            for (int dz = 10; dz >= -10; dz--) {
                level.setBlockAndUpdate(roomCenter.offset(dx, -1, dz), floor);
                for(int dy=0; dy<=16; dy++) {
                    level.setBlockAndUpdate(roomCenter.offset(dx, dy, dz), Blocks.AIR.defaultBlockState());
                }
                level.setBlockAndUpdate(roomCenter.offset(dx,  17, dz), Blocks.BARRIER.defaultBlockState());
            }
        }

        for(int dx = 9; dx >= -9; dx-- ) {
            for (int dy=0; dy<=16; dy++) {

                level.setBlockAndUpdate(roomCenter.offset(dx, dy,  10), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(roomCenter.offset(dx, dy, -10), Blocks.BARRIER.defaultBlockState());

                setBlockPortalBlock(level, roomCenter.offset(dx, dy, 9),  south);
                setBlockPortalBlock(level, roomCenter.offset(dx, dy, -9), north);
            }
        }

        for(int dz = 9; dz >= -9; dz-- ) {
            for (int dy=0; dy<=16; dy++) {

                level.setBlockAndUpdate(roomCenter.offset(10,  dy, dz),  Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(roomCenter.offset(-10, dy, dz), Blocks.BARRIER.defaultBlockState());

                setBlockPortalBlock(level, roomCenter.offset(9,  dy, dz),  east);
                setBlockPortalBlock(level, roomCenter.offset(-9, dy, dz), west);
            }
        }
    }
}
