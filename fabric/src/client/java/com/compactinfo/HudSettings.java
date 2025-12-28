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

    private static final int DEFAULT_PADDING = 3;
    private int hudPosX = DEFAULT_PADDING;
    private int hudPosY = DEFAULT_PADDING;

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

    public float getHudScale() { return hudScale; }
    public void setHudScale(float scale) {
        hudScale = Math.max(0.5f, Math.min(3.0f, scale));
    }

    public int getHudPosX() { return hudPosX; }
    public int getHudPosY() { return hudPosY; }

    public void setHudPosX(int x) {
        hudPosX = Math.max(DEFAULT_PADDING, Math.min(1000, x));
    }

    public void setHudPosY(int y) {
        hudPosY = Math.max(DEFAULT_PADDING, Math.min(1000, y));
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
            hudScale = Float.parseFloat(props.getProperty("hudScale", "1.0"));
            hudPosX = Integer.parseInt(props.getProperty("hudPosX", String.valueOf(DEFAULT_PADDING)));
            hudPosY = Integer.parseInt(props.getProperty("hudPosY", String.valueOf(DEFAULT_PADDING)));
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
            props.setProperty("hudScale", Float.toString(hudScale));
            props.setProperty("hudPosX", Integer.toString(hudPosX));
            props.setProperty("hudPosY", Integer.toString(hudPosY));
            props.store(out, "Compact Info HUD Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}