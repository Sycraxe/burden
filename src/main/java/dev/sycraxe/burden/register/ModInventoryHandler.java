package dev.sycraxe.burden.register;

import dev.sycraxe.burden.inventory.InventoryHandler;
import dev.sycraxe.burden.inventory.InventoryHandlerResolver;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModInventoryHandler {
    private static final Map<String, InventoryHandlerResolver> RESOLVERS = new ConcurrentHashMap<>();

    public static final String ON_CLICK_ID = "on_click";
    public static final String ON_KEYBIND_ID = "on_keybind";

    public static final InventoryHandlerResolver ON_KEYBIND = registerContext(
            ON_KEYBIND_ID,
            new InventoryHandlerResolver()
    );
    public static final InventoryHandlerResolver ON_CLICK = registerContext(
            ON_CLICK_ID,
            new InventoryHandlerResolver()
    );

    public static final InventoryHandler CHEST_INVENTORY = registerInventoryHandler(
            new InventoryHandler(
                    (player, integer) -> player.getInventory().armor.get(EquipmentSlot.CHEST.getIndex()),
                    player -> 1
            ),
            Map.of(
                    ON_KEYBIND_ID, 100
            )
    );
    public static final InventoryHandler MAIN_HAND_INVENTORY = registerInventoryHandler(
            new InventoryHandler(
                    (player, integer) -> player.getInventory().getSelected(),
                    player -> 1
            ),
            Map.of(
                    ON_KEYBIND_ID, 200,
                    ON_CLICK_ID, 100
            )
    );
    public static final InventoryHandler OFFHAND_INVENTORY = registerInventoryHandler(
            new InventoryHandler(
                    (player, slot) -> player.getInventory().offhand.get(slot),
                    player -> player.getInventory().offhand.size()
            ),
            Map.of(
                    ON_KEYBIND_ID, 300,
                    ON_CLICK_ID, 200
            )
    );

    public static InventoryHandlerResolver registerContext(String id, InventoryHandlerResolver context) {
        RESOLVERS.put(id, context);
        return context;
    }

    public static InventoryHandler registerInventoryHandler(InventoryHandler handler, Map<String, Integer> priorities) {
        for (Map.Entry<String, Integer> entry : priorities.entrySet()) {
            InventoryHandlerResolver context = RESOLVERS.get(entry.getKey());
            if (context == null) {
                continue;
            }
            context.register(handler, entry.getValue());
        }
        return handler;
    }
}
