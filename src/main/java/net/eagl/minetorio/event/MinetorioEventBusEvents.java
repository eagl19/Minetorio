package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.entity.MinetorioEntities;
import net.eagl.minetorio.entity.custom.RhinoEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinetorioEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(MinetorioEntities.RHINO.get(), RhinoEntity.createAttributes().build());
    }
}
