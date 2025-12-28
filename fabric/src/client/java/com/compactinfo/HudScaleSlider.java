package com.compactinfo;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class HudScaleSlider extends SliderWidget {
    private final HudSettings settings;
    private static final double MIN = 0.5;
    private static final double MAX = 3.0;

    public HudScaleSlider(int x, int y, int width, int height, HudSettings settings) {
        super(x, y, width, height,
                Text.literal("HUD Scale: " + String.format("%.2f", settings.getHudScale())),
                normalize(settings.getHudScale())); // value від 0 до 1
        this.settings = settings;
        this.updateMessage();
    }


    private static double normalize(double scale) {
        return (scale - MIN) / (MAX - MIN);
    }


    private static double denormalize(double value) {
        return MIN + value * (MAX - MIN);
    }

    @Override
    protected void applyValue() {
        double scale = denormalize(this.value);
        settings.setHudScale((float) scale);
        settings.save();
    }

    @Override
    protected void updateMessage() {
        double scale = denormalize(this.value);
        this.setMessage(Text.literal("HUD Scale: " + String.format("%.2f", scale)));
    }
}
