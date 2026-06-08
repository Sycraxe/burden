package dev.sycraxe.burden.register;

import dev.sycraxe.burden.Burden;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Burden.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BURDEN_TAB = TABS.register("burden_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("tab.burden"))
            .icon(() -> ModItem.BACKPACK.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItem.BACKPACK);
                output.accept(ModItem.FABRIC);
                output.accept(ModItem.STRAW);
            }).build());

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
