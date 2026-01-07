package com.compactinfo;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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

       
        int currentPosX = cfg.getHudPosX(screenWidth);
        int currentPosY = cfg.getHudPosY(screenHeight);

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("screen.compactinfo.title"));

        builder.setSavingRunnable(() -> {
           
            cfg.save();
        });

        var category = builder.getOrCreateCategory(Text.translatable("category.compactinfo"));
        var entryBuilder = ConfigEntryBuilder.create();

       
        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.coords"), cfg.showCoords())
                .setDefaultValue(true)
                .setSaveConsumer(cfg::setShowCoords)
                .build());

       
        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.yaw"), cfg.showYaw())
                .setDefaultValue(false)
                .setSaveConsumer(cfg::setShowYaw)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.pitch"), cfg.showPitch())
                .setDefaultValue(false)
                .setSaveConsumer(cfg::setShowPitch)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.facing"), cfg.showFacing())
                .setDefaultValue(false)
                .setSaveConsumer(cfg::setShowFacing)
                .build());

       
        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.surfacey"), cfg.showSurfaceY())
                .setDefaultValue(false)
                .setSaveConsumer(cfg::setShowSurfaceY)
                .build());

       
        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.chunk"), cfg.showChunk())
                .setDefaultValue(false)
                .setSaveConsumer(cfg::setShowChunk)
                .build());

       
        category.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("hud.option.hostile"), cfg.showHostile())
                .setDefaultValue(false)
                .setSaveConsumer(cfg::setShowHostile)
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
                .startIntSlider(Text.translatable("hud.option.posX"), currentPosX, minX, maxX)
                .setDefaultValue(padding)
                .setSaveConsumer(value -> {
                   
                    MinecraftClient mc = MinecraftClient.getInstance();
                    int currentScreenWidth = mc.getWindow().getScaledWidth();
                    int currentScreenHeight = mc.getWindow().getScaledHeight();

                   
                    int currentY = cfg.getHudPosY(currentScreenHeight);

                   
                    cfg.setHudPosition(value, currentY, currentScreenWidth, currentScreenHeight);
                })
                .setTextGetter(value -> Text.literal(value + "px"))
                .build());

       
        category.addEntry(entryBuilder
                .startIntSlider(Text.translatable("hud.option.posY"), currentPosY, minY, maxY)
                .setDefaultValue(padding)
                .setSaveConsumer(value -> {
                   
                    MinecraftClient mc = MinecraftClient.getInstance();
                    int currentScreenWidth = mc.getWindow().getScaledWidth();
                    int currentScreenHeight = mc.getWindow().getScaledHeight();

                   
                    int currentX = cfg.getHudPosX(currentScreenWidth);

                   
                    cfg.setHudPosition(currentX, value, currentScreenWidth, currentScreenHeight);
                })
                .setTextGetter(value -> Text.literal(value + "px"))
                .build());

       
        category.addEntry(entryBuilder
                .startIntSlider(Text.translatable("hud.option.scale"), (int)(cfg.getHudScale() * 10), 5, 30)
                .setDefaultValue(10)
                .setSaveConsumer(value -> cfg.setHudScale(value / 10.0f))
                .setTextGetter(value -> Text.literal(String.format("%.1fx", value / 10.0f)))
                .build());

       
        category.addEntry(entryBuilder
                .startIntSlider(Text.translatable("hud.option.layout"), cfg.getHudLayout(), 0, 6)
                .setDefaultValue(6)
                .setSaveConsumer(cfg::setHudLayout)
                .setTextGetter(value -> {
                    String[] layoutNames = {
                            Text.translatable("hud.layout.top_left").getString(),
                            Text.translatable("hud.layout.top_center").getString(),
                            Text.translatable("hud.layout.top_right").getString(),
                            Text.translatable("hud.layout.bottom_left").getString(),
                            Text.translatable("hud.layout.bottom_center").getString(),
                            Text.translatable("hud.layout.bottom_right").getString(),
                            Text.translatable("hud.layout.auto").getString() + " " + Formatting.GRAY
                    };
                    return Text.literal(layoutNames[value]);
                })
                .build());

       
        category.addEntry(entryBuilder
                .startIntSlider(Text.translatable("hud.option.opacity"), cfg.getOpacity(), 0, 100)
                .setDefaultValue(100)
                .setSaveConsumer(cfg::setOpacity)
                .setTextGetter(value -> Text.literal(value + "%"))
                .build());

        return builder.build();
    }
}