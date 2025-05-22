package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.gui.PatternsCollectorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PatternsCollectorBlockEntity extends BlockEntity implements MenuProvider {

    private final float speedX;
    private final float speedY;
    private final float speedZ;
    private float rotation = 0;

    private float currentYOffset = 0;


    public PatternsCollectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.PATTERNS_COLLECTOR.get(), pPos, pBlockState);

        RandomSource rand = RandomSource.create(pPos.asLong()); // Детермінований для кожного блоку
        this.speedX = 0.5f + rand.nextFloat(); // від 0.5 до 1.5
        this.speedY = 0.5f + rand.nextFloat();
        this.speedZ = 0.5f + rand.nextFloat();
    }



    public void setCurrentOffset(float offset) {
        this.currentYOffset=offset;
    }

    public void tickClient() {
        rotation += 1.0f;
    }

    public void tickServer() {

    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Patterns Collector");
    }


    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new PatternsCollectorMenu(pContainerId, pPlayerInventory,this);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat("rotation", rotation);
        tag.putFloat("currentYOffset", currentYOffset);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.rotation = tag.getFloat("rotation");
        this.currentYOffset = tag.getFloat("currentYOffset");
    }

    public float getCurrentYOffset() {
        return this.currentYOffset;
    }

    public float getSpeedZ() {
        return speedZ;
    }

    public float getSpeedY() {
        return speedY;
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getRotation() {
        return rotation;
    }
}
