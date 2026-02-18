package dev.sycraxe.burden;

import dev.sycraxe.burden.event.CuriosEvents;
import dev.sycraxe.burden.gui.backpack.menus.EquippedBackpackMenu;
import dev.sycraxe.burden.gui.backpack.menus.HandheldBackpackMenu;
import dev.sycraxe.burden.item.backpack.Backpack;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.neoforged.fml.ModList;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod(Burden.MOD_ID)
public class Burden {
    public static final String MOD_ID = "burden";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, MOD_ID);

    public static final DeferredItem<Item> BACKPACK = ITEMS.register("backpack", Backpack::new);
    public static final DeferredItem<Item> FABRIC = ITEMS.register("fabric", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STRAW = ITEMS.register("straw", () -> new Item(new Item.Properties()));

    public static final Supplier<MenuType<EquippedBackpackMenu>> EQUIPPED_BACKPACK_MENU = MENU_TYPES.register("equipped_backpack_menu", () -> new MenuType<>(EquippedBackpackMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<HandheldBackpackMenu>> HANDHELD_BACKPACK_MENU = MENU_TYPES.register("handheld_backpack_menu", () -> new MenuType<>(HandheldBackpackMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = TABS.register("burden_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("tab.burden"))
            .icon(() -> BACKPACK.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(BACKPACK.get());
                output.accept(FABRIC.get());
                output.accept(STRAW.get());
            }).build());

    public static boolean isCuriosCompatLoaded() {
        return ModList.get().isLoaded("curios");
    }

    public Burden(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        TABS.register(modEventBus);
        MENU_TYPES.register(modEventBus);

        if (isCuriosCompatLoaded())  {
            new CuriosEvents(modEventBus);
        }
    }
}

