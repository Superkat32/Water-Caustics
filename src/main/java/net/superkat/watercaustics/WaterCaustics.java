package net.superkat.watercaustics;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
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
