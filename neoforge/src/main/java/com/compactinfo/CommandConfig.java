package com.compactinfo;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
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
                    context.getSource().sendSuccess(
                            () -> Component.translatable("screen.compactinfo.title").withStyle(ChatFormatting.AQUA),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.coords", HudConfigScreen.SHOW_COORDS.get()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.converted", HudConfigScreen.SHOW_CONV.get()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.biome", HudConfigScreen.SHOW_BIOME.get()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.days", HudConfigScreen.SHOW_DAYS.get()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.fps", HudConfigScreen.SHOW_FPS.get()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> createSettingText("hud.option.memory", HudConfigScreen.SHOW_MEMORY.get()),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> Component.literal(
                                    Component.translatable("hud.option.posX").getString() +
                                            ": " + ChatFormatting.YELLOW + HudConfigScreen.HUD_POS_X.get()
                            ),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> Component.literal(
                                    Component.translatable("hud.option.posY").getString() +
                                            ": " + ChatFormatting.YELLOW + HudConfigScreen.HUD_POS_Y.get()
                            ),
                            false
                    );
                    context.getSource().sendSuccess(
                            () -> Component.literal(
                                    Component.translatable("hud.option.scale").getString() +
                                            ": " + formatScale(HudConfigScreen.HUD_SCALE.get().floatValue())
                            ),
                            false
                    );
                    return 1;
                })

                .then(Commands.literal("coords")
                        .then(Commands.argument("значення", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean value = BoolArgumentType.getBool(context, "значення");
                                    HudConfigScreen.SHOW_COORDS.set(value);
                                    HudConfigScreen.SPEC.save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.coords", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudConfigScreen.SHOW_COORDS.get();
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
                                    HudConfigScreen.SHOW_CONV.set(value);
                                    HudConfigScreen.SPEC.save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.converted", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudConfigScreen.SHOW_CONV.get();
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
                                    HudConfigScreen.SHOW_BIOME.set(value);
                                    HudConfigScreen.SPEC.save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.biome", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudConfigScreen.SHOW_BIOME.get();
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
                                    HudConfigScreen.SHOW_DAYS.set(value);
                                    HudConfigScreen.SPEC.save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.days", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudConfigScreen.SHOW_DAYS.get();
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
                                    HudConfigScreen.SHOW_FPS.set(value);
                                    HudConfigScreen.SPEC.save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.fps", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudConfigScreen.SHOW_FPS.get();
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
                                    HudConfigScreen.SHOW_MEMORY.set(value);
                                    HudConfigScreen.SPEC.save();
                                    context.getSource().sendSuccess(
                                            () -> createSimpleToggleText("hud.option.memory", value),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            boolean current = HudConfigScreen.SHOW_MEMORY.get();
                            context.getSource().sendSuccess(
                                    () -> createSettingText("hud.option.memory", current),
                                    false
                            );
                            return 1;
                        })
                )

                .then(Commands.literal("posX")
                        .then(Commands.argument("значення", IntegerArgumentType.integer(3, 1000))
                                .executes(context -> {
                                    int value = IntegerArgumentType.getInteger(context, "значення");
                                    HudConfigScreen.HUD_POS_X.set(value);
                                    HudConfigScreen.SPEC.save();
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
                            int current = HudConfigScreen.HUD_POS_X.get();
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
                        .then(Commands.argument("значення", IntegerArgumentType.integer(3, 1000))
                                .executes(context -> {
                                    int value = IntegerArgumentType.getInteger(context, "значення");
                                    HudConfigScreen.HUD_POS_Y.set(value);
                                    HudConfigScreen.SPEC.save();
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
                            int current = HudConfigScreen.HUD_POS_Y.get();
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
                                    HudConfigScreen.HUD_SCALE.set((double) value);
                                    HudConfigScreen.SPEC.save();
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
                            float current = HudConfigScreen.HUD_SCALE.get().floatValue();
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

                .then(Commands.literal("reset")
                        .executes(context -> {
                            HudConfigScreen.SHOW_COORDS.set(true);
                            HudConfigScreen.SHOW_CONV.set(false);
                            HudConfigScreen.SHOW_BIOME.set(false);
                            HudConfigScreen.SHOW_FPS.set(true);
                            HudConfigScreen.SHOW_DAYS.set(false);
                            HudConfigScreen.SHOW_MEMORY.set(false);
                            HudConfigScreen.HUD_SCALE.set(1.0);
                            HudConfigScreen.HUD_POS_X.set(3);
                            HudConfigScreen.HUD_POS_Y.set(3);
                            HudConfigScreen.SPEC.save();
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
}