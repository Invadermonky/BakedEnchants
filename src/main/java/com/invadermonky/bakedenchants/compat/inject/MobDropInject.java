package com.invadermonky.bakedenchants.compat.inject;

import com.invadermonky.bakedenchants.config.ConfigTags;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobDropInject {
    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        event.getDrops().stream().map(EntityItem::getItem)
                .filter(item -> ConfigTags.hasBakedEnchants(item.getItem()))
                .forEach(ConfigTags::addBakedEnchants);
    }
}
