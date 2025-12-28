package com.compactinfo;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {

    private static boolean checkClothConfigAvailable() {
        try {
            Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {

        if (!checkClothConfigAvailable()) {

            return null;
        }


        return parent -> {
            try {
                return HudConfigScreen.createConfigScreen(parent);
            } catch (Exception e) {

                return null;
            }
        };
    }
}