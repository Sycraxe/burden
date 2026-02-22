package dev.sycraxe.burden.event;

import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.gui.menu.BackpackItemMenuProvider;
import dev.sycraxe.burden.networking.BackpackOpeningData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static dev.sycraxe.burden.gui.menu.BackpackItemMenuProvider.KEYBIND_INIT_SLOT_FUNCTION;

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
                (final BackpackOpeningData data, final IPayloadContext context) -> context.player().openMenu(
                        new BackpackItemMenuProvider(KEYBIND_INIT_SLOT_FUNCTION.apply(context.player().getInventory()).getHoverName(), KEYBIND_INIT_SLOT_FUNCTION)
                )
        );
    }
}
