package net.eagl.minetorio.block.custom;


import net.eagl.minetorio.event.PlayerTimers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class GlowingBedrockBlock extends Block {

    public GlowingBedrockBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(-1.0F, 3600000.0F)
                .noLootTable()
                .lightLevel(state -> 15)
                .noOcclusion()
        );
    }


    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                          @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer && pLevel instanceof ServerLevel serverLevel) {
            PlayerTimers.addTimer(serverPlayer, pPos, 200);
            pPlayer.displayClientMessage(Component.literal("Блок зміниться через 10 секунд!"), true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

}
