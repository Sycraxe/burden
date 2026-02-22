package dev.sycraxe.burden.container;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.function.Supplier;

public class ItemBackpackContainer extends SimpleContainer {
    public static final int DEFAULT_SIZE = 27;
    private final ItemStack stack;
    private final Supplier<ItemStack> originSlotGetter;

    public ItemBackpackContainer(ItemStack stack, Supplier<ItemStack> originSlotGetter) {
        super(DEFAULT_SIZE);
        this.stack = stack;
        this.originSlotGetter = originSlotGetter;
        ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        contents.copyInto(this.getItems());
    }

    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.stack == this.originSlotGetter.get();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getItems()));
    }
}
