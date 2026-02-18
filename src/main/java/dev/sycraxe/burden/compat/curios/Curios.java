package dev.sycraxe.burden.compat.curios;

import dev.sycraxe.burden.AllItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class Curios {
    public static ItemStack getEquippedBackpack(Player player) {
        if (CuriosApi.getCuriosInventory(player).isEmpty()) {
            return ItemStack.EMPTY;
        }
        ICuriosItemHandler inventory = CuriosApi.getCuriosInventory(player).get();
        if (inventory.getStacksHandler("back").isEmpty()) {
            return ItemStack.EMPTY;
        }
        ICurioStacksHandler slot = inventory.getStacksHandler("back").get();
        IDynamicStackHandler stacks = slot.getStacks();
        for (int i = 0; i < stacks.getSlots(); i++) {
            ItemStack stack = stacks.getStackInSlot(i);
            if (stack.is(AllItem.BACKPACK)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
