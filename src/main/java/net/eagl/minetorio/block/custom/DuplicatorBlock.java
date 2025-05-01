package net.eagl.minetorio.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DuplicatorBlock extends Block {
    public DuplicatorBlock(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public void stepOn(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Entity pEntity) {
        if (!pLevel.isClientSide && pEntity instanceof Player player) {
            BlockPos spawnPos = pPos.relative(player.getDirection());
            if (pLevel.isEmptyBlock(spawnPos)) {
                pLevel.setBlockAndUpdate(spawnPos, this.defaultBlockState());
                pLevel.scheduleTick(spawnPos, this, 200);
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable BlockGetter pLevel, @NotNull List<Component> pTooltip, @NotNull TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("tooltip.minetorio.duplicator_block.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos,
                     @NotNull RandomSource pRandom) {
        pLevel.removeBlock(pPos,false);
    }
}
