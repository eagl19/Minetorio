package net.eagl.minetorio.block.custom;

import net.eagl.minetorio.block.entity.LavaGeneratorBlockEntity;
import net.eagl.minetorio.block.entity.MinetorioBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LavaGenerator extends Block implements EntityBlock {

    public static final EnumProperty<GeneratorState> STATE = EnumProperty.create("state", GeneratorState.class);

    public LavaGenerator() {
        super(Properties.of()
                .mapColor(MapColor.FIRE)
                .liquid()
                .sound(SoundType.EMPTY)
                .noCollission()
                .strength(100.0F)
                .noLootTable()
                .isViewBlocking((state, reader, pos) -> false)
                .isSuffocating((state, world, pos) -> false));

        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, GeneratorState.UNSTABLE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new LavaGeneratorBlockEntity(pPos, pState);
    }
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level pLevel,
            @NotNull BlockState pState,
            @NotNull BlockEntityType<T> pBlockEntityType
    ) {
        if (pBlockEntityType == MinetorioBlockEntities.LAVA_GENERATOR_ENTITY.get()) {
            return pLevel.isClientSide
                    ? (lvl, pos, blockState, be) -> ((LavaGeneratorBlockEntity) be).tickClient()
                    : (lvl, pos, blockState, be) -> ((LavaGeneratorBlockEntity) be).tickServer();
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STATE);
    }
}
