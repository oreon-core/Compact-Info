package com.compactinfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;

public class HudConfigScreen extends Screen {
    private final Screen parent;
    private final HudSettings settings;

    public HudConfigScreen(Screen parent) {
        super(Text.translatable("modmenu.description.compactinfo"));
        this.parent = parent;
        this.settings = HudSettings.getInstance();
    }

    @Override
    protected void init() {
        int buttonWidth = 150;
        int buttonHeight = 16;
        int spacing = 6;
        int totalHeight = (buttonHeight + spacing) * 5 - spacing;
        int startY = (this.height - totalHeight) / 2;

        int y = startY;

        addToggleButton("hud.option.coords", y, settings::showCoords, b -> {
            settings.setShowCoords(!settings.showCoords());
            settings.save();
        });
        y += buttonHeight + spacing;

        addToggleButton("hud.option.converted", y, settings::showConv, b -> {
            settings.setShowConv(!settings.showConv());
            settings.save();
        });
        y += buttonHeight + spacing;

        addToggleButton("hud.option.biome", y, settings::showBiome, b -> {
            settings.setShowBiome(!settings.showBiome());
            settings.save();
        });
        y += buttonHeight + spacing;

        addToggleButton("hud.option.fps", y, settings::showFPS, b -> {
            settings.setShowFPS(!settings.showFPS());
            settings.save();
        });
        y += buttonHeight + spacing;


        int sliderY = y;
        addDrawableChild(new HudScaleSlider(
                (this.width - 150) / 2, sliderY, 150, 20, settings
        ));
        y += 26;


        addButton("hud.option.back", y, b -> MinecraftClient.getInstance().setScreen(parent));
    }


    private void addToggleButton(String translationKey, int y, BooleanSupplier valueSupplier, ButtonWidget.PressAction action) {
        int buttonWidth = 150;
        int buttonHeight = 16;
        int centerX = (this.width - buttonWidth) / 2;

        Text buttonText = Text.translatable(translationKey)
                .append(Text.literal(": " + valueSupplier.getAsBoolean()));

        ButtonWidget button = ButtonWidget.builder(buttonText, b -> {
                    action.onPress(b);
                    b.setMessage(Text.translatable(translationKey)
                            .append(Text.literal(": " + valueSupplier.getAsBoolean())));
                }).dimensions(centerX, y, buttonWidth, buttonHeight)
                .build();

        addDrawableChild(button);
    }


    private void addButton(String translationKey, int y, ButtonWidget.PressAction action) {
        int buttonWidth = 150;
        int buttonHeight = 16;
        int centerX = (this.width - buttonWidth) / 2;

        addDrawableChild(ButtonWidget.builder(Text.translatable(translationKey), action)
                .dimensions(centerX, y, buttonWidth, buttonHeight)
                .build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {

        ctx.fillGradient(0, 0, this.width, this.height, 0xC0101010, 0xD0101010);


        super.render(ctx, mouseX, mouseY, delta);


        int titleY = (this.height - ((16 + 6) * 5 - 6)) / 2 - 20;
        ctx.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.translatable("hud.title.elements"),
                this.width / 2,
                titleY,
                0xFFFFFFFF
        );
    }
}
