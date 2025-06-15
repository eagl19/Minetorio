package net.eagl.minetorio.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StaticFluidBlock extends Block implements EntityBlock {
    public StaticFluidBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return null;
    }
}
