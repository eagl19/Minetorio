package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class BlockInteractionEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if (state.is(MinetorioBlocks.GLOWING_BEDROCK.get())) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                InteractionResult result = GlowingBlockUseHandler.handle(state, level, pos, serverPlayer, event.getHand(), event.getHitVec());
                event.setCancellationResult(result);
                event.setCanceled(true);
            }
        }
    }
}
