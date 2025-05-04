package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.util.MinetorioTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class MinetorioItemTagGenerator extends ItemTagsProvider {
    public MinetorioItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                                     CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, Minetorio.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {


        this.tag(MinetorioTags.Items.FOODS)
                .add(MinetorioItems.STRAWBERRY.get());

        this.tag(MinetorioTags.Items.FUELS)
                .add(MinetorioItems.PINE_CONE.get());

        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(MinetorioItems.SAPPHIRE_HELMET.get(),
                        MinetorioItems.SAPPHIRE_CHESTPLATE.get(),
                        MinetorioItems.SAPPHIRE_LEGGINGS.get(),
                        MinetorioItems.SAPPHIRE_BOOTS.get());

        this.tag(ItemTags.MUSIC_DISCS)
                .add(MinetorioItems.BAR_BRAWL_MUSIC_DISC.get());

        this.tag(ItemTags.CREEPER_DROP_MUSIC_DISCS)
                .add(MinetorioItems.BAR_BRAWL_MUSIC_DISC.get());

    }
}
