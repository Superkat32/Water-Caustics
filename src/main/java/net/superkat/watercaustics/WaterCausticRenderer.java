package net.superkat.watercaustics;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.OverlayVertexConsumer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.superkat.watercaustics.animation.WaterCausticAnimationTicker;
import net.superkat.watercaustics.config.CausticConfig;
import org.joml.Matrix4f;

public class WaterCausticRenderer {

    public static void renderWaterCaustics(WorldRenderContext context) {
        ClientWorld world = context.world();
        MatrixStack matrixStack = context.matrixStack();
        Camera camera = context.camera();
        VertexConsumerProvider consumers = context.consumers();

        int radius = CausticConfig.renderRadius;
        int radiusY = CausticConfig.renderRadiusY;
        BlockPos currentPos = camera.getBlockPos();
        BlockPos minPos = currentPos.add(-radius, -radiusY, -radius);
        BlockPos maxPos = currentPos.add(radius, radiusY, radius);

        boolean consumerCreated = false;
        VertexConsumerProvider.Immediate immediate = null;
        RenderLayer renderLayer = null;
        VertexConsumer consumer = null;

        for (BlockPos pos : BlockPos.iterate(minPos, maxPos)) {
            if(isWater(world, pos)) {
                if(shouldRenderWaterCaustic(world, pos)) {
//                    if(!consumerCreated) {
                    immediate = (VertexConsumerProvider.Immediate) consumers;
                    matrixStack.push();
                    Vec3d target = Vec3d.of(pos.add(0, -1, 0));
                    Vec3d transformedPos = target.subtract(camera.getPos());
                    matrixStack.translate(transformedPos);

//                    consumer = new OverlayVertexConsumer(
//                        consumers.getBuffer(WaterCausticAnimationTicker.WATER_CAUSTIC_RENDER_LAYER_FANCY.apply(Identifier.of(WaterCaustics.MOD_ID, "textures/animation/water1.png"))), matrixStack.peek(), 1f
//                    );
//                    consumer = WaterCausticAnimationTicker.getWaterCausticVertexConsumer(consumers);
                    consumer = new OverlayVertexConsumer(
                            consumers.getBuffer(WaterCausticAnimationTicker.WATER_CAUSTIC_FANCY_RENDER_LAYERS.get(WaterCausticAnimationTicker.frame)), matrixStack.peek(), 1.0F
                    );
                    consumerCreated = true;
//                    }

                    //TODO - glass check here
                    renderWaterCaustic(world, pos, camera, matrixStack, consumer);
                    matrixStack.pop();
                }
            }
        }

        if(consumerCreated) {
//            immediate.draw(renderLayer);
            immediate.draw();
        }
    }

    public static void renderWaterCaustic(ClientWorld world, BlockPos pos, Camera camera, MatrixStack matrixStack, VertexConsumer consumer) {
//        RenderSystem.disableCull();
//        RenderSystem.enableBlend();
//        RenderSystem.enableDepthTest();
//        MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager().enable();
//        camera = MinecraftClient.getInstance().gameRenderer.getCamera();

        matrixStack.push();

//        VertexConsumerProvider.Immediate immediate = (VertexConsumerProvider.Immediate) consumers;
//        VertexConsumer consumer = immediate.getBuffer(RenderLayer.getTranslucent());

//        render(world, pos, camera, matrixStack, consumer);
        BlockPos aPos = pos.add(0, -1, 0);
        BlockState state = world.getBlockState(aPos);
        BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
        BakedModel bakedModel = blockRenderManager.getModel(state);
        long l = state.getRenderingSeed(aPos);
//        Sprite sprite = WaterCaustics.WATER_CAUSTICS_SPRITE.getSprite();
//        int u = sprite.getX();
//        int v = sprite.getY();
//        float u1 = sprite.getFrameU(0.0F);
//        float u2 = sprite.getFrameU(1.0F);
//        float v1 = sprite.getFrameV(0.0F);
//        float v2 = sprite.getFrameV(1.0F);

//        sprite.getTextureSpecificVertexConsumer()

//        Vec3d target = Vec3d.of(aPos);
//        Vec3d transformedPos = target.subtract(camera.getPos());
//        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
//        matrixStack.translate(transformedPos);
        blockRenderManager.getModelRenderer().render(world, bakedModel, state, aPos, matrixStack, consumer, true, Random.create(), l, OverlayTexture.DEFAULT_UV);

//        immediate.draw(RenderLayer.getTranslucent());

        matrixStack.pop();

//        RenderSystem.disableBlend();
//        RenderSystem.enableCull();
//        MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager().disable();
    }

