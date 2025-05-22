package com.invadermonky.bakedenchants;

import com.invadermonky.bakedenchants.compat.crafttweaker.CTIntegration;
import com.invadermonky.bakedenchants.handler.BakedEnchantmentHandler;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BakedEnchants.MOD_ID, name = BakedEnchants.MOD_NAME, version = BakedEnchants.MOD_VERSION, dependencies = BakedEnchants.DEPENDENCIES)
@Mod.EventBusSubscriber(modid = BakedEnchants.MOD_ID)
public class BakedEnchants {
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final String MOD_VERSION = "2.1.0";
    public static final String DEPENDENCIES = "required-after:mixinbooter;after:crafttweaker";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static boolean crafttweakerLoaded = Loader.isModLoaded("crafttweaker");

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        BakedEnchantmentHandler.registerDefaultBakedEnchants();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (crafttweakerLoaded) {
            MinecraftForge.EVENT_BUS.register(new CTIntegration());
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (crafttweakerLoaded) {
            CTIntegration.postInit();
        }
    }
}
