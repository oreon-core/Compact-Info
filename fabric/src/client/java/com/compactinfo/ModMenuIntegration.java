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
        // Перевіряємо наявність Cloth Config ще ДО повернення фабрики
        if (!checkClothConfigAvailable()) {
            // Повертаємо null, щоб ModMenu не показував кнопку конфігурації взагалі
            return null;
        }

        // Тільки якщо Cloth Config доступний, повертаємо фабрику
        return parent -> {
            try {
                return HudConfigScreen.createConfigScreen(parent);
            } catch (Exception e) {
                // На всякий випадок, якщо щось піде не так при створенні екрана
                return null;
            }
        };
    }
}