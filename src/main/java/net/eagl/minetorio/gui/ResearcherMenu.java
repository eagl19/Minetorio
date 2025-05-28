package net.eagl.minetorio.gui;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.eagl.minetorio.util.InventorySlotHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ResearcherMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final Container container;


    public ResearcherMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(MinetorioMenus.RESEARCHER_MENU.get(), id);
        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());

        InventorySlotHelper.addPlayerInventorySlots(this::addSlot, playerInventory, 8, 140);

        InventorySlotHelper.addHotbarSlots(this::addSlot, playerInventory, 8, 198);


        if (entity instanceof ResearcherBlockEntity researcherEntity) {
            this.container = researcherEntity.getContainer();

            for (int i = 0; i < 9; i++) {
                this.addSlot(new Slot(container, i, 8 + i * 18, 12));
            }
            this.addSlot(new Slot(container, 9, 80, 37));
        } else {
            throw new IllegalStateException("Invalid block entity for ResearcherMenu");
        }
    }

    public ResearcherMenu (int id, Inventory inv, FriendlyByteBuf extraData){
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }


    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }



    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(access, pPlayer, MinetorioBlocks.RESEARCHER.get());
    }
}
