package dev.sycraxe.burden.compatibility;

import net.neoforged.bus.api.IEventBus;

import java.util.HashMap;
import java.util.function.Supplier;

public class CompatibilityRegistry {
    private final HashMap<CompatibiltyData, Supplier<Compatibility>> compats = new HashMap<>();

    public void register(CompatibiltyData data, Supplier<Compatibility> supplier) {
        compats.put(data, supplier);
    }

    public void register(IEventBus modBus) {
        compats.forEach((compatibiltyData, compatSupplier) -> {
            if (!compatibiltyData.isLoaded()) {
                return;
            }
            compatSupplier.get().register(modBus);
        });
    }
}
