package net.superkat.watercaustics.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import eu.midnightdust.lib.config.MidnightConfig;
import net.superkat.watercaustics.WaterCaustics;

public class ModMenuInt implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> CausticConfig.getScreen(parent, WaterCaustics.MOD_ID);
    }
}
