package com.compactinfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.WorldProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Environment(EnvType.CLIENT)
public class HudOverlay {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean enabled = true;

    private static long lastMemoryUpdateTime = 0;
    private static final long MEMORY_UPDATE_INTERVAL = 50;
    private static String cachedMemoryText = "RAM: 0MB";

    private static long lastHostileUpdateTime = 0;
    private static final long HOSTILE_UPDATE_INTERVAL = 500;
    private static int cachedHostileCount = 0;

    private static final int SCREEN_PADDING = 3;

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

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

       
        int hudPosX = settings.getHudPosX(screenWidth);
        int hudPosY = settings.getHudPosY(screenHeight);

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

       
        int surfaceY = getSurfaceY(client.player.getBlockPos());
        float yaw = client.player.getYaw();
        float pitch = client.player.getPitch();
        String dir = getDirectionAbbreviation(yaw);
        int chunkX = (int) Math.floor(xi / 16.0);
        int chunkZ = (int) Math.floor(zi / 16.0);

       
        int opacityPercent = settings.getOpacity();
        int alpha;
        if (opacityPercent == 0) {
            alpha = 0;
        } else {
            alpha = Math.max(30, (int) (opacityPercent * 2.55f));
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

       
        int zone;
        int layout = settings.getHudLayout();
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

            drawTextWithAutoAlignment(ctx, text, hudPosX, y, color, scale,
                    alignment, effectiveMinX, effectiveMaxX, effectiveMinY, effectiveMaxY);
        }
    }

    private static void updateHostileCount() {
        if (client.player == null || client.world == null) {
            cachedHostileCount = 0;
            return;
        }

        Box searchBox = new Box(
                client.player.getX() - 16, client.player.getY() - 16, client.player.getZ() - 16,
                client.player.getX() + 16, client.player.getY() + 16, client.player.getZ() + 16
        );

        List<Entity> entities = client.world.getOtherEntities(client.player, searchBox, entity ->
                entity instanceof HostileEntity
        );

        cachedHostileCount = entities.size();
    }

    private static int getSurfaceY(BlockPos playerPos) {
        BlockPos groundPos = client.world.getTopPosition(
                net.minecraft.world.Heightmap.Type.MOTION_BLOCKING,
                new BlockPos(playerPos.getX(), 0, playerPos.getZ())
        );
        return groundPos.getY();
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
            if (client.world != null) {
                WorldProperties worldProperties = client.world.getLevelProperties();
                if (worldProperties != null) {
                    return worldProperties.getTime();
                }
            }

            if (client.world != null) {
                return client.world.getTime();
            }

            return client.world != null ? client.world.getTimeOfDay() : 0;
        } catch (Exception e) {
            return client.world != null ? client.world.getTimeOfDay() : 0;
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
        return switch (zone) {
            case 1, 4 -> AlignmentType.LEFT;
            case 3, 6 -> AlignmentType.RIGHT;
            default -> AlignmentType.CENTER;
        };
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
            case CENTER -> x -= textWidth / 2;
            case RIGHT -> x -= textWidth;
            default -> {}
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