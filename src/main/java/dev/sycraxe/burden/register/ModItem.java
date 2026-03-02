package dev.sycraxe.burden.register;

import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.backpack.BackpackItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItem {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Burden.MOD_ID);

    public static final DeferredItem<Item> BACKPACK = ITEMS.register("backpack", BackpackItem::new);
    public static final DeferredItem<Item> FABRIC = ITEMS.register("fabric", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STRAW = ITEMS.register("straw", () -> new Item(new Item.Properties()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
