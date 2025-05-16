package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.GlowingBedrockBlock;
import net.eagl.minetorio.block.custom.GlowingBedrockBlockState;
import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class PlayerClickEvents {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if (event.getEntity() instanceof ServerPlayer serverPlayer && !level.isClientSide) {
            if (event.getHand() == InteractionHand.MAIN_HAND) return;
            if (state.is(MinetorioBlocks.GLOWING_BEDROCK.get())) {
                switch (state.getValue(GlowingBedrockBlock.STATE)) {
                    case BEDROCK -> {

                        level.setBlock(pos,
                                MinetorioBlocks.GLOWING_BEDROCK.get()
                                        .defaultBlockState()
                                        .setValue(GlowingBedrockBlock.STATE, GlowingBedrockBlockState.INFINITY), 3);

                        Timer.addTimer(
                                pos,
                                MinetorioBlocks.GLOWING_BEDROCK.get().defaultBlockState().setValue(
                                        GlowingBedrockBlock.STATE,
                                        GlowingBedrockBlockState.BEDROCK),
                                level.dimension(),
                                200);
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        event.setCanceled(true);

                    }
                    case INFINITY -> {

                        addItemIfNotContains(serverPlayer, MinetorioItems.INFINITY.get());
                        event.setCanceled(true);

                    }
                    case VOID -> {

                        addItemIfNotContains(serverPlayer, MinetorioItems.VOID.get());
                        event.setCanceled(true);
                    }
                }

            } else if (state.is(Blocks.BARRIER)){
                level.setBlock(pos,
                        MinetorioBlocks.GLOWING_BEDROCK.get()
                                .defaultBlockState()
                                .setValue(GlowingBedrockBlock.STATE, GlowingBedrockBlockState.VOID), 3);

                Timer.addTimer(
                        pos,
                        Blocks.BARRIER.defaultBlockState(),
                        level.dimension(),
                        200);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }

    private static void addItemIfNotContains(ServerPlayer serverPlayer, @NotNull Item item) {
        if(!serverPlayer.getInventory().contains(new ItemStack(item))){
            serverPlayer.getInventory().add(new ItemStack(item, 1));
        }
    }
}
