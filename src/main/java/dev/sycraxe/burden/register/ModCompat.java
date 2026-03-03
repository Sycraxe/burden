package dev.sycraxe.burden.register;

import dev.sycraxe.burden.compat.CompatRegistry;
import dev.sycraxe.burden.compat.CompatData;
import dev.sycraxe.burden.compat.curios.Curios;
import net.neoforged.bus.api.IEventBus;

public class ModCompat {
    public static final CompatRegistry COMPAT_REGISTRY = new CompatRegistry();

    public static void register(IEventBus bus) {
        COMPAT_REGISTRY.register(new CompatData("curios"), Curios::new);
        COMPAT_REGISTRY.register(bus);
    }
}
