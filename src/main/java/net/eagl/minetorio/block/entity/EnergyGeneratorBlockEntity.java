package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.block.custom.EnergyGenerator;
import net.eagl.minetorio.block.custom.GeneratorState;
import net.eagl.minetorio.util.storage.MinetorioEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyGeneratorBlockEntity extends BlockEntity implements MenuProvider {

    public static final int ENERGY = 0;
    public static final int MAX_ENERGY = 1;
    public static final int PRODUCE = 2;
    public static final int MAX_PRODUCE = 3;

    public static final int MAX_ENERGY_STORAGE = 1000000;

    public static final int MAX_RECEIVE_ENERGY = 100;
    public static final int MAX_EXTRACT_ENERGY = 100;

    public static final int START_ENERGY_STORAGE = 250000;

    private final MinetorioEnergyStorage energyStorage = new MinetorioEnergyStorage(MAX_ENERGY_STORAGE,
            MAX_RECEIVE_ENERGY, MAX_EXTRACT_ENERGY, START_ENERGY_STORAGE, this::setChanged);
    private final LazyOptional<IEnergyStorage> optionalEnergy = LazyOptional.of(() -> energyStorage);


    private int currentTime;
    private final int timeInterval = 100;
    private boolean permanentlyStabilized;
    private final ContainerData containerData = new SimpleContainerData(2);

    public EnergyGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.ENERGY_GENERATOR_ENTITY.get(), pPos, pBlockState);
        this.currentTime = timeInterval;
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

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return optionalEnergy.cast();
        }
        return super.getCapability(cap, side);
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public void updateContainerData() {
        containerData.set(ENERGY, energyStorage.getEnergyStored());
        containerData.set(MAX_ENERGY, energyStorage.getMaxEnergyStored());
        containerData.set(PRODUCE, currentTime);
        containerData.set(MAX_PRODUCE, timeInterval);
    }

    public void tickClient() {
    }

    public void tickServer() {
        if(getBlockState().getValue(EnergyGenerator.STATE) == GeneratorState.STABILIZED) {
            this.currentTime--;
            updateContainerData();
            if(currentTime < 1){
                Level level = getLevel();
                if (!permanentlyStabilized && level != null) {
                    level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(EnergyGenerator.STATE, GeneratorState.UNSTABLE));
                    currentTime = timeInterval;
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
