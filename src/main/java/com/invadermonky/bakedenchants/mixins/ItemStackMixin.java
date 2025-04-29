package com.invadermonky.bakedenchants.mixins;

import com.invadermonky.bakedenchants.config.ConfigTags;
import com.invadermonky.bakedenchants.util.EnchantmentHelperHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
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

import java.util.Map;

@Mixin(value = ItemStack.class, priority = 999)
public class ItemStackMixin {

    @Unique
    public ItemStack getItemStack() {
        return (ItemStack) (Object) this;
    }

    @Inject(method = "isItemEnchanted", at = @At("HEAD"), cancellable = true)
    public void isItemEnchantedMixin(CallbackInfoReturnable<Boolean> ci) {
        if (ConfigTags.canBeEnchanted(getItemStack())) {
            ci.setReturnValue(false);
        }
    }

    @Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
    public void addEnchantmentMixin(Enchantment enchToAdd, int level, CallbackInfo ci) {
        ItemStack stack = this.getItemStack();
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
            currEnchants.keySet().stream().filter(enchant -> !enchToAdd.isCompatibleWith(enchant)).forEach(enchant -> EnchantmentHelperHelper.removeEnchantment(stack, enchant));
        }
    }
}
