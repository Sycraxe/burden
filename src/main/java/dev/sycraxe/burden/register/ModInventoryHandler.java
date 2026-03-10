package dev.sycraxe.burden.register;

import dev.sycraxe.burden.inventory.InventoryHandler;
import dev.sycraxe.burden.inventory.InventoryHandlerResolver;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModInventoryHandler {
    private static final Map<String, InventoryHandlerResolver> RESOLVERS = new ConcurrentHashMap<>();
    private static final Map<String, InventoryHandler> HANDLERS = new ConcurrentHashMap<>();

    public static final String ON_CLICK_ID = "click";
    public static final String ON_KEYBIND_ID = "keybind";

    public static final String CHEST_INVENTORY_ID = "chest";
    public static final String MAIN_HAND_INVENTORY_ID = "main_hand";
    public static final String OFFHAND_INVENTORY_ID = "offhand";

    public static final String EMPTY_ID = "empty";

    public static final InventoryHandler EMPTY_INVENTORY = registerInventoryHandler(
            EMPTY_ID,
            new InventoryHandler(
                    (player, integer) -> ItemStack.EMPTY,
                    player -> 0
            ),
            Map.of()
    );

    public static final InventoryHandlerResolver ON_KEYBIND = registerContext(
            ON_KEYBIND_ID,
            new InventoryHandlerResolver()
    );
    public static final InventoryHandlerResolver ON_CLICK = registerContext(
            ON_CLICK_ID,
            new InventoryHandlerResolver()
    );

    public static final InventoryHandler CHEST_INVENTORY = registerInventoryHandler(
            CHEST_INVENTORY_ID,
            new InventoryHandler(
                    (player, integer) -> player.getInventory().armor.get(EquipmentSlot.CHEST.getIndex()),
                    player -> 1
            ),
            Map.of(
                    ON_KEYBIND_ID, 100
            )
    );
    public static final InventoryHandler MAIN_HAND_INVENTORY = registerInventoryHandler(
            MAIN_HAND_INVENTORY_ID,
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
            OFFHAND_INVENTORY_ID,
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

    public static InventoryHandler registerInventoryHandler(String id, InventoryHandler handler, Map<String, Integer> priorities) {
        HANDLERS.put(id, handler);
        for (Map.Entry<String, Integer> entry : priorities.entrySet()) {
            InventoryHandlerResolver resolver = RESOLVERS.get(entry.getKey());
            if (resolver == null) continue;
            resolver.register(handler, entry.getValue());
        }
        return handler;
    }

    public static InventoryHandler getHandler(String id) {
        InventoryHandler handler = HANDLERS.get(id);
        return handler == null ? EMPTY_INVENTORY : handler;
    }

    public static String getHandlerId(InventoryHandler handler) {
        for (Map.Entry<String, InventoryHandler> entry : HANDLERS.entrySet()) {
            if (handler.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return EMPTY_ID;
    }
}
