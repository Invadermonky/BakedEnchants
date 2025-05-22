package com.invadermonky.bakedenchants.compat.crafttweaker;

import com.invadermonky.bakedenchants.handler.BakedEnchantmentHandler;
import com.invadermonky.bakedenchants.handler.BakedEnchantmentRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.events.ScriptRunEvent;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.GameData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CTIntegration {
    public static final Map<ResourceLocation, IRecipe> craftingRecipeCache = new HashMap<>();
    private static final Set<BakedEnchantmentRecipe> bakedRecipeCache = new HashSet<>();

    public static void cacheBakedEnchantRecipe(BakedEnchantmentRecipe recipe) {
        bakedRecipeCache.add(recipe);
    }

    public static void cacheCraftingTableRecipe(IRecipe recipe) {
        craftingRecipeCache.put(recipe.getRegistryName(), recipe);
    }

    public static void postInit() {
        CraftTweakerAPI.logInfo("Post-Script run Baked Enchants recipe additions running.");
        bakedRecipeCache.forEach(BakedEnchantmentHandler::addBakedEnchantRecipe);
        craftingRecipeCache.values().forEach(recipe -> {
            CraftTweakerAPI.logInfo("Late register recipe " + recipe.getRegistryName().toString());
            GameData.register_impl(recipe);
        });
    }

    @SubscribeEvent
    public void onScriptLoadPre(ScriptRunEvent.Pre event) {
        bakedRecipeCache.clear();
        BakedEnchantmentHandler.removeAll();
        BakedEnchantmentHandler.registerDefaultBakedEnchants();
    }
}
