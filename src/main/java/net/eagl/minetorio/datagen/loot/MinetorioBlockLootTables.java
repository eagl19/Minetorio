package net.eagl.minetorio.datagen.loot;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.CornCropBlock;
import net.eagl.minetorio.block.custom.StrawberryCropBlock;
import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class MinetorioBlockLootTables extends BlockLootSubProvider {
    public MinetorioBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(MinetorioBlocks.SAPPHIRE_BLOCK.get());
        this.dropSelf(MinetorioBlocks.RAW_SAPPHIRE_BLOCK.get());
        this.dropSelf(MinetorioBlocks.DUPLICATOR_BLOCK.get());
        this.dropSelf(MinetorioBlocks.SOUND_BLOCK.get());

        this.add(MinetorioBlocks.SAPPHIRE_ORE.get(),
                block -> createSapphireOreDrops(MinetorioBlocks.SAPPHIRE_ORE.get(), MinetorioItems.RAW_SAPPHIRE.get()));
        this.add(MinetorioBlocks.DEEPSLATE_SAPPHIRE_ORE.get(),
                block -> createSapphireOreDrops(MinetorioBlocks.DEEPSLATE_SAPPHIRE_ORE.get(), MinetorioItems.RAW_SAPPHIRE.get()));
        this.add(MinetorioBlocks.NETHER_SAPPHIRE_ORE.get(),
                block -> createSapphireOreDrops(MinetorioBlocks.NETHER_SAPPHIRE_ORE.get(), MinetorioItems.RAW_SAPPHIRE.get()));
        this.add(MinetorioBlocks.END_STONE_SAPPHIRE_ORE.get(),
                block -> createSapphireOreDrops(MinetorioBlocks.END_STONE_SAPPHIRE_ORE.get(), MinetorioItems.RAW_SAPPHIRE.get()));

        this.dropSelf(MinetorioBlocks.SAPPHIRE_STAIRS.get());
        this.dropSelf(MinetorioBlocks.SAPPHIRE_BUTTON.get());
        this.dropSelf(MinetorioBlocks.SAPPHIRE_PRESSURE_PLATE.get());
        this.dropSelf(MinetorioBlocks.SAPPHIRE_TRAPDOOR.get());
        this.dropSelf(MinetorioBlocks.SAPPHIRE_FENCE.get());
        this.dropSelf(MinetorioBlocks.SAPPHIRE_FENCE_GATE.get());
        this.dropSelf(MinetorioBlocks.SAPPHIRE_WALL.get());

        this.add(MinetorioBlocks.SAPPHIRE_SLAB.get(),
                block -> createSlabItemTable(MinetorioBlocks.SAPPHIRE_SLAB.get()));
        this.add(MinetorioBlocks.SAPPHIRE_DOOR.get(),
                block -> createDoorTable(MinetorioBlocks.SAPPHIRE_DOOR.get()));

        LootItemCondition.Builder lootitemcondition$builder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(MinetorioBlocks.STRAWBERRY_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StrawberryCropBlock.AGE, 5));

        this.add(MinetorioBlocks.STRAWBERRY_CROP.get(), createCropDrops(MinetorioBlocks.STRAWBERRY_CROP.get(), MinetorioItems.STRAWBERRY.get(),
                MinetorioItems.STRAWBERRY_SEEDS.get(), lootitemcondition$builder));

        LootItemCondition.Builder lootitemcondition$builder2 = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(MinetorioBlocks.CORN_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CornCropBlock.AGE, 7))
                .or(LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(MinetorioBlocks.CORN_CROP.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CornCropBlock.AGE, 8)));

        // LootItemCondition.Builder lootitemcondition$builder2 = LootItemBlockStatePropertyCondition
        //         .hasBlockStateProperties(ModBlocks.CORN_CROP.get())
        //         .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CornCropBlock.AGE, 8));

        this.add(MinetorioBlocks.CORN_CROP.get(), createCropDrops(MinetorioBlocks.CORN_CROP.get(), MinetorioItems.CORN.get(),
                MinetorioItems.CORN_SEEDS.get(), lootitemcondition$builder2));

        this.dropSelf(MinetorioBlocks.CATMINT.get());
        this.add(MinetorioBlocks.POTTED_CATMINT.get(), createPotFlowerItemTable(MinetorioBlocks.CATMINT.get()));

    }

    protected LootTable.Builder createSapphireOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return MinetorioBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
