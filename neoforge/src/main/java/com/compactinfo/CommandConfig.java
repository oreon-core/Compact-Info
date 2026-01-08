package com.compactinfo;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(modid = CompactInfo.MOD_ID, value = Dist.CLIENT)
public class CommandConfig {

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent event) {
        var dispatcher = event.getDispatcher();

        var rootCommand = Commands.literal("ci")
                .executes(context -> {
                    HudSettings settings = HudSettings.getInstance();
                    Minecraft client = Minecraft.getInstance();
                    int screenWidth = client.getWindow().getGuiScaledWidth();
                    int screenHeight = client.getWindow().getGuiScaledHeight();

                    context.getSource().sendSuccess(
                            () -> Component.translatable("screen.compactinfo.title").withStyle(ChatFormatting.AQUA),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.coords", settings.showCoords()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.converted", settings.showConv()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.biome", settings.showBiome()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.days", settings.showDays()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.fps", settings.showFPS()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.memory", settings.showMemory()),
                            false
                    );
                   
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.yaw", settings.showYaw()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.pitch", settings.showPitch()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.facing", settings.showFacing()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.surfacey", settings.showSurfaceY()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.chunk", settings.showChunk()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.hostile", settings.showHostile()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> Component.literal(
                                    Component.translatable("hud.option.posX").getString() +
                                            ": " + ChatFormatting.YELLOW + settings.getHudPosX(screenWidth)
                            ),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> Component.literal(
                                    Component.translatable("hud.option.posY").getString() +
                                            ": " + ChatFormatting.YELLOW + settings.getHudPosY(screenHeight)
                            ),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> Component.literal(
                                    Component.translatable("hud.option.scale").getString() +
                                            ": " + formatScale(settings.getHudScale())
                            ),
                            false
                    );
                   
                    context.getSource().sendSuccess(
                            () -> getLayoutText(settings.getHudLayout()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> Component.literal(
                                    Component.translatable("hud.option.opacity").getString() +
                                            ": " + ChatFormatting.YELLOW + settings.getOpacity() + "%"
                            ),
                            false
                    );
                    return 1;
                })

                .then(Commands.literal("coords")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudSettings.getInstance().setShowCoords(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.coords", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudSettings.getInstance().showCoords();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.coords", current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("conv")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudSettings.getInstance().setShowConv(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.converted", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudSettings.getInstance().showConv();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.converted", current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("biome")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudSettings.getInstance().setShowBiome(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.biome", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudSettings.getInstance().showBiome();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.biome", current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("days")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudSettings.getInstance().setShowDays(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.days", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudSettings.getInstance().showDays();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.days", current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("fps")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudSettings.getInstance().setShowFPS(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.fps", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudSettings.getInstance().showFPS();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.fps", current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("memory")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudSettings.getInstance().setShowMemory(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.memory", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudSettings.getInstance().showMemory();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.memory", current),
                                    false
                            );
                            return 1;
                        })
                )

               
                .then(Commands.literal("yaw")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudSettings.getInstance().setShowYaw(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.yaw", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudSettings.getInstance().showYaw();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.yaw", current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("pitch")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudSettings.getInstance().setShowPitch(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.pitch", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudSettings.getInstance().showPitch();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.pitch", current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("facing")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudSettings.getInstance().setShowFacing(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.facing", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudSettings.getInstance().showFacing();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.facing", current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("surfacey")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudSettings.getInstance().setShowSurfaceY(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.surfacey", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudSettings.getInstance().showSurfaceY();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.surfacey", current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("chunk")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudSettings.getInstance().setShowChunk(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.chunk", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudSettings.getInstance().showChunk();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.chunk", current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("hostile")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudSettings.getInstance().setShowHostile(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.hostile", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudSettings.getInstance().showHostile();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.hostile", current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("posX")
                        .then(Commands.argument("значення", IntegerArgumentType.integer(0, 1000))
                                .executes(context -> {
                                    int value = IntegerArgumentType.getInteger(context, "значення");
                                    Minecraft client = Minecraft.getInstance();
                                    int screenWidth = client.getWindow().getGuiScaledWidth();
                                    int screenHeight = client.getWindow().getGuiScaledHeight();
                                    HudSettings settings = HudSettings.getInstance();

                                    int currentY = settings.getHudPosY(screenHeight);
                                    settings.setHudPosition(value, currentY, screenWidth, screenHeight);
                                    settings.save();
                                    context.getSource().sendSuccess(
                                            () -> Component.literal(
                                                    Component.translatable("hud.option.posX").getString() +
                                                            ": " + ChatFormatting.YELLOW + value
                                            ),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            Minecraft client = Minecraft.getInstance();
                            HudSettings settings = HudSettings.getInstance();
                            int screenWidth = client.getWindow().getGuiScaledWidth();
                            int current = settings.getHudPosX(screenWidth);
                            context.getSource().sendSuccess(
                                    () -> Component.literal(
                                            Component.translatable("hud.option.posX").getString() +
                                                    ": " + ChatFormatting.YELLOW + current
                                    ),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("posY")
                        .then(Commands.argument("значення", IntegerArgumentType.integer(0, 1000))
                                .executes(context -> {
                                    int value = IntegerArgumentType.getInteger(context, "значення");
                                    Minecraft client = Minecraft.getInstance();
                                    int screenWidth = client.getWindow().getGuiScaledWidth();
                                    int screenHeight = client.getWindow().getGuiScaledHeight();
                                    HudSettings settings = HudSettings.getInstance();

                                    int currentX = settings.getHudPosX(screenWidth);
                                    settings.setHudPosition(currentX, value, screenWidth, screenHeight);
                                    settings.save();
                                    context.getSource().sendSuccess(
                                            () -> Component.literal(
                                                    Component.translatable("hud.option.posY").getString() +
                                                            ": " + ChatFormatting.YELLOW + value
                                            ),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            Minecraft client = Minecraft.getInstance();
                            HudSettings settings = HudSettings.getInstance();
                            int screenHeight = client.getWindow().getGuiScaledHeight();
                            int current = settings.getHudPosY(screenHeight);
                            context.getSource().sendSuccess(
                                    () -> Component.literal(
                                            Component.translatable("hud.option.posY").getString() +
                                                    ": " + ChatFormatting.YELLOW + current
                                    ),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("scale")
                        .then(Commands.argument("значення", FloatArgumentType.floatArg(0.5f, 3.0f))
                                .executes(context -> {
                                    float value = FloatArgumentType.getFloat(context, "значення");
                                    HudSettings.getInstance().setHudScale(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> Component.literal(
                                                    Component.translatable("hud.option.scale").getString() +
                                                            ": " + formatScale(value)
                                            ),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            float current = HudSettings.getInstance().getHudScale();
                            context.getSource().sendSuccess(
                                    () -> Component.literal(
                                            Component.translatable("hud.option.scale").getString() +
                                                    ": " + formatScale(current)
                                    ),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("layout")
                        .then(Commands.argument("значення", IntegerArgumentType.integer(0, 6))
                                .executes(context -> {
                                    int value = IntegerArgumentType.getInteger(context, "значення");
                                    HudSettings.getInstance().setHudLayout(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> getLayoutText(value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            int current = HudSettings.getInstance().getHudLayout();
                            context.getSource().sendSuccess(
                                    () -> getLayoutText(current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("opacity")
                        .then(Commands.argument("значення", IntegerArgumentType.integer(0, 100))
                                .executes(context -> {
                                    int value = IntegerArgumentType.getInteger(context, "значення");
                                    HudSettings.getInstance().setOpacity(value);
                                    HudSettings.getInstance().save();
                                    context.getSource().sendSuccess(
                                            () -> Component.literal(
                                                    Component.translatable("hud.option.opacity").getString() +
                                                            ": " + ChatFormatting.YELLOW + value + "%"
                                            ),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            int current = HudSettings.getInstance().getOpacity();
                            context.getSource().sendSuccess(
                                    () -> Component.literal(
                                            Component.translatable("hud.option.opacity").getString() +
                                                    ": " + ChatFormatting.YELLOW + current + "%"
                                    ),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("reset")
                        .executes(context -> {
                            Minecraft client = Minecraft.getInstance();
                            HudSettings settings = HudSettings.getInstance();
                            int screenWidth = client.getWindow().getGuiScaledWidth();
                            int screenHeight = client.getWindow().getGuiScaledHeight();

                           
                            settings.setShowCoords(true);
                            settings.setShowConv(false);
                            settings.setShowBiome(false);
                            settings.setShowFPS(true);
                            settings.setShowDays(false);
                            settings.setShowMemory(false);

                           
                            settings.setShowYaw(false);
                            settings.setShowPitch(false);
                            settings.setShowFacing(false);
                            settings.setShowSurfaceY(false);
                            settings.setShowChunk(false);
                            settings.setShowHostile(false);

                            settings.setHudScale(1.0f);
                            settings.setHudPosition(3, 3, screenWidth, screenHeight);
                            settings.setHudLayout(6);
                            settings.setOpacity(100);
                            settings.save();
                            context.getSource().sendSuccess(
                                    () -> Component.translatable("command.compactinfo.reset").withStyle(ChatFormatting.GREEN),
                                    false
                            );
                            return 1;
                        })
                );

        dispatcher.register(rootCommand);
    }

    private static String getStatusText(boolean value) {
        String statusKey = value ? "status.compactinfo.enabled" : "status.compactinfo.disabled";
        ChatFormatting color = value ? ChatFormatting.GREEN : ChatFormatting.RED;
        Component statusText = Component.translatable(statusKey).withStyle(color);
        return statusText.getString();
    }

    private static Component createSettingText(String translationKey, boolean value) {
        return Component.literal(
                Component.translatable(translationKey).getString() +
                        ": " + getStatusText(value)
        );
    }

    private static Component createSimpleToggleText(String translationKey, boolean value) {
        return Component.literal(
                Component.translatable(translationKey).getString() +
                        " " + getStatusText(value)
        );
    }

    private static String formatScale(float scale) {
        return ChatFormatting.YELLOW + String.format("%.1fx", scale);
    }

    private static Component getLayoutText(int layout) {
        String[] layoutNames = {
                Component.translatable("hud.layout.top_left").getString(),
                Component.translatable("hud.layout.top_center").getString(),
                Component.translatable("hud.layout.top_right").getString(),
                Component.translatable("hud.layout.bottom_left").getString(),
                Component.translatable("hud.layout.bottom_center").getString(),
                Component.translatable("hud.layout.bottom_right").getString(),
                Component.translatable("hud.layout.auto").getString()
        };
        return Component.literal(
                Component.translatable("hud.option.layout").getString() +
                        ": " + ChatFormatting.YELLOW + layoutNames[layout]
        );
    }
}