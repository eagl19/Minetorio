package net.eagl.minetorio.gui.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IGeneratorMenu {
    List<BlockPos> getTargetsList();
    List<BlockPos> getConsumersList();
    ItemStack getItemFromBlockPos(BlockPos pos);
    BlockPos getBlockPos();
    int getConsumersCount();
}
