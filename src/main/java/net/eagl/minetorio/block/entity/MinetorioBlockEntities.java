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

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<PatternsCollectorBlockEntity>> PATTERNS_COLLECTOR =
            BLOCK_ENTITIES.register("minetorio_beacon", () ->
                    BlockEntityType.Builder.of(PatternsCollectorBlockEntity::new, MinetorioBlocks.PATTERNS_COLLECTOR.get()).build(null));

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<PortalBlockEntity>> PORTAL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("portal", () ->
                    BlockEntityType.Builder.of(PortalBlockEntity::new, MinetorioBlocks.PORTAL.get()).build(null));

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<ResearcherBlockEntity>> RESEARCHER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("researcher", () ->
                    BlockEntityType.Builder.of(ResearcherBlockEntity::new, MinetorioBlocks.RESEARCHER.get()).build(null));

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<WaterGeneratorBlockEntity>> WATER_GENERATOR_ENTITY =
            BLOCK_ENTITIES.register("water_generator", () ->
                    BlockEntityType.Builder.of(WaterGeneratorBlockEntity::new, MinetorioBlocks.WATER_GENERATOR.get()).build(null));

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<LavaGeneratorBlockEntity>> LAVA_GENERATOR_ENTITY =
            BLOCK_ENTITIES.register("lava_generator", () ->
                    BlockEntityType.Builder.of(LavaGeneratorBlockEntity::new, MinetorioBlocks.LAVA_GENERATOR.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
