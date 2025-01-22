package net.superkat.watercaustics;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.superkat.watercaustics.config.CausticConfig;

public class WaterCaustics implements ModInitializer {
    public static final String MOD_ID = "watercaustics";

    @SuppressWarnings("deprecation")
    public static final SpriteIdentifier SPRITE = new SpriteIdentifier(
            SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
            Identifier.of(WaterCaustics.MOD_ID, "block/watercaustic")
    );

    @Override
    public void onInitialize() {
        CausticConfig.init(MOD_ID, CausticConfig.class);
    }
}
