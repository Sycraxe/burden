package dev.sycraxe.burden.event;

import dev.sycraxe.burden.backpack.BackpackContext;
import dev.sycraxe.burden.backpack.BackpackMenu;
import dev.sycraxe.burden.inventory.InventoryHandlerSlot;
import dev.sycraxe.burden.network.BackpackOpeningData;
import dev.sycraxe.burden.register.*;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Optional;

public class CommonEventHandler {
    public static void register(IEventBus modBus) {
        ModBlock.register(modBus);
        ModBlockEntity.register(modBus);
        ModItem.register(modBus);
        ModMenuType.register(modBus);
        ModTab.register(modBus);
        ModCompatibility.register(modBus);
        modBus.addListener(CommonEventHandler::registerPayloads);
    }

    private static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
            BackpackOpeningData.TYPE,
            BackpackOpeningData.STREAM_CODEC,
            (final BackpackOpeningData data, final IPayloadContext payloadContext) -> {
                Player player = payloadContext.player();
                Optional<InventoryHandlerSlot> maybeHandlerSlot = ModInventoryHandler.ON_KEYBIND.find(player, (s) -> s.is(ModItem.BACKPACK));
                if (maybeHandlerSlot.isPresent()) {
                    InventoryHandlerSlot handlerSlot = maybeHandlerSlot.get();
                    BackpackContext backpackContext = new BackpackContext.Item(handlerSlot);
                    player.openMenu(new SimpleMenuProvider((id, inventory, p) -> new BackpackMenu(id, inventory, backpackContext), handlerSlot.handler().get(player, handlerSlot.index()).getHoverName()), backpackContext::toBuffer);
                }
            }
        );
    };
}
