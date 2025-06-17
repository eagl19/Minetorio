package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.block.custom.GeneratorState;
import net.eagl.minetorio.block.custom.WaterGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WaterGeneratorBlockEntity extends BlockEntity implements MenuProvider {

    private int currentTime;
    private boolean permanentlyStabilized = false;

    public WaterGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.WATER_GENERATOR_ENTITY.get(), pPos, pBlockState);
        this.currentTime = 100;

    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.minetorio.water_generator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return null;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("PermanentlyStabilized", this.permanentlyStabilized);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.permanentlyStabilized = tag.getBoolean("PermanentlyStabilized");
    }
    public void tickClient() {
    }

    public void tickServer() {
        if (this.getBlockState().getValue(WaterGenerator.STATE) == GeneratorState.STABILIZED) {
            if (permanentlyStabilized) return;

            this.currentTime--;
            if (this.currentTime < 1) {
                Level level = this.getLevel();
                if (level != null) {
                    level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(WaterGenerator.STATE, GeneratorState.UNSTABLE));
                    currentTime = 100;
                }
            }
        }
    }

    public void setPermanentlyStabilized(boolean value) {
        this.permanentlyStabilized = value;
    }
}
