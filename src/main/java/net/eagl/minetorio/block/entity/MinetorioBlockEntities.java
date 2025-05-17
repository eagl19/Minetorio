package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MinetorioBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Minetorio.MOD_ID);

    public static final RegistryObject<BlockEntityType<MinetorioBeaconBlockEntity>> MINETORIO_BEACON =
            BLOCK_ENTITIES.register("minetorio_beacon", () ->
                    BlockEntityType.Builder.of(MinetorioBeaconBlockEntity::new, MinetorioBlocks.BEACON.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
