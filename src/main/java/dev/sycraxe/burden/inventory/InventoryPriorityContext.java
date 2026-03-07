package dev.sycraxe.burden.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Predicate;

public class InventoryPriorityContext implements InventoryHandler {
    private final Set<PrioritizedHandler> HANDLERS = new ConcurrentSkipListSet<>();

    public void register(String name, InventoryHandler handler, int priority) {
        HANDLERS.add(new PrioritizedHandler(handler, priority, name));
    }

    public ItemStack find(Player player, Predicate<ItemStack> condition) {
        for (PrioritizedHandler prioritizedHandler : HANDLERS) {
            ItemStack result = prioritizedHandler.handler().find(player, condition);
            if (!result.isEmpty()) {
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    public record PrioritizedHandler(InventoryHandler handler, int priority, String id) implements Comparable<PrioritizedHandler> {
        @Override
        public int compareTo(PrioritizedHandler other) {
            int res = Integer.compare(this.priority, other.priority);
            return res != 0 ? res : this.id.compareTo(other.id);
        }
    }
}
