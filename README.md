## Baked Enchants

Baked Enchants is a backport of Quarks "Fortunate Gold" feature commissioned by [Foreck1](https://www.curseforge.com/members/foreck1/projects) for use in the [Rebirth of the Night (RotN)](https://www.curseforge.com/minecraft/modpacks/rebirth-of-the-night) modpack.

## What does it do? 

Baked Enchants allows users to define enchantments that will be added to items by default. Configured enchantments will be included on the item when pulled from JEI, the creative menu or crafted at a crafting table.

## Integration

Because most mods do not call the initialization code for item creation through crafting, Baked Enchants includes a simple scripting helper method to automatically add all baked enchants to an item. Simply call the method from within any function, and it will add all associated baked enchantments to the item.

```zenscript
import mods.bakedenchants.BakedEnchants;

//Method returns an IItemStack object
//var baked = mods.bakedenchants.BakedEnchants.bake(<minecraft:golden_pickaxe>);

//The baked item can be defined externally
var baked = BakedEnchants.bake(<minecraft:golden_pickaxe>);
furnace.addRecipe(baked, <ore:ingotGold>);

//Or it can be called from inside most recipe functions
furnace.addRecipe(BakedEnchants.bake(<minecraft:golden_pickaxe>), <ore:ingotGold>);
```

Groovyscript uses the same syntax.
```groovy
import mods.bakedenchants.BakedEnchants

furnace.add(ore('ingotGold'), BakedEnchants.bake(item('minecraft:golden_pickaxe')))
```