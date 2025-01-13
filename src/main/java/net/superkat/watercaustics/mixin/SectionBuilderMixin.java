package net.superkat.watercaustics.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.BlockBufferAllocatorStorage;
import net.minecraft.client.render.chunk.SectionBuilder;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.superkat.watercaustics.WaterCaustics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(SectionBuilder.class)
public abstract class SectionBuilderMixin {
    @WrapOperation(
            method = "build",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/BlockRenderManager;renderFluid(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)V"
            )
    )
    private void renderCaustics(
            BlockRenderManager instance,
            BlockPos pos,
            BlockRenderView world,
            VertexConsumer vertexConsumer,
            BlockState blockState,
            FluidState fluidState,
            Operation<Void> original,
            @Local Map<RenderLayer, BufferBuilder> map,
            @Local(argsOnly = true) BlockBufferAllocatorStorage allocatorStorage
    ) {
        original.call(instance, pos, world, vertexConsumer, blockState, fluidState);

        WaterCaustics.renderWaterCaustic(world, pos, vertexConsumer, blockState, fluidState, WaterCaustics.getLight(world, pos));
    }
}
