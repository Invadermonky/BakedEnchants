package com.invadermonky.bakedenchants.compat.inject;

import com.invadermonky.bakedenchants.config.ConfigTags;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobUpdateInject {
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!(entity instanceof EntityPlayer)) {
            for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                ItemStack slotStack = entity.getItemStackFromSlot(slot);
                if (ConfigTags.hasBakedEnchants(slotStack.getItem())) {
                    ConfigTags.addBakedEnchants(slotStack);
                }
            }
        }
    }
}
