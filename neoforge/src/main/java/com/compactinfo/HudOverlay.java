package com.compactinfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HudOverlay {
    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static boolean enabled = true;

    private static long lastMemoryUpdateTime = 0;
    private static final long MEMORY_UPDATE_INTERVAL = 50;
    private static String cachedMemoryText = "RAM: 0MB";

    private static long lastHostileUpdateTime = 0;
    private static final long HOSTILE_UPDATE_INTERVAL = 500;
    private static int cachedHostileCount = 0;

    private static final int SCREEN_PADDING = 3;

    public static void render(GuiGraphics guiGraphics) {
        if (!enabled || CLIENT.player == null || CLIENT.level == null || CLIENT.options.hideGui) {
            return;
        }

        HudSettings settings = HudSettings.getInstance();
        float scale = settings.getHudScale();
        int screenWidth = CLIENT.getWindow().getGuiScaledWidth();
        int screenHeight = CLIENT.getWindow().getGuiScaledHeight();

       
        int hudPosX = settings.getHudPosX(screenWidth);
        int hudPosY = settings.getHudPosY(screenHeight);

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

       
        int surfaceY = getSurfaceY(pos);
        float yaw = CLIENT.player.getYRot();
        float pitch = CLIENT.player.getXRot();
        String dir = getDirectionAbbreviation(yaw);

       
        int chunkX = (int) Math.floor(xi / 16.0);
        int chunkZ = (int) Math.floor(zi / 16.0);

       
        int opacityPercent = settings.getOpacity();
        int alpha;
        if (opacityPercent == 0) {
            alpha = 0;
        } else {
           
            alpha = Math.max(30, (int)(opacityPercent * 2.55f));
        }

       
        if (alpha == 0) {
            return;
        }

        int color = (alpha << 24) | 0x00FFFFFF;

        int effectiveMinX = SCREEN_PADDING;
        int effectiveMaxX = screenWidth - SCREEN_PADDING;
        int effectiveMinY = SCREEN_PADDING;
        int effectiveMaxY = screenHeight - SCREEN_PADDING;

        List<String> lines = new ArrayList<>();

       
        if (settings.showCoords()) {
            StringBuilder coordsLine = new StringBuilder();
            coordsLine.append((int) xi).append(" ").append((int) yi).append(" ").append((int) zi);

            if (settings.showFacing()) {
                coordsLine.append(" Dir: ").append(dir);
            }

            lines.add(coordsLine.toString());
        } else if (settings.showFacing()) {
            lines.add("Dir: " + dir);
        }

       
        if (settings.showYaw() || settings.showPitch()) {
            StringBuilder yawPitchLine = new StringBuilder();
            if (settings.showYaw()) {
                yawPitchLine.append("Yaw: ").append(String.format("%.1f°", yaw));
            }
            if (settings.showPitch()) {
                if (!yawPitchLine.isEmpty()) {
                    yawPitchLine.append(", ");
                }
                yawPitchLine.append("Pitch: ").append(String.format("%.1f°", pitch));
            }
            lines.add(yawPitchLine.toString());
        }

       
        if (settings.showSurfaceY()) {
            lines.add("Surf: " + surfaceY);
        }

       
        if (settings.showChunk()) {
            lines.add("Chunk: " + chunkX + " " + chunkZ);
        }

       
        if (settings.showHostile()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastHostileUpdateTime > HOSTILE_UPDATE_INTERVAL) {
                updateHostileCount();
                lastHostileUpdateTime = currentTime;
            }
            lines.add("Hos: " + cachedHostileCount);
        }

        if (settings.showConv()) {
            lines.add(conv);
        }

        if (settings.showBiome()) {
            lines.add("B: " + biome);
        }

       
        if (settings.showDays()) {
            long totalWorldTime = getTotalWorldTime();
            long days = totalWorldTime / 24000L;
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

       
        int layout = settings.getHudLayout();
        int zone;
        if (layout >= 0 && layout <= 5) {
            zone = layout + 1;
        } else {
            zone = determineZone(hudPosX, hudPosY, effectiveMinX, effectiveMaxX, effectiveMinY, effectiveMaxY);
        }

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

    private static void updateHostileCount() {
        if (CLIENT.player == null || CLIENT.level == null) {
            cachedHostileCount = 0;
            return;
        }

        AABB searchBox = new AABB(
                CLIENT.player.getX() - 16, CLIENT.player.getY() - 16, CLIENT.player.getZ() - 16,
                CLIENT.player.getX() + 16, CLIENT.player.getY() + 16, CLIENT.player.getZ() + 16
        );

        List<net.minecraft.world.entity.Entity> entities = CLIENT.level.getEntities(
                CLIENT.player,
                searchBox,
                entity -> entity instanceof Enemy
        );

        cachedHostileCount = entities.size();
    }

    private static int getSurfaceY(BlockPos playerPos) {
        return CLIENT.level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, playerPos.getX(), playerPos.getZ());
    }

    private static String getDirectionAbbreviation(float yaw) {
       
        float normalizedYaw = yaw % 360;
        if (normalizedYaw < 0) {
            normalizedYaw += 360;
        }

        if (normalizedYaw >= 337.5 || normalizedYaw < 22.5) {
            return "S";
        } else if (normalizedYaw >= 22.5 && normalizedYaw < 67.5) {
            return "SW";
        } else if (normalizedYaw >= 67.5 && normalizedYaw < 112.5) {
            return "W";
        } else if (normalizedYaw >= 112.5 && normalizedYaw < 157.5) {
            return "NW";
        } else if (normalizedYaw >= 157.5 && normalizedYaw < 202.5) {
            return "N";
        } else if (normalizedYaw >= 202.5 && normalizedYaw < 247.5) {
            return "NE";
        } else if (normalizedYaw >= 247.5 && normalizedYaw < 292.5) {
            return "E";
        } else {
            return "SE";
        }
    }

    private static long getTotalWorldTime() {
        try {
           
            if (CLIENT.level != null && CLIENT.level.getLevelData() != null) {
                return CLIENT.level.getLevelData().getGameTime();
            }

           
            if (CLIENT.level != null) {
                return CLIENT.level.getDayTime();
            }

            return 0;
        } catch (Exception e) {
            return CLIENT.level != null ? CLIENT.level.getDayTime() : 0;
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
       
        int alpha = (color >> 24) & 0xFF;

       
        if (alpha == 0) {
            return;
        }

       
       

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1.0f);
        guiGraphics.drawString(
                CLIENT.font,
                Component.literal(text),
                0,
                0,
                color,
                false 
        );
        guiGraphics.pose().popPose();
    }
}