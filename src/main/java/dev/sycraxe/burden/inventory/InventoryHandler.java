package dev.sycraxe.burden.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public interface InventoryHandler {
    ItemStack find(Player player, Predicate<ItemStack> condition);
}
