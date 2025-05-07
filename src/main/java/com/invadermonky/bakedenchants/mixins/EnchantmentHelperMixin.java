package com.invadermonky.bakedenchants.mixins;

import com.invadermonky.bakedenchants.handler.BakedEnchantmentHandler;
import com.invadermonky.bakedenchants.handler.BakedEnchantmentRecipe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Mixin(value = EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "setEnchantments", at = @At("HEAD"))
    private static void setEnchantmentsMixin(Map<Enchantment, Integer> enchMap, ItemStack stack, CallbackInfo ci) {
        BakedEnchantmentRecipe recipe = BakedEnchantmentHandler.getBakedEnchantRecipe(stack);
        if (recipe != null) {
            enchMap.entrySet().stream().filter(Objects::nonNull).collect(Collectors.toList()).removeIf(entry -> {
                Enchantment enchant = entry.getKey();
                int level = entry.getValue();
                if (recipe.isBakedEnchant(enchant, level)) {
                    return enchMap.keySet().stream().anyMatch(checkEnch -> checkEnch != enchant && !checkEnch.isCompatibleWith(enchant));
                }
                return false;
            });
        }
    }
}
