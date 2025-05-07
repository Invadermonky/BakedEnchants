package com.invadermonky.bakedenchants.mixins;

import com.invadermonky.bakedenchants.handler.BakedEnchantmentHandler;
import com.invadermonky.bakedenchants.handler.BakedEnchantmentRecipe;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ContainerRepair.class)
public class ContainerRepairMixin {
    @Redirect(method = "updateRepairOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;isCompatibleWith(Lnet/minecraft/enchantment/Enchantment;)Z"))
    private boolean updateRepairOutputMixin(Enchantment applyEnchant, Enchantment itemEnchant, @Local(ordinal = 0) ItemStack stack) {
        BakedEnchantmentRecipe recipe = BakedEnchantmentHandler.getBakedEnchantRecipe(stack);
        if (recipe != null && !applyEnchant.isCompatibleWith(itemEnchant) && recipe.isBakedEnchant(stack, itemEnchant)) {
            return true;
        }
        return applyEnchant.isCompatibleWith(itemEnchant);
    }

}
