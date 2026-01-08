package com.compactinfo;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.Properties;

public class HudSettings {
    private static HudSettings instance;

    private boolean showCoords = true;
    private boolean showConv = false;
    private boolean showBiome = false;
    private boolean showFPS = true;
    private boolean showDays = false;
    private boolean showMemory = false;
    private float hudScale = 1.0f;
    private int hudLayout = 6;
    private int opacity = 100;

   
    private boolean showSurfaceY = false;
    private boolean showYaw = false;
    private boolean showPitch = false;
    private boolean showFacing = false;
    private boolean showChunk = false;
    private boolean showHostile = false;

   
    private float hudRelX = 0.0f;
    private float hudRelY = 0.0f;
    private int hudPosX = 3;
    private int hudPosY = 3;
    private boolean useLegacyPosition = true;

    private final File configFile;

    private HudSettings() {
        configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "compactinfo-hud.properties");
        load();
    }

    public static HudSettings getInstance() {
        if (instance == null) {
            instance = new HudSettings();
        }
        return instance;
    }

    public boolean showCoords() { return showCoords; }
    public void setShowCoords(boolean value) { showCoords = value; }

    public boolean showConv() { return showConv; }
    public void setShowConv(boolean value) { showConv = value; }

    public boolean showBiome() { return showBiome; }
    public void setShowBiome(boolean value) { showBiome = value; }

    public boolean showFPS() { return showFPS; }
    public void setShowFPS(boolean value) { showFPS = value; }

    public boolean showDays() { return showDays; }
    public void setShowDays(boolean value) { showDays = value; }

    public boolean showMemory() { return showMemory; }
    public void setShowMemory(boolean value) { showMemory = value; }

   
    public boolean showSurfaceY() { return showSurfaceY; }
    public void setShowSurfaceY(boolean value) { showSurfaceY = value; }

    public boolean showYaw() { return showYaw; }
    public void setShowYaw(boolean value) { showYaw = value; }

    public boolean showPitch() { return showPitch; }
    public void setShowPitch(boolean value) { showPitch = value; }

    public boolean showFacing() { return showFacing; }
    public void setShowFacing(boolean value) { showFacing = value; }

    public boolean showChunk() { return showChunk; }
    public void setShowChunk(boolean value) { showChunk = value; }

    public boolean showHostile() { return showHostile; }
    public void setShowHostile(boolean value) { showHostile = value; }

    public float getHudScale() { return hudScale; }
    public void setHudScale(float scale) {
        hudScale = Math.max(0.5f, Math.min(3.0f, scale));
    }

   
    public int getHudPosX(int screenWidth) {
        if (useLegacyPosition) {
            return Math.max(3, Math.min(screenWidth - 3, hudPosX));
        }
        return Math.max(3, Math.min(screenWidth - 3, (int)(hudRelX * screenWidth)));
    }

    public int getHudPosY(int screenHeight) {
        if (useLegacyPosition) {
            return Math.max(3, Math.min(screenHeight - 3, hudPosY));
        }
        return Math.max(3, Math.min(screenHeight - 3, (int)(hudRelY * screenHeight)));
    }

   
    public void setHudPosition(int x, int y, int screenWidth, int screenHeight) {
        if (screenWidth <= 0 || screenHeight <= 0) return;

        hudRelX = Math.max(0.0f, Math.min(1.0f, (float)x / screenWidth));
        hudRelY = Math.max(0.0f, Math.min(1.0f, (float)y / screenHeight));
        useLegacyPosition = false;
    }

    public int getHudLayout() { return hudLayout; }
    public void setHudLayout(int layout) {
        hudLayout = Math.max(0, Math.min(6, layout));
    }

    public int getOpacity() { return opacity; }
    public void setOpacity(int opacity) {
        this.opacity = Math.max(0, Math.min(100, opacity));
    }

    public void load() {
        if (!configFile.exists()) return;
        try (InputStream in = new FileInputStream(configFile)) {
            Properties props = new Properties();
            props.load(in);
            showCoords = Boolean.parseBoolean(props.getProperty("showCoords", "true"));
            showConv = Boolean.parseBoolean(props.getProperty("showConv", "false"));
            showBiome = Boolean.parseBoolean(props.getProperty("showBiome", "false"));
            showFPS = Boolean.parseBoolean(props.getProperty("showFPS", "true"));
            showDays = Boolean.parseBoolean(props.getProperty("showDays", "false"));
            showMemory = Boolean.parseBoolean(props.getProperty("showMemory", "false"));

           
            showSurfaceY = Boolean.parseBoolean(props.getProperty("showSurfaceY", "false"));
            showYaw = Boolean.parseBoolean(props.getProperty("showYaw", "false"));
            showPitch = Boolean.parseBoolean(props.getProperty("showPitch", "false"));
            showFacing = Boolean.parseBoolean(props.getProperty("showFacing", "false"));
            showChunk = Boolean.parseBoolean(props.getProperty("showChunk", "false"));
            showHostile = Boolean.parseBoolean(props.getProperty("showHostile", "false"));

            hudScale = Float.parseFloat(props.getProperty("hudScale", "1.0"));
            hudPosX = Integer.parseInt(props.getProperty("hudPosX", "3"));
            hudPosY = Integer.parseInt(props.getProperty("hudPosY", "3"));

           
            String relXStr = props.getProperty("hudRelX");
            String relYStr = props.getProperty("hudRelY");
            if (relXStr != null && relYStr != null) {
                hudRelX = Float.parseFloat(relXStr);
                hudRelY = Float.parseFloat(relYStr);
                useLegacyPosition = false;
            } else {
                useLegacyPosition = true;
            }

            hudLayout = Integer.parseInt(props.getProperty("hudLayout", "6"));
            opacity = Integer.parseInt(props.getProperty("opacity", "100"));
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (OutputStream out = new FileOutputStream(configFile)) {
            Properties props = new Properties();
            props.setProperty("showCoords", Boolean.toString(showCoords));
            props.setProperty("showConv", Boolean.toString(showConv));
            props.setProperty("showBiome", Boolean.toString(showBiome));
            props.setProperty("showFPS", Boolean.toString(showFPS));
            props.setProperty("showDays", Boolean.toString(showDays));
            props.setProperty("showMemory", Boolean.toString(showMemory));

           
            props.setProperty("showSurfaceY", Boolean.toString(showSurfaceY));
            props.setProperty("showYaw", Boolean.toString(showYaw));
            props.setProperty("showPitch", Boolean.toString(showPitch));
            props.setProperty("showFacing", Boolean.toString(showFacing));
            props.setProperty("showChunk", Boolean.toString(showChunk));
            props.setProperty("showHostile", Boolean.toString(showHostile));

            props.setProperty("hudScale", Float.toString(hudScale));
            props.setProperty("hudPosX", Integer.toString(hudPosX));
            props.setProperty("hudPosY", Integer.toString(hudPosY));
            props.setProperty("hudRelX", Float.toString(hudRelX));
            props.setProperty("hudRelY", Float.toString(hudRelY));
            props.setProperty("hudLayout", Integer.toString(hudLayout));
            props.setProperty("opacity", Integer.toString(opacity));
            props.store(out, "Compact Info HUD Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}