package net.eagl.minetorio.util.enums;

import net.eagl.minetorio.block.entity.EnergyGeneratorBlockEntity;
import net.eagl.minetorio.block.entity.LavaGeneratorBlockEntity;
import net.eagl.minetorio.block.entity.WaterGeneratorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Fluid;

public enum ResourceType {
    WATER {
        @Override
        public boolean matches(BlockEntity be) {
            return hasFluid(be, Fluids.WATER);
        }

        @Override
        public boolean isGenerator(BlockEntity be) {
            return be instanceof WaterGeneratorBlockEntity;
        }
    },
    LAVA {
        @Override
        public boolean matches(BlockEntity be) {
            return hasFluid(be, Fluids.LAVA);
        }

        @Override
        public boolean isGenerator(BlockEntity be) {
            return be instanceof LavaGeneratorBlockEntity;
        }
    },
    ENERGY {
        @Override
        public boolean matches(BlockEntity be) {
            LazyOptional<IEnergyStorage> cap = be.getCapability(ForgeCapabilities.ENERGY, null);
            return cap.map(storage -> storage.getMaxEnergyStored() > 0).orElse(false);
        }
        @Override
        public boolean isGenerator(BlockEntity be) {
            return be instanceof EnergyGeneratorBlockEntity;
        }
    };

    public abstract boolean matches(BlockEntity be);
    public abstract boolean isGenerator(BlockEntity be);


    protected static boolean hasFluid(BlockEntity be, Fluid fluidToMatch) {

        LazyOptional<IFluidHandler> cap = be.getCapability(ForgeCapabilities.FLUID_HANDLER, null);
        return cap.map(handler -> {
            for (int i = 0; i < handler.getTanks(); i++) {
                if (handler.getFluidInTank(i).getFluid().isSame(fluidToMatch) &&
                        handler.getTankCapacity(i) > 0) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }
}

