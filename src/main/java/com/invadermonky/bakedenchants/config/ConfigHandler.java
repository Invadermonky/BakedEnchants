package com.invadermonky.bakedenchants.config;

import com.invadermonky.bakedenchants.BakedEnchants;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = BakedEnchants.MOD_ID)
public class ConfigHandler {
    @Config.Name("Hide Baked Effects")
    @Config.Comment("Hides the glow effect for items that have baked enchantment effects.")
    public static boolean hideBakedEffects = true;

    @Config.Name("Hide Baked Rarity")
    @Config.Comment("Hides the rarity item name color for items that have baked enchantment effects.")
    public static boolean hideBakedRarity = true;

    @Mod.EventBusSubscriber(modid = BakedEnchants.MOD_ID)
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(BakedEnchants.MOD_ID)) {
                ConfigManager.sync(BakedEnchants.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
