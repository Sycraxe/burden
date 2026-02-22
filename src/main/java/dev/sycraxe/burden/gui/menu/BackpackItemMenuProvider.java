package dev.sycraxe.burden.gui.menu;

import dev.sycraxe.burden.AllItem;
import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.compat.curios.Curios;
import dev.sycraxe.burden.container.ItemBackpackContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class BackpackItemMenuProvider implements MenuProvider {
    public static final int CHEST_INVENTORY_SLOT = 38;

    public static final Function<Inventory, ItemStack> KEYBIND_INIT_SLOT_FUNCTION =
            (inventory) -> Burden.isCuriosCompatLoaded()
                    ? Curios.getEquippedBackpack(inventory.player)
                    : inventory.getItem(CHEST_INVENTORY_SLOT);

    public static final Function<Inventory, ItemStack> CLICK_INIT_SLOT_FUNCTION =
            (inventory) -> inventory.getItem(
                    (inventory.getItem(Inventory.SLOT_OFFHAND).is(AllItem.BACKPACK) && !inventory.getSelected().is(AllItem.BACKPACK))
                        ? Inventory.SLOT_OFFHAND : inventory.selected);

    private final Component title;
    private final Function<Inventory, ItemStack> initSlotItemStackFunction;

    public BackpackItemMenuProvider(Component title, Function<Inventory, ItemStack> initSlotItemStackFunction) {
        this.title = title;
        this.initSlotItemStackFunction = initSlotItemStackFunction;
    }

    @Override
    public Component getDisplayName() {
        return this.title;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BackpackMenu(i, inventory, new ItemBackpackContainer(initSlotItemStackFunction.apply(inventory), () -> initSlotItemStackFunction.apply(inventory)));
    }
}
