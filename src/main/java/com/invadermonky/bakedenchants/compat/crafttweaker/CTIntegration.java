package com.invadermonky.bakedenchants.compat.crafttweaker;

import com.invadermonky.bakedenchants.handler.BakedEnchantmentHandler;
import crafttweaker.mc1120.events.ScriptRunEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CTIntegration {
    @SubscribeEvent
    public void onScriptLoadPre(ScriptRunEvent.Pre event) {
        BakedEnchantmentHandler.removeAll();
        BakedEnchantmentHandler.registerDefaultBakedEnchants();
    }
}
