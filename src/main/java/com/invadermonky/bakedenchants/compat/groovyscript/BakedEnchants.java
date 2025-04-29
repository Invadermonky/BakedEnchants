package com.invadermonky.bakedenchants.compat.groovyscript;

import com.cleanroommc.groovyscript.api.documentation.annotations.Example;
import com.cleanroommc.groovyscript.api.documentation.annotations.MethodDescription;
import com.cleanroommc.groovyscript.registry.NamedRegistry;
import com.invadermonky.bakedenchants.config.ConfigTags;
import net.minecraft.item.ItemStack;

public class BakedEnchants extends NamedRegistry {
    @MethodDescription(
            type = MethodDescription.Type.QUERY,
            example = @Example("item('minecraft:golden_pickaxe')")
    )
    public ItemStack bake(ItemStack stack) {
        if (ConfigTags.hasBakedEnchants(stack.getItem())) {
            ConfigTags.addBakedEnchants(stack);
        }
        return stack;
    }
}
