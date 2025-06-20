package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.block.custom.GeneratorState;
import net.eagl.minetorio.block.custom.LavaGenerator;
import net.eagl.minetorio.gui.LavaGeneratorMenu;
import net.eagl.minetorio.util.enums.FluidType;
import net.eagl.minetorio.util.storage.MinetorioEnergyStorage;
import net.eagl.minetorio.util.storage.MinetorioFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LavaGeneratorBlockEntity extends AbstractFluidGeneratorBlockEntity{

    private static final int GENERATE_INTERVAL = 100;
    private static final int GENERATE_AMOUNT = 2000;

    private static final int MAX_TRANSFER_AMOUNT = 1000;
    private static final int TRANSFER_TIME = 100;

    private static final int MAX_ENERGY_STORAGE = 1000000;
    private static final int MAX_LAVA_STORAGE = 1000000;

    private static final int MAX_RECEIVE_ENERGY = 100;
    private static final int MAX_EXTRACT_ENERGY = 100;

    private static final int START_ENERGY_STORAGE = 250000;
    private static final int START_LAVA_STORAGE = 250000;

    private final MinetorioFluidStorage fluidStorage = new MinetorioFluidStorage(1,
            new int[]{MAX_LAVA_STORAGE}, new FluidType[]{FluidType.LAVA},
            new int[]{START_LAVA_STORAGE}, this::setChanged);

    private final LazyOptional<IFluidHandler> optionalFluid = LazyOptional.of(() -> fluidStorage);

    private final MinetorioEnergyStorage energyStorage = new MinetorioEnergyStorage(MAX_ENERGY_STORAGE,
            MAX_RECEIVE_ENERGY, MAX_EXTRACT_ENERGY, START_ENERGY_STORAGE, this::setChanged);

    private final LazyOptional<IEnergyStorage> optionalEnergy = LazyOptional.of(() -> energyStorage);

    public LavaGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.LAVA_GENERATOR_ENTITY.get(), pPos, pBlockState, GENERATE_INTERVAL, FluidType.LAVA);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.minetorio.lava_generator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new LavaGeneratorMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        optionalFluid.invalidate();
        optionalEnergy.invalidate();
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return optionalFluid.cast();
        }
        if (cap == ForgeCapabilities.ENERGY) {
            return optionalEnergy.cast();
        }
        return super.getCapability(cap, side);
    }

    public void tickClient() {
    }

    @Override
    protected MinetorioEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    protected MinetorioFluidStorage getFluidStorage() {
        return fluidStorage;
    }

    @Override
    protected int getGenerateAmount() {
        return GENERATE_AMOUNT;
    }

    @Override
    protected int getGenerateInterval() {
        return GENERATE_INTERVAL;
    }

    @Override
    protected int getTransferTime() {
        return TRANSFER_TIME;
    }

    @Override
    protected int getMaxTransferAmount() {
        return MAX_TRANSFER_AMOUNT;
    }

    @Override
    protected GeneratorState getGeneratorStateProperty() {
        return getBlockState().getValue(LavaGenerator.STATE);
    }

    @Override
    protected Property<GeneratorState> getGeneratorStatePropertyKey() {
        return LavaGenerator.STATE;
    }

}
