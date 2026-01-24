package com.compactinfo;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
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
