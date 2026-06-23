package dev.sycraxe.burden.register;

import dev.sycraxe.burden.Burden;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Burden.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BURDEN_TAB = TABS.register("burden_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("tab.burden"))
            .icon(() -> ModItem.BACKPACK.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(backpackColor(DyeColor.WHITE));
                output.accept(backpackColor(DyeColor.ORANGE));
                output.accept(backpackColor(DyeColor.MAGENTA));
                output.accept(backpackColor(DyeColor.LIGHT_BLUE));
                output.accept(backpackColor(DyeColor.YELLOW));
                output.accept(backpackColor(DyeColor.LIME));
                output.accept(backpackColor(DyeColor.PINK));
                output.accept(backpackColor(DyeColor.GRAY));
                output.accept(backpackColor(DyeColor.LIGHT_GRAY));
                output.accept(backpackColor(DyeColor.CYAN));
                output.accept(backpackColor(DyeColor.PURPLE));
                output.accept(backpackColor(DyeColor.BLUE));
                output.accept(backpackColor(DyeColor.BROWN));
                output.accept(backpackColor(DyeColor.GREEN));
                output.accept(backpackColor(DyeColor.RED));
                output.accept(backpackColor(DyeColor.BLACK));
            }).build()
    );

    private static ItemStack backpackColor(DyeColor color) {
        ItemStack stack = new ItemStack(ModItem.BACKPACK.get());
        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color.getTextureDiffuseColor(), false));
        return stack;
    }

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
