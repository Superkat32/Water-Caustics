package net.superkat.watercaustics;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.compress.utils.Lists;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class WaterCaustics implements ModInitializer {
	public static final String MOD_ID = "watercaustics";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier WATER_CAUSTICS_TEXTURE = Identifier.of(MOD_ID, "block/watercaustic");
	public static final SpriteIdentifier WATER_CAUSTICS_SPRITE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, WATER_CAUSTICS_TEXTURE);

	public static final RenderLayer WATER_CAUSTIC = RenderLayer.of(
			"water_caustic",
			VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
			VertexFormat.DrawMode.QUADS,
			786432,
			true,
			true,
			RenderLayer.MultiPhaseParameters.builder()
					.lightmap(RenderPhase.ENABLE_LIGHTMAP)
					.program(RenderPhase.TRANSLUCENT_PROGRAM)
					.texture(new RenderPhase.Texture(WATER_CAUSTICS_TEXTURE, TriState.FALSE, false))
					.transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
					.target(RenderPhase.TRANSLUCENT_TARGET)
					.build(true)
	);

	public static final Function<Identifier, RenderLayer> WATER_TWO = Util.memoize(
			(Function<Identifier, RenderLayer>)(texture -> {
				RenderPhase.Texture texture2 = new RenderPhase.Texture(texture, TriState.FALSE, false);
				return RenderLayer.of(
						"water_two",
						VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
						VertexFormat.DrawMode.QUADS,
						1536,
						false,
						true,
						RenderLayer.MultiPhaseParameters.builder()
								.program(RenderPhase.CRUMBLING_PROGRAM)
								.texture(texture2)
								.transparency(RenderLayer.CRUMBLING_TRANSPARENCY)
								.writeMaskState(RenderPhase.COLOR_MASK)
								.layering(RenderPhase.POLYGON_OFFSET_LAYERING)
								.build(false)
				);
			})
	);

	public static List<Identifier> WATER_CAUSTIC_ANIMATION = getWaterCausticAnimation();

	public static List<Identifier> getWaterCausticAnimation() {
		ArrayList<Identifier> arrayList = Lists.newArrayList();
		for (int i = 0; i < 15; i++) {
			arrayList.add(wc(i + 1));
		}
		return arrayList;
	}

	public static Identifier wc(int number) {
		return Identifier.of(MOD_ID, "textures/animation/water" + number + ".png");
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		WorldRenderEvents.AFTER_ENTITIES.register(WaterCausticRenderer::renderWaterCaustics);

//		WorldRenderEvents.END.register(context -> {
//			Camera camera = context.camera();
//			Vec3d target = new Vec3d(0, -55, 0);
//			Vec3d transPos = target.subtract(camera.getPos());
//
//			MatrixStack matrixStack = new MatrixStack();
//			matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
//			matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
//			matrixStack.translate(transPos);
//
//			Matrix4f posMatrix = matrixStack.peek().getPositionMatrix();
//			VertexConsumerProvider.Immediate immediate = (VertexConsumerProvider.Immediate) context.consumers();
//			VertexConsumer buffer = immediate.getBuffer(RenderLayer.getTranslucent());
////			Tessellator tessellator = Tessellator.getInstance();
////			BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
//			buffer.vertex(posMatrix, 0, 1, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f).light(15).normal(0f, 1f, 0f);
//			buffer.vertex(posMatrix, 0, 0, 0).color(1f, 0f, 0f, 1f).texture(0f, 1f).light(15).normal(0f, 1f, 0f);
//			buffer.vertex(posMatrix, 1, 0, 0).color(0f, 1f, 0f, 1f).texture(1f, 1f).light(15).normal(0f, 1f, 0f);
//			buffer.vertex(posMatrix, 1, 1, 0).color(0f, 0f, 1f, 1f).texture(1f, 0f).light(15).normal(0f, 1f, 0f);
//
////			RenderSystem.setShaderTexture(0, WATER_CAUSTICS_TEXTURE);
//			RenderSystem.setShaderColor(1, 1,1 ,1);
//			RenderSystem.disableCull();
////			BuiltBuffer builtBuffer = buffer.endNullable();
////			if (builtBuffer != null) {
////				BufferRenderer.drawWithGlobalProgram(builtBuffer);
////			}
//			immediate.draw();
//			RenderSystem.enableCull();
//		});

	}
}