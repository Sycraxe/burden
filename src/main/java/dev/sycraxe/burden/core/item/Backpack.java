package dev.sycraxe.burden.core.item;

import dev.sycraxe.burden.core.codec.BackpackEventCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class Backpack extends Item implements Equipable {
    public Backpack() {
        super(new Properties().stacksTo(1));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            return InteractionResultHolder.sidedSuccess(stack, false);
        }
        PacketDistributor.sendToServer(new BackpackEventCodec(stack, false));
        return InteractionResultHolder.sidedSuccess(stack, true);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }
}