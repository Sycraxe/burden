package dev.sycraxe.burden.core.event;

import dev.sycraxe.burden.core.codec.BackpackEventCodec;
import dev.sycraxe.burden.core.gui.BackpackMenuProvider;
import dev.sycraxe.burden.core.gui.menu.AbstractBackpackMenu;
import dev.sycraxe.burden.core.gui.menu.EquippedBackpackMenu;
import dev.sycraxe.burden.core.gui.menu.HandheldBackpackMenu;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.BiFunction;

public class BackpackServerPayloadHandler {
    public static void handleDataOnMain(final BackpackEventCodec data, final IPayloadContext context) {
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