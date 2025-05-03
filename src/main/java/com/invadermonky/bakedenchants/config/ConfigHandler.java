package com.invadermonky.bakedenchants.config;

import com.cleanroommc.configanytime.ConfigAnytime;
import com.invadermonky.bakedenchants.BakedEnchants;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = BakedEnchants.MOD_ID)
public class ConfigHandler {
    @Config.RequiresMcRestart
    @Config.Name("Baked Enchantments")
    @Config.Comment({"Any items and the enchants that will be applied to them by default. This feature will automatically", "adapt for crafting table recipes and creative tab entries. Other crafting processes may need custom", "handling via Crafttweaker or GroovyScript.", "Format:", "  modid:itemid;enchant1=level;enchant2=level...", "Examples:", "  minecraft:golden_pickaxe;minecraft:fortune=3;minecraft:unbreaking=1", "  minecraft:golden_sword;minecraft:looting=3"})
    public static String[] bakedEnchants = new String[]{
            "minecraft:golden_pickaxe;minecraft:fortune=2",
            "minecraft:golden_sword;minecraft:looting=2"
    };

    @Config.Name("Hide Baked Effects")
    @Config.Comment("Hides the glow effect for items that have baked enchantment effects.")
    public static boolean hideBakedEffects = true;

    @Config.Name("Hide Baked Rarity")
    @Config.Comment("Hides the rarity item name color for items that have baked enchantment effects.")
    public static boolean hideBakedRarity = true;

    @Config.RequiresMcRestart
    @Config.Name("Inject Baked Drops")
    @Config.Comment
            ({
                    "Items dropped from mobs will automatically have baked enchants applied if they are missing. This is",
                    "a less intrusive alternative to the 'Inject Item Creation' option."
            })
    public static boolean injectBakedDrops = false;

    @Config.RequiresMcRestart
    @Config.Name("Inject Baked Equipment")
    @Config.Comment
            ({
                    "Equipment carried or worn by mobs, but not players, will automatically have baked enchants applied if",
                    "they are missing. This is a less intrusive alternative to the 'Inject Item Creation' option."
            })
    public static boolean injectBakedEquipment = false;

    @Config.Name("Inject Item Creation")
    @Config.Comment
            ({
                    "Adds baked enchantments onto items whenever the associated ItemStack is created."
            })
    public static boolean injectItemCreation = true;


    @Mod.EventBusSubscriber(modid = BakedEnchants.MOD_ID)
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(BakedEnchants.MOD_ID)) {
                ConfigManager.sync(BakedEnchants.MOD_ID, Config.Type.INSTANCE);
                ConfigTags.syncConfig();
            }
        }
    }

    static {
        //ConfigManager.sync(BakedEnchants.MOD_ID, Config.Type.INSTANCE);
        ConfigAnytime.register(ConfigHandler.class);
    }
}
