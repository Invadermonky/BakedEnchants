package com.invadermonky.bakedenchants.handler;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BakedEnchantmentRecipe {
    protected final ItemStack bakedStack;
    protected final Map<Enchantment, Short> bakedEnchantments;

    public BakedEnchantmentRecipe(@NotNull ItemStack bakedStack) throws IllegalArgumentException {
        Preconditions.checkArgument(!bakedStack.isEmpty(), "ItemStack cannot be empty");
        this.bakedStack = bakedStack;
        this.bakedEnchantments = new Object2ShortOpenHashMap<>();
    }

    public BakedEnchantmentRecipe addBakedEnchantment(@NotNull Enchantment enchantment, int level) throws IllegalArgumentException {
        Preconditions.checkArgument(level > 0 && level <= Short.MAX_VALUE, "Enchantment level must be between 1 and 32767");
        this.bakedEnchantments.put(enchantment, (short) level);
        return this;
    }

    public @NotNull ItemStack getBakedStack() {
        return this.bakedStack;
    }

    public Map<Enchantment, Short> getBakedEnchantments() {
        return this.bakedEnchantments;
    }

    public boolean isBakedEnchant(Enchantment enchantment, int level) {
        return this.bakedEnchantments.containsKey(enchantment) && this.bakedEnchantments.get(enchantment) == level;
    }


    public boolean isBakedEnchant(ItemStack stack, Enchantment enchantment) {
        return matches(stack) && isBakedEnchant(enchantment, EnchantmentHelper.getEnchantmentLevel(enchantment, stack));
    }

    /**
     * Validates the enchantments on the item, determining if it has all valid baked enchantments and no other enchantments.
     * This method checks if the item matches this enchantment baking recipe.
     *
     * @param stack the ItemStack to query
     * @return true if the item has all baked enchants and only baked enchants
     */
    public boolean validateBakedEnchantments(ItemStack stack) {
        if (!matches(stack))
            return false;

        for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : EnchantmentHelper.getEnchantments(stack).entrySet()) {
            if (!this.bakedEnchantments.containsKey(enchantmentIntegerEntry.getKey())
                    || this.bakedEnchantments.get(enchantmentIntegerEntry.getKey()) != enchantmentIntegerEntry.getValue().shortValue()) {
                return false;
            }
        }
        for (Map.Entry<Enchantment, Short> entry : this.bakedEnchantments.entrySet()) {
            if (EnchantmentHelper.getEnchantmentLevel(entry.getKey(), stack) != entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds all associated baked enchantments to the passed ItemStack. This method <b>DOES NOT</b> check if the item matches
     * this enchantment baking recipe and will apply the enchants to any ItemStack passed to it.
     *
     * @param stack The ItemStack to bake enchantments onto
     */
    public void bakeEnchantments(@NotNull ItemStack stack) {
        Map<Enchantment, Short> bakedEnchants = new HashMap<>(this.bakedEnchantments);
        for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet()) {
            bakedEnchants.keySet().removeIf(bakedEnchant -> !entry.getKey().isCompatibleWith(bakedEnchant)
                    || (entry.getKey() == bakedEnchant && entry.getValue() >= bakedEnchants.get(bakedEnchant)));
        }
        bakedEnchants.forEach(stack::addEnchantment);
    }

    /**
     * Returns true if this item has baked enchantments
     *
     * @param stack the ItemStack to query
     * @return true if the item has baked enchantments
     */
    public boolean matches(ItemStack stack) {
        return !this.bakedStack.isEmpty() && !stack.isEmpty()
                && this.bakedStack.getItem() == stack.getItem()
                && (this.bakedStack.getMetadata() == Short.MAX_VALUE || this.bakedStack.getMetadata() == stack.getMetadata());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getBakedStack());
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BakedEnchantmentRecipe))
            return false;
        BakedEnchantmentRecipe that = (BakedEnchantmentRecipe) object;
        return this.matches(that.getBakedStack());
    }
}
