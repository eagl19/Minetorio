package net.eagl.minetorio.gui;

import net.eagl.minetorio.block.entity.AbstractFluidGeneratorBlockEntity;
import net.eagl.minetorio.capability.MinetorioCapabilities;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.client.CachedBlockPosConsumerSyncToClientPacket;
import net.eagl.minetorio.network.client.CachedBlockPosListPosSyncToClientPacket;
import net.eagl.minetorio.util.CachedBlockPos;
import net.eagl.minetorio.util.InventorySlot;
import net.eagl.minetorio.util.Technology;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractFluidGeneratorMenu<T extends AbstractFluidGeneratorBlockEntity> extends AbstractContainerMenu {

    protected final ContainerLevelAccess access;
    protected final ContainerData data;
    protected final T blockEntity;
    private int consumersCount = 6;
    private final Technology tech;

    protected AbstractFluidGeneratorMenu(int id, Inventory playerInventory, T blockEntity, MenuType<?> menuType, Technology tech) {
        super(menuType, id);
       this.tech = tech;

        this.access = ContainerLevelAccess.create(Objects.requireNonNull(blockEntity.getLevel()), blockEntity.getBlockPos());

        InventorySlot.addHotbarAndPlayerInventorySlots(this::addSlot, playerInventory, 0, 8, 140, 3, 9, 18, 18, 58);

            this.data = blockEntity.getContainerData();

            this.blockEntity = blockEntity;
            if (playerInventory.player instanceof ServerPlayer serverPlayer) {
                blockEntity.initializedTargets();
                MinetorioNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new CachedBlockPosConsumerSyncToClientPacket(blockEntity.getBlockPos(), blockEntity.getCachedFluidTargets().getConsumers())
                );
                MinetorioNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new CachedBlockPosListPosSyncToClientPacket(blockEntity.getBlockPos(), blockEntity.getCachedFluidTargets().getListPos())
                );
                serverPlayer.getCapability(MinetorioCapabilities.TECHNOLOGY_PROGRESS).ifPresent(techCap -> {
                    if (techCap.hasLearned(tech.getId())) {
                        consumersCount = 12;
                    }
                });
            }
            this.data.set(AbstractFluidGeneratorBlockEntity.MAX_CONSUMERS, consumersCount);
            addDataSlots(this.data);

    }

    public CachedBlockPos getFluidTargets(){
        return blockEntity.getCachedFluidTargets();
    }

    public ItemStack getItemFromBlockPos(BlockPos target) {
        Level beLevel = blockEntity.getLevel();
        if (beLevel != null) {
            BlockEntity be = beLevel.getBlockEntity(target);
            if (be != null) {
                return be.getBlockState().getBlock().asItem().getDefaultInstance();
            }
        }
        return ItemStack.EMPTY;
    }

    public T getGeneratorBlockEntity() {
        return blockEntity;
    }
    public ContainerData getData() {
        return this.data;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(access, pPlayer, blockEntity.getBlockState().getBlock());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    public int getMaxEnergyStorage() {
        return data.get(AbstractFluidGeneratorBlockEntity.MAX_ENERGY);
    }

    public int getEnergy() {
        return data.get(AbstractFluidGeneratorBlockEntity.ENERGY);
    }

    public int getFluid() {
        return data.get(AbstractFluidGeneratorBlockEntity.FLUID);
    }

    public int getMaxFluidStorage() {
        return data.get(AbstractFluidGeneratorBlockEntity.MAX_FLUID);
    }

    public int getProduce() {
        return data.get(AbstractFluidGeneratorBlockEntity.PRODUCE);
    }

    public int getMaxProduce() {
        return data.get(AbstractFluidGeneratorBlockEntity.MAX_PRODUCE);
    }

    public int getConsumersCount() {
        return data.get(AbstractFluidGeneratorBlockEntity.MAX_CONSUMERS);
    }

    public Technology getTech(){
        return tech;
    }
}

