package dev.sycraxe.burden.compatibility;

import net.neoforged.bus.api.IEventBus;

public interface Compatibility {
    void register(IEventBus modBus);
}
