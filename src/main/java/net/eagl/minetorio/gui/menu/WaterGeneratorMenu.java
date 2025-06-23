package net.eagl.minetorio.gui.menu;

import net.eagl.minetorio.block.entity.WaterGeneratorBlockEntity;
import net.eagl.minetorio.gui.MinetorioMenus;
import net.eagl.minetorio.util.Technologies;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;


import java.util.Objects;

public class WaterGeneratorMenu extends AbstractFluidGeneratorMenu<WaterGeneratorBlockEntity> {

    public WaterGeneratorMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(id, playerInventory, (WaterGeneratorBlockEntity) entity, MinetorioMenus.WATER_GENERATOR_MENU.get(), Technologies.WATER);
    }

    public WaterGeneratorMenu (int id, Inventory inv, FriendlyByteBuf extraData){
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }

}
