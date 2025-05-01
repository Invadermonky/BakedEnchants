package com.invadermonky.bakedenchants;

import com.invadermonky.bakedenchants.compat.inject.MobDropInject;
import com.invadermonky.bakedenchants.compat.inject.MobUpdateInject;
import com.invadermonky.bakedenchants.config.ConfigHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BakedEnchants.MOD_ID, name = BakedEnchants.MOD_NAME, version = BakedEnchants.MOD_VERSION, dependencies = BakedEnchants.DEPENDENCIES)
public class BakedEnchants {
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final String MOD_VERSION = "1.1.0";
    public static final String DEPENDENCIES = "required-after:mixinbooter" + ";required-after:configanytime" + ";after:quark";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (ConfigHandler.injectBakedDrops)
            MinecraftForge.EVENT_BUS.register(new MobDropInject());
        if (ConfigHandler.injectBakedEquipment)
            MinecraftForge.EVENT_BUS.register(new MobUpdateInject());
    }
}
