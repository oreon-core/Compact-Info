package com.compactinfo;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(modid = CompactInfo.MOD_ID, value = Dist.CLIENT)
public class Keybinds {
    public static final KeyMapping TOGGLE_HUD = new KeyMapping(
            "key.compactinfo.toggle_hud",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            "category.compactinfo"
    );

    public static final KeyMapping OPEN_CONFIG = new KeyMapping(
            "key.compactinfo.open_hud_config",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            "category.compactinfo"
    );

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_HUD);
        event.register(OPEN_CONFIG);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft client = Minecraft.getInstance();

        if (TOGGLE_HUD.consumeClick()) {
            HudOverlay.toggle();
        }

        if (OPEN_CONFIG.consumeClick()) {
            if (client.player != null) {
                openConfigScreen(client);
            }
        }
    }

    private static void openConfigScreen(Minecraft client) {
        if (CompactInfo.isClothConfigAvailable()) {

            client.setScreen(HudSettings.createConfigScreen(client.screen));
        } else {

            String message = "§cCompact Info: §fNeed Cloth-Config dependency§f!";
            if (client.player != null) {
                client.player.sendSystemMessage(Component.literal(message));
            }
        }
    }
}