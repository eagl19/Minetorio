package net.eagl.minetorio.block.custom;

import net.eagl.minetorio.block.entity.EnergyGeneratorBlockEntity;
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

public class EnergyGenerator extends Block implements EntityBlock {

    public static final EnumProperty<MinetorioBlockState> STATE = EnumProperty.create("state", MinetorioBlockState.class);

    public EnergyGenerator() {
        super(Properties.of()
                .mapColor(MapColor.SAND)
                .sound(SoundType.GLASS)
                .noCollission()
                .strength(-1.0F, 3600000.0F)
                .lightLevel(state -> 15)
                .noLootTable()
                .isViewBlocking((state, reader, pos) -> false)
                .isSuffocating((state, world, pos) -> false));

        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, MinetorioBlockState.UNSTABLE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new EnergyGeneratorBlockEntity(pPos, pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STATE);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level pLevel,
            @NotNull BlockState pState,
            @NotNull BlockEntityType<T> pBlockEntityType
    ) {
        if (pBlockEntityType == MinetorioBlockEntities.ENERGY_GENERATOR_ENTITY.get()) {
            return pLevel.isClientSide
                    ? (lvl, pos, blockState, be) -> ((EnergyGeneratorBlockEntity) be).tickClient()
                    : (lvl, pos, blockState, be) -> ((EnergyGeneratorBlockEntity) be).tickServer();
        }
        return null;
    }
}
