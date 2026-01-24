package com.compactinfo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
@Environment(EnvType.CLIENT)
public class CompactInfoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudOverlay.register();
        Keybinds.register();
        CommandConfig.register();
    }
}