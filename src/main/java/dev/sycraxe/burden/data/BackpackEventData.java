package dev.sycraxe.burden.data;

import dev.sycraxe.burden.Burden;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record BackpackEventData(ItemStack stack, boolean equipped) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BackpackEventData> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Burden.MOD_ID, "backpack"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BackpackEventData> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            BackpackEventData::stack,
            ByteBufCodecs.BOOL,
            BackpackEventData::equipped,
            BackpackEventData::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}