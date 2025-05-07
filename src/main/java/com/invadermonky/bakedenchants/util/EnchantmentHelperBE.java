package com.invadermonky.bakedenchants.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Because Quark is dumb...
 */
public class EnchantmentHelperBE {
    public static void removeEnchantment(ItemStack stack, Enchantment enchantment) {
        if (!stack.hasTagCompound())
            return;

        int enchId = Enchantment.getEnchantmentID(enchantment);
        NBTTagList nbttaglist = stack.getTagCompound().getTagList("ench", 10);
        Iterator<NBTBase> iterator = nbttaglist.iterator();
        Set<Integer> toRemove = new HashSet<>();
        int i = 0;
        while (iterator.hasNext()) {
            NBTBase nbtBase = iterator.next();
            if (nbtBase instanceof NBTTagCompound && ((NBTTagCompound) nbtBase).getShort("id") == enchId) {
                toRemove.add(i);
            }
            i++;
        }
        toRemove.forEach(nbttaglist::removeTag);
    }
}
