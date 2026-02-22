package dev.sycraxe.burden.item;

import dev.sycraxe.burden.AllBlocks;
import dev.sycraxe.burden.gui.menu.BackpackItemMenuProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import static dev.sycraxe.burden.gui.menu.BackpackItemMenuProvider.CLICK_INIT_SLOT_FUNCTION;

public class BackpackItem extends BlockItem implements Equipable {
    public BackpackItem() {
        super(AllBlocks.BACKPACK.get(), new Properties().stacksTo(1));
    }

    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer().isCrouching()) {
            InteractionResult result = this.place(new BlockPlaceContext(context));
        }
        return InteractionResult.PASS;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && !player.isCrouching()) {
            player.openMenu(new BackpackItemMenuProvider(CLICK_INIT_SLOT_FUNCTION.apply(player.getInventory()).getHoverName(), CLICK_INIT_SLOT_FUNCTION));
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }
}