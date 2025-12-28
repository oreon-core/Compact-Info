package com.compactinfo;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class HudSettings {
    private static HudSettings instance;

    public static HudSettings getInstance() {
        if (instance == null) {
            instance = new HudSettings();
        }
        return instance;
    }

    private HudSettings() {}


    public boolean showCoords() {
        return HudConfigScreen.SHOW_COORDS.get();
    }

    public boolean showConv() {
        return HudConfigScreen.SHOW_CONV.get();
    }

    public boolean showBiome() {
        return HudConfigScreen.SHOW_BIOME.get();
    }

    public boolean showDays() {
        return HudConfigScreen.SHOW_DAYS.get();
    }

    public boolean showFPS() {
        return HudConfigScreen.SHOW_FPS.get();
    }

    public boolean showMemory() {
        return HudConfigScreen.SHOW_MEMORY.get();
    }

    public float getHudScale() {
        return HudConfigScreen.HUD_SCALE.get().floatValue();
    }


    public void setShowCoords(boolean value) {
        HudConfigScreen.SHOW_COORDS.set(value);
        save();
    }

    public void setShowConv(boolean value) {
        HudConfigScreen.SHOW_CONV.set(value);
        save();
    }

    public void setShowBiome(boolean value) {
        HudConfigScreen.SHOW_BIOME.set(value);
        save();
    }

    public void setShowDays(boolean value) {
        HudConfigScreen.SHOW_DAYS.set(value);
        save();
    }

    public void setShowFPS(boolean value) {
        HudConfigScreen.SHOW_FPS.set(value);
        save();
    }

    public void setShowMemory(boolean value) {
        HudConfigScreen.SHOW_MEMORY.set(value);
        save();
    }

    public void setHudScale(float value) {
        HudConfigScreen.HUD_SCALE.set((double) value);
        save();
    }

    public void save() {
        HudConfigScreen.SPEC.save();
    }

    public static Screen createConfigScreen(Screen parent) {
        HudSettings config = getInstance();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("screen.compactinfo.title"));

        builder.setSavingRunnable(config::save);

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("category.compactinfo"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();


        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.coords"), config.showCoords())
                .setDefaultValue(true)
                .setSaveConsumer(config::setShowCoords)
                .build());


        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.converted"), config.showConv())
                .setDefaultValue(false)
                .setSaveConsumer(config::setShowConv)
                .build());


        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.biome"), config.showBiome())
                .setDefaultValue(false)
                .setSaveConsumer(config::setShowBiome)
                .build());


        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.days"), config.showDays())
                .setDefaultValue(false)
                .setSaveConsumer(config::setShowDays)
                .build());


        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.fps"), config.showFPS())
                .setDefaultValue(true)
                .setSaveConsumer(config::setShowFPS)
                .build());


        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.memory"), config.showMemory())
                .setDefaultValue(false)
                .setSaveConsumer(config::setShowMemory)
                .build());


        general.addEntry(entryBuilder
                .startIntSlider(Component.translatable("hud.option.scale"), (int)(config.getHudScale() * 10), 5, 30)
                .setDefaultValue(10)
                .setSaveConsumer(value -> config.setHudScale(value / 10.0f))
                .setTextGetter(value -> Component.literal(String.format("%.1fx", value / 10.0f)))
                .build());

        return builder.build();
    }
}