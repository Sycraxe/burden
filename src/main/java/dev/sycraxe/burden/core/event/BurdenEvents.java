package dev.sycraxe.burden.core.event;

import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.core.codec.BackpackEventCodec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(value = Burden.MOD_ID)
@EventBusSubscriber(modid = Burden.MOD_ID)
public class BurdenEvents {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                BackpackEventCodec.TYPE,
                BackpackEventCodec.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        (data, context) -> {},
                        BackpackServerPayloadHandler::handleDataOnMain
                )
        );
    }
}
