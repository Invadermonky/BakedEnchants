package com.invadermonky.bakedenchants.mixins;

import com.invadermonky.bakedenchants.handler.BakedEnchantmentHandler;
import com.invadermonky.bakedenchants.handler.BakedEnchantmentRecipe;
import com.invadermonky.bakedenchants.util.EnchantmentHelperBE;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(value = ItemStack.class, priority = 999)
public class ItemStackMixin {

    @Unique
    private ItemStack getItemStack() {
        return (ItemStack) (Object) this;
    }

    @Inject(method = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)V", at = @At("TAIL"))
    private void constructorMixin(Item itemIn, int amount, int meta, @Nullable NBTTagCompound capNBT, CallbackInfo ci) {
        if (itemIn != null && itemIn != Items.AIR) {
            BakedEnchantmentRecipe recipe = BakedEnchantmentHandler.getBakedEnchantRecipe(getItemStack());
            if (recipe != null) {
                recipe.bakeEnchantments(getItemStack());
            }
        }
    }

    @Inject(method = "isItemEnchanted", at = @At("HEAD"), cancellable = true)
    private void isItemEnchantedMixin(CallbackInfoReturnable<Boolean> ci) {
        if (!BakedEnchantmentHandler.canBeEnchanted(getItemStack())) {
            ci.setReturnValue(false);
        }
    }

    @Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
    private void addEnchantmentMixin(Enchantment enchToAdd, int level, CallbackInfo ci) {
        ItemStack stack = this.getItemStack();
        if (BakedEnchantmentHandler.isBakedEnchantItem(stack)) {
            int enchId = Enchantment.getEnchantmentID(enchToAdd);
            int currLevel = EnchantmentHelper.getEnchantmentLevel(enchToAdd, stack);
            if (currLevel > 0) {
                if (currLevel < level) {
                    NBTTagList nbttaglist = stack.getTagCompound().getTagList("ench", 10);
                    for (NBTBase nbtBase : nbttaglist) {
                        if (nbtBase instanceof NBTTagCompound && ((NBTTagCompound) nbtBase).getShort("id") == enchId) {
                            ((NBTTagCompound) nbtBase).setShort("lvl", (byte) level);
                        }
                    }
                }
                ci.cancel();
            } else {
                Map<Enchantment, Integer> currEnchants = EnchantmentHelper.getEnchantments(stack);
                currEnchants.keySet().stream().filter(enchant -> !enchToAdd.isCompatibleWith(enchant)).forEach(enchant -> EnchantmentHelperBE.removeEnchantment(stack, enchant));
            }
        }
    }
}
