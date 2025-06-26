package net.eagl.minetorio.gui.menu;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.EnergyGeneratorBlockEntity;
import net.eagl.minetorio.capability.MinetorioCapabilities;
import net.eagl.minetorio.gui.MinetorioMenus;
import net.eagl.minetorio.gui.slot.FlaskSlot;
import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.client.CachedBlockPosConsumerSyncToClientPacket;
import net.eagl.minetorio.network.client.CachedBlockPosListPosSyncToClientPacket;
import net.eagl.minetorio.util.CachedBlockPos;
import net.eagl.minetorio.util.InventorySlot;
import net.eagl.minetorio.util.Technologies;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.storage.UpgradeStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class EnergyGeneratorMenu extends AbstractContainerMenu implements IGeneratorMenu {

    private final ContainerLevelAccess access;
    private final EnergyGeneratorBlockEntity be;
    private int consumersCount = 7;
    private final ContainerData data;
    private final Technology tech = Technologies.SUN;

    public EnergyGeneratorMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(MinetorioMenus.ENERGY_GENERATOR_MENU.get(), id);

        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());
        InventorySlot.addHotbarAndPlayerInventorySlots(this::addSlot, playerInventory, 0, 8, 140, 3, 9, 18, 18, 58);

        if (entity instanceof EnergyGeneratorBlockEntity energyGenerator) {
            be = energyGenerator;
            this.data = energyGenerator.getContainerData();

            UpgradeStorage container = energyGenerator.getUpgrades();

            this.addSlot(new FlaskSlot(container, 0, 8, 27 , MinetorioItems.BATTERY_BUSTER.get()));
            this.addSlot(new FlaskSlot(container, 1, 8, 45 , MinetorioItems.MICRO_INVERTER.get()));
            this.addSlot(new FlaskSlot(container, 2, 8, 63 , MinetorioItems.NANO_ENCHANTER.get()));
            this.addSlot(new FlaskSlot(container, 3, 8, 81 , MinetorioItems.PHOTON_AMPLIFIER.get()));
            this.addSlot(new FlaskSlot(container, 4, 8, 99 , MinetorioItems.QUARTZ_LENS.get()));

            if (playerInventory.player instanceof ServerPlayer serverPlayer) {
                energyGenerator.initializedTargets();
                MinetorioNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new CachedBlockPosConsumerSyncToClientPacket(energyGenerator.getBlockPos(), energyGenerator.getCachedEnergyTargets().getConsumers())
                );
                MinetorioNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new CachedBlockPosListPosSyncToClientPacket(energyGenerator.getBlockPos(), energyGenerator.getCachedEnergyTargets().getListPos())
                );
                serverPlayer.getCapability(MinetorioCapabilities.TECHNOLOGY_PROGRESS).ifPresent(techCap -> {
                    if (techCap.hasLearned(tech.getId())) {
                        consumersCount = 14;
                    }
                });
            }
            this.data.set(EnergyGeneratorBlockEntity.MAX_CONSUMERS, consumersCount);
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
        Slot slot = this.slots.get(pIndex);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack originalStack = slot.getItem();
        ItemStack copy = originalStack.copy();

        if (pIndex >= 9 && pIndex < 36) {
            if (!moveItemStackTo(originalStack, 36, 41, false) &&
                    !moveItemStackTo(originalStack, 0, 9, false)) {
                return ItemStack.EMPTY;
            }

        } else if (pIndex >= 0 && pIndex < 9) {

            if (!moveItemStackTo(originalStack, 36, 41, false) &&
                    !moveItemStackTo(originalStack, 9, 36, false)) {
                return ItemStack.EMPTY;
            }

        }else if (pIndex >= 36 && pIndex < 41) {
            if (!moveItemStackTo(originalStack, 0, 9, false) &&
                    !moveItemStackTo(originalStack, 9, 36, false)) {
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
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(access, pPlayer, MinetorioBlocks.ENERGY_GENERATOR.get());
    }

    public CachedBlockPos getTargets(){
        return be.getCachedEnergyTargets();
    }

    @Override
    public List<BlockPos> getTargetsList() {
        return this.getTargets().getListPos();
    }

    @Override
    public List<BlockPos> getConsumersList() {
        return this.getTargets().getConsumers();
    }

    public ItemStack getItemFromBlockPos(BlockPos target) {
        Level beLevel = be.getLevel();
        if (beLevel != null) {
            BlockEntity beTarget = beLevel.getBlockEntity(target);
            if (beTarget != null) {
                return beTarget.getBlockState().getBlock().asItem().getDefaultInstance();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public BlockPos getBlockPos() {
        return be.getBlockPos();
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
    public int getConsumersCount() {
        return data.get(EnergyGeneratorBlockEntity.MAX_CONSUMERS);
    }

    public Technology getTech() {
        return tech;
    }
}
