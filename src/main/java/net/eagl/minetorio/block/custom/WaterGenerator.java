package net.eagl.minetorio.block.custom;

import net.eagl.minetorio.block.entity.MinetorioBlockEntities;
import net.eagl.minetorio.block.entity.WaterGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WaterGenerator extends Block implements EntityBlock {
    public WaterGenerator() {
        super(Properties.copy(Blocks.WATER)
                .noCollission()
                .strength(100.0F)
                .noLootTable());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new WaterGeneratorBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level pLevel,
            @NotNull BlockState pState,
            @NotNull BlockEntityType<T> pBlockEntityType
    ) {
        if (pBlockEntityType == MinetorioBlockEntities.WATER_GENERATOR_ENTITY.get()) {
            return pLevel.isClientSide
                    ? (lvl, pos, blockState, be) -> ((WaterGeneratorBlockEntity) be).tickClient()
                    : (lvl, pos, blockState, be) -> ((WaterGeneratorBlockEntity) be).tickServer();
        }
        return null;
    }
}
