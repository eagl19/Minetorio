package net.eagl.minetorio.entity.client;

import net.eagl.minetorio.Minetorio;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class MinetorioModelLayers {
    public static final ModelLayerLocation RHINO_LAYER = new ModelLayerLocation(
            Minetorio.resourceLocation("rhino_layer"), "main");

    public static final ModelLayerLocation PINE_BOAT_LAYER = new ModelLayerLocation(
            Minetorio.resourceLocation("boat/pine"), "main");
    public static final ModelLayerLocation PINE_CHEST_BOAT_LAYER = new ModelLayerLocation(
            Minetorio.resourceLocation("chest_boat/pine"), "main");

}
