package com.compactinfo;

import net.fabricmc.api.ClientModInitializer;

public class CompactInfoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudOverlay.register();
        Keybinds.register();
    }
}