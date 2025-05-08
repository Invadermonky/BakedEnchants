## Baked Enchants

Baked Enchants is a backport of Quarks "Fortunate Gold" feature commissioned by [Foreck1](https://www.curseforge.com/members/foreck1/projects) for use in the [Rebirth of the Night (RotN)](https://www.curseforge.com/minecraft/modpacks/rebirth-of-the-night) modpack.

## What does it do? 

Baked Enchants allows users to define enchantments that will be added to items by default. Configured enchantments will be included on the item when pulled from JEI/Creative menus, looted from monsters or chests, or crafted via modded or vanilla crafting processes.

Items with baked enchants can be enchanted normally, with higher enchant levels or incompatible enchantments overwriting baked enchants.

By default, Baked Enchants gives Golden Pickaxes Fortune II and Golden Swords Looting II.

## Integration

Additional baked enchanted items can be defined using either Crafttweaker or GroovyScript.

### Crafttweaker
```zenscript
import mods.bakedenchants.BakedEnchants;

//Add baked enchants to an item
//BakedEnchants.add(IItemStack, IEnchantment...)
BakedEnchants.add(<minecraft:iron_sword:*>, <enchantment:minecraft:sharpness>.makeEnchantment(5));
BakedEnchants.add(<minecraft:iron_sword:*>, <enchantment:minecraft:sharpness>.makeEnchantment(5), <enchantment:minecraft:unbreaking>.makeEnchantment(2));

//Remove all baked enchantments from an item
//BakedEnchants.remove(IItemStack)
BakedEnchants.remove(<minecraft:golden_pickaxe:*>);

//Remove all default baked enchantment items
//BakedEnchants.removeAll()
BakedEnchants.removeAll();
```

### GroovyScript
```groovy
import mods.bakedenchants.BakedEnchants

//Add a single baked enchantment to an item
//BakedEnchants.add(ItemStack, Enchantment, int)
BakedEnchants.add(item('minecraft:iron_sword'), enchantment('minecraft:sharpness'), 5)

//Add multiple baked enchantments to an item
//BakedEnchants.add(ItemStack, Map<Enchantment, Integer>)
BakedEnchants.add(item('minecraft:iron_sword:*'), [
    (enchantment('minecraft:looting')): 2, 
    (enchantment('minecraft:sharpness')): 5
])

//Remove all baked enchants from an item
//BakedEnchants.remove(ItemStack)
BakedEnchants.remove(item('minecraft:golden_pickaxe:*'))

//Remove all default baked enchantment items
BakedEnchants.removeAll()
```

## Credits
- Logo made by hand by Foreck
