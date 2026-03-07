package dev.sycraxe.burden.register;

import dev.sycraxe.burden.inventory.InventoryHandler;
import dev.sycraxe.burden.inventory.InventoryPriorityContext;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModInventoryPriority {
    private static final Map<String, InventoryPriorityContext> CONTEXTS = new ConcurrentHashMap<>();

    public static final String ON_CLICK_ID = "on_click";
    public static final String ON_KEYBIND_ID = "on_keybind";

    public static final String CHEST_INVENTORY_ID = "chest_inventory";
    public static final String MAIN_HAND_INVENTORY_ID = "main_hand_inventory";
    public static final String OFFHAND_INVENTORY_ID = "offhand_inventory";

    public static final InventoryPriorityContext ON_KEYBIND = registerContext(
            ON_KEYBIND_ID,
            new InventoryPriorityContext()
    );
    public static final InventoryPriorityContext ON_CLICK = registerContext(
            ON_KEYBIND_ID,
            new InventoryPriorityContext(),
            Map.of(ON_KEYBIND_ID, 20)
    );

    public static final InventoryHandler CHEST_INVENTORY = registerInventoryHandler(
            CHEST_INVENTORY_ID,
            (player, condition) -> {
                ItemStack stack = player.getInventory().armor.get(EquipmentSlot.CHEST.getIndex());
                return condition.test(stack) ? stack : ItemStack.EMPTY;
            },
            Map.of(ON_KEYBIND_ID, 10)
    );

    public static final InventoryHandler MAIN_HAND_INVENTORY = registerInventoryHandler(
            MAIN_HAND_INVENTORY_ID,
            (player, condition) -> {
                ItemStack stack = player.getInventory().getSelected();
                return condition.test(stack) ? stack : ItemStack.EMPTY;
            },
            Map.of(ON_CLICK_ID, 10)
    );
    public static final InventoryHandler OFFHAND_INVENTORY = registerInventoryHandler(
            OFFHAND_INVENTORY_ID,
            (player, condition) -> {
                for (ItemStack stack : player.getInventory().offhand) {
                    if (condition.test(stack)) return stack;
                }
                return ItemStack.EMPTY;
            },
            Map.of(ON_CLICK_ID, 20)
    );

    private static InventoryPriorityContext registerContext(String id, InventoryPriorityContext context) {
        return registerContext(id, context, Map.of());
    }

    private static InventoryPriorityContext registerContext(String id, InventoryPriorityContext context, Map<String, Integer> priorities) {
        registerInventoryHandler(id, context, priorities);
        CONTEXTS.put(id, context);
        return context;
    }

    private static InventoryHandler registerInventoryHandler(String id, InventoryHandler handler, Map<String, Integer> priorities) {
        for (Map.Entry<String, Integer> entry : priorities.entrySet()) {
            InventoryPriorityContext context = CONTEXTS.get(entry.getKey());
            if (context == null) {
                continue;
            }
            context.register(id, handler, entry.getValue());
        }
        return handler;
    }
}
