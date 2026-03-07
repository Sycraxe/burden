package dev.sycraxe.burden.register;

import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.backpack.BackpackBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Burden.MOD_ID);

    public static final Supplier<BlockEntityType<BackpackBlockEntity>> BACKPACK_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "backpack",
            () -> BlockEntityType.Builder.of(BackpackBlockEntity::new, ModBlock.BACKPACK.get())
                    .build(null)
    );

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
