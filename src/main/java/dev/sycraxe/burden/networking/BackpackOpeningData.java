package dev.sycraxe.burden.networking;

import dev.sycraxe.burden.Burden;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record BackpackOpeningData() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BackpackOpeningData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Burden.MOD_ID, "backpack_opening"));

    public static final StreamCodec<ByteBuf, BackpackOpeningData> STREAM_CODEC = StreamCodec.unit(new BackpackOpeningData());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
