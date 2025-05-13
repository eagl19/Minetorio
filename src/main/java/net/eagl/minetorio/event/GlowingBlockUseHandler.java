package net.eagl.minetorio.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class GlowingBlockUseHandler {
    public static InteractionResult handle(BlockState state, Level level, BlockPos pos, ServerPlayer player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            level.setBlock(pos, Blocks.GLOWSTONE.defaultBlockState(), 3);
            PlayerTimers.addTimer(player, pos, 200);
        }
        return InteractionResult.SUCCESS;
    }
}
