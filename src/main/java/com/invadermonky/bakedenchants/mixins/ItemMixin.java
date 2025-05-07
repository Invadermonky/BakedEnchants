package com.invadermonky.bakedenchants.mixins;

import com.invadermonky.bakedenchants.handler.BakedEnchantmentHandler;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Item.class, priority = 999)
public abstract class ItemMixin {
    @Inject(method = "getForgeRarity", at = @At("RETURN"), remap = false, cancellable = true)
    private void getForgeRarityMixin(ItemStack stack, CallbackInfoReturnable<IRarity> cir) {
        if (!stack.isEmpty() && BakedEnchantmentHandler.shouldHideRarity(stack)) {
            cir.setReturnValue(EnumRarity.COMMON);
        }
    }

    @SideOnly(Side.CLIENT)
    @ModifyReturnValue(method = "hasEffect", at = @At("RETURN"))
    private boolean hasEffectMixin(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (original && BakedEnchantmentHandler.shouldHideEffect(stack)) {
            return false;
        }
        return original;
    }
}
