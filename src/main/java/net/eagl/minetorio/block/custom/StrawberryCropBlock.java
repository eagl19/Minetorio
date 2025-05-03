package net.eagl.minetorio.block.custom;

import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class StrawberryCropBlock extends CropBlock {
    public static final int MAX_AGE = 5;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_5;

    public StrawberryCropBlock(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer,
                                          @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (pState.getValue(AGE) >= getMaxAge()) {
            if (!pLevel.isClientSide) {
                ItemStack fruit = new ItemStack(MinetorioItems.STRAWBERRY.get());
                boolean added = pPlayer.addItem(fruit);
                if (!added) {
                    popResource(pLevel, pPos, fruit);
                }
                pLevel.setBlock(pPos, pState.setValue(AGE, 1), 2);
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return MinetorioItems.STRAWBERRY_SEEDS.get();
    }

    @Override
    public @NotNull IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
    }
}
