package dev.sycraxe.burden;

import dev.sycraxe.burden.event.ClientEventHandler;
import dev.sycraxe.burden.event.CommonEventHandler;
import net.neoforged.api.distmarker.Dist;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(Burden.MOD_ID)
public class Burden {
    public static final String MOD_ID = "burden";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Burden(IEventBus modBus, Dist dist, ModContainer modContainer) {
        CommonEventHandler.register(modBus);
        if (dist == Dist.CLIENT) {
            ClientEventHandler.register(modBus);
        }
    }
}

