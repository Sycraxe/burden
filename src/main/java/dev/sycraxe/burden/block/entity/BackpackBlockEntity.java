package dev.sycraxe.burden.block.entity;

import dev.sycraxe.burden.AllBlockEntities;
import dev.sycraxe.burden.AllItem;
import dev.sycraxe.burden.gui.menu.BackpackMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class BackpackBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    public static final int CONTAINER_SIZE = 27;
    private static final int[] SLOTS = IntStream.range(0, CONTAINER_SIZE).toArray();
    private NonNullList<ItemStack> items;

    public BackpackBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntities.BACKPACK_BLOCK_ENTITY.get(), pos, blockState);
        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.burden.backpack");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemStackList) {
        this.items = itemStackList;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new BackpackMenu(i, inventory, this);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return !itemStack.is(AllItem.BACKPACK);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }
}
