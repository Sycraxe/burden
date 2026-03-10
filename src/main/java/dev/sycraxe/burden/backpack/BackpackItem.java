package dev.sycraxe.burden.backpack;

import dev.sycraxe.burden.inventory.InventoryHandlerSlot;
import dev.sycraxe.burden.register.ModBlock;
import dev.sycraxe.burden.register.ModInventoryHandler;
import dev.sycraxe.burden.register.ModItem;
import dev.sycraxe.burden.compatibility.curios.Curios;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import java.util.Optional;

public class BackpackItem extends BlockItem implements Equipable {

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
            Optional<InventoryHandlerSlot> handlerSlot = ModInventoryHandler.ON_CLICK.find(player, (s) -> s.is(ModItem.BACKPACK));
            if (handlerSlot.isPresent()) {
                BackpackContext context = new BackpackContext.Item(handlerSlot.get());
                player.openMenu(new SimpleMenuProvider((id, inventory, p) -> new BackpackMenu(id, inventory, context), stack.getHoverName()), context::toBuffer);
            }
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }
}