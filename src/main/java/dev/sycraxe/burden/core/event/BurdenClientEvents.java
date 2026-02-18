package dev.sycraxe.burden.core.event;

import com.mojang.blaze3d.platform.InputConstants;
import dev.sycraxe.burden.AllMenuTypes;
import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.compat.curios.Curios;
import dev.sycraxe.burden.core.codec.BackpackEventCodec;
import dev.sycraxe.burden.core.gui.BackpackScreen;
import dev.sycraxe.burden.core.gui.menu.EquippedBackpackMenu;
import dev.sycraxe.burden.AllItem;
import dev.sycraxe.burden.core.rendering.BackpackLayerRenderer;
import dev.sycraxe.burden.core.rendering.BackpackModel;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@Mod(value = Burden.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Burden.MOD_ID, value = Dist.CLIENT)
public class BurdenClientEvents {
    public BurdenClientEvents(ModContainer container) {}

    public static final Lazy<KeyMapping> BACKPACK_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.burden.backpack",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.categories.burden"
    ));

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(BACKPACK_MAPPING.get());
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (BACKPACK_MAPPING.get().consumeClick()) {
            Player player = Minecraft.getInstance().player;
            ItemStack stack = Burden.isCuriosCompatLoaded() ? Curios.getEquippedBackpack(player) : player.getInventory().getItem(EquippedBackpackMenu.CHEST_INVENTORY_SLOT);
            if (stack.is(AllItem.BACKPACK)) {
                PacketDistributor.sendToServer(new BackpackEventCodec(stack, true));
            }
        }
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(AllMenuTypes.EQUIPPED_BACKPACK_MENU.get(), BackpackScreen::new);
        event.register(AllMenuTypes.HANDHELD_BACKPACK_MENU.get(), BackpackScreen::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BackpackModel.LAYER_LOCATION, BackpackModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skin : event.getSkins()) {
            if (event.getSkin(skin) instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new BackpackLayerRenderer(playerRenderer, event.getEntityModels()));
            }
        }
    }
}
