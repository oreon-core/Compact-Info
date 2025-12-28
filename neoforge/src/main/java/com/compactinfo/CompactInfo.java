package com.compactinfo;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(CompactInfo.MOD_ID)
public class CompactInfo {
    public static final String MOD_ID = "compactinfo";

    // Флаг наявності Cloth Config
    private static boolean clothConfigAvailable = false;

    public CompactInfo(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, HudConfigScreen.SPEC);

        // Перевірка наявності Cloth Config
        checkClothConfig();

        registerConfigScreen();

        System.out.println("CompactInfo mod loaded successfully!");
        System.out.println("Cloth Config available: " + clothConfigAvailable);
    }

    private static void checkClothConfig() {
        try {
            Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder");
            clothConfigAvailable = true;
        } catch (ClassNotFoundException e) {
            clothConfigAvailable = false;
        }
    }

    public static boolean isClothConfigAvailable() {
        return clothConfigAvailable;
    }

    private static void registerConfigScreen() {
        if (clothConfigAvailable) {
            ModLoadingContext.get().registerExtensionPoint(
                    IConfigScreenFactory.class,
                    () -> (minecraft, parent) -> HudSettings.createConfigScreen(parent)
            );
        }
    }
}