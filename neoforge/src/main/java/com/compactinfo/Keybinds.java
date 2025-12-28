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

    public static final KeyMapping COPY_COORDS = new KeyMapping(
            "key.compactinfo.copy_coords",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            "category.compactinfo"
    );

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_HUD);
        event.register(OPEN_CONFIG);
        event.register(COPY_COORDS);
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

        if (COPY_COORDS.consumeClick()) {
            if (client.player != null) {
                copyCoordinatesToClipboard(client);
            }
        }
    }

    private static void openConfigScreen(Minecraft client) {
        if (CompactInfo.isClothConfigAvailable()) {
            client.setScreen(HudSettings.createConfigScreen(client.screen));
        } else {
            if (client.player != null) {
                client.player.sendSystemMessage(Component.literal("§cCompact Info: §fNeed Cloth-Config dependency!"));
            }
        }
    }

    private static void copyCoordinatesToClipboard(Minecraft client) {
        if (client.player == null || client.level == null) return;

        int x = (int) client.player.getX();
        int y = (int) client.player.getY();
        int z = (int) client.player.getZ();

        String coords = String.format("%d %d %d", x, y, z);

        client.keyboardHandler.setClipboard(coords);

        if (client.player != null) {
            client.player.sendSystemMessage(
                    Component.translatable("message.compactinfo.coords_copied", coords)
            );
        }
    }
}