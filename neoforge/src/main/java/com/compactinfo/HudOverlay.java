package com.compactinfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.Level;

public class HudOverlay {
    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static boolean enabled = true;

    private static long lastMemoryUpdateTime = 0;
    private static final long MEMORY_UPDATE_INTERVAL = 60;
    private static String cachedMemoryText = "RAM: 0MB";

    public static void render(GuiGraphics guiGraphics) {
        if (!enabled || CLIENT.player == null || CLIENT.level == null || CLIENT.options.hideGui) {
            return;
        }


        float scale = HudConfigScreen.HUD_SCALE.get().floatValue();

        double xi = CLIENT.player.getX();
        double yi = CLIENT.player.getY();
        double zi = CLIENT.player.getZ();

        BlockPos pos = CLIENT.player.blockPosition();

        String biome = "unknown";
        try {
            Holder<Biome> biomeHolder = CLIENT.level.getBiome(pos);
            if (biomeHolder.isBound()) {
                ResourceLocation biomeLocation = biomeHolder.unwrapKey()
                        .map(key -> key.location())
                        .orElse(null);

                if (biomeLocation != null) {
                    biome = biomeLocation.getPath();
                    biome = biome.replace("_", " ");
                    if (!biome.isEmpty()) {
                        biome = biome.substring(0, 1).toUpperCase() + biome.substring(1);
                    }
                }
            }
        } catch (Exception e) {
            biome = "error";
        }

        int fps = CLIENT.getFps();

        boolean inNether = CLIENT.level.dimension() == Level.NETHER;
        boolean inOverworld = CLIENT.level.dimension() == Level.OVERWORLD;
        String conv = inOverworld
                ? String.format("N: %.0f %.0f %.0f", xi / 8.0, yi, zi / 8.0)
                : inNether
                ? String.format("O: %.0f %.0f %.0f", xi * 8.0, yi, zi * 8.0)
                : "Dim: " + CLIENT.level.dimension().location().getPath();

        int left = 5;
        int top = 5;
        int lineGap = 12;
        int color = 0xFFFFFFFF;
        int line = top;

        if (HudConfigScreen.SHOW_COORDS.get()) {
            drawScaled(guiGraphics, (int) xi + " " + (int) yi + " " + (int) zi, left, line, color, scale);
            line += scaled(lineGap, scale);
        }

        if (HudConfigScreen.SHOW_CONV.get()) {
            drawScaled(guiGraphics, conv, left, line, color, scale);
            line += scaled(lineGap, scale);
        }

        if (HudConfigScreen.SHOW_BIOME.get()) {
            drawScaled(guiGraphics, "B: " + biome, left, line, color, scale);
            line += scaled(lineGap, scale);
        }

        if (HudConfigScreen.SHOW_DAYS.get()) {
            long time = CLIENT.level.getDayTime();
            long days = time / 24000L;
            drawScaled(guiGraphics, "Day: " + days, left, line, color, scale);
            line += scaled(lineGap, scale);
        }

        if (HudConfigScreen.SHOW_FPS.get()) {
            drawScaled(guiGraphics, "FPS: " + fps, left, line, color, scale);
            line += scaled(lineGap, scale);
        }

        if (HudConfigScreen.SHOW_MEMORY.get()) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastMemoryUpdateTime > MEMORY_UPDATE_INTERVAL) {
                updateMemoryText();
                lastMemoryUpdateTime = currentTime;
            }

            drawScaled(guiGraphics, cachedMemoryText, left, line, color, scale);
        }
    }

    public static void toggle() {
        enabled = !enabled;
    }

    private static void updateMemoryText() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        cachedMemoryText = String.format("RAM: %dMB", usedMemory / 1024 / 1024);
    }

    private static int scaled(int v, float scale) {
        return Math.max(1, (int) Math.floor(v * scale));
    }

    private static void drawScaled(GuiGraphics guiGraphics, String text, int x, int y, int color, float scale) {
        var poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale(scale, scale, 1.0f);

        guiGraphics.drawString(
                CLIENT.font,
                Component.literal(text),
                0,
                0,
                color,
                true
        );

        poseStack.popPose();
    }
}