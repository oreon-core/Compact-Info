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

   
    public static final ModConfigSpec.BooleanValue SHOW_YAW;
    public static final ModConfigSpec.BooleanValue SHOW_PITCH;
    public static final ModConfigSpec.BooleanValue SHOW_FACING;
    public static final ModConfigSpec.BooleanValue SHOW_SURFACE_Y;
    public static final ModConfigSpec.BooleanValue SHOW_CHUNK;
    public static final ModConfigSpec.BooleanValue SHOW_HOSTILE;

    public static final ModConfigSpec.DoubleValue HUD_SCALE;
    public static final ModConfigSpec.IntValue HUD_POS_X;
    public static final ModConfigSpec.IntValue HUD_POS_Y;

   
    public static final ModConfigSpec.DoubleValue HUD_REL_X;
    public static final ModConfigSpec.DoubleValue HUD_REL_Y;

   
    public static final ModConfigSpec.IntValue HUD_LAYOUT;
    public static final ModConfigSpec.IntValue OPACITY;

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

       
        SHOW_YAW = BUILDER
                .comment("Show yaw rotation")
                .define("showYaw", false);

        SHOW_PITCH = BUILDER
                .comment("Show pitch rotation")
                .define("showPitch", false);

        SHOW_FACING = BUILDER
                .comment("Show cardinal direction")
                .define("showFacing", false);

        SHOW_SURFACE_Y = BUILDER
                .comment("Show surface Y level")
                .define("showSurfaceY", false);

        SHOW_CHUNK = BUILDER
                .comment("Show chunk coordinates")
                .define("showChunk", false);

        SHOW_HOSTILE = BUILDER
                .comment("Show hostile mob count")
                .define("showHostile", false);

        HUD_SCALE = BUILDER
                .comment("HUD scale factor")
                .defineInRange("hudScale", 1.0, 0.5, 3.0);

        HUD_POS_X = BUILDER
                .comment("HUD X position (legacy)")
                .defineInRange("hudPosX", 3, 0, 1000);

        HUD_POS_Y = BUILDER
                .comment("HUD Y position (legacy)")
                .defineInRange("hudPosY", 3, 0, 1000);

       
        HUD_REL_X = BUILDER
                .comment("HUD relative X position (0.0-1.0)")
                .defineInRange("hudRelX", 0.0, 0.0, 1.0);

        HUD_REL_Y = BUILDER
                .comment("HUD relative Y position (0.0-1.0)")
                .defineInRange("hudRelY", 0.0, 0.0, 1.0);

       
        HUD_LAYOUT = BUILDER
                .comment("HUD layout (0-6, where 6=auto)")
                .defineInRange("hudLayout", 6, 0, 6);

        OPACITY = BUILDER
                .comment("HUD opacity (0-100)")
                .defineInRange("opacity", 100, 0, 100);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}