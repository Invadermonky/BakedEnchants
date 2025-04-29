package com.invadermonky.bakedenchants.compat.crafttweaker;

import com.invadermonky.bakedenchants.BakedEnchants;
import com.invadermonky.bakedenchants.config.ConfigTags;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + BakedEnchants.MOD_ID + ".BakedEnchants")
public class CTBakedEnchants {
    @ZenMethod
    public static IItemStack bake(IItemStack iStack) {
        ItemStack stack = CraftTweakerMC.getItemStack(iStack);
        if (ConfigTags.hasBakedEnchants(stack.getItem())) {
            ConfigTags.addBakedEnchants(stack);
        } else {
            CraftTweakerAPI.logError("No valid baked enchantments found for: " + iStack);
        }
        return CraftTweakerMC.getIItemStack(stack);
    }
}
