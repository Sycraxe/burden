package dev.sycraxe.burden.register;

import com.mojang.serialization.MapCodec;
import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.loot.StrawFromWheatLootModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModLootModifier {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Burden.MOD_ID);

    public static final Supplier<MapCodec<StrawFromWheatLootModifier>> STRAW_FROM_WHEAT_LOOT_MODIFIER =
            GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("straw_from_wheat", () -> StrawFromWheatLootModifier.CODEC);

    public static void register(IEventBus bus) {
        GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(bus);
    }
}
