package net.eagl.minetorio.entity.custom;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.entity.MinetorioEntities;
import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.block.custom.DiceBlock;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class DiceProjectileEntity extends ThrowableItemProjectile {
    public DiceProjectileEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public DiceProjectileEntity(Level pLevel) {
        super(MinetorioEntities.DICE_PROJECTILE.get(), pLevel);
    }

    public DiceProjectileEntity(Level pLevel, LivingEntity livingEntity) {
        super(MinetorioEntities.DICE_PROJECTILE.get(), livingEntity, pLevel);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return MinetorioItems.DICE.get();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult pResult) {
        if(!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, ((byte) 3));
            this.level().setBlock(blockPosition(), ((DiceBlock) MinetorioBlocks.DICE_BLOCK.get()).getRandomBlockState(), 3);
        }

        this.discard();
        super.onHitBlock(pResult);
    }
}
