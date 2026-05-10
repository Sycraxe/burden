package dev.sycraxe.burden.register;

import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.backpack.BackpackBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlock {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Burden.MOD_ID);

    public static final DeferredBlock<Block> BACKPACK = BLOCKS.register("backpack", () -> new BackpackBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
