package net.superkat.watercaustics.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.superkat.watercaustics.WaterCaustics;
import net.superkat.watercaustics.config.CausticConfig;

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

        Sprite sprite = WaterCaustics.SPRITE.getSprite();
        float u1 = sprite.getFrameU(0.0F);
        float u2 = sprite.getFrameU(1.0F);
        float v1 = sprite.getFrameV(0.0F);
        float v2 = sprite.getFrameV(1.0F);

        vertex(vertexConsumer, x + 0, y, z + 0, u1, v1, light);
        vertex(vertexConsumer, x + 0, y, z + 1, u1, v2, light);
        vertex(vertexConsumer, x + 1, y, z + 1, u2, v2, light);
        vertex(vertexConsumer, x + 1, y, z + 0, u2, v1, light);
    }

//    private static void renderFabulous(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, int light) {
//        MinecraftClient client = MinecraftClient.getInstance();
//        Camera camera = client.gameRenderer.getCamera();
//
//        Vec3d target = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
//        Vec3d transPos = target.subtract(camera.getPos());
//        MatrixStack matrices = new MatrixStack();
//        matrices.multiply(RotationAxis.POSITIVE_X.rotation(camera.getPitch()));
//        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(camera.getYaw() + 180f));
//        matrices.translate(transPos);
//
//        BakedModel model = client.getBlockRenderManager().getModel(blockState);
//
//        client.getBlockRenderManager()
//                .getModelRenderer()
//                .render(
//                        world,
//                        model,
//                        blockState,
//                        pos,
//                        matrices,
//                        vertexConsumer,
//                        false,
//                        Random.create(),
//                        blockState.getRenderingSeed(pos),
//                        OverlayTexture.DEFAULT_UV
//                );
//    }

    private static void vertex(VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, int light) {
        vertexConsumer
                .vertex(x, y, z)
                .color(0.9F, 0.95F, 1.0F, 0.35f)
                .texture(u, v)
                .light(light)
                .normal(0.0F, 1.0F, 0.0F);
    }
}
