package net.eagl.minetorio.block.custom;

import net.eagl.minetorio.block.entity.GlowingBedrockBlockEntity;
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
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GlowingBedrockBlock extends Block implements EntityBlock {


    public static final EnumProperty<GeneratorState> STATE = EnumProperty.create("state", GeneratorState.class);

    public GlowingBedrockBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(-1.0F, 3600000.0F)
                .noLootTable()
                .lightLevel(state -> 15)
                .noOcclusion()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, GeneratorState.STABILIZED));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new GlowingBedrockBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level pLevel,
            @NotNull BlockState pState,
            @NotNull BlockEntityType<T> pBlockEntityType
    ) {
        if (pBlockEntityType == MinetorioBlockEntities.GLOWING_BEDROCK_ENTITY.get()) {
            return pLevel.isClientSide
                    ? (lvl, pos, blockState, be) -> ((GlowingBedrockBlockEntity) be).tickClient()
                    : (lvl, pos, blockState, be) -> ((GlowingBedrockBlockEntity) be).tickServer();
        }
        return null;
    }
}
