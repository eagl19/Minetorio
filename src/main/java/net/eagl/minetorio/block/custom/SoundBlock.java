package net.eagl.minetorio.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SoundBlock extends Block {
    public SoundBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public void stepOn(Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Entity pEntity) {
        if(!pLevel.isClientSide && pEntity instanceof Player player){
            BlockPos spawnPos = pPos.relative(player.getDirection());
            if(pLevel.isEmptyBlock(spawnPos)){
                pLevel.setBlockAndUpdate(spawnPos, this.defaultBlockState());
            }
        }
    }


}
