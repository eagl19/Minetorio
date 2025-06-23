package net.eagl.minetorio.util;

import net.eagl.minetorio.block.entity.AbstractFluidGeneratorBlockEntity;
import net.eagl.minetorio.util.enums.ResourceType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class CachedBlockPos implements INBTSerializable<CompoundTag> {

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
        listConsumers.clear();
        listConsumers.addAll(list);
    }

    public List<BlockPos> getConsumers(){
        return listConsumers;
    }

    public List<BlockPos> getListPos(){
        return listPos;
    }

    public void setPos(List<BlockPos> list) {
        listPos.clear();
        listPos.addAll(list);
    }

    public void initialize(Level level, BlockPos center, ResourceType resourceType) {
        listPos.clear();

        if (level == null || center == null || resourceType == null) return;

        for (int dx = -8; dx <= 8; dx++) {
            for (int dy = -8; dy <= 8; dy++) {
                for (int dz = -8; dz <= 8; dz++) {
                    BlockPos checkPos = center.offset(dx, dy, dz);
                    BlockEntity be = level.getBlockEntity(checkPos);
                    if (be == null || be instanceof AbstractFluidGeneratorBlockEntity) continue;

                    if(resourceType.matches(be)){
                        listPos.add(checkPos.immutable());
                    }
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
            listConsumers.remove(index);
            return true;
        }
        return false;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        ListTag consumersList = new ListTag();
        for (BlockPos pos : listConsumers) {
            consumersList.add(NbtUtils.writeBlockPos(pos));
        }
        tag.put("Consumers", consumersList);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {

        listConsumers.clear();
        ListTag consumersList = tag.getList("Consumers", Tag.TAG_COMPOUND);
        for (Tag t : consumersList) {
            listConsumers.add(NbtUtils.readBlockPos((CompoundTag) t));
        }
    }
}
