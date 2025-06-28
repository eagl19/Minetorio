package net.eagl.minetorio.block.custom;

import net.eagl.minetorio.block.entity.BarrierBlockEntity;
import net.eagl.minetorio.block.entity.MinetorioBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Barrier extends Block implements EntityBlock {

    public static final EnumProperty<MinetorioBlockState> STATE = EnumProperty.create("state", MinetorioBlockState.class);

    public Barrier() {
        super(BlockBehaviour.Properties.of()
                .strength(-1.0F, 3600000.8F)
                .noLootTable()
                .noOcclusion()
                .isValidSpawn((s, w, p, e) -> false)
                .noParticlesOnBreak()
                .pushReaction(PushReaction.BLOCK));

        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, MinetorioBlockState.STABILIZED));

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STATE);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new BarrierBlockEntity(pPos, pState);
    }
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level pLevel,
            @NotNull BlockState pState,
            @NotNull BlockEntityType<T> pBlockEntityType
    ) {
        if (pBlockEntityType == MinetorioBlockEntities.BARRIER_ENTITY.get()) {
            return pLevel.isClientSide
                    ? (lvl, pos, blockState, be) -> ((BarrierBlockEntity) be).tickClient()
                    : (lvl, pos, blockState, be) -> ((BarrierBlockEntity) be).tickServer();
        }
        return null;
    }
}
