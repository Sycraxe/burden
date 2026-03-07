package dev.sycraxe.burden.backpack;

import dev.sycraxe.burden.register.ModBlock;
import dev.sycraxe.burden.register.ModItem;
import dev.sycraxe.burden.compatibility.curios.Curios;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class BackpackItem extends BlockItem implements Equipable {
    public static final Map<BackpackItemOpeningMode, Function<Inventory, ItemStack>> OPENING_MODES_TRIGGERER_SLOT_STACK_GETTER;
    public static final Map<BackpackItemOpeningMode, Function<Inventory, Optional<Integer>>> OPENING_MODES_UNPICKABLE_SLOT;
    public enum BackpackItemOpeningMode {
        CURIOS,
        EQUIPPED,
        MAIN_HAND,
        SECONDARY_HAND,
        NONE
    }

    public BackpackItem() {
        super(ModBlock.BACKPACK.get(), new Properties().stacksTo(1));
    }

    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer().isCrouching()) {
            this.place(new BlockPlaceContext(context));
        }
        return InteractionResult.PASS;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && !player.isCrouching()) {
            BackpackItem.BackpackItemOpeningMode mode = BackpackItem.onClickOpeningMode(player.getInventory());
            if (mode != BackpackItem.BackpackItemOpeningMode.NONE) {
                Function<Inventory, ItemStack> origin = BackpackItem.OPENING_MODES_TRIGGERER_SLOT_STACK_GETTER.get(mode);
                player.openMenu(new BackpackItemMenuProvider(origin.apply(player.getInventory()).getHoverName(), origin, BackpackItem.OPENING_MODES_UNPICKABLE_SLOT.get(mode).apply(player.getInventory())));
            }
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    public static BackpackItemOpeningMode onKeybindOpeningMode(Inventory inventory) {
        if (OPENING_MODES_TRIGGERER_SLOT_STACK_GETTER.get(BackpackItemOpeningMode.CURIOS).apply(inventory).is(ModItem.BACKPACK)) {
            return BackpackItemOpeningMode.CURIOS;
        }
        if (OPENING_MODES_TRIGGERER_SLOT_STACK_GETTER.get(BackpackItemOpeningMode.EQUIPPED).apply(inventory).is(ModItem.BACKPACK)) {
            return BackpackItemOpeningMode.EQUIPPED;
        }
        return onClickOpeningMode(inventory);
    }

    public static BackpackItemOpeningMode onClickOpeningMode(Inventory inventory) {
        if (OPENING_MODES_TRIGGERER_SLOT_STACK_GETTER.get(BackpackItemOpeningMode.MAIN_HAND).apply(inventory).is(ModItem.BACKPACK)) {
            return BackpackItemOpeningMode.MAIN_HAND;
        }
        if (OPENING_MODES_TRIGGERER_SLOT_STACK_GETTER.get(BackpackItemOpeningMode.SECONDARY_HAND).apply(inventory).is(ModItem.BACKPACK)) {
            return BackpackItemOpeningMode.SECONDARY_HAND;
        }
        return BackpackItemOpeningMode.NONE;
    }

    static {
        OPENING_MODES_TRIGGERER_SLOT_STACK_GETTER = Map.of(
                BackpackItemOpeningMode.CURIOS, (inventory -> ModList.get().isLoaded("curios") ? Curios.getEquippedBackpack(inventory.player) : ItemStack.EMPTY),
                BackpackItemOpeningMode.EQUIPPED, (inventory -> inventory.getItem(38)),
                BackpackItemOpeningMode.MAIN_HAND, (inventory -> inventory.getItem(inventory.selected)),
                BackpackItemOpeningMode.SECONDARY_HAND, (inventory -> inventory.getItem(Inventory.SLOT_OFFHAND))
        );
        OPENING_MODES_UNPICKABLE_SLOT = Map.of(
                BackpackItemOpeningMode.CURIOS, (inventory -> Optional.empty()),
                BackpackItemOpeningMode.EQUIPPED, (inventory -> Optional.of(38)),
                BackpackItemOpeningMode.MAIN_HAND, (inventory -> Optional.of(inventory.selected)),
                BackpackItemOpeningMode.SECONDARY_HAND, (inventory -> Optional.of(Inventory.SLOT_OFFHAND))
        );
    }
}