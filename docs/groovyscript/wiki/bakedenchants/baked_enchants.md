---
title: "Baked Enchants"
titleTemplate: "Baked Enchants | CleanroomMC"
description: "Adds enchantments included on items by default. This includes items pulled from Creative/JEI menus, loot from drops or chests, and created by modded or vanilla crafting processes."
source_code_link: "https://github.com/Invadermonky/BakedEnchants/blob/master/src/main/java/com/invadermonky/bakedenchants/compat/groovyscript/BakedEnchants.java"
---

# Baked Enchants (Baked Enchants)

## Description

Adds enchantments included on items by default. This includes items pulled from Creative/JEI menus, loot from drops or chests, and created by modded or vanilla crafting processes.

## Identifier

Refer to this via any of the following:

```groovy:no-line-numbers {1}
mods.bakedenchants.baked_enchants/* Used as page default */ // [!code focus]
mods.bakedenchants.bakedenchants
mods.bakedenchants.bakedEnchants
mods.bakedenchants.BakedEnchants
```


## Adding Recipes

- Adds a single baked enchantment to an item. This method can be called multiple times for the same item:

    ```groovy:no-line-numbers
    mods.bakedenchants.baked_enchants.add(ItemStack, Enchantment, int)
    ```

- Adds multiple baked enchantments to an item. This method can be called multiple times for the same item:

    ```groovy:no-line-numbers
    mods.bakedenchants.baked_enchants.add(ItemStack, Map<Enchantment, Integer>)
    ```

:::::::::: details Example {open id="example"}
```groovy:no-line-numbers
mods.bakedenchants.baked_enchants.add(item('minecraft:stone_pickaxe:*'), enchantment('minecraft:fortune'), 3)
mods.bakedenchants.baked_enchants.add(item('minecraft:stone_sword:*'), [(enchantment('minecraft:looting')): 3, (enchantment('minecraft:sharpness')): 5])
```

::::::::::

### Recipe Builder

Just like other recipe types, the Baked Enchants also uses a recipe builder.

Don't know what a builder is? Check [the builder info page](../../getting_started/builder.md) out.

:::::::::: details mods.bakedenchants.baked_enchants.recipeBuilder() {open id="abstract"}
- `ItemStack`. The item the baked enchantments will be applied to. Requires exactly 1.

    ```groovy:no-line-numbers
    setBakedStack(ItemStack)
    ```

- `Map<Enchantment, Integer>`. Enchantments that will be applied to the item. Requires greater than or equal to 1.

    ```groovy:no-line-numbers
    addEnchantment(Enchantment, int)
    ```

- First validates the builder, returning `null` and outputting errors to the log file if the validation failed, then registers the builder and returns the registered object. (returns `null` or `com.invadermonky.bakedenchants.handler.BakedEnchantmentRecipe`).

    ```groovy:no-line-numbers
    register()
    ```

::::::::: details Example {open id="example"}
```groovy:no-line-numbers
mods.bakedenchants.baked_enchants.recipeBuilder()
    .setStack(item('minecraft:stone_sword:*'))
    .addEnchantment(enchantment('minecraft:looting'), 2)
    .addEnchantment(enchantment('minecraft:sharpness'), 5)
    .register()
```

:::::::::

::::::::::

## Removing Recipes

- Removing all baked enchantments from an item:

    ```groovy:no-line-numbers
    mods.bakedenchants.baked_enchants.remove(ItemStack)
    ```

- Removes all registered recipes:

    ```groovy:no-line-numbers
    mods.bakedenchants.baked_enchants.removeAll()
    ```

:::::::::: details Example {open id="example"}
```groovy:no-line-numbers
mods.bakedenchants.baked_enchants.remove(item('minecraft:golden_pickaxe:*'))
mods.bakedenchants.baked_enchants.removeAll()
```

::::::::::
