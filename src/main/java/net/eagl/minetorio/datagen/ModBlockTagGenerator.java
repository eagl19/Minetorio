package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.util.MinetorioTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Minetorio.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(MinetorioBlocks.SAPPHIRE_BLOCK.get(),
                        MinetorioBlocks.RAW_SAPPHIRE_BLOCK.get(),
                        MinetorioBlocks.SAPPHIRE_ORE.get(),
                        MinetorioBlocks.DEEPSLATE_SAPPHIRE_ORE.get(),
                        MinetorioBlocks.NETHER_SAPPHIRE_ORE.get(),
                        MinetorioBlocks.END_STONE_SAPPHIRE_ORE.get(),
                        MinetorioBlocks.DUPLICATOR_BLOCK.get());

        this.tag(MinetorioTags.Blocks.ORES)
                .add(MinetorioBlocks.SAPPHIRE_ORE.get(),
                        MinetorioBlocks.DEEPSLATE_SAPPHIRE_ORE.get(),
                        MinetorioBlocks.NETHER_SAPPHIRE_ORE.get(),
                        MinetorioBlocks.END_STONE_SAPPHIRE_ORE.get());

        this.tag(MinetorioTags.Blocks.METAL_DETECTOR_VALUABLES)
                .addTag(MinetorioTags.Blocks.ORES)
                .addTag(Tags.Blocks.ORES);


        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(MinetorioBlocks.SAPPHIRE_BLOCK.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(MinetorioBlocks.RAW_SAPPHIRE_BLOCK.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(MinetorioBlocks.NETHER_SAPPHIRE_ORE.get());

        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                .add(MinetorioBlocks.END_STONE_SAPPHIRE_ORE.get());


    }
}
