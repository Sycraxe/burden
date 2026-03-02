package dev.sycraxe.burden.event;

import dev.sycraxe.burden.backpack.BackpackItemMenuProvider;
import dev.sycraxe.burden.backpack.BackpackItem;
import dev.sycraxe.burden.network.BackpackOpeningData;
import dev.sycraxe.burden.register.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.Function;

public class CommonEventHandler {
    public static void register(IEventBus modBus) {
        ModBlock.register(modBus);
        ModBlockEntities.register(modBus);
        ModItem.register(modBus);
        ModMenuType.register(modBus);
        ModTab.register(modBus);
        modBus.addListener(CommonEventHandler::registerPayloads);
    }

    private static void registerPayloads(final RegisterPayloadHandlersEvent event) {
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
    };
}
