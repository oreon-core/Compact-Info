package com.compactinfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HudOverlay {
    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static boolean enabled = true;

    private static long lastMemoryUpdateTime = 0;
    private static final long MEMORY_UPDATE_INTERVAL = 50;
    private static String cachedMemoryText = "RAM: 0MB";

    private static final int SCREEN_PADDING = 3;

    public static void render(GuiGraphics guiGraphics) {
        if (!enabled || CLIENT.player == null || CLIENT.level == null || CLIENT.options.hideGui) {
            return;
        }

        float scale = HudConfigScreen.HUD_SCALE.get().floatValue();
        int hudPosX = HudConfigScreen.HUD_POS_X.get();
        int hudPosY = HudConfigScreen.HUD_POS_Y.get();

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

        int screenWidth = CLIENT.getWindow().getGuiScaledWidth();
        int screenHeight = CLIENT.getWindow().getGuiScaledHeight();
        int color = 0xFFFFFFFF;

        int effectiveMinX = SCREEN_PADDING;
        int effectiveMaxX = screenWidth - SCREEN_PADDING;
        int effectiveMinY = SCREEN_PADDING;
        int effectiveMaxY = screenHeight - SCREEN_PADDING;

        List<String> lines = new ArrayList<>();

        if (HudConfigScreen.SHOW_COORDS.get()) {
            lines.add((int) xi + " " + (int) yi + " " + (int) zi);
        }

        if (HudConfigScreen.SHOW_CONV.get()) {
            lines.add(conv);
        }

        if (HudConfigScreen.SHOW_BIOME.get()) {
            lines.add("B: " + biome);
        }

        if (HudConfigScreen.SHOW_DAYS.get()) {
            long time = CLIENT.level.getDayTime();
            long days = time / 24000L;
            lines.add("Day: " + days);
        }

        if (HudConfigScreen.SHOW_FPS.get()) {
            lines.add("FPS: " + fps);
        }

        if (HudConfigScreen.SHOW_MEMORY.get()) {
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

            drawTextWithAutoAlignment(guiGraphics, text, hudPosX, y, color, scale,
                    alignment, effectiveMinX, effectiveMaxX, effectiveMinY, effectiveMaxY);
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

    private static void drawTextWithAutoAlignment(GuiGraphics guiGraphics, String text, int baseX, int y,
                                                  int color, float scale, AlignmentType alignment,
                                                  int minX, int maxX, int minY, int maxY) {
        int textWidth = (int)(CLIENT.font.width(text) * scale);
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

        drawScaled(guiGraphics, text, x, y, color, scale);
    }

    private static void drawScaled(GuiGraphics guiGraphics, String text, int x, int y, int color, float scale) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1.0f);
        guiGraphics.drawString(
                CLIENT.font,
                Component.literal(text),
                0,
                0,
                color,
                true
        );
        guiGraphics.pose().popPose();
    }
}