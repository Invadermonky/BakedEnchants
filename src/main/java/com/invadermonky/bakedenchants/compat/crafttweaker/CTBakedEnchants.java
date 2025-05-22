package com.invadermonky.bakedenchants.compat.crafttweaker;

import com.invadermonky.bakedenchants.BakedEnchants;
import com.invadermonky.bakedenchants.handler.BakedEnchantmentHandler;
import com.invadermonky.bakedenchants.handler.BakedEnchantmentRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.enchantments.IEnchantment;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

@ZenRegister
@ZenClass("mods." + BakedEnchants.MOD_ID + ".BakedEnchants")
public class CTBakedEnchants {

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients) {
        addShapedRecipe(CraftTweakerMC.getItemStack(output).getItem().getRegistryName().getPath(), output, ingredients);
    }

    @ZenMethod
    public static void addShapedRecipe(String recipeName, IItemStack output, IIngredient[][] ingredients) {
        int height = ingredients.length;
        int width = Arrays.stream(ingredients).mapToInt(ing -> ing.length).max().orElse(0);
        NonNullList<Ingredient> inputs = NonNullList.create();
        Arrays.stream(ingredients).forEach(inputRow -> Arrays.stream(inputRow).map(CraftTweakerMC::getIngredient).forEach(inputs::add));
        IRecipe recipe = new ShapedRecipes("", width, height, inputs, CraftTweakerMC.getItemStack(output));
        ResourceLocation loc = new ResourceLocation(BakedEnchants.MOD_ID, recipeName);
        int i = 0;
        while (CTIntegration.craftingRecipeCache.containsKey(loc)) {
            loc = new ResourceLocation(BakedEnchants.MOD_ID, recipeName + "_alt" + i++);
        }
        recipe.setRegistryName(loc);
        CTIntegration.cacheCraftingTableRecipe(recipe);
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients) {
        addShapelessRecipe(CraftTweakerMC.getItemStack(output).getItem().getRegistryName().getPath(), output, ingredients);
    }

    @ZenMethod
    public static void addShapelessRecipe(String recipeName, IItemStack output, IIngredient[] ingredients) {
        NonNullList<Ingredient> inputs = NonNullList.create();
        Arrays.stream(ingredients).map(CraftTweakerMC::getIngredient).forEach(inputs::add);
        IRecipe recipe = new ShapelessRecipes("", CraftTweakerMC.getItemStack(output), inputs);
        ResourceLocation loc = new ResourceLocation(BakedEnchants.MOD_ID, recipeName);
        int i = 0;
        while (CTIntegration.craftingRecipeCache.containsKey(loc)) {
            loc = new ResourceLocation(BakedEnchants.MOD_ID, recipeName + "_alt" + i++);
        }
        recipe.setRegistryName(loc);
        CTIntegration.cacheCraftingTableRecipe(recipe);
    }

    @ZenMethod
    public static void addBakedEnchantment(IItemStack iStack, IEnchantment... iEnchantments) {
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
