package dev.sycraxe.burden.compatibility.curios;

import dev.sycraxe.burden.compatibility.Compatibility;
import dev.sycraxe.burden.inventory.InventoryHandler;
import dev.sycraxe.burden.register.ModInventoryHandler;
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

import java.util.Map;
import java.util.Optional;

public class Curios implements Compatibility {

    public static final InventoryHandler CURIOS_BACK_INVENTORY = ModInventoryHandler.registerInventoryHandler(
            new InventoryHandler(
                    (player, slot) -> {
                        Optional<IDynamicStackHandler> maybeStackHandler = getStackHandler(player);
                        if (maybeStackHandler.isEmpty()) return ItemStack.EMPTY;
                        IDynamicStackHandler stackHandler = maybeStackHandler.get();
                        if (slot >= stackHandler.getSlots()) return ItemStack.EMPTY;
                        return stackHandler.getStackInSlot(slot);
                    },
                    player -> {
                        Optional<IDynamicStackHandler> maybeStackHandler = getStackHandler(player);
                        if (maybeStackHandler.isEmpty()) return 0;
                        return maybeStackHandler.get().getSlots();
                    }
            ),
            Map.of(ModInventoryHandler.ON_KEYBIND_ID, 0)
    );

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

    private static Optional<IDynamicStackHandler> getStackHandler(Player player) {
        Optional<ICuriosItemHandler> maybeInventory = CuriosApi.getCuriosInventory(player);
        if (maybeInventory.isEmpty()) return Optional.empty();
        ICuriosItemHandler inventory = maybeInventory.get();
        Optional<ICurioStacksHandler> maybeStacksHandler = inventory.getStacksHandler("back");
        if (maybeStacksHandler.isEmpty()) return Optional.empty();
        return Optional.of(maybeStacksHandler.get().getStacks());
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
