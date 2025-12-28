package com.compactinfo;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class HudConfigScreen {

    public static Screen createConfigScreen(Screen parent) {
        HudSettings cfg = HudSettings.getInstance();
        MinecraftClient client = MinecraftClient.getInstance();

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int padding = 3;

        int minX = padding;
        int maxX = screenWidth - padding;
        int minY = padding;
        int maxY = screenHeight - padding;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("screen.compactinfo.title"));

        builder.setSavingRunnable(cfg::save);

        var category = builder.getOrCreateCategory(Text.translatable("category.compactinfo"));
        var entryBuilder = ConfigEntryBuilder.create();

        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.coords"), cfg.showCoords())
                .setDefaultValue(true)
                .setSaveConsumer(cfg::setShowCoords)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.converted"), cfg.showConv())
                .setDefaultValue(false)
                .setSaveConsumer(cfg::setShowConv)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.biome"), cfg.showBiome())
                .setDefaultValue(false)
                .setSaveConsumer(cfg::setShowBiome)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.days"), cfg.showDays())
                .setDefaultValue(false)
                .setSaveConsumer(cfg::setShowDays)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.fps"), cfg.showFPS())
                .setDefaultValue(true)
                .setSaveConsumer(cfg::setShowFPS)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.memory"), cfg.showMemory())
                .setDefaultValue(false)
                .setSaveConsumer(cfg::setShowMemory)
                .build());

        category.addEntry(entryBuilder
                .startIntSlider(Text.translatable("hud.option.posX"), cfg.getHudPosX(), minX, maxX)
                .setDefaultValue(padding)
                .setSaveConsumer(cfg::setHudPosX)
                .setTextGetter(value -> Text.literal(value + "px"))
                .build());

        category.addEntry(entryBuilder
                .startIntSlider(Text.translatable("hud.option.posY"), cfg.getHudPosY(), minY, maxY)
                .setDefaultValue(padding)
                .setSaveConsumer(cfg::setHudPosY)
                .setTextGetter(value -> Text.literal(value + "px"))
                .build());

        category.addEntry(entryBuilder
                .startIntSlider(Text.translatable("hud.option.scale"), (int)(cfg.getHudScale() * 10), 5, 30)
                .setDefaultValue(10)
                .setSaveConsumer(value -> cfg.setHudScale(value / 10.0f))
                .setTextGetter(value -> Text.literal(String.format("%.1fx", value / 10.0f)))
                .build());

        return builder.build();
    }
}