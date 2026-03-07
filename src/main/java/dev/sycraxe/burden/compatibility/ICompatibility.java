package dev.sycraxe.burden.compatibility;

import net.neoforged.bus.api.IEventBus;

public interface ICompatibility {
    void register(IEventBus modBus);
}
