package net.eagl.minetorio.gui;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.WaterGeneratorBlockEntity;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.client.CachedFluidTargetsSyncToClientPacket;
import net.eagl.minetorio.util.InventorySlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class WaterGeneratorMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final ContainerData data;
    private final WaterGeneratorBlockEntity blockEntity;

    public WaterGeneratorMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(MinetorioMenus.WATER_GENERATOR_MENU.get(), id);

        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());

        InventorySlot.addHotbarAndPlayerInventorySlots(this::addSlot, playerInventory, 0, 8, 140, 3, 9, 18, 18, 58);
        if (entity instanceof WaterGeneratorBlockEntity waterGenerator) {
            this.blockEntity = waterGenerator;
            this.data = waterGenerator.getContainerData();
            addDataSlots(this.data);
        }else {
            throw new IllegalStateException("Invalid block entity for Water Generator");
        }
        if (playerInventory.player instanceof ServerPlayer serverPlayer) {
            MinetorioNetwork.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new CachedFluidTargetsSyncToClientPacket(blockEntity.getBlockPos(), blockEntity.getCachedFluidTargets())
            );
        }
    }

    public ItemStack getItemFromFluidTarget(int target) {
        List<BlockPos> targets = blockEntity.getCachedFluidTargets();
        if (!targets.isEmpty() && targets.size() >= target) {
            BlockPos pos = targets.get(target);
            Level beLevel = blockEntity.getLevel();
            if(beLevel != null) {
                BlockEntity be = beLevel.getBlockEntity(pos);
                if (be != null) {
                    return be.getBlockState().getBlock().asItem().getDefaultInstance();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public ContainerData getData() {
        return this.data;
    }

    public WaterGeneratorMenu (int id, Inventory inv, FriendlyByteBuf extraData){
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(access, pPlayer, MinetorioBlocks.WATER_GENERATOR.get());
    }

    public  int getMaxEnergyStorage(){
        return data.get(WaterGeneratorBlockEntity.MAX_ENERGY);
    }

    public int getEnergy(){
        return data.get(WaterGeneratorBlockEntity.ENERGY);
    }

    public int getWater() {
        return data.get(WaterGeneratorBlockEntity.FLUID);
    }
    public int getMaxWaterStorage(){
        return data.get(WaterGeneratorBlockEntity.MAX_FLUID);
    }

    public int getProduce() {
        return data.get(WaterGeneratorBlockEntity.PRODUCE);
    }
    public int getMaxProduce(){
        return data.get(WaterGeneratorBlockEntity.MAX_PRODUCE);
    }
}
