package dev.sycraxe.burden.core.codec;

import dev.sycraxe.burden.Burden;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record BackpackEventCodec(ItemStack stack, boolean equipped) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BackpackEventCodec> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Burden.MOD_ID, "backpack"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BackpackEventCodec> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            BackpackEventCodec::stack,
            ByteBufCodecs.BOOL,
            BackpackEventCodec::equipped,
            BackpackEventCodec::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}