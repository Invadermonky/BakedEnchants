package com.invadermonky.bakedenchants.compat.groovyscript;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;

public class GSContainer extends GroovyPropertyContainer {
    public final BakedEnchants BakedEnchants = new BakedEnchants();

    public GSContainer() {
        this.addProperty(BakedEnchants);
    }
}
