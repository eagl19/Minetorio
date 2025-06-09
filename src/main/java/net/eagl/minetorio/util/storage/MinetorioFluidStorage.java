package net.eagl.minetorio.util.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class MinetorioFluidStorage implements IFluidHandler, INBTSerializable<CompoundTag> {

    private FluidStack fluid;
    private final int capacity;
    private final Runnable onChange;

    public MinetorioFluidStorage(int capacity, Runnable onChange) {
        this.capacity = capacity;
        this.fluid = FluidStack.EMPTY;
        this.onChange = onChange;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return tank == 0 ? fluid : FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        return tank == 0 ? capacity : 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return true; // або обмежити дозволені рідини
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) return 0;
        if (!fluid.isEmpty() && !fluid.isFluidEqual(resource)) return 0;

        int fillAmount = Math.min(capacity - fluid.getAmount(), resource.getAmount());
        if (action.execute() && fillAmount > 0) {
            if (fluid.isEmpty()) {
                fluid = new FluidStack(resource, fillAmount);
            } else {
                fluid.grow(fillAmount);
            }
            onChange.run();
        }
        return fillAmount;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !resource.isFluidEqual(fluid)) return FluidStack.EMPTY;
        return drain(resource.getAmount(), action);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        int drained = Math.min(fluid.getAmount(), maxDrain);
        FluidStack result = new FluidStack(fluid, drained);
        if (action.execute() && drained > 0) {
            fluid.shrink(drained);
            if (fluid.isEmpty()) fluid = FluidStack.EMPTY;
            onChange.run();
        }
        return result;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("Fluid", fluid.writeToNBT(new CompoundTag()));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        fluid = FluidStack.loadFluidStackFromNBT(nbt.getCompound("Fluid"));
    }
}

