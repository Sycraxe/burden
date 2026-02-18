package dev.sycraxe.burden.gui.backpack.menus;

import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.gui.backpack.AbstractBackpackMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class HandheldBackpackMenu extends AbstractBackpackMenu {
    private final int slot;

    public HandheldBackpackMenu(int id, Inventory inventory) {
        super(id, inventory, inventory.getItem(getInitSlot(inventory)), Burden.HANDHELD_BACKPACK_MENU.get(), Optional.of(getInitSlot(inventory)));
        this.slot = getInitSlot(inventory);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.getItemStack() == player.getInventory().getItem(this.slot);
    }

    private static int getInitSlot(Inventory inventory) {
        return (inventory.getItem(Inventory.SLOT_OFFHAND).is(Burden.BACKPACK)
            && !inventory.getSelected().is(Burden.BACKPACK))
            ? Inventory.SLOT_OFFHAND
            : inventory.selected;
    }
}
