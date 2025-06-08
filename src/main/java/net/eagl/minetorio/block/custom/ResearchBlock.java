package net.eagl.minetorio.block.custom;

import net.eagl.minetorio.block.entity.MinetorioBlockEntities;
import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResearchBlock extends Block implements EntityBlock {

    public ResearchBlock() {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .strength(-1.0F)
                .lightLevel(state -> 11)
                .sound(SoundType.GLASS)
                .noLootTable()
                .isViewBlocking((state, reader, pos) -> false)
                .isSuffocating((state, world, pos) -> false)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new ResearcherBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level pLevel,
            @NotNull BlockState pState,
            @NotNull BlockEntityType<T> pBlockEntityType
    ) {
        if (pBlockEntityType == MinetorioBlockEntities.RESEARCHER_BLOCK_ENTITY.get()) {
            return pLevel.isClientSide
                    ? (lvl, pos, blockState, be) -> ((ResearcherBlockEntity) be).tickClient()
                    : (lvl, pos, blockState, be) -> ((ResearcherBlockEntity) be).tickServer();
        }
        return null;
    }
}
