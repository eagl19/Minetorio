package net.eagl.minetorio.gui;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.LavaGenetatorBlockEntity;
import net.eagl.minetorio.util.InventorySlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LavaGeneratorMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final ContainerData data;

    public LavaGeneratorMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(MinetorioMenus.LAVA_GENERATOR_MENU.get(), id);

        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());

        InventorySlot.addHotbarAndPlayerInventorySlots(this::addSlot, playerInventory, 0, 8, 140, 3, 9, 18, 18, 58);
        if (entity instanceof LavaGenetatorBlockEntity lavaGenerator) {

            this.data = lavaGenerator.getContainerData();
            addDataSlots(this.data);
        }else {
            throw new IllegalStateException("Invalid block entity for Water Generator");
        }
    }

    public ContainerData getData() {
        return this.data;
    }

    public LavaGeneratorMenu (int id, Inventory inv, FriendlyByteBuf extraData){
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(access, pPlayer, MinetorioBlocks.LAVA_GENERATOR.get());
    }

    public  int getMaxEnergyStorage(){
        return data.get(LavaGenetatorBlockEntity.MAX_ENERGY);
    }

    public int getEnergy(){
        return data.get(LavaGenetatorBlockEntity.ENERGY);
    }

    public int getLava() {
        return data.get(LavaGenetatorBlockEntity.LAVA);
    }
    public int getMaxLavaStorage(){
        return data.get(LavaGenetatorBlockEntity.MAX_LAVA);
    }

    public int getProduce() {
        return data.get(LavaGenetatorBlockEntity.PRODUCE);
    }
    public int getMaxProduce(){
        return data.get(LavaGenetatorBlockEntity.MAX_PRODUCE);
    }
}
