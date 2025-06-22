package net.eagl.minetorio.gui;

import net.eagl.minetorio.block.entity.LavaGeneratorBlockEntity;
import net.eagl.minetorio.util.Technologies;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class LavaGeneratorMenu extends AbstractFluidGeneratorMenu<LavaGeneratorBlockEntity> {

    public LavaGeneratorMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(id, playerInventory, (LavaGeneratorBlockEntity) entity, MinetorioMenus.LAVA_GENERATOR_MENU.get(), Technologies.FIRE);
    }

    public LavaGeneratorMenu (int id, Inventory inv, FriendlyByteBuf extraData){
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }
}
