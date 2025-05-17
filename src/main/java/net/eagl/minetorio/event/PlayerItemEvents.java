package net.eagl.minetorio.event;


import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.util.PatternItemsCollector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class PlayerItemEvents {

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        Player player = event.getPlayer();

        Item item = event.getEntity().getItem().getItem();

        if (PatternItemsCollector.getPatternItems().contains(item))  {
            // Скасувати викидання предмета
            event.setCanceled(true);

            // Повернути предмет назад у інвентар
            ItemStack stack = event.getEntity().getItem();
            if (!player.getInventory().add(stack)) {
                // Якщо інвентар повний — повертаємо назад у світ
                player.drop(stack, false);
            }
        }
    }
}

