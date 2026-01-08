package com.compactinfo;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class HudSettings {

    @SuppressWarnings("InstantiationOfUtilityClass")
    public static HudSettings getInstance() {
        return new HudSettings();
    }

   
    public boolean showCoords() {
        return HudConfigScreen.SHOW_COORDS.get();
    }

    public boolean showConv() {
        return HudConfigScreen.SHOW_CONV.get();
    }

    public boolean showBiome() {
        return HudConfigScreen.SHOW_BIOME.get();
    }

    public boolean showDays() {
        return HudConfigScreen.SHOW_DAYS.get();
    }

    public boolean showFPS() {
        return HudConfigScreen.SHOW_FPS.get();
    }

    public boolean showMemory() {
        return HudConfigScreen.SHOW_MEMORY.get();
    }

   
    public boolean showYaw() {
        return HudConfigScreen.SHOW_YAW.get();
    }

    public boolean showPitch() {
        return HudConfigScreen.SHOW_PITCH.get();
    }

    public boolean showFacing() {
        return HudConfigScreen.SHOW_FACING.get();
    }

    public boolean showSurfaceY() {
        return HudConfigScreen.SHOW_SURFACE_Y.get();
    }

    public boolean showChunk() {
        return HudConfigScreen.SHOW_CHUNK.get();
    }

    public boolean showHostile() {
        return HudConfigScreen.SHOW_HOSTILE.get();
    }

    public float getHudScale() {
        return HudConfigScreen.HUD_SCALE.get().floatValue();
    }

   
    public int getHudPosX(int screenWidth) {
       
        double relX = HudConfigScreen.HUD_REL_X.get();
        if (relX > 0.0) {
            int pos = (int)(relX * screenWidth);
            return Math.max(3, Math.min(screenWidth - 3, pos));
        }
       
        int pos = HudConfigScreen.HUD_POS_X.get();
        return Math.max(3, Math.min(screenWidth - 3, pos));
    }

    public int getHudPosY(int screenHeight) {
       
        double relY = HudConfigScreen.HUD_REL_Y.get();
        if (relY > 0.0) {
            int pos = (int)(relY * screenHeight);
            return Math.max(3, Math.min(screenHeight - 3, pos));
        }
       
        int pos = HudConfigScreen.HUD_POS_Y.get();
        return Math.max(3, Math.min(screenHeight - 3, pos));
    }

    public int getHudLayout() {
        return HudConfigScreen.HUD_LAYOUT.get();
    }

    public int getOpacity() {
        return HudConfigScreen.OPACITY.get();
    }

   
    public void setShowCoords(boolean value) {
        HudConfigScreen.SHOW_COORDS.set(value);
        save();
    }

    public void setShowConv(boolean value) {
        HudConfigScreen.SHOW_CONV.set(value);
        save();
    }

    public void setShowBiome(boolean value) {
        HudConfigScreen.SHOW_BIOME.set(value);
        save();
    }

    public void setShowDays(boolean value) {
        HudConfigScreen.SHOW_DAYS.set(value);
        save();
    }

    public void setShowFPS(boolean value) {
        HudConfigScreen.SHOW_FPS.set(value);
        save();
    }

    public void setShowMemory(boolean value) {
        HudConfigScreen.SHOW_MEMORY.set(value);
        save();
    }

   
    public void setShowYaw(boolean value) {
        HudConfigScreen.SHOW_YAW.set(value);
        save();
    }

    public void setShowPitch(boolean value) {
        HudConfigScreen.SHOW_PITCH.set(value);
        save();
    }

    public void setShowFacing(boolean value) {
        HudConfigScreen.SHOW_FACING.set(value);
        save();
    }

    public void setShowSurfaceY(boolean value) {
        HudConfigScreen.SHOW_SURFACE_Y.set(value);
        save();
    }

    public void setShowChunk(boolean value) {
        HudConfigScreen.SHOW_CHUNK.set(value);
        save();
    }

    public void setShowHostile(boolean value) {
        HudConfigScreen.SHOW_HOSTILE.set(value);
        save();
    }

    public void setHudScale(float value) {
        HudConfigScreen.HUD_SCALE.set((double) value);
        save();
    }

   
    public void setHudPosition(int x, int y, int screenWidth, int screenHeight) {
        if (screenWidth <= 0 || screenHeight <= 0) return;

       
        double relX = Math.max(0.0, Math.min(1.0, (double)x / screenWidth));
        double relY = Math.max(0.0, Math.min(1.0, (double)y / screenHeight));

        HudConfigScreen.HUD_REL_X.set(relX);
        HudConfigScreen.HUD_REL_Y.set(relY);

       
        HudConfigScreen.HUD_POS_X.set(x);
        HudConfigScreen.HUD_POS_Y.set(y);

        save();
    }

    public void setHudLayout(int layout) {
        HudConfigScreen.HUD_LAYOUT.set(Math.max(0, Math.min(6, layout)));
        save();
    }

    public void setOpacity(int opacity) {
        HudConfigScreen.OPACITY.set(Math.max(0, Math.min(100, opacity)));
        save();
    }

    public void save() {
        HudConfigScreen.SPEC.save();
    }

    @SuppressWarnings("resource")
    public static Screen createConfigScreen(Screen parent) {
        Minecraft client = Minecraft.getInstance();
        int screenWidth = client.getWindow().getGuiScaledWidth();
        int screenHeight = client.getWindow().getGuiScaledHeight();
        int padding = 3;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("screen.compactinfo.title"));

        builder.setSavingRunnable(HudConfigScreen.SPEC::save);

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("category.compactinfo"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

       
        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.coords"), HudConfigScreen.SHOW_COORDS.get())
                .setDefaultValue(true)
                .setSaveConsumer(HudConfigScreen.SHOW_COORDS::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.converted"), HudConfigScreen.SHOW_CONV.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_CONV::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.biome"), HudConfigScreen.SHOW_BIOME.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_BIOME::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.days"), HudConfigScreen.SHOW_DAYS.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_DAYS::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.fps"), HudConfigScreen.SHOW_FPS.get())
                .setDefaultValue(true)
                .setSaveConsumer(HudConfigScreen.SHOW_FPS::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.memory"), HudConfigScreen.SHOW_MEMORY.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_MEMORY::set)
                .build());

       
        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.yaw"), HudConfigScreen.SHOW_YAW.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_YAW::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.pitch"), HudConfigScreen.SHOW_PITCH.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_PITCH::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.facing"), HudConfigScreen.SHOW_FACING.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_FACING::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.surfacey"), HudConfigScreen.SHOW_SURFACE_Y.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_SURFACE_Y::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.chunk"), HudConfigScreen.SHOW_CHUNK.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_CHUNK::set)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("hud.option.hostile"), HudConfigScreen.SHOW_HOSTILE.get())
                .setDefaultValue(false)
                .setSaveConsumer(HudConfigScreen.SHOW_HOSTILE::set)
                .build());

       
        HudSettings settings = getInstance();
        int currentPosX = settings.getHudPosX(screenWidth);
        int currentPosY = settings.getHudPosY(screenHeight);

       
        general.addEntry(entryBuilder
                .startIntSlider(Component.translatable("hud.option.posX"), currentPosX, padding, screenWidth - padding)
                .setDefaultValue(padding)
                .setSaveConsumer(value -> {
                    Minecraft mc = Minecraft.getInstance();
                    int currentScreenWidth = mc.getWindow().getGuiScaledWidth();
                    int currentScreenHeight = mc.getWindow().getGuiScaledHeight();
                    HudSettings hudSettings = HudSettings.getInstance();
                    int currentY = hudSettings.getHudPosY(currentScreenHeight);
                    hudSettings.setHudPosition(value, currentY, currentScreenWidth, currentScreenHeight);
                })
                .setTextGetter(value -> Component.literal(value + "px"))
                .build());

       
        general.addEntry(entryBuilder
                .startIntSlider(Component.translatable("hud.option.posY"), currentPosY, padding, screenHeight - padding)
                .setDefaultValue(padding)
                .setSaveConsumer(value -> {
                    Minecraft mc = Minecraft.getInstance();
                    int currentScreenWidth = mc.getWindow().getGuiScaledWidth();
                    int currentScreenHeight = mc.getWindow().getGuiScaledHeight();
                    HudSettings hudSettings = HudSettings.getInstance();
                    int currentX = hudSettings.getHudPosX(currentScreenWidth);
                    hudSettings.setHudPosition(currentX, value, currentScreenWidth, currentScreenHeight);
                })
                .setTextGetter(value -> Component.literal(value + "px"))
                .build());

        general.addEntry(entryBuilder
                .startIntSlider(Component.translatable("hud.option.scale"), (int)(HudConfigScreen.HUD_SCALE.get() * 10), 5, 30)
                .setDefaultValue(10)
                .setSaveConsumer(value -> HudConfigScreen.HUD_SCALE.set(value / 10.0))
                .setTextGetter(value -> Component.literal(String.format("%.1fx", value / 10.0f)))
                .build());

       
        general.addEntry(entryBuilder
                .startIntSlider(Component.translatable("hud.option.layout"), HudConfigScreen.HUD_LAYOUT.get(), 0, 6)
                .setDefaultValue(6)
                .setSaveConsumer(HudConfigScreen.HUD_LAYOUT::set)
                .setTextGetter(value -> {
                    String[] layoutNames = {
                            Component.translatable("hud.layout.top_left").getString(),
                            Component.translatable("hud.layout.top_center").getString(),
                            Component.translatable("hud.layout.top_right").getString(),
                            Component.translatable("hud.layout.bottom_left").getString(),
                            Component.translatable("hud.layout.bottom_center").getString(),
                            Component.translatable("hud.layout.bottom_right").getString(),
                            Component.translatable("hud.layout.auto").getString()
                    };
                    return Component.literal(layoutNames[value]);
                })
                .build());

       
        general.addEntry(entryBuilder
                .startIntSlider(Component.translatable("hud.option.opacity"), HudConfigScreen.OPACITY.get(), 0, 100)
                .setDefaultValue(100)
                .setSaveConsumer(HudConfigScreen.OPACITY::set)
                .setTextGetter(value -> Component.literal(value + "%"))
                .build());

        return builder.build();
    }
}