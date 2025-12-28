package com.compactinfo;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
public final class Keybinds {
    private static KeyBinding toggleHudKey;
    private static KeyBinding openHudConfigKey;


    private static final KeyBinding.Category COMPACTINFO_CATEGORY =
            KeyBinding.Category.create(Identifier.of("compactinfo", "keybind_category"));
    public static void register() {
        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.compactinfo.toggle_hud",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                COMPACTINFO_CATEGORY
        ));

        openHudConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.compactinfo.open_hud_config",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                COMPACTINFO_CATEGORY
        ));


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleHudKey.wasPressed()) {
                HudOverlay.toggle();
            }

            while (openHudConfigKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(
                        new HudConfigScreen(MinecraftClient.getInstance().currentScreen)
                );
            }
        });
    }
}
