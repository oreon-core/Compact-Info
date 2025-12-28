package com.compactinfo;

import net.neoforged.neoforge.common.ModConfigSpec;

public class HudConfigScreen {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue SHOW_COORDS;
    public static final ModConfigSpec.BooleanValue SHOW_CONV;
    public static final ModConfigSpec.BooleanValue SHOW_BIOME;
    public static final ModConfigSpec.BooleanValue SHOW_DAYS;
    public static final ModConfigSpec.BooleanValue SHOW_FPS;
    public static final ModConfigSpec.BooleanValue SHOW_MEMORY;
    public static final ModConfigSpec.DoubleValue HUD_SCALE;

    static {
        BUILDER.push("HUD Settings");

        SHOW_COORDS = BUILDER
                .comment("Show coordinates")
                .define("showCoords", true);

        SHOW_CONV = BUILDER
                .comment("Show converted coordinates (Nether/Overworld)")
                .define("showConv", false);

        SHOW_BIOME = BUILDER
                .comment("Show biome information")
                .define("showBiome", false);

        SHOW_DAYS = BUILDER
                .comment("Show current day")
                .define("showDays", false);

        SHOW_FPS = BUILDER
                .comment("Show FPS counter")
                .define("showFPS", true);

        SHOW_MEMORY = BUILDER
                .comment("Show memory usage")
                .define("showMemory", false);

        HUD_SCALE = BUILDER
                .comment("HUD scale factor")
                .defineInRange("hudScale", 1.0, 0.5, 3.0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}