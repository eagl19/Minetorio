package net.eagl.minetorio.util;

import net.eagl.minetorio.block.entity.AbstractFluidGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class CachedBlockPos {

    private final List<BlockPos> listPos;
    private final List<BlockPos> listConsumers;

    public CachedBlockPos(){
        this.listPos = new ArrayList<>();
        listConsumers = new ArrayList<>();
    }

    public boolean setConsumersById(List<Integer> list){
        listConsumers.clear();
        boolean g = false;
        for (int id:list){
            if(id<listPos.size()) {
                listConsumers.add(listPos.get(id));
                g = true;
            }
        }
        return g;
    }

    public void setConsumers(List<BlockPos> list){
        if(!list.isEmpty()) {
            listConsumers.clear();
            listConsumers.addAll(list);
        }
    }

    public List<BlockPos> getConsumers(){
        return listConsumers;
    }

    public List<BlockPos> getListPos(){
        return listPos;
    }

    public void setPos(List<BlockPos> list){
        if(!list.isEmpty()) {
            listPos.clear();
            listPos.addAll(list);
        }

    }

    public void initialize(Level level, BlockPos center, Fluid fluidToMatch) {
        listPos.clear();

        if (level == null || center == null || fluidToMatch == null) return;

        for (int dx = -8; dx <= 8; dx++) {
            for (int dy = -8; dy <= 8; dy++) {
                for (int dz = -8; dz <= 8; dz++) {
                    BlockPos checkPos = center.offset(dx, dy, dz);
                    BlockEntity be = level.getBlockEntity(checkPos);
                    if (be == null) continue;

                    if (be instanceof AbstractFluidGeneratorBlockEntity) continue;

                    LazyOptional<IFluidHandler> cap = be.getCapability(ForgeCapabilities.FLUID_HANDLER, null);
                    cap.ifPresent(handler -> {
                        for (int tank = 0; tank < handler.getTanks(); tank++) {
                            if (handler.getFluidInTank(tank).getFluid().isSame(fluidToMatch) &&
                                    handler.getTankCapacity(tank) > 0) {
                                listPos.add(checkPos.immutable());
                                break;
                            }
                        }
                    });
                }
            }
        }
        validateConsumers();
    }
    private void validateConsumers(){
        listConsumers.removeIf(pos -> !listPos.contains(pos));
    }

    public boolean removeConsumer(int index) {
        if(listConsumers.size() > index){
            listConsumers.remove(listConsumers.get(index));
            return true;
        }
        return false;
    }
}
