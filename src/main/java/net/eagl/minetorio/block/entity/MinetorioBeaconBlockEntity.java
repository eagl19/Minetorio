package net.eagl.minetorio.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MinetorioBeaconBlockEntity extends BlockEntity {
    public MinetorioBeaconBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.MINETORIO_BEACON.get(), pPos, pBlockState);
    }
}
