package net.eagl.minetorio.item;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.util.MinetorioTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class MinetorioToolTiers {
    public static final Tier SAPPHIRE = TierSortingRegistry.registerTier(
            new ForgeTier(5, 1500, 5f, 4f, 25,
                    MinetorioTags.Blocks.NEEDS_SAPPHIRE_TOOL, () -> Ingredient.of(MinetorioItems.SAPPHIRE.get())),
            Minetorio.resourceLocation("sapphire"), List.of(Tiers.NETHERITE), List.of());

}
