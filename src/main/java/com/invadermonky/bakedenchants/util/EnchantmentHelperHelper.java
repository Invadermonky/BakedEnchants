package com.invadermonky.bakedenchants.util;

import com.google.common.collect.Maps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Because Quark is dumb...
 */
public class EnchantmentHelperHelper {
    public static Map<Enchantment, Integer> getEnchantments(ItemStack stack) {
        Map<Enchantment, Integer> map = Maps.newLinkedHashMap();
        NBTTagList nbttaglist = stack.getItem() == Items.ENCHANTED_BOOK ? ItemEnchantedBook.getEnchantments(stack) : stack.getEnchantmentTagList();

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            Enchantment enchantment = Enchantment.getEnchantmentByID(nbttagcompound.getShort("id"));
            int j = nbttagcompound.getShort("lvl");
            map.put(enchantment, j);
        }

        return map;
    }

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
