package net.eagl.minetorio.gui;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.eagl.minetorio.util.InventorySlotHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ResearcherMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final SimpleContainerData data;


    public ResearcherMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(MinetorioMenus.RESEARCHER_MENU.get(), id);
        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());

        InventorySlotHelper.addPlayerInventorySlots(this::addSlot, playerInventory, 8, 140);

        InventorySlotHelper.addHotbarSlots(this::addSlot, playerInventory, 8, 198);


        if (entity instanceof ResearcherBlockEntity researcherEntity) {
            ItemStackHandler container = Objects.requireNonNull(researcherEntity.getItemStackHandler(), "Researcher container is null");

            for (int i = 0; i < 9; i++) {
                this.addSlot(new SlotItemHandler(container, i, 8 + i * 18, 12));
            }
            this.addSlot(new SlotItemHandler(container, 9, 80, 37));

            this.data = new SimpleContainerData(2); // [0] — енергія, [1] — макс. енергія

            addDataSlots(data);
            updateEnergyValues(researcherEntity);

        } else {
            throw new IllegalStateException("Invalid block entity for ResearcherMenu");
        }


    }

    public ContainerData getData() {
        return this.data;
    }

    private void updateEnergyValues(ResearcherBlockEntity blockEntity) {
        //data.set(0, blockEntity.getEnergyStorage().getEnergyStored());
        //data.set(1, blockEntity.getEnergyStorage().getMaxEnergyStored());
        data.set(0, 5000);
        data.set(1, 10000);
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
