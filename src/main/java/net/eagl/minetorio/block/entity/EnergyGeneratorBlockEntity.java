package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.block.custom.EnergyGenerator;
import net.eagl.minetorio.block.custom.GeneratorState;
import net.minecraft.core.BlockPos;
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

public class EnergyGeneratorBlockEntity extends BlockEntity implements MenuProvider {

    private int currentTime;
    private boolean permanentlyStabilized;

    public EnergyGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.ENERGY_GENERATOR_ENTITY.get(), pPos, pBlockState);
        this.currentTime = 100;
        this.permanentlyStabilized = false;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.minetorio.energy_generator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return null;
    }

    public void tickClient() {
    }

    public void tickServer() {
        if(getBlockState().getValue(EnergyGenerator.STATE) == GeneratorState.STABILIZED) {
            this.currentTime--;
            if(currentTime < 1){
                Level level = getLevel();
                if (!permanentlyStabilized && level != null) {
                    level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(EnergyGenerator.STATE, GeneratorState.UNSTABLE));
                    currentTime = 100;
                }
            }
        }
    }
    public boolean getPermanentlyStabilized(){
        return permanentlyStabilized;
    }
     public void setPermanentlyStabilized(boolean stabilized){
        permanentlyStabilized = stabilized;
     }
}
