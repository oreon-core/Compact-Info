package com.compactinfo;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class HudSettings {

    @SuppressWarnings("InstantiationOfUtilityClass")
    public static HudSettings getInstance() {
        return new HudSettings();
    }

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

    public int getHudPosX() {
        return HudConfigScreen.HUD_POS_X.get();
    }

    public int getHudPosY() {
        return HudConfigScreen.HUD_POS_Y.get();
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

    public void setHudPosX(int value) {
        HudConfigScreen.HUD_POS_X.set(value);
        save();
    }

    public void setHudPosY(int value) {
        HudConfigScreen.HUD_POS_Y.set(value);
        save();
    }

    public void save() {
        HudConfigScreen.SPEC.save();
    }

    @SuppressWarnings("resource")
    public static Screen createConfigScreen(Screen parent) {
        Minecraft client = Minecraft.getInstance();
        int screenWidth = client.getWindow().getGuiScaledWidth();
        int screenHeight = client.getWindow().getGuiScaledHeight();
        int padding = 3;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("screen.compactinfo.title"));

        builder.setSavingRunnable(HudConfigScreen.SPEC::save);

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("category.compactinfo"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.coords"), HudConfigScreen.SHOW_COORDS.get())
                .setDefaultValue(true)
                .setSaveConsumer(HudConfigScreen.SHOW_COORDS::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.converted"), HudConfigScreen.SHOW_CONV.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_CONV::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.biome"), HudConfigScreen.SHOW_BIOME.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_BIOME::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.days"), HudConfigScreen.SHOW_DAYS.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_DAYS::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.fps"), HudConfigScreen.SHOW_FPS.get())
                .setDefaultValue(true)
                .setSaveConsumer(HudConfigScreen.SHOW_FPS::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.memory"), HudConfigScreen.SHOW_MEMORY.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_MEMORY::set)
                .build());

        general.addEntry(entryBuilder
                .startIntSlider(Component.translatable("hud.option.posX"), HudConfigScreen.HUD_POS_X.get(), padding, screenWidth - padding)
                .setDefaultValue(padding)
                .setSaveConsumer(HudConfigScreen.HUD_POS_X::set)
                .setTextGetter(value -> Component.literal(value + "px"))
                .build());

        general.addEntry(entryBuilder
                .startIntSlider(Component.translatable("hud.option.posY"), HudConfigScreen.HUD_POS_Y.get(), padding, screenHeight - padding)
                .setDefaultValue(padding)
                .setSaveConsumer(HudConfigScreen.HUD_POS_Y::set)
                .setTextGetter(value -> Component.literal(value + "px"))
                .build());

        general.addEntry(entryBuilder
                .startIntSlider(Component.translatable("hud.option.scale"), (int)(HudConfigScreen.HUD_SCALE.get() * 10), 5, 30)
                .setDefaultValue(10)
                .setSaveConsumer(value -> HudConfigScreen.HUD_SCALE.set(value / 10.0))
                .setTextGetter(value -> Component.literal(String.format("%.1fx", value / 10.0f)))
                .build());

        return builder.build();
    }
}