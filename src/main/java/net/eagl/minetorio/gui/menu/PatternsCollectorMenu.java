package net.eagl.minetorio.gui.menu;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.PatternsCollectorBlockEntity;
import net.eagl.minetorio.gui.MinetorioMenus;
import net.eagl.minetorio.util.InventorySlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PatternsCollectorMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private PatternsCollectorBlockEntity be;

    public PatternsCollectorMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(MinetorioMenus.PATTERNS_COLLECTOR_MENU.get(), id);
        System.out.println("Collector Menu");
        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());

        InventorySlot.addHotbarAndPlayerInventorySlots(this::addSlot, playerInventory, 0, 8, 140, 3, 9, 18, 18, 58);

        if(entity instanceof PatternsCollectorBlockEntity blockEntity){
            be = blockEntity;
        }

    }

    public PatternsCollectorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        Slot slot = this.slots.get(pIndex);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack originalStack = slot.getItem();
        ItemStack copy = originalStack.copy();

        if (pIndex >= 9 && pIndex < 36) {
            if (!moveItemStackTo(originalStack, 0, 9, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex >= 0 && pIndex < 9) {

            if (!moveItemStackTo(originalStack, 9, 36, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (originalStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return copy;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, MinetorioBlocks.PATTERNS_COLLECTOR.get());
    }

    public PatternsCollectorBlockEntity getBlockEntity(){
        return be;
    }
}
