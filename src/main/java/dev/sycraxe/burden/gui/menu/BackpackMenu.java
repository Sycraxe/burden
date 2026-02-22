package dev.sycraxe.burden.gui.menu;

import com.mojang.datafixers.util.Pair;
import dev.sycraxe.burden.AllItem;
import dev.sycraxe.burden.AllMenuTypes;
import dev.sycraxe.burden.container.ItemBackpackContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;
import java.util.Optional;

public class BackpackMenu extends AbstractContainerMenu {
    private static final EquipmentSlot[] EQUIPMENT_SLOTS;
    private static final Map<EquipmentSlot, ResourceLocation> TEXTURE_EMPTY_SLOTS;
    private final Container container;

    public BackpackMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(ItemBackpackContainer.DEFAULT_SIZE));
    }

    public BackpackMenu(int id, Inventory inventory, Container container) {
        this(id, inventory, container, Optional.empty());
    }

    public BackpackMenu(int id, Inventory inventory, Container container, Optional<Integer> unpickableSlot) {
        super(AllMenuTypes.BACKPACK_MENU.get(), id);
        this.container = container;

        for (int i1 = 0; i1 < 3; i1++) {
            for (int j1 = 0; j1 < 9; j1++) {
                this.addSlot(new Slot(this.container, j1 + i1 * 9, j1 * 18 + 44, i1 * 18 + 23) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return !stack.is(AllItem.BACKPACK);
                    }
                });
            }
        }

        addInventorySlots(inventory);
        addHotbarSlots(inventory, unpickableSlot);
        addArmorAndOffhandSlots(inventory, unpickableSlot);
    }

    private void addInventorySlots(Inventory inventory) {
        for (int i2 = 0; i2 < 3; i2++) {
            for (int j2 = 0; j2 < 9; j2++) {
                this.addSlot(new Slot(inventory, 9 + i2 * 9 + j2, j2 * 18 + 44, i2 * 18 + 103));
            }
        }
    }

    private void addHotbarSlots(Inventory inventory, Optional<Integer> unpickableSlot) {
        for (int slotCount = 0; slotCount < 9; slotCount++) {
            this.addSlot(unpickableSlot.isPresent() && unpickableSlot.get() == slotCount
                ? new Slot(inventory, slotCount, slotCount * 18 + 44, 161) {
                    @Override
                    public boolean mayPickup(Player player) {
                        return false;
                    }
                }
                : new Slot(inventory, slotCount, slotCount * 18 + 44, 161)
            );
        }
    }

    private void addArmorAndOffhandSlots(Inventory inventory, Optional<Integer> unpickableSlot) {
        this.addSlot(unpickableSlot.isPresent() && unpickableSlot.get() == Inventory.SLOT_OFFHAND
            ? new ArmorSlot(inventory, inventory.player, EQUIPMENT_SLOTS[4], Inventory.SLOT_OFFHAND, 224, 161, TEXTURE_EMPTY_SLOTS.get(EQUIPMENT_SLOTS[4])) {
                @Override
                public boolean mayPickup(Player player) {
                    return false;
                }
            }
            : new ArmorSlot(inventory, inventory.player, EQUIPMENT_SLOTS[4], Inventory.SLOT_OFFHAND, 224, 161, TEXTURE_EMPTY_SLOTS.get(EQUIPMENT_SLOTS[4]))
        );

        for (int slotCount = 0; slotCount < 4; slotCount++) {
            this.addSlot(unpickableSlot.isPresent() && unpickableSlot.get() == 39 - slotCount
                ? new ArmorSlot(inventory, inventory.player, EQUIPMENT_SLOTS[slotCount], 39 - slotCount, 224, slotCount * 18 + 85, TEXTURE_EMPTY_SLOTS.get(EQUIPMENT_SLOTS[slotCount])) {
                    @Override
                    public boolean mayPickup(Player player) {
                        return false;
                    }
                }
                : new ArmorSlot(inventory, inventory.player, EQUIPMENT_SLOTS[slotCount], 39 - slotCount, 224, slotCount * 18 + 85, TEXTURE_EMPTY_SLOTS.get(EQUIPMENT_SLOTS[slotCount]))
            );
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stackToMove = slot.getItem();
            quickMovedStack = stackToMove.copy();
            if (index < 27) {
                if (!this.moveItemStackTo(stackToMove, 64, 68, true)
                        && !this.moveItemStackTo(stackToMove, 27, 63, true)
                        && !this.moveItemStackTo(stackToMove, 63, 64, true)) {
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

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (clickType == ClickType.SWAP) {
            this.doSwapClick(slotId, button, player);
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

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    static class ArmorSlot extends Slot {
        private final LivingEntity owner;
        private final EquipmentSlot slot;
        private final ResourceLocation emptyIcon;

        public ArmorSlot(Container container, LivingEntity owner, EquipmentSlot slot, int slotIndex, int x, int y, ResourceLocation emptyIcon) {
            super(container, slotIndex, x, y);
            this.owner = owner;
            this.slot = slot;
            this.emptyIcon = emptyIcon;
        }

        public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
            this.owner.onEquipItem(this.slot, oldStack, newStack);
            super.setByPlayer(newStack, oldStack);
        }

        public int getMaxStackSize() {
            return 1;
        }

        public boolean mayPlace(ItemStack stack) {
            return stack.canEquip(this.slot, this.owner);
        }

        public boolean mayPickup(Player player) {
            ItemStack itemstack = this.getItem();
            return !itemstack.isEmpty() && !player.isCreative() && EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) ? false : super.mayPickup(player);
        }

        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            return this.emptyIcon != null ? Pair.of(InventoryMenu.BLOCK_ATLAS, this.emptyIcon) : super.getNoItemIcon();
        }
    }

    static {
        EQUIPMENT_SLOTS = new EquipmentSlot[]{
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.OFFHAND
        };
        TEXTURE_EMPTY_SLOTS = Map.of(
                EquipmentSlot.FEET, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS,
                EquipmentSlot.LEGS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
                EquipmentSlot.CHEST, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE,
                EquipmentSlot.HEAD, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET,
                EquipmentSlot.OFFHAND, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD
        );
    }
}
