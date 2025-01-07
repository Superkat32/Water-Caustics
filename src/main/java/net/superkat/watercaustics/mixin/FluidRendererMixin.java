package net.superkat.watercaustics.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.superkat.watercaustics.WaterCausticRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin {
//	@ModifyArg(method = "vertex", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;vertex(FFF)Lnet/minecraft/client/render/VertexConsumer;"), index = 1)
//	private float injected(float y) {
//		return y + 5;
//	}

//	@Shadow protected abstract void vertex(VertexConsumer vertexConsumer, float f, float g, float h, float i, float j, float k, float l, float m, int n);

	@Shadow protected abstract float getFluidHeight(BlockRenderView world, Fluid fluid, BlockPos pos, BlockState blockState, FluidState fluidState);

	@Shadow @Final private Sprite[] waterSprites;

	@Shadow protected abstract int getLight(BlockRenderView world, BlockPos pos);

	@Inject(method = "render", at = @At(value = "TAIL"))
	public void renderWaterCaustic(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, CallbackInfo ci) {

//		if(fluidState.isIn(FluidTags.WATER)) {
//			WaterCausticRenderer.renderWaterCaustic(world, pos, vertexConsumer, blockState, fluidState, this.getLight(world, pos));
//		}

//		float x = (float)(pos.getX() & 15);
//		float y = (float)(pos.getY() & 15);
//		float z = (float)(pos.getZ() & 15);
//
//		Fluid fluid = fluidState.getFluid();
////		float n = this.getFluidHeight(world, fluid, pos, blockState, fluidState);
//        float o = 1.0F;
//        float p = 1.0F;
//        float q = 1.0F;
//        float r = 1.0F;
//
//		float k = world.getBrightness(Direction.UP, true);
//		int i = BiomeColors.getWaterColor(world, pos);
//		float f = (float)(i >> 16 & 0xFF) / 255.0F;
//		float g = (float)(i >> 8 & 0xFF) / 255.0F;
//		float h = (float)(i & 0xFF) / 255.0F;
//
//		float red = k * f;
//		float green = k * g;
//		float blue = k * h;
//
////		Sprite sprite = waterSprites[0];
//		Sprite sprite = WaterCaustics.WATER_CAUSTICS_SPRITE.getSprite();
//		float u1 = sprite.getFrameU(0.0F);
//		float v1 = sprite.getFrameV(0.0F);
//		float u2 = u1;
//		float v2 = sprite.getFrameV(1.0F);
//		float u3 = sprite.getFrameU(1.0F);
//		float v3 = v2;
//		float u4 = u3;
//		float v4 = v1;
//
//		int light = this.getLight(world, pos);
//
//		this.vertex(vertexConsumer, x + 0.0F, y + p, z + 0.0F, red, green, blue, u1, v1, light);
//		this.vertex(vertexConsumer, x + 0.0F, y + r, z + 1.0F, red, green, blue, u2, v2, light);
//		this.vertex(vertexConsumer, x + 1.0F, y + q, z + 1.0F, red, green, blue, u3, v3, light);
//		this.vertex(vertexConsumer, x + 1.0F, y + o, z + 0.0F, red, green, blue, u4, v4, light);
	}

//	@Unique
//	private void vertex(VertexConsumer vertexConsumer, float x, float y, float z, float red, float green, float blue, float u, float v, int light) {
//		vertexConsumer.vertex(x, y + 5, z).color(red, green, blue, 1.0F).texture(u, v).light(light).normal(0.0F, 1.0F, 0.0F);
//	}
}