    public static boolean shouldRenderWaterCaustic(ClientWorld world, BlockPos pos) {
        return !isWater(world, pos, Direction.DOWN);
    }

    private static boolean isWater(ClientWorld world, BlockPos pos) {
        return world.isWater(pos);
    }

    private static boolean isWater(ClientWorld world, BlockPos pos, Direction offsetDirection) {
        return isWater(world, pos.offset(offsetDirection));
    }

    public static void render(ClientWorld world, BlockPos pos, Camera camera, MatrixStack matrixStack, VertexConsumer vertexConsumer) {
//        float x = (float)(pos.getX() & 15);
//        float y = (float)(pos.getY() & 15);
//        float z = (float)(pos.getZ() & 15);

//        float x = (float)(pos.getX());
//        float y = (float)(pos.getY()) - 1;
//        float z = (float)(pos.getZ());

        float o = 1.0F;
        float p = 1.0F;
        float q = 1.0F;
        float r = 1.0F;

        float brightness = world.getBrightness(Direction.UP, true);
        int waterColor = BiomeColors.getWaterColor(world, pos);
//        waterColor = 16777215;
        waterColor = 4445678;
        //idk if this is actually hue but I wanted to keep the red/green/blue for later
        float redHue = (float)(waterColor >> 16 & 0xFF) / 255.0F;
        float greenHue = (float)(waterColor >> 8 & 0xFF) / 255.0F;
        float blueHue = (float)(waterColor & 0xFF) / 255.0F;

        float red = brightness * redHue;
        float green = brightness * greenHue;
        float blue = brightness * blueHue;

        int light = getLight(world, pos);

        Sprite sprite = WaterCaustics.WATER_CAUSTICS_SPRITE.getSprite();
        float u1 = sprite.getFrameU(0.0F);
        float u2 = sprite.getFrameU(1.0F);
        float v1 = sprite.getFrameV(0.0F);
        float v2 = sprite.getFrameV(1.0F);

        Vec3d target = Vec3d.of(pos);
        Vec3d transformedPos = target.subtract(camera.getPos());
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
        matrixStack.translate(transformedPos);

        Matrix4f posMatrix = matrixStack.peek().getPositionMatrix();

        vertex(posMatrix, vertexConsumer, 0, 0, 0, red, green, blue, u1, v1, light);
        vertex(posMatrix, vertexConsumer, 0, 0, 1, red, green, blue, u1, v2, light);
        vertex(posMatrix, vertexConsumer, 1, 0, 1, red, green, blue, u2, v2, light);
        vertex(posMatrix, vertexConsumer, 1, 0, 0, red, green, blue, u2, v1, light);
    }

    private static void vertex(Matrix4f posMatrix, VertexConsumer vertexConsumer, float x, float y, float z, float red, float green, float blue, float u, float v, int light) {
        vertexConsumer
                .vertex(posMatrix, x, y + 0.01f, z)
                .color(red, green, blue, 0.7f)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0f, 1f, 0f);
    }

    private static int getLight(ClientWorld world, BlockPos pos) {
        int posLight = WorldRenderer.getLightmapCoordinates(world, pos);
        int aboveLight = WorldRenderer.getLightmapCoordinates(world, pos.up());
        int k = posLight & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 15);
        int l = aboveLight & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 15);
        int m = posLight >> 16 & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 15);
        int n = aboveLight >> 16 & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 15);
        return (k > l ? k : l) | (m > n ? m : n) << 16;
    }

