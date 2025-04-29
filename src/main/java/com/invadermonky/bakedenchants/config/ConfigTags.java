package com.invadermonky.bakedenchants.config;

import com.invadermonky.bakedenchants.BakedEnchants;
import com.invadermonky.bakedenchants.util.EnchantmentHelperHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigTags {
    private static final HashMap<Item, HashMap<Enchantment, Integer>> BAKED_ENCHANTMENTS = new HashMap<>();

    /**
     * Determines whether the item can be enchanted. This method will return true if the item is a baked enchantment item
     * and the only enchantments on the item are valid baked enchantments.
     *
     * @param stack The ItemStack to query
     * @return true if the only enchantments on the item are baked enchantments
     */
    public static boolean canBeEnchanted(ItemStack stack) {
        return hasBakedEnchants(stack.getItem()) && verifyBakedEnchants(stack);
    }

    /**
     * Determines if the passed enchant is a baked enchantment for the item.
     *
     * @param stack       The ItemStack to query
     * @param enchantment The Enchantment to query
     * @param level       The queried Enchantment level
     * @return true if the queried enchantment is a valid baked enchantment for the item
     */
    public static boolean isBakedEnchant(ItemStack stack, Enchantment enchantment, int level) {
        return BAKED_ENCHANTMENTS.containsKey(stack.getItem()) && BAKED_ENCHANTMENTS.get(stack.getItem()).containsKey(enchantment) && BAKED_ENCHANTMENTS.get(stack.getItem()).get(enchantment) == level;
    }

    /**
     * Determines whether the passed enchant is a baked enchantment for the item. Enchantment level is pulled from the item
     * itself.
     *
     * @param stack       The ItemStack to query
     * @param enchantment The Enchantment to query
     * @return true if the queried enchantment is a valid baked enchantment for the item
     */
    public static boolean isBakedEnchant(ItemStack stack, Enchantment enchantment) {
        int level = EnchantmentHelper.getEnchantmentLevel(enchantment, stack);
        return isBakedEnchant(stack, enchantment, level);
    }

    /**
     * Determines whether the item should have the enchanted glow effect. This method includes a check for the configuration
     * based disable of this feature.
     *
     * @param stack The ItemStack to query
     * @return true if the item should hide the enchanted item glow effect
     */
    public static boolean shouldHideEffect(ItemStack stack) {
        return hasBakedEnchants(stack.getItem()) && ConfigHandler.hideBakedEffects && verifyBakedEnchants(stack);
    }

    /**
     * Determines whether the item should have its tooltip name colored. This method inlcudes a check for the configuration
     * based disable of this feature.
     *
     * @param stack The ItemStack to query
     * @return true if the item should hide the enchanted item rarity text color
     */
    public static boolean shouldHideRarity(ItemStack stack) {
        return hasBakedEnchants(stack.getItem()) && ConfigHandler.hideBakedRarity && verifyBakedEnchants(stack);
    }

    /**
     * Verifies that the only enchantments on the item are the baked enchantments.
     *
     * @param stack The ItemStack to query
     * @return true if the only enchantments on the item are the baked enchantments
     */
    public static boolean verifyBakedEnchants(ItemStack stack) {
        return EnchantmentHelperHelper.getEnchantments(stack).keySet().stream().allMatch(enchantment -> BAKED_ENCHANTMENTS.get(stack.getItem()).containsKey(enchantment) && BAKED_ENCHANTMENTS.get(stack.getItem()).get(enchantment) == EnchantmentHelper.getEnchantmentLevel(enchantment, stack));
    }

    /**
     * Determines if the item has defined baked enchantments.
     *
     * @param item The Item to query
     * @return true if the item has defined baked enchantments
     */
    public static boolean hasBakedEnchants(Item item) {
        return BAKED_ENCHANTMENTS.containsKey(item);
    }

    /**
     * Adds all defined baked enchantments to the item.
     *
     * @param stack The itemstack to add the baked enchantments to
     */
    public static void addBakedEnchants(ItemStack stack) {
        BAKED_ENCHANTMENTS.get(stack.getItem()).forEach(stack::addEnchantment);
    }

    public static void syncConfig() {
        BAKED_ENCHANTMENTS.clear();
        Pattern enchPattern = Pattern.compile("([^=;]+)=(\\d+)");
        for (String bakedStr : ConfigHandler.bakedEnchants) {
            try {
                String[] split = bakedStr.split(";");
                if (split.length >= 2) {
                    ResourceLocation itemLoc = new ResourceLocation(split[0]);
                    Item item = ForgeRegistries.ITEMS.getValue(itemLoc);
                    if (item != null) {
                        for (int i = 1; i < split.length; i++) {
                            Matcher enchMatcher = enchPattern.matcher(split[i]);
                            if (enchMatcher.find()) {
                                ResourceLocation enchantLoc = new ResourceLocation(enchMatcher.group(1));
                                Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(enchantLoc);
                                int level = Integer.parseInt(enchMatcher.group(2));
                                if (enchantment != null && level > 0) {
                                    BAKED_ENCHANTMENTS.putIfAbsent(item, new HashMap<>());
                                    BAKED_ENCHANTMENTS.get(item).put(enchantment, level);
                                } else {
                                    throw new IllegalArgumentException("No registered enchantment found: " + enchantLoc);
                                }
                            } else {
                                throw new IllegalArgumentException("Invalid enchantment configuration string: " + split[i]);
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("No registered item found: " + itemLoc);
                    }
                } else {
                    throw new IllegalArgumentException("Invalid configuration string: " + bakedStr);
                }
            } catch (IllegalArgumentException e) {
                BakedEnchants.LOGGER.error(e.getMessage());
            }
        }
    }

    static {
        syncConfig();
    }
}
