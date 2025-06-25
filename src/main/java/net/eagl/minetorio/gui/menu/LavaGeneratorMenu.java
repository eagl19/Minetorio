package net.eagl.minetorio.gui.menu;

import net.eagl.minetorio.block.entity.LavaGeneratorBlockEntity;
import net.eagl.minetorio.gui.MinetorioMenus;
import net.eagl.minetorio.gui.slot.FlaskSlot;
import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.util.Technologies;
import net.eagl.minetorio.util.storage.UpgradeStorage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class LavaGeneratorMenu extends AbstractFluidGeneratorMenu<LavaGeneratorBlockEntity> {

    public LavaGeneratorMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(id, playerInventory, (LavaGeneratorBlockEntity) entity, MinetorioMenus.LAVA_GENERATOR_MENU.get(), Technologies.FIRE);

        UpgradeStorage container = blockEntity.getUpgrades();

        this.addSlot(new FlaskSlot(container, 0, 8, 25 , MinetorioItems.ABYSSAL_STONE.get()));
        this.addSlot(new FlaskSlot(container, 1, 8, 43 , MinetorioItems.BLAZE_CRYSTAL.get()));
        this.addSlot(new FlaskSlot(container, 2, 8, 61 , MinetorioItems.INFERNAL_CATALYST.get()));
        this.addSlot(new FlaskSlot(container, 3, 8, 79 , MinetorioItems.MAGMATIC_CORE.get()));
        this.addSlot(new FlaskSlot(container, 4, 8, 97 , MinetorioItems.VOLCANO_ESSENCE.get()));
    }

    public LavaGeneratorMenu (int id, Inventory inv, FriendlyByteBuf extraData){
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }
}
