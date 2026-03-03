package dev.sycraxe.burden.compat;

import net.neoforged.bus.api.IEventBus;

import java.util.HashMap;
import java.util.function.Supplier;

public class CompatRegistry {
    private final HashMap<CompatData, Supplier<ICompat>> compats = new HashMap<>();

    public void register(CompatData data, Supplier<ICompat> supplier) {
        compats.put(data, supplier);
    }

    public void register(IEventBus modBus) {
        compats.forEach((compatData, compatSupplier) -> {
            if (!compatData.isLoaded()) {
                return;
            }
            compatSupplier.get().register(modBus);
        });
    }
}
