package com.compactinfo;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = CompactInfo.MOD_ID, value = Dist.CLIENT)
public class CompactInfoClient {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {

        HudOverlay.render(event.getGuiGraphics());
    }
}