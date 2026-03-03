package dev.sycraxe.burden.event;

import com.mojang.blaze3d.platform.InputConstants;
import dev.sycraxe.burden.register.ModMenuType;
import dev.sycraxe.burden.backpack.BackpackScreen;
import dev.sycraxe.burden.network.BackpackOpeningData;
import dev.sycraxe.burden.backpack.BackpackLayerRenderer;
import dev.sycraxe.burden.backpack.BackpackModel;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class ClientEventHandler {
    public static void register(IEventBus modBus) {
        modBus.addListener(ClientEventHandler::registerBindings);
        modBus.addListener(ClientEventHandler::registerScreens);
        modBus.addListener(ClientEventHandler::registerLayerDefinitions);
        modBus.addListener(ClientEventHandler::addPlayerLayers);
        IEventBus eventBus = NeoForge.EVENT_BUS;
        eventBus.addListener(ClientEventHandler::onClientTick);
    }

    public static final Lazy<KeyMapping> BACKPACK_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.burden.backpack",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.categories.burden"
    ));

    private static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(BACKPACK_MAPPING.get());
    }

    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuType.BACKPACK_MENU.get(), BackpackScreen::new);
    }

    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BackpackModel.LAYER_LOCATION, BackpackModel::createBodyLayer);
    }

    private static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skin : event.getSkins()) {
            if (event.getSkin(skin) instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new BackpackLayerRenderer(playerRenderer, event.getEntityModels()));
            }
        }
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        while (BACKPACK_MAPPING.get().consumeClick()) {
            PacketDistributor.sendToServer(new BackpackOpeningData());
        }
    }
}
