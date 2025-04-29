# Baked Enchants

Baked Enchants is a backport of Quarks "Fortunate Gold" feature commissioned [Foreck1](https://www.curseforge.com/members/foreck1/projects) for use in the [Rebirth of the Night (RotN)](https://www.curseforge.com/minecraft/modpacks/rebirth-of-the-night) modpack.

This mod allows users to define enchantments that will be included on items by default when pulled from JEI or crafted at a crafting table. By default, Baked Enchants gives Golden Pickaxes Fortune II and Golden Swords Looting II. Additional items and enchantments can be defined in the configuration.

Baked Enchantments will add all defined enchantments to the item display in JEI and in the creative menu. Items crafted at a crafting table will have the enchantments applied automatically.

However, nearly all modded crafting methods will not have the enchantments applied automatically. Any items crafted through modded processes will require manually adding the enchantments via Crafttweaker or Groovyscript.

Because mods do not call the initialization code for item creation through crafting, Baked Enchants includes a simple Crafttweaker helper method to automatically add all baked enchants to an item. Simply call the method from within any function, and it will take care of the rest.
```zenscript
import mods.bakedenchants.BakedEnchants;

//Method returns an IItemStack object
//var baked = mods.bakedenchants.BakedEnchants.bake(<minecraft:golden_pickaxe>);

//The baked item can be defined externally
var baked = BakedEnchants.bake(<minecraft:golden_pickaxe>);
brewing.addBrew(<ore:stickWood>, <ore:oreGold>, baked);

//Or it can be called from inside most functions
brewing.addBrew(<ore:stickWood>, <ore:ingotGold>, BakedEnchants.bake(<minecraft:golden_pickaxe>));
```
