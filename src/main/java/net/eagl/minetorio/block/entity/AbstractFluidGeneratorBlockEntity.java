package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.block.custom.GeneratorState;
import net.eagl.minetorio.util.CachedBlockPos;
import net.eagl.minetorio.util.enums.FluidType;
import net.eagl.minetorio.util.storage.MinetorioEnergyStorage;
import net.eagl.minetorio.util.storage.MinetorioFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractFluidGeneratorBlockEntity extends BlockEntity implements MenuProvider {

    public static final int ENERGY = 0;
    public static final int MAX_ENERGY = 1;
    public static final int FLUID = 2;
    public static final int MAX_FLUID = 3;
    public static final int PRODUCE = 4;
    public static final int MAX_PRODUCE = 5;

    private final ContainerData containerData = new SimpleContainerData(6);

    private int currentTime;
    private int currentTransfer;
    private boolean permanentlyStabilized = false;

    private final CachedBlockPos cachedFluidTargets = new CachedBlockPos();
    private boolean initializedTargets = false;

    private final FluidType fluidType;

    public AbstractFluidGeneratorBlockEntity(BlockEntityType<?> pType, BlockPos pPos,
                                             BlockState pBlockState, int currentTime, FluidType fluid) {
        super(pType, pPos, pBlockState);
        this.currentTime = currentTime;
        this.fluidType = fluid;
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public void updateContainerData() {
        containerData.set(ENERGY, getEnergyStorage().getEnergyStored());
        containerData.set(MAX_ENERGY, getEnergyStorage().getMaxEnergyStored());
        containerData.set(FLUID, getFluidStorage().getFluidInTank(fluidType).getAmount());
        containerData.set(MAX_FLUID, getFluidStorage().getTankCapacity(fluidType));
        containerData.set(PRODUCE, getProduceTime());
        containerData.set(MAX_PRODUCE, getGenerateInterval());
    }

    public void initializedTargets() {
        cachedFluidTargets.initialize(level, getBlockPos(), getFluidStorage().getFluidInTank(0).getFluid());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!initializedTargets && level != null && !level.isClientSide) {
            initializedTargets();
            initializedTargets = true;
        }
    }


    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putBoolean("PermanentlyStabilized", permanentlyStabilized);
        tag.putInt("currentTime", currentTime);
        tag.putInt("currentTransfer", currentTransfer);

        tag.put("Energy", getEnergyStorage().serializeNBT());

        tag.put("Fluid", getFluidStorage().serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if(tag.contains("PermanentlyStabilized")) {
            permanentlyStabilized = tag.getBoolean("PermanentlyStabilized");
        }

        if(tag.contains("currentTime")) {
            currentTime = tag.getInt("currentTime");
        }

        if(tag.contains("currentTransfer")) {
            currentTransfer = tag.getInt("currentTransfer");
        }

        if (tag.contains("Energy")) {
            getEnergyStorage().deserializeNBT(tag.get("Energy"));
        }

        if (tag.contains("Fluid", Tag.TAG_COMPOUND)) {
            getFluidStorage().deserializeNBT(tag.getCompound("Fluid"));
        }
    }



    public void tickServer() {
        boolean changed = false;
        if (getGeneratorStateProperty().equals(GeneratorState.STABILIZED)) {

            this.currentTransfer--;
            this.currentTime--;
            boolean canEnergy = getEnergyStorage().extractEnergy(1, true) >= 1;
            boolean canWater = getFluidStorage().fill(fluidType, 1, IFluidHandler.FluidAction.SIMULATE) >= 1;
            if (canEnergy && canWater && currentTime < 1) {
                getFluidStorage().fill(fluidType, getGenerateAmount(), IFluidHandler.FluidAction.EXECUTE);
                getEnergyStorage().extractEnergy(1, false);
                this.currentTime = getGenerateInterval();
                changed = true;
            }
            updateContainerData();
            if (this.currentTransfer < 1) {
                Level level = getLevel();

                if (!permanentlyStabilized && level != null) {
                    level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(getGeneratorStatePropertyKey(), GeneratorState.UNSTABLE));
                }
                if (transferFluidToTargets()) {
                    this.currentTransfer = getTransferTime();
                    changed = true;
                }

            }
        }
        if(changed){
            setChanged();
        }
    }

    private boolean transferFluidToTargets() {
        if (level == null) return false;
        AtomicBoolean transferred = new AtomicBoolean(false);

        LazyOptional<IFluidHandler> optionalFrom = this.getCapability(ForgeCapabilities.FLUID_HANDLER, null);
        optionalFrom.ifPresent(from -> {
            for (BlockPos pos : cachedFluidTargets.getConsumers()) {
                BlockEntity targetBE = level.getBlockEntity(pos);
                if (targetBE == null) continue;

                LazyOptional<IFluidHandler> optionalTo = targetBE.getCapability(ForgeCapabilities.FLUID_HANDLER, null);
                optionalTo.ifPresent(to -> {
                    int moved = FluidUtil.tryFluidTransfer(to, from, getMaxTransferAmount(), true).getAmount();
                    if (moved > 0) transferred.set(true);
                });
            }
        });

        return transferred.get();
    }

    public CachedBlockPos getCachedFluidTargets(){
        return cachedFluidTargets;
    }

    public void setPermanentlyStabilized(boolean value) {
        this.permanentlyStabilized = value;
    }

    public boolean getPermanentlyStabilized(){
        return this.permanentlyStabilized;
    }

    protected int getProduceTime(){
        return currentTime;
    }

    public abstract void tickClient();
    protected abstract MinetorioEnergyStorage getEnergyStorage();
    protected abstract MinetorioFluidStorage getFluidStorage();
    protected abstract int getGenerateAmount();
    protected abstract int getGenerateInterval();
    protected abstract int getTransferTime();
    protected abstract int getMaxTransferAmount();
    protected abstract GeneratorState getGeneratorStateProperty();
    protected abstract Property<GeneratorState> getGeneratorStatePropertyKey();
}

