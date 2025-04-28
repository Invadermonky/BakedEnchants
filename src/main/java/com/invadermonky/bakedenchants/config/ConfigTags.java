package com.invadermonky.bakedenchants.config;

import com.invadermonky.bakedenchants.BakedEnchants;
import com.invadermonky.bakedenchants.util.EnchantmentHelperHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigTags {
    public static final HashMap<Item, HashMap<Enchantment, Integer>> BAKED_ENCHANTMENTS = new HashMap<>();

    public static boolean shouldHideEffect(ItemStack stack) {
        if (hasBakedEnchants(stack.getItem()) && ConfigHandler.hideBakedEffects) {
            return EnchantmentHelperHelper.getEnchantments(stack).keySet().stream().allMatch(enchantment -> BAKED_ENCHANTMENTS.get(stack.getItem()).containsKey(enchantment));
        }
        return false;
    }

    public static boolean shouldHideRarity(ItemStack stack) {
        if (hasBakedEnchants(stack.getItem()) && ConfigHandler.hideBakedRarity) {
            return EnchantmentHelperHelper.getEnchantments(stack).keySet().stream().allMatch(enchantment -> BAKED_ENCHANTMENTS.get(stack.getItem()).containsKey(enchantment));
        }
        return false;
    }

    public static boolean hasBakedEnchants(Item item) {
        return BAKED_ENCHANTMENTS.containsKey(item);
    }

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
