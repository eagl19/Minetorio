package net.eagl.minetorio.block.custom;


import net.eagl.minetorio.block.entity.MinetorioBlockEntities;
import net.eagl.minetorio.block.entity.PatternsCollectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PatternsCollectorBlock extends Block implements EntityBlock {

    public PatternsCollectorBlock() {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .strength(-1.0F, 3600000.0F)
                .lightLevel(state -> 15)
                .noOcclusion());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PatternsCollectorBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        if (pBlockEntityType == MinetorioBlockEntities.PATTERNS_COLLECTOR.get()) {
            return pLevel.isClientSide
                    ? (lvl, pos, blockState, be) -> ((PatternsCollectorBlockEntity) be).tickClient()
                    : (lvl, pos, blockState, be) -> ((PatternsCollectorBlockEntity) be).tickServer();
        }
        return null;
    }
}
