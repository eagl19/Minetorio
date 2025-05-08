package net.eagl.minetorio.util;

import net.eagl.minetorio.Minetorio;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class MinetorioWoodTypes {
    public static final WoodType PINE = WoodType.register(new WoodType(Minetorio.MOD_ID + ":pine", BlockSetType.OAK));
}
