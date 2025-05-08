package net.eagl.minetorio.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MinetorioSignBlockEntity extends SignBlockEntity {
    public MinetorioSignBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.MOD_SIGN.get(), pPos, pBlockState);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return MinetorioBlockEntities.MOD_SIGN.get();
    }
}
