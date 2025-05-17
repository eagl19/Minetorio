package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.GlowingBedrockBlock;
import net.eagl.minetorio.block.custom.GlowingBedrockBlockState;
import net.eagl.minetorio.data.MinetorioDimensionSavedData;
import net.eagl.minetorio.worldgen.dimension.MinetorioDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class WorldInitEvents {

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        if (!level.dimension().equals(MinetorioDimensions.MINETORIO_DIM_EMPTY_LEVEL_KEY)) return;

        MinetorioDimensionSavedData data = MinetorioDimensionSavedData.get(level);

        if (!data.isInitialized()) {
            BlockState blockState = MinetorioBlocks.GLOWING_BEDROCK.get().defaultBlockState().setValue(GlowingBedrockBlock.STATE, GlowingBedrockBlockState.BEDROCK);
            BlockPos blockPos = new BlockPos(0, 100, 0);

            for (int dx = 3; dx >= -3; dx--) {
                for (int dz = 3; dz >= -3; dz--) {
                    level.setBlockAndUpdate(blockPos.offset(dx, -1, dz), blockState);
                    level.setBlockAndUpdate(blockPos.offset(dx,  0, dz), Blocks.AIR.defaultBlockState());
                    level.setBlockAndUpdate(blockPos.offset(dx,  1, dz), Blocks.AIR.defaultBlockState());
                    level.setBlockAndUpdate(blockPos.offset(dx,  2, dz), Blocks.AIR.defaultBlockState());
                    level.setBlockAndUpdate(blockPos.offset(dx,  3, dz), Blocks.AIR.defaultBlockState());
                    level.setBlockAndUpdate(blockPos.offset(dx,  4, dz), Blocks.BARRIER.defaultBlockState());
                }
            }
            for(int dx = 3; dx >= -3; dx-- ) {
                level.setBlockAndUpdate(blockPos.offset(dx,  0, 3), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx,  1, 3), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx,  2, 3), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx,  3, 3), Blocks.BARRIER.defaultBlockState());

                level.setBlockAndUpdate(blockPos.offset(dx,  0, -3), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx,  1, -3), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx,  2, -3), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(dx,  3, -3), Blocks.BARRIER.defaultBlockState());

                level.setBlockAndUpdate(blockPos.offset(3,  0, dx), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(3,  1, dx), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(3,  2, dx), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(3,  3, dx), Blocks.BARRIER.defaultBlockState());

                level.setBlockAndUpdate(blockPos.offset(-3,  0, dx), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(-3,  1, dx), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(-3,  2, dx), Blocks.BARRIER.defaultBlockState());
                level.setBlockAndUpdate(blockPos.offset(-3,  3, dx), Blocks.BARRIER.defaultBlockState());
            }

            blockPos = new BlockPos(1000,100,1000);

            level.setBlockAndUpdate(blockPos.offset(0, 8, 0), MinetorioBlocks.PATTERNS_COLLECTOR.get().defaultBlockState());

            for (int dx = 9; dx >= -9; dx--) {
                for (int dz = 9; dz >= -9; dz--) {
                    level.setBlockAndUpdate(blockPos.offset(dx, -1, dz), blockState);
                    level.setBlockAndUpdate(blockPos.offset(dx, 17, dz), blockState);
                }
            }
            for (int dx = 9; dx >= -9; dx--) {
                for (int dy = 0; dy <= 16; dy++) {
                    level.setBlockAndUpdate(blockPos.offset(dx, dy, 9), blockState);
                    level.setBlockAndUpdate(blockPos.offset(dx, dy, -9), blockState);

                    level.setBlockAndUpdate(blockPos.offset( 9, dy, dx), blockState);
                    level.setBlockAndUpdate(blockPos.offset(-9, dy, dx), blockState);
                }

            }

            data.markInitialized();
        }
    }
}
