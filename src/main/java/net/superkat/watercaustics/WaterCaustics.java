package net.superkat.watercaustics;

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

public class WaterCaustics {
    public static final String MOD_ID = "watercaustics";

    @SuppressWarnings("deprecation")
    public static final SpriteIdentifier SPRITE = new SpriteIdentifier(
            SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
            Identifier.of(WaterCaustics.MOD_ID, "block/watercaustic")
    );

    public static int getLight(BlockRenderView world, BlockPos pos) {
        int posLight = WorldRenderer.getLightmapCoordinates(world, pos);
        int aboveLight = WorldRenderer.getLightmapCoordinates(world, pos.up());
        int k = posLight & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 0xF);
        int l = aboveLight & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 0xF);
        int m = posLight >> 16 & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 0xF);
        int n = aboveLight >> 16 & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 0xF);

        return (Math.max(k, l)) | (Math.max(m, n)) << 16;
    }

    public static void renderWaterCaustic(BlockRenderView world, BlockPos pos, VertexConsumer consumer, BlockState blockState, FluidState fluidState, int light) {
        if (!isWater(world, pos)) return;
        if (!shouldRenderWaterCaustic(world, pos)) return;

        //used for checking glass stuff
        boolean shouldRender = true;

        if(isGlassBeneath(world, pos)) {
            shouldRender = false;
            int glassCheckLength = 12;
            for (int i = 1; i <= glassCheckLength; i++) {
                BlockState beneathState = world.getBlockState(pos.offset(Direction.DOWN, i + 1));
                if(!beneathState.isAir()) {
                    //if beneath state is water, break out of loop but don't render
                    if(!stateIsWater(beneathState)) {
                        shouldRender = true;
                        pos = pos.offset(Direction.DOWN, i);
                    }
                    break;
                }
            }
        }

        if(!shouldRender) return;
        render(world, pos, consumer, blockState, fluidState, light);
    }


    public static boolean shouldRenderWaterCaustic(BlockRenderView world, BlockPos pos) {
        return !isWater(world, pos, Direction.DOWN);
    }

    private static boolean isWater(BlockRenderView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return stateIsWater(blockState);
    }

    private static boolean isWater(BlockRenderView world, BlockPos pos, Direction offsetDirection) {
        return isWater(world, pos.offset(offsetDirection));
    }

    private static boolean stateIsWater(BlockState blockState) {
        FluidState fluidState = blockState.getFluidState();
        return fluidState.isIn(FluidTags.WATER);
    }

    private static boolean isGlassBeneath(BlockRenderView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.offset(Direction.DOWN));
        return blockState.isIn(BlockTags.IMPERMEABLE);
    }


    private static void render(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, int light) {
        float x = (float)(pos.getX() & 15);
        float y = (float)(pos.getY() & 15) - 1;
        float z = (float)(pos.getZ() & 15);

        float o = 1.0F;
        float p = 1.0F;
        float q = 1.0F;
        float r = 1.0F;

        Sprite sprite = SPRITE.getSprite();
        float u1 = sprite.getFrameU(0.0F);
        float u2 = sprite.getFrameU(1.0F);
        float v1 = sprite.getFrameV(0.0F);
        float v2 = sprite.getFrameV(1.0F);

        //adding zero for readability here
        vertex(vertexConsumer, x + 0, y + p, z + 0, u1, v1, light);
        vertex(vertexConsumer, x + 0, y + r, z + 1, u1, v2, light);
        vertex(vertexConsumer, x + 1, y + q, z + 1, u2, v2, light);
        vertex(vertexConsumer, x + 1, y + o, z + 0, u2, v1, light);
    }

    private static void vertex(VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, int light) {
        vertexConsumer
                .vertex(x, y, z)
                .color(0.9F, 0.95F, 1.0F, 0.35f)
                .texture(u, v)
                .light(light)
                .normal(0.0F, 1.0F, 0.0F);
    }
}
