package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new MinetorioRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), MinetorioLootTableProvider.create(packOutput));

        generator.addProvider(event.includeClient(), new MinetorioBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new MinetorioItemModelProvider(packOutput, existingFileHelper));

        MinetorioBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new MinetorioBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new MinetorioItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));

        generator.addProvider(event.includeServer(), new MinetorioGlobalLootModifiersProvider(packOutput));
        generator.addProvider(event.includeServer(), new MinetorioPoiTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
    }
}
