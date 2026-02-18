package dev.sycraxe.burden.compat.curios;

import dev.sycraxe.burden.AllItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CuriosEvents {
    public CuriosEvents(final IEventBus modEventBus) {
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::clientSetup);
    }

    public void registerCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerItem(
                CuriosCapability.ITEM,
                (stack, context) -> new ICurio() {

                    @Override
                    public ItemStack getStack() {
                        return stack;
                    }

                    @Override
                    public void curioTick(SlotContext slotContext) {}
                },
                AllItem.BACKPACK.get()
        );
    }

    public void clientSetup(final FMLClientSetupEvent evt) {
        CuriosRendererRegistry.register(AllItem.BACKPACK.get(), BackpackCurioRenderer::new);
    }
}
