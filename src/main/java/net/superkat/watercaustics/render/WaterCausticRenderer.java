package net.superkat.watercaustics.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.BlockRenderView;
import net.superkat.watercaustics.WaterCaustics;
import net.superkat.watercaustics.config.CausticConfig;
import org.joml.Matrix4f;

import java.awt.*;

public class WaterCausticRenderer {

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

        if(CausticConfig.renderBeneathGlass && isGlassBeneath(world, pos)) {
            shouldRender = false;
            int glassCheckLength = CausticConfig.glassFalloffDistance;
            for (int i = 1; i <= glassCheckLength; i++) {
                BlockState beneathState = world.getBlockState(pos.offset(Direction.DOWN, i + 1));
                if(!beneathState.isAir() && !stateIsGlass(beneathState)) {
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
        boolean fancy = CausticConfig.fancyRendering;
        render(world, pos, consumer, blockState, fluidState, light, fancy);
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
        return stateIsGlass(blockState);
    }

    private static boolean stateIsGlass(BlockState blockState) {
        return blockState.isIn(BlockTags.IMPERMEABLE);
    }

    private static void render(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, int light, boolean fancy) {
        float x = (float)(pos.getX() & 15);
        float y = (float)(pos.getY() & 15);
        float z = (float)(pos.getZ() & 15);

        Color causticColor = Color.decode(CausticConfig.causticColor);
        float red, green, blue;
        float waterRed = 1, waterGreen = 1, waterBlue = 1;
        float causticRed, causticGreen, causticBlue;

        causticRed = causticColor.getRed() / 255f;
        causticGreen = causticColor.getGreen() / 255f;
        causticBlue = causticColor.getBlue() / 255f;
        if(!CausticConfig.colorOverride) {
            int waterColor = BiomeColors.getWaterColor(world, pos);
            waterRed = (float)(waterColor >> 16 & 0xFF) / 255.0F;
            waterGreen = (float)(waterColor >> 8 & 0xFF) / 255.0F;
            waterBlue = (float)(waterColor & 0xFF) / 255.0F;
        }

        if(CausticConfig.customColorBlending) {
            red = waterRed * 0.5f * causticRed;
            green = waterGreen * 0.8f * causticGreen;
            blue = waterBlue * 0.8f * causticBlue;
        } else {
            red = waterRed * causticRed;
            green = waterGreen * causticGreen;
            blue = waterBlue * causticBlue;
        }

        Sprite sprite = WaterCaustics.SPRITE.getSprite();
        float u1 = sprite.getFrameU(0.0F);
        float u2 = sprite.getFrameU(1.0F);
        float v1 = sprite.getFrameV(0.0F);
        float v2 = sprite.getFrameV(1.0F);

        MatrixStack matrices = new MatrixStack();
        //.001 to avoid z-fighting - can't be too little otherwise it still z-fights at a distance(somehow)
        matrices.push();
        matrices.translate(x, y + 0.001f, z);

        //TODO - fix hackyness on the corners here (apparently possible from the initial translation only)
        //TODO - Check side blocks first before beginning to render this (look at Sodium's water rendering code)
        //TODO - Something to do with not calculating the colors every time this is rendered

        //This has not been fixed, in fact I'd say I made it worse because I know I'm going to update it later therefore I wasted time on it but whatever

        quad(vertexConsumer, matrices, 0, 0, 0, u1, u2, v1, v2, red, green, blue, light);
        if(CausticConfig.fancyRendering) {
            //south
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));
            quad(vertexConsumer, matrices, 0, 1.001f, 0, u1, u2, v1, v2, red, green, blue, light);

            //west
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90f));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90f));
            quad(vertexConsumer, matrices, -1, 0.001f, 0, u1, u2, v1, v2, red, green, blue, light);

            //north
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90f));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90f));
            quad(vertexConsumer, matrices, 0, 0.001f, -1, u1, u2, v1, v2, red, green, blue, light);

            //east
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90f));
            quad(vertexConsumer, matrices, 0, 1.001f, -1, u1, u2, v1, v2, red, green, blue, light);

            //bottom - 1.002 because of the .001 from the initial translation
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90f));
            quad(vertexConsumer, matrices, 0, 1.002f, 0, u1, u2, v1, v2, red, green, blue, light);

        }
        matrices.pop();
    }

    private static void quad(VertexConsumer vertexConsumer, MatrixStack matrices, float x, float y, float z, float u1, float u2, float v1, float v2, float red, float green, float blue, int light) {
        Matrix4f pos = matrices.peek().getPositionMatrix();
        vertex(vertexConsumer, pos, x + 0, y, z + 0, u1, v1, red, green, blue, light);
        vertex(vertexConsumer, pos, x + 0, y, z + 1, u1, v2, red, green, blue, light);
        vertex(vertexConsumer, pos, x + 1, y, z + 1, u2, v2, red, green, blue, light);
        vertex(vertexConsumer, pos, x + 1, y, z + 0, u2, v1, red, green, blue, light);
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f pos, float x, float y, float z, float u, float v, float red, float green, float blue, int light) {
        vertexConsumer
                .vertex(pos, x, y, z)
                .color(red, green, blue, 0.35f)
                .texture(u, v)
                .light(light)
                .normal(0.0F, 1.0F, 0.0F);
    }
}
