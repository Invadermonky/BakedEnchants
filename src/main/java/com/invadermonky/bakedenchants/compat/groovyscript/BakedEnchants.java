package com.invadermonky.bakedenchants.compat.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.invadermonky.bakedenchants.handler.BakedEnchantmentHandler;
import com.invadermonky.bakedenchants.handler.BakedEnchantmentRecipe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RegistryDescription(linkGenerator = com.invadermonky.bakedenchants.BakedEnchants.MOD_ID)
public class BakedEnchants extends VirtualizedRegistry<BakedEnchantmentRecipe> {
    @Override
    public void onReload() {
        BakedEnchantmentHandler.removeAll();
        BakedEnchantmentHandler.registerDefaultBakedEnchants();
    }


    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            description = "groovyscript.wiki.bakedenchants.baked_enchants.add.single",
            example = @Example(value = "item('minecraft:golden_pickaxe:*'), enchantment('minecraft:fortune'), 2")
    )
    public void add(ItemStack stack, Enchantment enchantment, int level) {
        this.recipeBuilder()
                .setBakedStack(stack)
                .addEnchantment(enchantment, level)
                .register();
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = @Example(value = "item('minecraft:golden_sword:*'), [(enchantment('minecraft:looting')): 2, (enchantment('minecraft:sharpness')): 5]"),
            description = "groovyscript.wiki.bakedenchants.baked_enchants.add.multi",
            priority = 1001
    )
    public void add(ItemStack stack, Map<Enchantment, Integer> enchants) {
        RecipeBuilder builder = this.recipeBuilder();
        builder.setBakedStack(stack);
        enchants.forEach(builder::addEnchantment);
        builder.register();
    }

    @MethodDescription(
            type = MethodDescription.Type.REMOVAL,
            example = @Example(value = "item('minecraft:golden_pickaxe:*')")
    )
    public void remove(ItemStack stack) {
        BakedEnchantmentHandler.removeBakedEnchant(stack);
    }

    @MethodDescription(
            type = MethodDescription.Type.REMOVAL,
            priority = 1001
    )
    public void removeAll() {
        BakedEnchantmentHandler.removeAll();
    }

    @RecipeBuilderDescription(
            example = @Example(".setStack(item('minecraft:golden_sword:*')).addEnchantment(enchantment('minecraft:looting'), 2).addEnchantment(enchantment('minecraft:sharpness'), 5)")
    )
    public RecipeBuilder recipeBuilder() {
        return new RecipeBuilder();
    }

    public static class RecipeBuilder extends AbstractRecipeBuilder<BakedEnchantmentRecipe> {
        @Property(comp = @Comp(gte = 1))
        private final Map<Enchantment, Integer> bakedEnchants;
        @Property(comp = @Comp(eq = 1))
        private ItemStack bakedStack;

        public RecipeBuilder() {
            this.bakedStack = ItemStack.EMPTY;
            this.bakedEnchants = new HashMap<>();
        }

        @RecipeBuilderMethodDescription(field = "bakedStack")
        public RecipeBuilder setBakedStack(ItemStack stack) {
            this.bakedStack = stack;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "bakedEnchants")
        public RecipeBuilder addEnchantment(Enchantment enchantment, int level) {
            this.bakedEnchants.put(enchantment, level);
            return this;
        }

        @Override
        public String getErrorMsg() {
            return "Error adding Baked Enchantment";
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            msg.add(bakedStack.isEmpty(), "ItemStack cannot be empty");
            msg.add(bakedEnchants.isEmpty(), "Item must have at least one baked enchantment");
            msg.add(bakedEnchants.keySet().stream().anyMatch(Objects::isNull), "Invalid enchantment registered");
            msg.add(bakedEnchants.values().stream().anyMatch(level -> level <= 0 || level > Short.MAX_VALUE), "Enchantment level must be between 1 and 32767");
        }

        @RecipeBuilderRegistrationMethod
        @Override
        public @Nullable BakedEnchantmentRecipe register() {
            if (validate()) {
                BakedEnchantmentRecipe recipe = new BakedEnchantmentRecipe(this.bakedStack);
                this.bakedEnchants.forEach(recipe::addBakedEnchantment);
                BakedEnchantmentHandler.addBakedEnchant(recipe);
                return recipe;
            }
            return null;
        }
    }
}
