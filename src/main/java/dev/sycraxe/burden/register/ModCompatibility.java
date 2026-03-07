package dev.sycraxe.burden.register;

import dev.sycraxe.burden.compatibility.CompatibilityRegistry;
import dev.sycraxe.burden.compatibility.CompatibiltyData;
import dev.sycraxe.burden.compatibility.curios.Curios;
import net.neoforged.bus.api.IEventBus;

public class ModCompatibility {
    public static final CompatibilityRegistry COMPAT_REGISTRY = new CompatibilityRegistry();

    public static void register(IEventBus bus) {
        COMPAT_REGISTRY.register(new CompatibiltyData("curios"), Curios::new);
        COMPAT_REGISTRY.register(bus);
    }
}
