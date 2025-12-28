package com.compactinfo;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CommandConfig implements Command<Object> {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var rootCommand = ClientCommandManager.literal("ci")
                    .executes(context -> {
                        HudSettings settings = HudSettings.getInstance();
                        context.getSource().sendFeedback(Text.translatable("screen.compactinfo.title").formatted(Formatting.AQUA));
                        context.getSource().sendFeedback(createSettingText("hud.option.coords", settings.showCoords()));
                        context.getSource().sendFeedback(createSettingText("hud.option.converted", settings.showConv()));
                        context.getSource().sendFeedback(createSettingText("hud.option.biome", settings.showBiome()));
                        context.getSource().sendFeedback(createSettingText("hud.option.days", settings.showDays()));
                        context.getSource().sendFeedback(createSettingText("hud.option.fps", settings.showFPS()));
                        context.getSource().sendFeedback(createSettingText("hud.option.memory", settings.showMemory()));
                        context.getSource().sendFeedback(Text.literal(
                                Text.translatable("hud.option.posX").getString() +
                                        ": " + Formatting.YELLOW + settings.getHudPosX()
                        ));
                        context.getSource().sendFeedback(Text.literal(
                                Text.translatable("hud.option.posY").getString() +
                                        ": " + Formatting.YELLOW + settings.getHudPosY()
                        ));
                        context.getSource().sendFeedback(Text.literal(
                                Text.translatable("hud.option.scale").getString() +
                                        ": " + formatScale(settings.getHudScale())
                        ));
                        return 1;
                    })
                    .then(ClientCommandManager.literal("coords")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "value");
                                        HudSettings.getInstance().setShowCoords(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(createSimpleToggleText("hud.option.coords", value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                boolean current = HudSettings.getInstance().showCoords();
                                context.getSource().sendFeedback(createSettingText("hud.option.coords", current));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("conv")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "value");
                                        HudSettings.getInstance().setShowConv(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(createSimpleToggleText("hud.option.converted", value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                boolean current = HudSettings.getInstance().showConv();
                                context.getSource().sendFeedback(createSettingText("hud.option.converted", current));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("biome")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "value");
                                        HudSettings.getInstance().setShowBiome(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(createSimpleToggleText("hud.option.biome", value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                boolean current = HudSettings.getInstance().showBiome();
                                context.getSource().sendFeedback(createSettingText("hud.option.biome", current));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("days")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "value");
                                        HudSettings.getInstance().setShowDays(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(createSimpleToggleText("hud.option.days", value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                boolean current = HudSettings.getInstance().showDays();
                                context.getSource().sendFeedback(createSettingText("hud.option.days", current));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("fps")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "value");
                                        HudSettings.getInstance().setShowFPS(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(createSimpleToggleText("hud.option.fps", value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                boolean current = HudSettings.getInstance().showFPS();
                                context.getSource().sendFeedback(createSettingText("hud.option.fps", current));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("memory")
                            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "value");
                                        HudSettings.getInstance().setShowMemory(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(createSimpleToggleText("hud.option.memory", value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                boolean current = HudSettings.getInstance().showMemory();
                                context.getSource().sendFeedback(createSettingText("hud.option.memory", current));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("posX")
                            .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(3, 1000))
                                    .executes(context -> {
                                        int value = IntegerArgumentType.getInteger(context, "value");
                                        HudSettings.getInstance().setHudPosX(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(Text.literal(
                                                Text.translatable("hud.option.posX").getString() +
                                                        ": " + Formatting.YELLOW + value
                                        ));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                int current = HudSettings.getInstance().getHudPosX();
                                context.getSource().sendFeedback(Text.literal(
                                        Text.translatable("hud.option.posX").getString() +
                                                ": " + Formatting.YELLOW + current
                                ));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("posY")
                            .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(3, 1000))
                                    .executes(context -> {
                                        int value = IntegerArgumentType.getInteger(context, "value");
                                        HudSettings.getInstance().setHudPosY(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(Text.literal(
                                                Text.translatable("hud.option.posY").getString() +
                                                        ": " + Formatting.YELLOW + value
                                        ));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                int current = HudSettings.getInstance().getHudPosY();
                                context.getSource().sendFeedback(Text.literal(
                                        Text.translatable("hud.option.posY").getString() +
                                                ": " + Formatting.YELLOW + current
                                ));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("scale")
                            .then(ClientCommandManager.argument("value", FloatArgumentType.floatArg(0.5f, 3.0f))
                                    .executes(context -> {
                                        float value = FloatArgumentType.getFloat(context, "value");
                                        HudSettings.getInstance().setHudScale(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(Text.literal(
                                                Text.translatable("hud.option.scale").getString() +
                                                        ": " + formatScale(value)
                                        ));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                float current = HudSettings.getInstance().getHudScale();
                                context.getSource().sendFeedback(Text.literal(
                                        Text.translatable("hud.option.scale").getString() +
                                                ": " + formatScale(current)
                                ));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("reset")
                            .executes(context -> {
                                HudSettings settings = HudSettings.getInstance();
                                settings.setShowCoords(true);
                                settings.setShowConv(false);
                                settings.setShowBiome(false);
                                settings.setShowFPS(true);
                                settings.setShowDays(false);
                                settings.setShowMemory(false);
                                settings.setHudScale(1.0f);
                                settings.setHudPosX(3);
                                settings.setHudPosY(3);
                                settings.save();
                                context.getSource().sendFeedback(Text.translatable("command.compactinfo.reset").formatted(Formatting.GREEN));
                                return 1;
                            })
                    );

            dispatcher.register(rootCommand);
        });
    }

    private static String getStatusText(boolean value) {
        String statusKey = value ? "status.compactinfo.enabled" : "status.compactinfo.disabled";
        Formatting color = value ? Formatting.GREEN : Formatting.RED;
        Text statusText = Text.translatable(statusKey).formatted(color);
        return statusText.getString();
    }

    private static Text createSettingText(String translationKey, boolean value) {
        return Text.literal(
                Text.translatable(translationKey).getString() +
                        ": " + getStatusText(value)
        );
    }

    private static Text createSimpleToggleText(String translationKey, boolean value) {
        return Text.literal(
                Text.translatable(translationKey).getString() +
                        " " + getStatusText(value)
        );
    }

    private static String formatScale(float scale) {
        return Formatting.YELLOW + String.format("%.1fx", scale);
    }

    @Override
    public int run(CommandContext<Object> context) {
        return 0;
    }
}