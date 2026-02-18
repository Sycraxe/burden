package dev.sycraxe.burden.event.backpack;

import dev.sycraxe.burden.data.BackpackEventData;
import dev.sycraxe.burden.gui.backpack.BackpackMenuProvider;
import dev.sycraxe.burden.gui.backpack.AbstractBackpackMenu;
import dev.sycraxe.burden.gui.backpack.menus.EquippedBackpackMenu;
import dev.sycraxe.burden.gui.backpack.menus.HandheldBackpackMenu;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.BiFunction;

public class BackpackServerPayloadHandler {
    public static void handleDataOnMain(final BackpackEventData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!data.equipped()) {
                context.player().openMenu(new BackpackMenuProvider(data.stack(), new BiFunction<Integer, Inventory, AbstractBackpackMenu>() {
                    @Override
                    public AbstractBackpackMenu apply(Integer id, Inventory inventory) {
                        return new HandheldBackpackMenu(id, inventory);
                    }
                }));
            } else {
                context.player().openMenu(new BackpackMenuProvider(data.stack(), new BiFunction<Integer, Inventory, AbstractBackpackMenu>() {
                    @Override
                    public AbstractBackpackMenu apply(Integer id, Inventory inventory) {
                        return new EquippedBackpackMenu(id, inventory);
                    }
                }));
            }
        });
    }
}