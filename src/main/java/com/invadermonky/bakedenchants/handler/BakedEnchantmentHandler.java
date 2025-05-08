package com.invadermonky.bakedenchants.handler;

import com.invadermonky.bakedenchants.BakedEnchants;
import com.invadermonky.bakedenchants.config.ConfigHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BakedEnchantmentHandler {
    private static final Set<BakedEnchantmentRecipe> bakedEnchantments = new HashSet<>();

    /**
     * Returns true if the item is configured to have baked enchantments.
     */
    public static boolean isBakedEnchantItem(ItemStack stack) {
        return bakedEnchantments.stream().anyMatch(recipe -> recipe.matches(stack));
    }

    /**
     * Determines whether the item can be enchanted. This method will return true if the item is a baked enchantment item
     * and the only enchantments on the item are valid baked enchantments.
     *
     * @param stack The ItemStack to query
     * @return true if the only enchantments on the item are baked enchantments
     */
    public static boolean canBeEnchanted(ItemStack stack) {
        return bakedEnchantments.stream().anyMatch(recipe -> !recipe.validateBakedEnchantments(stack));
    }

    /**
     * Determines whether the item should have the enchanted glow effect. This method includes a check for the configuration
     * based disable of this feature.
     *
     * @param stack The ItemStack to query
     * @return true if the item should hide the enchanted item glow effect
     */
    public static boolean shouldHideEffect(ItemStack stack) {
        if (ConfigHandler.hideBakedEffects) {
            return bakedEnchantments.stream().anyMatch(recipe -> recipe.validateBakedEnchantments(stack));
        }
        return false;
    }

    /**
     * Determines whether the item should have its tooltip name colored. This method includes a check for the configuration
     * based disable of this feature.
     *
     * @param stack The ItemStack to query
     * @return true if the item should hide the enchanted item rarity text color
     */
    public static boolean shouldHideRarity(ItemStack stack) {
        if (ConfigHandler.hideBakedRarity) {
            return bakedEnchantments.stream().anyMatch(recipe -> recipe.validateBakedEnchantments(stack));
        }
        return false;
    }

    public static void addBakedEnchantRecipe(BakedEnchantmentRecipe recipe) {
        bakedEnchantments.add(recipe);
    }

    public static void addBakedEnchantRecipe(ItemStack stack, Enchantment enchantment, int level) {
        try {
            Optional<BakedEnchantmentRecipe> optional = bakedEnchantments.stream().filter(recipe -> recipe.matches(stack)).findFirst();
            if (optional.isPresent()) {
                optional.get().addBakedEnchantment(enchantment, level);
            } else {
                addBakedEnchantRecipe(new BakedEnchantmentRecipe(stack).addBakedEnchantment(enchantment, level));
            }
        } catch (IllegalArgumentException e) {
            BakedEnchants.LOGGER.error(e);
        }
    }

    public static void removeBakedEnchant(ItemStack stack) {
        bakedEnchantments.removeIf(recipe -> recipe.matches(stack));
    }

    public static void removeAll() {
        bakedEnchantments.clear();
    }

    public static @Nullable BakedEnchantmentRecipe getBakedEnchantRecipe(ItemStack stack) {
        return bakedEnchantments.stream().filter(recipe -> recipe.matches(stack)).findFirst().orElse(null);
    }

    public static void registerDefaultBakedEnchants() {
        BakedEnchantmentHandler.addBakedEnchantRecipe(new ItemStack(Items.GOLDEN_PICKAXE, 1, Short.MAX_VALUE), Enchantments.FORTUNE, 2);
        BakedEnchantmentHandler.addBakedEnchantRecipe(new ItemStack(Items.GOLDEN_SWORD, 1, Short.MAX_VALUE), Enchantments.LOOTING, 2);
    }
}
