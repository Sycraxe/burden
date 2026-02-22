package dev.sycraxe.burden;

import dev.sycraxe.burden.gui.menu.BackpackMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AllMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, Burden.MOD_ID);

    public static final Supplier<MenuType<BackpackMenu>> BACKPACK_MENU = MENU_TYPES.register("backpack_menu", () -> new MenuType<>(BackpackMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }
}
