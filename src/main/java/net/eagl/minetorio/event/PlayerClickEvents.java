package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.GlowingBedrockBlock;
import net.eagl.minetorio.block.custom.GlowingBedrockBlockState;
import net.eagl.minetorio.block.custom.PatternsCollectorBlock;
import net.eagl.minetorio.block.entity.PatternsCollectorBlockEntity;
import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.worldgen.dimension.MinetorioDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraftforge.network.NetworkHooks;
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

            if (!level.dimension().equals(MinetorioDimensions.MINETORIO_DIM_EMPTY_LEVEL_KEY)) return;
            if (event.getHand() == InteractionHand.MAIN_HAND) return;


            //Pattern collector
            if (state.getBlock() instanceof PatternsCollectorBlock) {
                if (level.getBlockEntity(pos) instanceof PatternsCollectorBlockEntity collector) {
                    float yOffset = collector.getCurrentYOffset();
                    if (Math.abs(yOffset) < 0.1f) {

                        NetworkHooks.openScreen(serverPlayer, collector, pos);

                        event.setCancellationResult(InteractionResult.SUCCESS);
                        event.setCanceled(true);
                        return;
                    }
                }
            }

            // перевірка на SHIFT
            if (serverPlayer.isCrouching()) {

                if (serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).getItem() == MinetorioItems.PATTERN_VOID.get() ) {

                    serverPlayer.teleportTo((ServerLevel) level,
                            1000 + 0.5,
                            100,
                            1000 + 0.5,
                            serverPlayer.getYRot(),
                            serverPlayer.getXRot());

                }
            }
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

                        addItemIfNotContains(serverPlayer, MinetorioItems.PATTERN_INFINITY.get());
                        event.setCanceled(true);

                    }
                    case VOID -> {

                        addItemIfNotContains(serverPlayer, MinetorioItems.PATTERN_VOID.get());
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
