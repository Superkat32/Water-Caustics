package net.superkat.watercaustics.compat.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.caffeinemc.mods.sodium.fabric.render.FluidRendererImpl;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRendering;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.superkat.watercaustics.config.CausticConfig;
import net.superkat.watercaustics.render.WaterCausticRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//@Mixin(ChunkBuilderMeshingTask.class)
@Mixin(FluidRendererImpl.class)
public class FluidRendererImplMixin {

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/fabricmc/fabric/api/client/render/fluid/v1/FluidRendering;render(Lnet/fabricmc/fabric/api/client/render/fluid/v1/FluidRenderHandler;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;Lnet/fabricmc/fabric/api/client/render/fluid/v1/FluidRendering$DefaultRenderer;)V"
            )
    )
    private void renderWaterCaustics(
            FluidRenderHandler handler,
            BlockRenderView world,
            BlockPos pos,
            VertexConsumer vertexConsumer,
            BlockState blockState,
            FluidState fluidState,
            FluidRendering.DefaultRenderer defaultRenderer,
            Operation<Void> original
    ) {
        original.call(handler, world, pos, vertexConsumer, blockState, fluidState, defaultRenderer);
        if(CausticConfig.modEnabled) {
            WaterCausticRenderer.renderWaterCaustic(world, pos, vertexConsumer, blockState, fluidState, WaterCausticRenderer.getLight(world, pos));
        }
    }

//    @WrapOperation(
//            method = "execute(Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lnet/caffeinemc/mods/sodium/client/util/task/CancellationToken;)Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/FluidRenderer;render(Lnet/caffeinemc/mods/sodium/client/world/LevelSlice;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/caffeinemc/mods/sodium/client/render/chunk/translucent_sorting/TranslucentGeometryCollector;Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildBuffers;)V"
//            )
//    )
//    private void renderWaterCaustics(
//            FluidRenderer instance,
//            LevelSlice levelSlice,
//            BlockState blockState,
//            FluidState fluidState,
//            BlockPos blockPos,
//            BlockPos modelOffset,
//            TranslucentGeometryCollector translucentGeometryCollector,
//            ChunkBuildBuffers chunkBuildBuffers,
//            Operation<Void> original
//    ) {
//        original.call(instance, levelSlice, blockState, fluidState, blockPos, modelOffset, translucentGeometryCollector, chunkBuildBuffers);
//        if(CausticConfig.modEnabled) {
//            WaterCausticRenderer.renderWaterCaustic(levelSlice, blockPos, vertexConsumer, blockState, fluidState, WaterCausticRenderer.getLight(levelSlice, blockPos));
//        }
//    }


}
