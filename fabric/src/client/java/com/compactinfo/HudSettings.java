package com.compactinfo;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.Properties;

public class HudSettings {
    private static HudSettings instance;

    private boolean showCoords = true;
    private boolean showConv = true;
    private boolean showBiome = true;
    private boolean showFPS = true;

    private final File configFile;

    private HudSettings() {
        configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "compactinfo-hud.properties");
        load();
    }
    private float hudScale = 1.0f;

    public float getHudScale() { return hudScale; }
    public void setHudScale(float scale) {
        hudScale = Math.max(0.5f, Math.min(3.0f, scale));
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

    public void load() {
        if (!configFile.exists()) return;
        try (InputStream in = new FileInputStream(configFile)) {
            Properties props = new Properties();
            props.load(in);
            showCoords = Boolean.parseBoolean(props.getProperty("showCoords", "true"));
            showConv = Boolean.parseBoolean(props.getProperty("showConv", "true"));
            showBiome = Boolean.parseBoolean(props.getProperty("showBiome", "true"));
            showFPS = Boolean.parseBoolean(props.getProperty("showFPS", "true"));
            hudScale = Float.parseFloat(props.getProperty("hudScale", "1.0"));
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
            props.setProperty("hudScale", Float.toString(hudScale));
            props.store(out, "Compact Info HUD Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
