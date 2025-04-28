package com.invadermonky.bakedenchants;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BakedEnchants.MOD_ID, name = BakedEnchants.MOD_NAME, version = BakedEnchants.MOD_VERSION, dependencies = BakedEnchants.DEPENDENCIES)
public class BakedEnchants {
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final String MOD_VERSION = "1.0.0";
    public static final String DEPENDENCIES = "required-after:mixinbooter" + ";required-after:configanytime" + ";after:quark";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
}
