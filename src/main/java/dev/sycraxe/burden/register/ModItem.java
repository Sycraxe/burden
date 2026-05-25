package dev.sycraxe.burden.register;

import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.backpack.BackpackItem;
import dev.sycraxe.burden.tool.Scythe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItem {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Burden.MOD_ID);

    public static final DeferredItem<Item> BACKPACK = ITEMS.register("backpack", BackpackItem::new);

    public static final DeferredItem<Item> FABRIC = ITEMS.register("fabric", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STRAW = ITEMS.register("straw", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> IRON_SCYTHE = ITEMS.register("iron_scythe", () -> new Scythe(Tiers.IRON, new Item.Properties().attributes(Scythe.createAttributes(Tiers.IRON, 2, -2))));
    public static final DeferredItem<Item> GOLDEN_SCYTHE = ITEMS.register("golden_scythe", () -> new Scythe(Tiers.GOLD, new Item.Properties().attributes(Scythe.createAttributes(Tiers.GOLD, 2, -2))));
    public static final DeferredItem<Item> NETHERITE_SCYTHE = ITEMS.register("netherite_scythe", () -> new Scythe(Tiers.NETHERITE, new Item.Properties().attributes(Scythe.createAttributes(Tiers.NETHERITE, 2, -2))));


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
