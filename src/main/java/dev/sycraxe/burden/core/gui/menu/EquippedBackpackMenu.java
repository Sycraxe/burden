package dev.sycraxe.burden.core.gui.menu;

import dev.sycraxe.burden.AllMenuTypes;
import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.compat.curios.Curios;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class EquippedBackpackMenu extends AbstractBackpackMenu {
    public static final int CHEST_INVENTORY_SLOT = 38;

    public EquippedBackpackMenu(int id, Inventory inventory) {
        super(id, inventory, Burden.isCuriosCompatLoaded() ? Curios.getEquippedBackpack(inventory.player) : inventory.getItem(CHEST_INVENTORY_SLOT), AllMenuTypes.EQUIPPED_BACKPACK_MENU.get(), Burden.isCuriosCompatLoaded() ? Optional.empty() : Optional.of(CHEST_INVENTORY_SLOT));
    }

    @Override
    public boolean stillValid(Player player) {
        return this.getItemStack() == (Burden.isCuriosCompatLoaded() ? Curios.getEquippedBackpack(player) : player.getInventory().getItem(CHEST_INVENTORY_SLOT));
    }
}