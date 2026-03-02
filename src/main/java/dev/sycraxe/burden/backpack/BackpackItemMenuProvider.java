package dev.sycraxe.burden.backpack;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Function;

public class BackpackItemMenuProvider implements MenuProvider {
    private final Component title;
    private final Function<Inventory, ItemStack> triggererSlotGetter;
    private final Optional<Integer> unpickableSlot;

    public BackpackItemMenuProvider(Component title, Function<Inventory, ItemStack> triggererSlotGetter) {
        this(title, triggererSlotGetter, Optional.empty());
    }

    public BackpackItemMenuProvider(Component title, Function<Inventory, ItemStack> triggererSlotGetter, Optional<Integer> unpickableSlot) {
        this.title = title;
        this.triggererSlotGetter = triggererSlotGetter;
        this.unpickableSlot = unpickableSlot;
    }

    @Override
    public Component getDisplayName() {
        return this.title;
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BackpackMenu(i, inventory, new BackpackItemContainer(triggererSlotGetter.apply(inventory), () -> triggererSlotGetter.apply(inventory)), this.unpickableSlot);
    }
}
