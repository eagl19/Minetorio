package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MinetorioItemTagGenerator extends ItemTagsProvider {

    public static final TagKey<Item> FLASKS = TagKey.create(
            net.minecraft.core.registries.Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, "flasks")
    );


    public MinetorioItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                                     CompletableFuture<TagLookup<Block>> p_275322_,
                                     @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, Minetorio.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {

        tag(FLASKS).add(MinetorioItems.FLASK_BLACK.get())
                .add(MinetorioItems.FLASK_BLUE.get())
                .add(MinetorioItems.FLASK_BROWN.get())
                .add(MinetorioItems.FLASK_CYAN.get())
                .add(MinetorioItems.FLASK_GRAY.get())
                .add(MinetorioItems.FLASK_GREEN.get())
                .add(MinetorioItems.FLASK_ORANGE.get())
                .add(MinetorioItems.FLASK_PINK.get())
                .add(MinetorioItems.FLASK_PURPLE.get())
                .add(MinetorioItems.FLASK_RED.get())
                .add(MinetorioItems.FLASK_WHITE.get())
                .add(MinetorioItems.FLASK_YELLOW.get());


    }
}