//    public static void renderWaterCaustic(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, int light) {
//        if(shouldRenderWaterCaustic(world, pos)) {
//            //used for checking glass stuff
//            boolean shouldRender = true;
//            if(isGlassBeneath(world, pos)) {
//                shouldRender = false;
//                int glassCheckLength = 12;
//                for (int i = 1; i <= glassCheckLength; i++) {
//                    BlockState beneathState = world.getBlockState(pos.offset(Direction.DOWN, i + 1));
//                    if(!beneathState.isAir()) {
//                        //if beneath state is water, break out of loop but don't render
//                        if(!stateIsWater(beneathState)) {
//                            shouldRender = true;
//                            pos = pos.offset(Direction.DOWN, i);
//                        }
//                        break;
//                    }
//                }
//            }
//            if(shouldRender) {
//                render(world, pos, vertexConsumer, blockState, fluidState, light);
//            }
//        }
//    }
//
//
//    public static boolean shouldRenderWaterCaustic(BlockRenderView world, BlockPos pos) {
//        return !isWater(world, pos, Direction.DOWN);
//    }
//
//    private static boolean isWater(BlockRenderView world, BlockPos pos) {
//        BlockState blockState = world.getBlockState(pos);
//        return stateIsWater(blockState);
////        FluidState fluidState = blockState.getFluidState();
////        return fluidState.isIn(FluidTags.WATER);
//    }
//
//    private static boolean isWater(BlockRenderView world, BlockPos pos, Direction offsetDirection) {
//        return isWater(world, pos.offset(offsetDirection));
////        BlockState blockState = world.getBlockState(pos.offset(offsetDirection));
////        FluidState fluidState = blockState.getFluidState();
////        return fluidState.isIn(FluidTags.WATER);
//    }
//
//    private static boolean stateIsWater(BlockState blockState) {
//        FluidState fluidState = blockState.getFluidState();
//        return fluidState.isIn(FluidTags.WATER);
//    }
//
//    private static boolean isGlassBeneath(BlockRenderView world, BlockPos pos) {
//        BlockState blockState = world.getBlockState(pos.offset(Direction.DOWN));
//        return blockState.isIn(BlockTags.IMPERMEABLE);
//    }
//
//    private static void render(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, int light) {
//        float x = (float)(pos.getX() & 15);
//        float y = (float)(pos.getY() & 15) - 1;
//        float z = (float)(pos.getZ() & 15);
//
////        Fluid fluid = fluidState.getFluid();
////		float n = this.getFluidHeight(world, fluid, pos, blockState, fluidState);
//        float o = 1.0F;
//        float p = 1.0F;
//        float q = 1.0F;
//        float r = 1.0F;
//
//        float brightness = world.getBrightness(Direction.UP, true);
//        int waterColor = BiomeColors.getWaterColor(world, pos);
//        //idk if this is actually hue but I wanted to keep the red/green/blue for later
//        float redHue = (float)(waterColor >> 16 & 0xFF) / 255.0F;
//        float greenHue = (float)(waterColor >> 8 & 0xFF) / 255.0F;
//        float blueHue = (float)(waterColor & 0xFF) / 255.0F;
//
//        float red = brightness * redHue;
//        float green = brightness * greenHue;
//        float blue = brightness * blueHue;
//
//        Sprite sprite = WaterCaustics.WATER_CAUSTICS_SPRITE.getSprite();
//        float u1 = sprite.getFrameU(0.0F);
//        float u2 = sprite.getFrameU(1.0F);
//        float v1 = sprite.getFrameV(0.0F);
//        float v2 = sprite.getFrameV(1.0F);
//
//        //adding zero for readability here
//        vertex(vertexConsumer, x + 0, y + p, z + 0, red, green, blue, u1, v1, light);
//        vertex(vertexConsumer, x + 0, y + r, z + 1, red, green, blue, u1, v2, light);
//        vertex(vertexConsumer, x + 1, y + q, z + 1, red, green, blue, u2, v2, light);
//        vertex(vertexConsumer, x + 1, y + o, z + 0, red, green, blue, u2, v1, light);
//    }
//
//    private static void vertex(VertexConsumer vertexConsumer, float x, float y, float z, float red, float green, float blue, float u, float v, int light) {
//        vertexConsumer.vertex(x, y, z).color(0.4f, 0.5f, 1f, 0.7f).texture(u, v).light(light).normal(0.0F, 1.0F, 0.0F);
//    }
}
