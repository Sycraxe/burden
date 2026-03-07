package dev.sycraxe.burden.compatibility.curios;

import dev.sycraxe.burden.compatibility.ICompatibility;
import dev.sycraxe.burden.register.ModItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class Curios implements ICompatibility {
    @Override
    public void register(IEventBus modBus) {
        modBus.addListener(Curios::registerCapabilities);
        modBus.addListener(Curios::clientSetup);
    }

    private static void registerCapabilities(final RegisterCapabilitiesEvent event) {
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
                ModItem.BACKPACK.get()
        );
    }

    private static void clientSetup(final FMLClientSetupEvent evt) {
        CuriosRendererRegistry.register(ModItem.BACKPACK.get(), BackpackCurioRenderer::new);
    }

    public static ItemStack getEquippedBackpack(Player player) {
        if (CuriosApi.getCuriosInventory(player).isEmpty()) {
            return ItemStack.EMPTY;
        }
        ICuriosItemHandler inventory = CuriosApi.getCuriosInventory(player).get();
        if (inventory.getStacksHandler("back").isEmpty()) {
            return ItemStack.EMPTY;
        }
        ICurioStacksHandler slot = inventory.getStacksHandler("back").get();
        IDynamicStackHandler stacks = slot.getStacks();
        for (int i = 0; i < stacks.getSlots(); i++) {
            ItemStack stack = stacks.getStackInSlot(i);
            if (stack.is(ModItem.BACKPACK)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
