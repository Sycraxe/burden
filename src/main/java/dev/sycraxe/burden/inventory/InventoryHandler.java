package dev.sycraxe.burden.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class InventoryHandler {
    private final BiFunction<Player, Integer, ItemStack> access;
    private final Function<Player, Integer> size;

    public InventoryHandler(BiFunction<Player, Integer, ItemStack> access, Function<Player, Integer> size) {
        this.access = access;
        this.size = size;
    }

    public ItemStack get(Player player, int index) {
        return index < this.getSize(player) ? this.access.apply(player, index) : ItemStack.EMPTY;
    };

    public int getSize(Player player) {
        return this.size.apply(player);
    };

    public OptionalInt find(Player player, Predicate<ItemStack> condition) {
        for (int index = 0; index < this.getSize(player); index++) {
            ItemStack stack = this.get(player, index);
            if (condition.test(stack)) return OptionalInt.of(index);
        }
        return OptionalInt.empty();
    }
}
