package net.eagl.minetorio.entity.custom;

import net.eagl.minetorio.entity.MinetorioEntities;
import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MinetorioChestBoatEntity extends ChestBoat {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);

    public MinetorioChestBoatEntity(EntityType<? extends ChestBoat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public MinetorioChestBoatEntity(Level pLevel, double pX, double pY, double pZ) {
        this(MinetorioEntities.MOD_CHEST_BOAT.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }

    @Override
    public @NotNull Item getDropItem() {
        switch (getModVariant()) {
            case PINE -> {
                return MinetorioItems.PINE_CHEST_BOAT.get();
            }
        }
        return super.getDropItem();
    }

    public void setVariant(MinetorioBoatEntity.Type pVariant) {
        this.entityData.set(DATA_ID_TYPE, pVariant.ordinal());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, MinetorioBoatEntity.Type.PINE.ordinal());
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putString("Type", this.getModVariant().getSerializedName());
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Type", 8)) {
            this.setVariant(MinetorioBoatEntity.Type.byName(pCompound.getString("Type")));
        }
    }

    public MinetorioBoatEntity.Type getModVariant() {
        return MinetorioBoatEntity.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }
}
