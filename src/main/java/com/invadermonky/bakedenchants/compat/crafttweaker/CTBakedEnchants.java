package com.invadermonky.bakedenchants.compat.crafttweaker;

import com.invadermonky.bakedenchants.BakedEnchants;
import com.invadermonky.bakedenchants.handler.BakedEnchantmentHandler;
import com.invadermonky.bakedenchants.handler.BakedEnchantmentRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.enchantments.IEnchantment;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + BakedEnchants.MOD_ID + ".BakedEnchants")
public class CTBakedEnchants {

    @ZenMethod
    public static void add(IItemStack iStack, IEnchantment... iEnchantments) {
        ItemStack stack = CraftTweakerMC.getItemStack(iStack);
        if (stack.isEmpty()) {
            CraftTweakerAPI.logError("IItemStack cannot be empty");
        }
        BakedEnchantmentRecipe recipe = new BakedEnchantmentRecipe(stack);
        for (IEnchantment iEnchantment : iEnchantments) {
            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(iEnchantment.getDefinition().getRegistryName()));
            if (enchantment == null) {
                CraftTweakerAPI.logError("Not a valid enchantment: " + iEnchantment);
                return;
            }
            if (iEnchantment.getLevel() <= 0 || iEnchantment.getLevel() > Short.MAX_VALUE) {
                CraftTweakerAPI.logError("Error adding " + iEnchantment + ", enchantment level must be between 1 and 32767");
                return;
            }
            recipe.addBakedEnchantment(enchantment, iEnchantment.getLevel());
        }
        CTIntegration.cacheBakedEnchantRecipe(recipe);
    }

    @ZenMethod
    public static IItemStack bake(IItemStack iStack) {
        ItemStack stack = CraftTweakerMC.getItemStack(iStack);
        BakedEnchantmentRecipe recipe = BakedEnchantmentHandler.getBakedEnchantRecipe(stack);
        if (recipe != null) {
            recipe.bakeEnchantments(stack);
            return CraftTweakerMC.getIItemStack(stack);
        }
        return iStack;
    }

    @ZenMethod
    public static void remove(IItemStack stack) {
        BakedEnchantmentHandler.removeBakedEnchant(CraftTweakerMC.getItemStack(stack));
    }

    @ZenMethod
    public static void removeAll() {
        BakedEnchantmentHandler.removeAll();
    }
}
