package net.eagl.minetorio.gui;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.eagl.minetorio.capability.MinetorioCapabilities;
import net.eagl.minetorio.gui.slot.FlaskSlot;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.client.ResearchListSyncToClientPacket;
import net.eagl.minetorio.network.client.SyncTechnologyProgressPacket;
import net.eagl.minetorio.util.InventorySlot;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.enums.FlaskColor;
import net.eagl.minetorio.util.storage.FlaskStorage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static net.eagl.minetorio.util.FlasksField.getFlaskItemByColor;

public class ResearcherMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final ResearcherBlockEntity be;

    private final ContainerData data;

    public ResearcherMenu(int id, Inventory playerInventory, BlockEntity entity) {
        super(MinetorioMenus.RESEARCHER_MENU.get(), id);



        this.access = ContainerLevelAccess.create(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos());

        InventorySlot.addHotbarAndPlayerInventorySlots(this::addSlot, playerInventory, 0, 8, 140, 3, 9, 18, 18, 58);

        if (entity instanceof ResearcherBlockEntity researcherEntity) {

            be = researcherEntity;

            FlaskStorage container = researcherEntity.getItemStackHandler();

            int startX = 8;
            int startY = 98;
            int slotSpacingX = 18;
            int slotsPerRow = 6;

            FlaskColor[] colors = FlaskColor.values();

            for (int i = 0; i < colors.length; i++) {
                int x = startX + (i % slotsPerRow) * slotSpacingX;
                int y = startY + (i / slotsPerRow) * 18;
                Item flaskItem = getFlaskItemByColor(colors[i]);
                this.addSlot(new FlaskSlot(container, i, x, y, flaskItem));
            }

            this.data = researcherEntity.getContainerData();
            addDataSlots(this.data);

        } else {
            throw new IllegalStateException("Invalid block entity for ResearcherMenu");
        }

        if (playerInventory.player instanceof ServerPlayer serverPlayer) {
            MinetorioNetwork.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new ResearchListSyncToClientPacket(be.getBlockPos(), be.getResearchPlan().getPlan())
            );

            serverPlayer.getCapability(MinetorioCapabilities.TECHNOLOGY_PROGRESS).ifPresent(progress -> {
                MinetorioNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new SyncTechnologyProgressPacket(progress.serializeNBT())
                );
            });
        }

    }


    public ContainerData getData() {
        return this.data;
    }

    public ResearcherMenu (int id, Inventory inv, FriendlyByteBuf extraData){
        this(id, inv, Objects.requireNonNull(inv.player.level().getBlockEntity(extraData.readBlockPos())));
    }


    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack originalStack = slot.getItem();
        ItemStack copy = originalStack.copy();

        if(slot instanceof FlaskSlot){
            return ItemStack.EMPTY;
        }

         if (index >= 9 && index < 36) {
            if (isFlaskItem(originalStack.getItem())) {
                if (!moveItemStackTo(originalStack, 36, 48, false) &&
                        !moveItemStackTo(originalStack, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!moveItemStackTo(originalStack, 0, 9, false)) {
                return ItemStack.EMPTY;
            }
        }
        else if (index >= 0 && index < 9) {
            if (isFlaskItem(originalStack.getItem())) {
                if (!moveItemStackTo(originalStack, 36, 48, false) &&
                        !moveItemStackTo(originalStack, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!moveItemStackTo(originalStack, 9, 36, false)) {
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

    private boolean isFlaskItem(Item item) {
        return item.getDescriptionId().contains("flask_");
    }


    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(access, pPlayer, MinetorioBlocks.RESEARCHER.get());
    }

    public  int getMaxEnergyStorage(){
        return data.get(ResearcherBlockEntity.MAX_ENERGY);
    }

    public int getEnergy(){
        return data.get(ResearcherBlockEntity.ENERGY);
    }

    public int getWater() {
        return data.get(ResearcherBlockEntity.WATER);
    }
    public int getMaxWaterStorage(){
        return data.get(ResearcherBlockEntity.MAX_WATER);
    }
    public int getLava(){
        return  data.get(ResearcherBlockEntity.LAVA);
    }
    public int getMaxLavaStorage(){
        return  data.get(ResearcherBlockEntity.MAX_LAVA);
    }
    public int getLearn(){
        return  data.get(ResearcherBlockEntity.LEARN);
    }
    public int getMaxLearn(){
        return data.get(ResearcherBlockEntity.MAX_LEARN);
    }

    public  List<Technology> getTechList(){
        return be.getResearchPlan().getPlan();
    }

    public ResearcherBlockEntity getBlockEntity(){
        return be;
    }

    public void handleButtonPress(int id, Player player) {

        if (id == 0) {
            be.researchTechnologyDone(player);
        }
    }
}
