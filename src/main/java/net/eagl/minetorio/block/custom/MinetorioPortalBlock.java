package net.eagl.minetorio.block.custom;

import net.eagl.minetorio.worldgen.dimension.MinetorioDimensions;
import net.eagl.minetorio.worldgen.portal.MinetorioTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class MinetorioPortalBlock extends Block {
    public MinetorioPortalBlock(Properties pProperties) {
        super(pProperties);
    }
    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (pPlayer.canChangeDimensions()) {
            handleMinetorioPortal(pPlayer, pPos);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    private void handleMinetorioPortal(Entity player, BlockPos pPos) {
        if (player.level() instanceof ServerLevel serverlevel) {
            MinecraftServer minecraftserver = serverlevel.getServer();
            ResourceKey<Level> resourcekey = player.level().dimension() == MinetorioDimensions.MINETORIO_DIM_LEVEL_KEY ?
                    Level.OVERWORLD : MinetorioDimensions.MINETORIO_DIM_LEVEL_KEY;

            ServerLevel portalDimension = minecraftserver.getLevel(resourcekey);
            if (portalDimension != null && !player.isPassenger()) {
                if(resourcekey == MinetorioDimensions.MINETORIO_DIM_LEVEL_KEY) {
                    player.changeDimension(portalDimension, new MinetorioTeleporter(pPos, true));
                } else {
                    player.changeDimension(portalDimension, new MinetorioTeleporter(pPos, false));
                }
            }
        }
    }
}
