package dev.sycraxe.burden.core.gui.menu;

import com.mojang.datafixers.util.Pair;
import dev.sycraxe.burden.AllMenuTypes;
import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.core.container.BackpackContainer;
import dev.sycraxe.burden.AllItem;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractBackpackMenu extends AbstractContainerMenu {
    private static final EquipmentSlot[] EQUIPMENT_SLOTS = new EquipmentSlot[]{
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.OFFHAND
    };
    private static final Map<EquipmentSlot, ResourceLocation> TEXTURE_EMPTY_SLOTS = Map.of(
            EquipmentSlot.FEET, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS,
            EquipmentSlot.LEGS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
            EquipmentSlot.CHEST, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE,
            EquipmentSlot.HEAD, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET,
            EquipmentSlot.OFFHAND, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD
    );
    private final BackpackContainer container;

    protected AbstractBackpackMenu(int id, Inventory inventory, ItemStack stack, MenuType<? extends AbstractBackpackMenu> type) {
        this(id, inventory, stack, type, Optional.empty());
    }

    protected AbstractBackpackMenu(int id, Inventory inventory, ItemStack stack, MenuType<? extends AbstractBackpackMenu> type, Optional<Integer> disabled) {
        super(type, id);
        this.container = new BackpackContainer(stack);

        // Backpack slots
        for (int i1 = 0; i1 < 3; i1++) {
            for (int j1 = 0; j1 < 9; j1++) {
                this.addSlot(new BackpackSlot(this.container, j1 + i1 * 9, j1 * 18 + 44, i1 * 18 + 23));
            }
        }

        // Inventory slots
        for (int i2 = 0; i2 < 3; i2++) {
            for (int j2 = 0; j2 < 9; j2++) {
                this.addSlot(new Slot(inventory, 9 + i2 * 9 + j2, j2 * 18 + 44, i2 * 18 + 103));
            }
        }

        // Hotbar slots
        for (int i3 = 0; i3 < 9; i3++) {
            this.addSlot(new DeactivableSlot(inventory, i3, i3 * 18 + 44, 161, disabled.isPresent() && disabled.get() == i3));
        }

        // Shield slot
        this.addSlot(
            new DeactivableArmorSlot(
                inventory,
                inventory.player,
                EQUIPMENT_SLOTS[4],
                Inventory.SLOT_OFFHAND,
                224,
                161,
                TEXTURE_EMPTY_SLOTS.get(EQUIPMENT_SLOTS[4]),
                disabled.isPresent() && disabled.get() == Inventory.SLOT_OFFHAND
            )
        );


        // Armor slots
        for (int idx = 0; idx < 4; idx++) {
            this.addSlot(
                new DeactivableArmorSlot(
                    inventory,
                    inventory.player,
                    EQUIPMENT_SLOTS[idx],
                    39 - idx,
                    224,
                    idx * 18 + 85,
                    TEXTURE_EMPTY_SLOTS.get(EQUIPMENT_SLOTS[idx]),
                    disabled.isPresent() && disabled.get() == 39 - idx
                )
            );
        }

    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stackToMove = slot.getItem();
            quickMovedStack = stackToMove.copy();
            if (index < 27) {
                if (
                    !this.moveItemStackTo(stackToMove, 64, 68, true)
                    && !this.moveItemStackTo(stackToMove, 27, 63, true)
                    && !this.moveItemStackTo(stackToMove, 63, 64, true)
                ) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackToMove, 0, 27, false)) {
                return ItemStack.EMPTY;
            }

            if (stackToMove.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return quickMovedStack;
    }

    protected ItemStack getItemStack() {
        return this.container.getStack();
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (clickType == ClickType.SWAP) {
            try {
                this.doSwapClick(slotId, button, player);
            } catch (Exception exception) {
                CrashReport crashreport = CrashReport.forThrowable(exception, "Container click");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Click info");
                crashreportcategory.setDetail("Menu Type", () -> AllMenuTypes.EQUIPPED_BACKPACK_MENU.get().toString());
                crashreportcategory.setDetail("Menu Class", () -> this.getClass().getCanonicalName());
                crashreportcategory.setDetail("Slot Count", this.slots.size());
                crashreportcategory.setDetail("Slot", slotId);
                crashreportcategory.setDetail("Button", button);
                crashreportcategory.setDetail("Type", ClickType.SWAP);
                throw new ReportedException(crashreport);
            }
        } else {
            super.clicked(slotId, button, clickType, player);
        }
    }

    private void doSwapClick(int slotId, int button, Player player) {
        if (button >= 0 && button < 9 || button == 40) {
            // Source slot
            Slot slot1 = this.slots.get(slotId);
            ItemStack stack1 = slot1.getItem().copy();
            // Destination slot
            Slot slot2;
            if (button < 9) {
                slot2 = this.slots.get(54 + button);
            } else {
                slot2 = this.slots.get(63);
            }
            ItemStack stack2 = slot2.getItem().copy();
            if (!stack1.isEmpty() || !stack2.isEmpty()) {
                if (stack1.isEmpty()) {
                    if (slot1.mayPlace(stack2) && slot2.mayPickup(player)) {
                        int max = slot1.getMaxStackSize(stack2);
                        if (stack2.getCount() > max) {
                            slot1.setByPlayer(stack2.split(max));
                        } else {
                            slot1.setByPlayer(stack2);
                            slot2.setByPlayer(ItemStack.EMPTY);
                        }
                    }
                } else if (stack2.isEmpty()) {
                    if (slot1.mayPickup(player) && slot2.mayPlace(stack1)) {
                        int max = slot2.getMaxStackSize(stack1);
                        if (stack1.getCount() > max) {
                            slot2.setByPlayer(stack1.split(max));
                        } else {
                            slot2.setByPlayer(stack1);
                            slot1.setByPlayer(ItemStack.EMPTY);
                        }
                    }
                } else if (slot1.mayPickup(player) && slot1.mayPlace(stack2)
                        && slot2.mayPickup(player) && slot2.mayPlace(stack1)) {
                    int max1 = slot1.getMaxStackSize(stack2);
                    int max2 = slot2.getMaxStackSize(stack1);
                    if (stack2.getCount() > max1) {
                        slot1.setByPlayer(stack2.split(max1));
                        player.drop(stack2, true);
                    } else {
                        slot1.setByPlayer(stack2);
                    }
                    if (stack1.getCount() > max2) {
                        slot2.setByPlayer(stack1.split(max2));
                        player.drop(stack1, true);
                    } else {
                        slot2.setByPlayer(stack1);
                    }
                }
            }
        }
    }

    private static class BackpackSlot extends Slot {
        public BackpackSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return !stack.is(AllItem.BACKPACK);
        }
    }

    private static class DeactivableArmorSlot extends DeactivableSlot {
        private final LivingEntity owner;
        private final EquipmentSlot slot;
        @Nullable
        private final ResourceLocation emptyIcon;

        public DeactivableArmorSlot(Container container, LivingEntity owner, EquipmentSlot slot, int slotIndex, int x, int y, @Nullable ResourceLocation emptyIcon, boolean disabled) {
            super(container, slotIndex, x, y, disabled);
            this.owner = owner;
            this.slot = slot;
            this.emptyIcon = emptyIcon;
        }

        public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
            this.owner.onEquipItem(this.slot, oldStack, newStack);
            super.setByPlayer(newStack, oldStack);
        }

        public int getMaxStackSize() {
            return this.slot == EquipmentSlot.OFFHAND ? super.getMaxStackSize() : 1;
        }

        public boolean mayPlace(ItemStack stack) {
            return super.mayPlace(stack) && (stack.canEquip(this.slot, this.owner) || this.slot == EquipmentSlot.OFFHAND);
        }

        public boolean mayPickup(Player player) {
            ItemStack itemstack = this.getItem();
            return (itemstack.isEmpty() || player.isCreative() || !EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) && super.mayPickup(player);
        }

        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            return this.emptyIcon != null ? Pair.of(InventoryMenu.BLOCK_ATLAS, this.emptyIcon) : super.getNoItemIcon();
        }
    }

    private static class DeactivableSlot extends Slot {
        private final boolean disabled;

        public DeactivableSlot(Container container, int slot, int x, int y, boolean disabled) {
            super(container, slot, x, y);
            this.disabled = disabled;
        }

        @Override
        public boolean mayPickup(@NotNull Player player) {
            return !disabled;
        }
    }
}
