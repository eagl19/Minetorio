package net.eagl.minetorio.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MinetorioHangingSignBlockEntity extends SignBlockEntity {
    public MinetorioHangingSignBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.MOD_HANGING_SIGN.get(), pPos, pBlockState);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return MinetorioBlockEntities.MOD_HANGING_SIGN.get();
    }
}
