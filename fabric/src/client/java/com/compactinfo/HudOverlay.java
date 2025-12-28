package com.compactinfo;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HudOverlay {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean enabled = true;

    private static long lastMemoryUpdateTime = 0;
    private static final long MEMORY_UPDATE_INTERVAL = 50;
    private static String cachedMemoryText = "RAM: 0MB";


    private static final int SCREEN_PADDING = 3;
    private static final int DEFAULT_POS_X = SCREEN_PADDING;
    private static final int DEFAULT_POS_Y = SCREEN_PADDING;

    @SuppressWarnings("deprecation")
    public static void register() {
        HudRenderCallback.EVENT.register(HudOverlay::render);
    }

    public static void toggle() {
        enabled = !enabled;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    private static void render(DrawContext ctx, RenderTickCounter tickCounter) {
        if (!enabled || client.player == null || client.world == null || client.options.hudHidden) return;

        HudSettings settings = HudSettings.getInstance();
        float scale = settings.getHudScale();


        int hudPosX = settings.getHudPosX();
        int hudPosY = settings.getHudPosY();

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

        int color = 0xFFFFFFFF;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();


        int effectiveMinX = SCREEN_PADDING;
        int effectiveMaxX = screenWidth - SCREEN_PADDING;
        int effectiveMinY = SCREEN_PADDING;
        int effectiveMaxY = screenHeight - SCREEN_PADDING;

        List<String> lines = new ArrayList<>();

        if (settings.showCoords()) {
            lines.add((int) xi + " " + (int) yi + " " + (int) zi);
        }

        if (settings.showConv()) {
            lines.add(conv);
        }

        if (settings.showBiome()) {
            lines.add("B: " + biome);
        }

        if (settings.showDays()) {
            long time = client.world.getTimeOfDay();
            long days = time / 24000L;
            lines.add("Day: " + days);
        }

        if (settings.showFPS()) {
            lines.add("FPS: " + fps);
        }

        if (settings.showMemory()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastMemoryUpdateTime > MEMORY_UPDATE_INTERVAL) {
                updateMemoryText();
                lastMemoryUpdateTime = currentTime;
            }
            lines.add(cachedMemoryText);
        }

        if (lines.isEmpty()) return;


        int zone = determineZone(hudPosX, hudPosY, effectiveMinX, effectiveMaxX, effectiveMinY, effectiveMaxY);


        AlignmentType alignment = getAlignmentForZone(zone);
        boolean reverseOrder = isReversedOrderForZone(zone);


        int lineHeight = (int)(12 * scale);
        int totalHeight = lines.size() * lineHeight;


        int adjustedY = adjustVerticalPosition(hudPosY, totalHeight, reverseOrder, effectiveMinY, effectiveMaxY, lineHeight);


        List<String> linesToDraw = new ArrayList<>(lines);
        if (reverseOrder) {
            Collections.reverse(linesToDraw);
        }


        for (int i = 0; i < linesToDraw.size(); i++) {
            String text = linesToDraw.get(i);


            int y;
            if (reverseOrder) {

                y = adjustedY - (linesToDraw.size() - i - 1) * lineHeight;
            } else {

                y = adjustedY + i * lineHeight;
            }

            drawTextWithAutoAlignment(ctx, text, hudPosX, y, color, scale,
                    alignment, effectiveMinX, effectiveMaxX, effectiveMinY, effectiveMaxY);
        }
    }

    private static void updateMemoryText() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        cachedMemoryText = String.format("RAM: %dMB", usedMemory / 1024 / 1024);
    }

    private enum AlignmentType {
        LEFT,
        CENTER,
        RIGHT
    }

    private static int determineZone(int x, int y, int minX, int maxX, int minY, int maxY) {

        int effectiveWidth = maxX - minX;
        int effectiveHeight = maxY - minY;


        int thirdWidth = effectiveWidth / 3;
        int halfHeight = effectiveHeight / 2;


        int normalizedX = x - minX;
        int normalizedY = y - minY;

        int zone = 0;


        if (normalizedX < thirdWidth) {
            zone = 1;
        } else if (normalizedX < 2 * thirdWidth) {
            zone = 2;
        } else {
            zone = 3;
        }


        if (normalizedY >= halfHeight) {
            zone += 3;
        }

        return zone;
    }

    private static AlignmentType getAlignmentForZone(int zone) {
        switch (zone) {
            case 1:
            case 4:
                return AlignmentType.LEFT;
            case 3:
            case 6:
                return AlignmentType.RIGHT;
            case 2:
            case 5:
            default:
                return AlignmentType.CENTER;
        }
    }

    private static boolean isReversedOrderForZone(int zone) {

        return zone >= 4;
    }

    private static int adjustVerticalPosition(int baseY, int totalHeight, boolean reverseOrder,
                                              int minY, int maxY, int lineHeight) {
        int adjustedY = baseY;

        if (reverseOrder) {

            int topMostLine = baseY - totalHeight;
            if (topMostLine < minY) {
                adjustedY = minY + totalHeight;
            }


            if (baseY > maxY - lineHeight) {
                adjustedY = maxY - lineHeight;
            }
        } else {

            int bottomMostLine = baseY + totalHeight;
            if (bottomMostLine > maxY) {
                adjustedY = maxY - totalHeight;
            }


            if (baseY < minY) {
                adjustedY = minY;
            }
        }

        return adjustedY;
    }

    private static void drawTextWithAutoAlignment(DrawContext ctx, String text, int baseX, int y,
                                                  int color, float scale, AlignmentType alignment,
                                                  int minX, int maxX, int minY, int maxY) {

        int textWidth = (int)(client.textRenderer.getWidth(text) * scale);
        int lineHeight = (int)(12 * scale);

        int x = baseX;


        switch (alignment) {
            case CENTER:
                x -= textWidth / 2;
                break;
            case RIGHT:
                x -= textWidth;
                break;
            case LEFT:
            default:

                break;
        }


        if (x < minX) {
            x = minX;
        } else if (x + textWidth > maxX) {
            x = maxX - textWidth;
        }


        if (y < minY) {
            y = minY;
        } else if (y + lineHeight > maxY) {
            y = maxY - lineHeight;
        }

        drawScaled(ctx, text, x, y, color, scale);
    }

    private static void drawScaled(DrawContext ctx, String s, int x, int y, int argb, float scale) {
        ctx.getMatrices().pushMatrix();
        ctx.getMatrices().translate(x, y);
        ctx.getMatrices().scale(scale, scale);
        ctx.drawTextWithShadow(client.textRenderer, Text.literal(s), 0, 0, argb);
        ctx.getMatrices().popMatrix();
    }
}