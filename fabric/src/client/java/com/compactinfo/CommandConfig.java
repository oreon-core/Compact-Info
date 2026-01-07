package com.compactinfo;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CommandConfig implements Command<Object> {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var rootCommand = ClientCommandManager.literal("ci")
                    .executes(context -> {
                        MinecraftClient client = MinecraftClient.getInstance();
                        HudSettings settings = HudSettings.getInstance();
                        int screenWidth = client.getWindow().getScaledWidth();
                        int screenHeight = client.getWindow().getScaledHeight();

                        context.getSource().sendFeedback(Text.translatable("screen.compactinfo.title").formatted(Formatting.AQUA));
                       
                        context.getSource().sendFeedback(createSettingText("hud.option.coords", settings.showCoords()));
                        context.getSource().sendFeedback(createSettingText("hud.option.yaw", settings.showYaw()));
                        context.getSource().sendFeedback(createSettingText("hud.option.pitch", settings.showPitch()));
                        context.getSource().sendFeedback(createSettingText("hud.option.facing", settings.showFacing()));
                        context.getSource().sendFeedback(createSettingText("hud.option.surfacey", settings.showSurfaceY()));
                        context.getSource().sendFeedback(createSettingText("hud.option.chunk", settings.showChunk()));
                        context.getSource().sendFeedback(createSettingText("hud.option.hostile", settings.showHostile()));
                        context.getSource().sendFeedback(createSettingText("hud.option.converted", settings.showConv()));
                        context.getSource().sendFeedback(createSettingText("hud.option.biome", settings.showBiome()));
                        context.getSource().sendFeedback(createSettingText("hud.option.days", settings.showDays()));
                        context.getSource().sendFeedback(createSettingText("hud.option.fps", settings.showFPS()));
                        context.getSource().sendFeedback(createSettingText("hud.option.memory", settings.showMemory()));

                        context.getSource().sendFeedback(Text.literal(
                                Text.translatable("hud.option.posX").getString() +
                                        ": " + Formatting.YELLOW + settings.getHudPosX(screenWidth)
                        ));
                        context.getSource().sendFeedback(Text.literal(
                                Text.translatable("hud.option.posY").getString() +
                                        ": " + Formatting.YELLOW + settings.getHudPosY(screenHeight)
                        ));
                        context.getSource().sendFeedback(Text.literal(
                                Text.translatable("hud.option.scale").getString() +
                                        ": " + formatScale(settings.getHudScale())
                        ));
                        context.getSource().sendFeedback(getLayoutText(settings.getHudLayout()));
                        context.getSource().sendFeedback(Text.literal(
                                Text.translatable("hud.option.opacity").getString() +
                                        ": " + Formatting.YELLOW + settings.getOpacity() + "%"
                        ));
                        return 1;
                    })
                   
                    .then(ClientCommandManager.literal("coords")
                            .then(ClientCommandManager.argument("значення", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "значення");
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
                   
                    .then(ClientCommandManager.literal("yaw")
                            .then(ClientCommandManager.argument("значення", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "значення");
                                        HudSettings.getInstance().setShowYaw(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(createSimpleToggleText("hud.option.yaw", value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                boolean current = HudSettings.getInstance().showYaw();
                                context.getSource().sendFeedback(createSettingText("hud.option.yaw", current));
                                return 1;
                            })
                    )
                   
                    .then(ClientCommandManager.literal("pitch")
                            .then(ClientCommandManager.argument("значення", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "значення");
                                        HudSettings.getInstance().setShowPitch(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(createSimpleToggleText("hud.option.pitch", value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                boolean current = HudSettings.getInstance().showPitch();
                                context.getSource().sendFeedback(createSettingText("hud.option.pitch", current));
                                return 1;
                            })
                    )
                   
                    .then(ClientCommandManager.literal("facing")
                            .then(ClientCommandManager.argument("значення", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "значення");
                                        HudSettings.getInstance().setShowFacing(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(createSimpleToggleText("hud.option.facing", value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                boolean current = HudSettings.getInstance().showFacing();
                                context.getSource().sendFeedback(createSettingText("hud.option.facing", current));
                                return 1;
                            })
                    )
                   
                    .then(ClientCommandManager.literal("surfacey")
                            .then(ClientCommandManager.argument("значення", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "значення");
                                        HudSettings.getInstance().setShowSurfaceY(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(createSimpleToggleText("hud.option.surfacey", value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                boolean current = HudSettings.getInstance().showSurfaceY();
                                context.getSource().sendFeedback(createSettingText("hud.option.surfacey", current));
                                return 1;
                            })
                    )
                   
                    .then(ClientCommandManager.literal("chunk")
                            .then(ClientCommandManager.argument("значення", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "значення");
                                        HudSettings.getInstance().setShowChunk(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(createSimpleToggleText("hud.option.chunk", value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                boolean current = HudSettings.getInstance().showChunk();
                                context.getSource().sendFeedback(createSettingText("hud.option.chunk", current));
                                return 1;
                            })
                    )
                   
                    .then(ClientCommandManager.literal("hostile")
                            .then(ClientCommandManager.argument("значення", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "значення");
                                        HudSettings.getInstance().setShowHostile(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(createSimpleToggleText("hud.option.hostile", value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                boolean current = HudSettings.getInstance().showHostile();
                                context.getSource().sendFeedback(createSettingText("hud.option.hostile", current));
                                return 1;
                            })
                    )
                   
                    .then(ClientCommandManager.literal("conv")
                            .then(ClientCommandManager.argument("значення", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "значення");
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
                            .then(ClientCommandManager.argument("значення", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "значення");
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
                            .then(ClientCommandManager.argument("значення", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "значення");
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
                            .then(ClientCommandManager.argument("значення", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "значення");
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
                            .then(ClientCommandManager.argument("значення", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "значення");
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
                            .then(ClientCommandManager.argument("значення", IntegerArgumentType.integer(0, 1000))
                                    .executes(context -> {
                                        MinecraftClient client = MinecraftClient.getInstance();
                                        int value = IntegerArgumentType.getInteger(context, "значення");
                                        int screenWidth = client.getWindow().getScaledWidth();
                                        int screenHeight = client.getWindow().getScaledHeight();
                                        HudSettings settings = HudSettings.getInstance();

                                        int currentY = settings.getHudPosY(screenHeight);
                                        settings.setHudPosition(value, currentY, screenWidth, screenHeight);
                                        settings.save();
                                        context.getSource().sendFeedback(Text.literal(
                                                Text.translatable("hud.option.posX").getString() +
                                                        ": " + Formatting.YELLOW + value
                                        ));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                MinecraftClient client = MinecraftClient.getInstance();
                                HudSettings settings = HudSettings.getInstance();
                                int screenWidth = client.getWindow().getScaledWidth();
                                int current = settings.getHudPosX(screenWidth);
                                context.getSource().sendFeedback(Text.literal(
                                        Text.translatable("hud.option.posX").getString() +
                                                ": " + Formatting.YELLOW + current
                                ));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("posY")
                            .then(ClientCommandManager.argument("значення", IntegerArgumentType.integer(0, 1000))
                                    .executes(context -> {
                                        MinecraftClient client = MinecraftClient.getInstance();
                                        int value = IntegerArgumentType.getInteger(context, "значення");
                                        int screenWidth = client.getWindow().getScaledWidth();
                                        int screenHeight = client.getWindow().getScaledHeight();
                                        HudSettings settings = HudSettings.getInstance();

                                        int currentX = settings.getHudPosX(screenWidth);
                                        settings.setHudPosition(currentX, value, screenWidth, screenHeight);
                                        settings.save();
                                        context.getSource().sendFeedback(Text.literal(
                                                Text.translatable("hud.option.posY").getString() +
                                                        ": " + Formatting.YELLOW + value
                                        ));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                MinecraftClient client = MinecraftClient.getInstance();
                                HudSettings settings = HudSettings.getInstance();
                                int screenHeight = client.getWindow().getScaledHeight();
                                int current = settings.getHudPosY(screenHeight);
                                context.getSource().sendFeedback(Text.literal(
                                        Text.translatable("hud.option.posY").getString() +
                                                ": " + Formatting.YELLOW + current
                                ));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("scale")
                            .then(ClientCommandManager.argument("значення", FloatArgumentType.floatArg(0.5f, 3.0f))
                                    .executes(context -> {
                                        float value = FloatArgumentType.getFloat(context, "значення");
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
                    .then(ClientCommandManager.literal("layout")
                            .then(ClientCommandManager.argument("значення", IntegerArgumentType.integer(0, 6))
                                    .executes(context -> {
                                        int value = IntegerArgumentType.getInteger(context, "значення");
                                        HudSettings.getInstance().setHudLayout(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(getLayoutText(value));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                int current = HudSettings.getInstance().getHudLayout();
                                context.getSource().sendFeedback(getLayoutText(current));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("opacity")
                            .then(ClientCommandManager.argument("значення", IntegerArgumentType.integer(0, 100))
                                    .executes(context -> {
                                        int value = IntegerArgumentType.getInteger(context, "значення");
                                        HudSettings.getInstance().setOpacity(value);
                                        HudSettings.getInstance().save();
                                        context.getSource().sendFeedback(Text.literal(
                                                Text.translatable("hud.option.opacity").getString() +
                                                        ": " + Formatting.YELLOW + value + "%"
                                        ));
                                        return 1;
                                    })
                            )
                            .executes(context -> {
                                int current = HudSettings.getInstance().getOpacity();
                                context.getSource().sendFeedback(Text.literal(
                                        Text.translatable("hud.option.opacity").getString() +
                                                ": " + Formatting.YELLOW + current + "%"
                                ));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("reset")
                            .executes(context -> {
                                MinecraftClient client = MinecraftClient.getInstance();
                                HudSettings settings = HudSettings.getInstance();
                                int screenWidth = client.getWindow().getScaledWidth();
                                int screenHeight = client.getWindow().getScaledHeight();

                               
                                settings.setShowCoords(true);
                                settings.setShowYaw(false);
                                settings.setShowPitch(false);
                                settings.setShowFacing(false);
                                settings.setShowSurfaceY(false);
                                settings.setShowChunk(false);
                                settings.setShowHostile(false);
                                settings.setShowConv(false);
                                settings.setShowBiome(false);
                                settings.setShowDays(false);
                                settings.setShowFPS(true);
                                settings.setShowMemory(false);

                                settings.setHudScale(1.0f);
                                settings.setHudPosition(3, 3, screenWidth, screenHeight);
                                settings.setHudLayout(6);
                                settings.setOpacity(100);
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

    private static Text getLayoutText(int layout) {
        String[] layoutNames = {
                Text.translatable("hud.layout.top_left").getString(),
                Text.translatable("hud.layout.top_center").getString(),
                Text.translatable("hud.layout.top_right").getString(),
                Text.translatable("hud.layout.bottom_left").getString(),
                Text.translatable("hud.layout.bottom_center").getString(),
                Text.translatable("hud.layout.bottom_right").getString(),
                Text.translatable("hud.layout.auto").getString() + " " + Formatting.GRAY
        };
        return Text.literal(
                Text.translatable("hud.option.layout").getString() +
                        ": " + Formatting.YELLOW + layoutNames[layout]
        );
    }

    @Override
    public int run(CommandContext<Object> context) {
        return 0;
    }
}