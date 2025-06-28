package net.eagl.minetorio.block.custom;

import net.eagl.minetorio.block.entity.PortalBlockEntity;
import net.eagl.minetorio.capability.MinetorioCapabilities;
import net.eagl.minetorio.util.Technologies;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PortalBlock extends NetherPortalBlock implements EntityBlock {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public PortalBlock() {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .strength(-1.0F)
                .lightLevel(state -> 11)
                .sound(SoundType.GLASS)
                .noLootTable()
                .isViewBlocking((state, reader, pos) -> false)
                .isSuffocating((state, world, pos) -> false)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState pState, @NotNull Direction pFacing, @NotNull BlockState pFacingState,
                                           @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
        return pState;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AXIS);
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public void entityInside(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Entity pEntity) {
        if (!pLevel.isClientSide) {
            if (pEntity instanceof ServerPlayer player) {
                BlockEntity be = pLevel.getBlockEntity(pPos);
                if (be instanceof PortalBlockEntity portalBE) {
                    BlockPos target = portalBE.getTeleportTarget();
                    if (target != null) {
                        pEntity.getCapability(MinetorioCapabilities.TECHNOLOGY_PROGRESS).ifPresent(techCap -> {
                            if (techCap.hasLearned(Technologies.VOID.getId())) {
                                player.teleportTo(target.getX() + 0.5, target.getY() + 1, target.getZ() + 0.5);
                            } else {
                                 ServerLevel level = player.server.getLevel(player.getRespawnDimension());
                                 BlockPos pos = player.getRespawnPosition();
                                 if(level != null && pos !=null) {
                                    player.teleportTo(level, pos.getX() + 0.5, pos.getY() + 0.5,  pos.getZ() + 0.5, player.getYRot(), player.getXRot());
                                }
                            }
                        });

                    }
                }
            }
        }
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
                            @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!level.isClientSide && placer != null) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PortalBlockEntity portalBE) {
                BlockPos playerPos = placer.blockPosition();
                portalBE.setTeleportTarget(playerPos);
            }
        }
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PortalBlockEntity(pos, state);
    }
}