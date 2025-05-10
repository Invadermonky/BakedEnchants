package com.invadermonky.bakedenchants.compat.crafttweaker;

import com.invadermonky.bakedenchants.handler.BakedEnchantmentHandler;
import com.invadermonky.bakedenchants.handler.BakedEnchantmentRecipe;
import crafttweaker.mc1120.events.ScriptRunEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

public class CTIntegration {
    private static final Set<BakedEnchantmentRecipe> recipeCache = new HashSet<>();

    public static void cacheBakedEnchantRecipe(BakedEnchantmentRecipe recipe) {
        recipeCache.add(recipe);
    }

    @SubscribeEvent
    public void onScriptLoadPre(ScriptRunEvent.Pre event) {
        recipeCache.clear();
        BakedEnchantmentHandler.removeAll();
        BakedEnchantmentHandler.registerDefaultBakedEnchants();
    }

    @SubscribeEvent
    public void onScriptLoadPost(ScriptRunEvent.Post event) {
        recipeCache.forEach(BakedEnchantmentHandler::addBakedEnchantRecipe);
    }
}
