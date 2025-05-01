package net.eagl.minetorio.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DuplicatorBlock extends Block {
    public DuplicatorBlock(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public void stepOn(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Entity pEntity) {
        if (!pLevel.isClientSide && pEntity instanceof Player player) {
            BlockPos spawnPos = pPos.relative(player.getDirection());
            if (pLevel.isEmptyBlock(spawnPos)) {
                pLevel.setBlockAndUpdate(spawnPos, this.defaultBlockState());
                pLevel.scheduleTick(spawnPos, this, 200);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos,
                     @NotNull RandomSource pRandom) {
        pLevel.removeBlock(pPos,false);
    }
}
