package net.eagl.minetorio.gui.menu;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.EnergyGeneratorBlockEntity;
import net.eagl.minetorio.gui.MinetorioMenus;
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

public class EnergyGeneratorMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final EnergyGeneratorBlockEntity be;

    private final ContainerData data;


    public EnergyGeneratorMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(MinetorioMenus.ENERGY_GENERATOR_MENU.get(), id);

        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());
        InventorySlot.addHotbarAndPlayerInventorySlots(this::addSlot, playerInventory, 0, 8, 140, 3, 9, 18, 18, 58);

        if (entity instanceof EnergyGeneratorBlockEntity energyGenerator) {
            be = energyGenerator;
            this.data = energyGenerator.getContainerData();
            addDataSlots(this.data);
        } else {
            throw new IllegalStateException("Invalid block entity for Energy Generator");
        }
    }

    public EnergyGeneratorMenu (int id, Inventory inv, FriendlyByteBuf extraData){
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(access, pPlayer, MinetorioBlocks.ENERGY_GENERATOR.get());
    }

    public EnergyGeneratorBlockEntity getBlockEntity(){
        return be;
    }

    public int getEnergy(){
        return data.get(EnergyGeneratorBlockEntity.ENERGY);
    }
    public int getMaxEnergy(){
        return data.get(EnergyGeneratorBlockEntity.MAX_ENERGY);
    }
     public int getProduce(){
        return data.get(EnergyGeneratorBlockEntity.PRODUCE);
     }
     public int getMaxProduce(){
        return data.get(EnergyGeneratorBlockEntity.MAX_PRODUCE);
     }
}
