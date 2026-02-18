package dev.sycraxe.burden;

import dev.sycraxe.burden.compat.curios.CuriosEvents;
import net.neoforged.fml.ModList;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(Burden.MOD_ID)
public class Burden {
    public static final String MOD_ID = "burden";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean isCuriosCompatLoaded() {
        return ModList.get().isLoaded("curios");
    }

    public Burden(IEventBus modEventBus, ModContainer modContainer) {
        AllItem.register(modEventBus);
        AllBlocks.register(modEventBus);
        AllBlockEntities.register(modEventBus);
        AllTabs.register(modEventBus);
        AllMenuTypes.register(modEventBus);

        if (isCuriosCompatLoaded())  {
            new CuriosEvents(modEventBus);
        }
    }
}

