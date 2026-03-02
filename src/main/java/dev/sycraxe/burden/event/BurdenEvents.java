package dev.sycraxe.burden.event;

import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.gui.menu.BackpackItemMenuProvider;
import dev.sycraxe.burden.item.BackpackItem;
import dev.sycraxe.burden.networking.BackpackOpeningData;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.Function;

@Mod(value = Burden.MOD_ID)
@EventBusSubscriber(modid = Burden.MOD_ID)
public class BurdenEvents {
    public BurdenEvents(ModContainer container) {}

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                BackpackOpeningData.TYPE,
                BackpackOpeningData.STREAM_CODEC,
                (final BackpackOpeningData data, final IPayloadContext context) -> {
                    Player player = context.player();
                    BackpackItem.BackpackItemOpeningMode mode = BackpackItem.onKeybindOpeningMode(player.getInventory());
                    if (mode != BackpackItem.BackpackItemOpeningMode.NONE) {
                        Function<Inventory, ItemStack> origin = BackpackItem.OPENING_MODES_TRIGGERER_SLOT_STACK_GETTER.get(mode);
                        player.openMenu(new BackpackItemMenuProvider(origin.apply(player.getInventory()).getHoverName(), origin, BackpackItem.OPENING_MODES_UNPICKABLE_SLOT.get(mode).apply(player.getInventory())));
                    }
                }
        );
    }
}
