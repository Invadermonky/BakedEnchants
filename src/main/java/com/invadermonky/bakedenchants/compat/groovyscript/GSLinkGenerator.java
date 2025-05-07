package com.invadermonky.bakedenchants.compat.groovyscript;

import com.cleanroommc.groovyscript.documentation.linkgenerator.BasicLinkGenerator;
import com.invadermonky.bakedenchants.BakedEnchants;

public class GSLinkGenerator extends BasicLinkGenerator {
    @Override
    public String id() {
        return BakedEnchants.MOD_ID;
    }

    @Override
    protected String domain() {
        return "https://github.com/Invadermonky/BakedEnchants/";
    }

    @Override
    protected String version() {
        return BakedEnchants.MOD_VERSION;
    }

}
