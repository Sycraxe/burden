package dev.sycraxe.burden;

import dev.sycraxe.burden.core.item.Backpack;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllItem {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Burden.MOD_ID);

    public static final DeferredItem<Item> BACKPACK = ITEMS.register("backpack", Backpack::new);
    public static final DeferredItem<Item> FABRIC = ITEMS.register("fabric", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STRAW = ITEMS.register("straw", () -> new Item(new Item.Properties()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
