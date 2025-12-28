package com.compactinfo;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class HudOverlay {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean enabled = true;

    private static final float FONT_SCALE = 1.0f;


    private static long lastMemoryUpdateTime = 0;
    private static final long MEMORY_UPDATE_INTERVAL = 60;
    private static String cachedMemoryText = "RAM: 0MB";

    @SuppressWarnings("deprecation")
    public static void register() {
        HudRenderCallback.EVENT.register(HudOverlay::render);
    }

    public static void toggle() {
        enabled = !enabled;
    }

    private static void render(DrawContext ctx, RenderTickCounter tickCounter) {
        if (!enabled || client.player == null || client.world == null || client.options.hudHidden) return;

        HudSettings settings = HudSettings.getInstance();
        float scale = settings.getHudScale();

        double xi = client.player.getX();
        double yi = client.player.getY();
        double zi = client.player.getZ();

        BlockPos pos = client.player.getBlockPos();

        String biome = client.world.getBiome(pos)
                .getKey().map(k -> k.getValue().getPath()).orElse("unknown");

        int fps = client.getCurrentFps();

        boolean inNether = client.world.getRegistryKey() == net.minecraft.world.World.NETHER;
        boolean inOverworld = client.world.getRegistryKey() == net.minecraft.world.World.OVERWORLD;
        String conv = inOverworld
                ? String.format("N: %.0f %.0f %.0f", xi / 8.0, yi, zi / 8.0)
                : inNether
                ? String.format("O: %.0f %.0f %.0f", xi * 8.0, yi, zi * 8.0)
                : "Dim: " + client.world.getRegistryKey().getValue().getPath();

        int left = 5;
        int top = 5;
        int lineGap = 12;
        int color = 0xFFFFFFFF;
        int line = top;

        if (settings.showCoords()) {
            drawScaled(ctx, (int) xi + " " + (int) yi + " " + (int) zi, left, line, color, scale);
            line += scaled(lineGap, scale);
        }

        if (settings.showConv()) {
            drawScaled(ctx, conv, left, line, color, scale);
            line += scaled(lineGap, scale);
        }

        if (settings.showBiome()) {
            drawScaled(ctx, "B: " + biome, left, line, color, scale);
            line += scaled(lineGap, scale);
        }


        if (settings.showDays()) {
            long time = client.world.getTimeOfDay();
            long days = time / 24000L;
            drawScaled(ctx, "Day: " + days, left, line, color, scale);
            line += scaled(lineGap, scale);
        }

        if (settings.showFPS()) {
            drawScaled(ctx, "FPS: " + fps, left, line, color, scale);
            line += scaled(lineGap, scale);
        }


        if (settings.showMemory()) {
            long currentTime = System.currentTimeMillis();


            if (currentTime - lastMemoryUpdateTime > MEMORY_UPDATE_INTERVAL) {
                updateMemoryText();
                lastMemoryUpdateTime = currentTime;
            }

            drawScaled(ctx, cachedMemoryText, left, line, color, scale);
        }
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

    private static void drawScaled(DrawContext ctx, String s, int x, int y, int argb, float scale) {
        ctx.getMatrices().pushMatrix();
        ctx.getMatrices().translate(x, y);
        ctx.getMatrices().scale(scale, scale);
        ctx.drawTextWithShadow(client.textRenderer, Text.literal(s), 0, 0, argb);
        ctx.getMatrices().popMatrix();
    }
}