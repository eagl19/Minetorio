package net.eagl.minetorio.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PortalBlockEntity extends BlockEntity {
    private BlockPos teleportTarget;

    public PortalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.PORTAL_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public BlockPos getTeleportTarget() {
        return teleportTarget;
    }

    public void setTeleportTarget(BlockPos target) {
        this.teleportTarget = target;
        setChanged(); // позначаємо, що BlockEntity оновився
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (teleportTarget != null) {
            tag.putInt("TeleportX", teleportTarget.getX());
            tag.putInt("TeleportY", teleportTarget.getY());
            tag.putInt("TeleportZ", teleportTarget.getZ());
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("TeleportX") && tag.contains("TeleportY") && tag.contains("TeleportZ")) {
            int x = tag.getInt("TeleportX");
            int y = tag.getInt("TeleportY");
            int z = tag.getInt("TeleportZ");
            teleportTarget = new BlockPos(x, y, z);
        }
    }
}

