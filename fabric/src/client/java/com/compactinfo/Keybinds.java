package com.compactinfo;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public final class Keybinds {
    private static KeyBinding toggleHudKey;
    private static KeyBinding openHudConfigKey;
    private static KeyBinding copyCoordsKey;

    private static Boolean hasClothConfig = null;

    private static boolean checkClothConfigAvailable() {
        if (hasClothConfig == null) {
            try {
                Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder");
                hasClothConfig = true;
            } catch (ClassNotFoundException e) {
                hasClothConfig = false;
            }
        }
        return hasClothConfig;
    }

    public static void register() {
        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.compactinfo.toggle_hud",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.compactinfo"
        ));

        openHudConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.compactinfo.open_hud_config",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.compactinfo"
        ));


        copyCoordsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.compactinfo.copy_coords",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.compactinfo"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleHudKey.wasPressed()) {
                HudOverlay.toggle();
            }

            while (openHudConfigKey.wasPressed()) {
                if (client.player != null) {
                    handleConfigScreen(client);
                }
            }


            while (copyCoordsKey.wasPressed()) {
                if (client.player != null) {
                    copyCoordinatesToClipboard(client);
                }
            }
        });
    }

    private static void handleConfigScreen(MinecraftClient client) {
        if (checkClothConfigAvailable()) {
            try {
                client.setScreen(HudConfigScreen.createConfigScreen(client.currentScreen));
            } catch (Exception e) {
                showMissingDependencyMessage(client);
            }
        } else {
            showMissingDependencyMessage(client);
        }
    }

    private static void copyCoordinatesToClipboard(MinecraftClient client) {
        if (client.player == null || client.world == null) return;


        int x = (int) client.player.getX();
        int y = (int) client.player.getY();
        int z = (int) client.player.getZ();


        String coords = String.format("%d %d %d", x, y, z);


        client.keyboard.setClipboard(coords);


        client.player.sendMessage(
                Text.translatable("message.compactinfo.coords_copied", coords),
                false
        );
    }

    private static void showMissingDependencyMessage(MinecraftClient client) {
        if (client.player != null) {
            client.player.sendMessage(
                    Text.translatable("message.compactinfo.missing_cloth_config"),
                    false
            );
        }
    }
}