package net.eagl.minetorio.gui.menu;

import net.eagl.minetorio.block.entity.WaterGeneratorBlockEntity;
import net.eagl.minetorio.gui.MinetorioMenus;
import net.eagl.minetorio.gui.slot.FlaskSlot;
import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.util.Technologies;
import net.eagl.minetorio.util.storage.UpgradeStorage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;


import java.util.Objects;

public class WaterGeneratorMenu extends AbstractFluidGeneratorMenu<WaterGeneratorBlockEntity> {

    public WaterGeneratorMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(id, playerInventory, (WaterGeneratorBlockEntity) entity, MinetorioMenus.WATER_GENERATOR_MENU.get(), Technologies.WATER);

        UpgradeStorage container = blockEntity.getUpgrades();

        this.addSlot(new FlaskSlot(container, 0, 8, 25 , MinetorioItems.COOLING_CORE.get()));
        this.addSlot(new FlaskSlot(container, 1, 8, 43 , MinetorioItems.DEW_COLLECTOR.get()));
        this.addSlot(new FlaskSlot(container, 2, 8, 61 , MinetorioItems.ENCHANTED_PIPE.get()));
        this.addSlot(new FlaskSlot(container, 3, 8, 79 , MinetorioItems.FILTERED_NOZZLE.get()));
        this.addSlot(new FlaskSlot(container, 4, 8, 97 , MinetorioItems.HYDRO_CATALYST.get()));
    }

    public WaterGeneratorMenu (int id, Inventory inv, FriendlyByteBuf extraData){
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }

}
