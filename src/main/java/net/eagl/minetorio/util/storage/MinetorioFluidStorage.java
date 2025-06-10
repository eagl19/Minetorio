package net.eagl.minetorio.util.storage;

import net.eagl.minetorio.util.enums.FluidType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class MinetorioFluidStorage implements IFluidHandler, INBTSerializable<CompoundTag> {

    private final FluidStack[] fluids;
    private final int[] capacities;
    private final FluidType[] fluidTypes;
    private final Runnable onChange;
    private final Map<FluidType, Integer> tankMap = new EnumMap<>(FluidType.class);

    public MinetorioFluidStorage(int tanks, int[] capacity, FluidType[] fluidTypes, int[] amounts, Runnable onChange) {
        this.fluids = new FluidStack[tanks];
        this.capacities = new int[tanks];
        this.fluidTypes = fluidTypes;
        this.onChange = onChange;
        for (int i = 0; i < tanks; i++) {
            FluidType type = fluidTypes[i];
            this.fluids[i] = new FluidStack(getFluidByType(type), amounts[i]);
            this.capacities[i] = capacity[i];
            this.tankMap.put(type, i);
        }
    }

    public static Fluid getFluidByType(FluidType type) {
        return switch (type) {
            case WATER -> Fluids.WATER;
            case LAVA -> Fluids.LAVA;
        };
    }

    public FluidType getTypeByTank(int tank) {
        return tank >= 0 && tank < fluidTypes.length ? fluidTypes[tank] : null;
    }

    public FluidStack getFluidInTank(FluidType type) {
        Integer i = tankMap.get(type);
        return i != null ? fluids[i] : FluidStack.EMPTY;
    }

    public int getTankCapacity(FluidType type) {
        Integer i = tankMap.get(type);
        return i != null ? capacities[i] : 0;
    }

    public int fill(FluidType type, int amount, FluidAction action) {
        Integer i = tankMap.get(type);
        FluidStack resource = new FluidStack(getFluidByType(type), amount);
        if (i == null || (!fluids[i].isEmpty() && !fluids[i].isFluidEqual(resource))) return 0;

        int fillAmount = Math.min(capacities[i] - fluids[i].getAmount(), amount);
        if (fillAmount > 0 && action.execute()) {
            if (fluids[i].isEmpty()) {
                fluids[i] = new FluidStack(resource, fillAmount);
            } else {
                fluids[i].grow(fillAmount);
            }
            onChange.run();
        }
        return fillAmount;
    }

    public FluidStack drain(FluidType type, int amount, FluidAction action) {
        Integer i = tankMap.get(type);
        if (i == null || fluids[i].isEmpty()) return FluidStack.EMPTY;

        int drained = Math.min(fluids[i].getAmount(), amount);
        FluidStack result = new FluidStack(fluids[i], drained);
        if (drained > 0 && action.execute()) {
            fluids[i].shrink(drained);
            if (fluids[i].isEmpty()) fluids[i] = FluidStack.EMPTY;
            onChange.run();
        }
        return result;
    }

    @Override
    public int getTanks() {
        return fluids.length;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return fluids[tank];
    }

    @Override
    public int getTankCapacity(int tank) {
        return capacities[tank];
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        FluidType type = getTypeByTank(tank);
        return type != null && getFluidByType(type).isSame(stack.getFluid());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        for (FluidType type : FluidType.values()) {
            if (getFluidByType(type).isSame(resource.getFluid())) {
                return fill(type, resource.getAmount(), action);
            }
        }
        return 0;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        for (FluidType type : FluidType.values()) {
            if (getFluidByType(type).isSame(resource.getFluid())) {
                return drain(type, resource.getAmount(), action);
            }
        }
        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        for (FluidType type : FluidType.values()) {
            FluidStack result = drain(type, maxDrain, action);
            if (!result.isEmpty()) return result;
        }
        return FluidStack.EMPTY;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < fluids.length; i++) {
            CompoundTag fluidTag = fluids[i].writeToNBT(new CompoundTag());
            fluidTag.putString("Type", fluidTypes[i].name());
            tag.put("Fluid" + i, fluidTag);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (int i = 0; i < fluids.length; i++) {
            CompoundTag fluidTag = nbt.getCompound("Fluid" + i);
            fluids[i] = FluidStack.loadFluidStackFromNBT(fluidTag);
            FluidType type = FluidType.valueOf(fluidTag.getString("Type"));
            tankMap.put(type, i);
        }
    }
}



