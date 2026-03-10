package dev.sycraxe.burden.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Predicate;

public class InventoryHandlerResolver {
    private final Set<PrioritizedInventoryHandler> ENTRIES = new ConcurrentSkipListSet<>();

    public void register(InventoryHandler handler, int priority) {
        ENTRIES.add(new PrioritizedInventoryHandler(handler, priority));
    }

    public Optional<InventoryHandlerSlot> find(Player player, Predicate<ItemStack> condition) {
        for (PrioritizedInventoryHandler entry : ENTRIES) {
            InventoryHandler handler = entry.handler();
            OptionalInt maybeInt = handler.find(player, condition);
            if (maybeInt.isPresent()) return Optional.of(new InventoryHandlerSlot(handler, maybeInt.getAsInt()));
        }
        return Optional.empty();
    }

    public record PrioritizedInventoryHandler(InventoryHandler handler, int priority) implements Comparable<PrioritizedInventoryHandler> {
        @Override
        public int compareTo(PrioritizedInventoryHandler other) {
            return Integer.compare(this.priority, other.priority());
        }
    }
}
