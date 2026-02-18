package dev.sycraxe.burden.gui.backpack;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiFunction;

public class BackpackMenuProvider implements MenuProvider {
    private final ItemStack stack;
    private final BiFunction<Integer, Inventory, ? extends AbstractBackpackMenu> menu;

    public BackpackMenuProvider(ItemStack stack, BiFunction<Integer, Inventory, ? extends AbstractBackpackMenu> menu) {
        this.stack = stack;
        this.menu = menu;
    }

    @Override
    public Component getDisplayName() {
        return stack.getHoverName();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return this.menu.apply(id, inventory);
    }
}
