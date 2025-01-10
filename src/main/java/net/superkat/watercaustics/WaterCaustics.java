package net.superkat.watercaustics;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.superkat.watercaustics.animation.WaterCausticAnimationTicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaterCaustics implements ModInitializer {
	public static final String MOD_ID = "watercaustics";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier WATER_CAUSTICS_TEXTURE = Identifier.of(MOD_ID, "block/watercaustic");
	public static final SpriteIdentifier WATER_CAUSTICS_SPRITE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, WATER_CAUSTICS_TEXTURE);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			WaterCausticAnimationTicker.tickAnimation();
		});
//		WorldRenderEvents.AFTER_ENTITIES.register(WaterCausticRenderer::renderWaterCaustics);

//		WorldRenderEvents.END.register(context -> {
//			MinecraftClient client = MinecraftClient.getInstance();
//			World world = client.world;
//			BlockPos pos = new BlockPos(-3, -60, -5);
//			BlockState state = world.getBlockState(pos);
//
//			Camera camera = client.gameRenderer.getCamera();
////			Vec3d target = new Vec3d(0, -55, 0);
//			Vec3d target = new Vec3d(-3, -60, -5);
//			Vec3d transPos = target.subtract(camera.getPos());
//
//			MatrixStack matrixStack = new MatrixStack();
//			matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
//			matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
//			matrixStack.translate(transPos);
//
//			BakedModel model = client.getBlockRenderManager().getModel(state);
//
////			VertexConsumer consumer = WATER_CAUSTICS_SPRITE.getSprite().getTextureSpecificVertexConsumer(context.consumers().getBuffer(RenderLayers.getMovingBlockLayer(state)));
////			VertexConsumer consumer = TexturedRenderLayers.CHRISTMAS.getVertexConsumer(context.consumers(), RenderLayer::getEntitySolid, true, true);
//			VertexConsumer consumer = WATER_CAUSTICS_SPRITE.getVertexConsumer(context.consumers(), RenderLayer::getEntitySolid);
////			VertexConsumer consumer = context.consumers().getBuffer(RenderLayers.getMovingBlockLayer(state));
//
////			VertexConsumer consumer = WATER_CAUSTICS_SPRITE.getSprite().getTextureSpecificVertexConsumer(
////					ItemRenderer.getItemGlintConsumer(
////						context.consumers(),
////						RenderLayer.getArmorCutoutNoCull(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE),
////							false,
////							false
////					)
////			);
//
//			client.getBlockRenderManager()
//					.getModelRenderer()
//					.render(
//							world,
//							model,
//							state,
//							pos,
//							matrixStack,
//							consumer,
//							false,
//							Random.create(),
//							state.getRenderingSeed(pos),
//							OverlayTexture.DEFAULT_UV
//					);
//
////			renderTest(consumer);
//
//		});

	}

	public static void renderTest(VertexConsumer buffer) {
//		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
//		Vec3d target = new Vec3d(0, -55, 0);
//		Vec3d transPos = target.subtract(camera.getPos());
//
//		MatrixStack matrixStack = new MatrixStack();
////		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
////		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
//		matrixStack.translate(transPos);
//
//		Matrix4f posMatrix = matrixStack.peek().getPositionMatrix();
////		VertexConsumerProvider.Immediate immediate = (VertexConsumerProvider.Immediate) context.consumers();
////		VertexConsumer buffer = immediate.getBuffer(RenderLayer.getTranslucent());
////			Tessellator tessellator = Tessellator.getInstance();
////			BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
//		int uv = OverlayTexture.DEFAULT_UV;
//		Sprite sprite = WATER_CAUSTICS_SPRITE.getSprite();
//		buffer.vertex(posMatrix, 0, 1, 0).color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMinV()).overlay(uv).light(15).normal(0f, 1f, 0f);
//		buffer.vertex(posMatrix, 0, 0, 0).color(1f, 0f, 0f, 1f).texture(sprite.getMinU(), sprite.getMaxV()).overlay(uv).light(15).normal(0f, 1f, 0f);
//		buffer.vertex(posMatrix, 1, 0, 0).color(0f, 1f, 0f, 1f).texture(sprite.getMaxU(), sprite.getMaxV()).overlay(uv).light(15).normal(0f, 1f, 0f);
//		buffer.vertex(posMatrix, 1, 1, 0).color(0f, 0f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMinV()).overlay(uv).light(15).normal(0f, 1f, 0f);
//
////			RenderSystem.setShaderTexture(0, WATER_CAUSTICS_TEXTURE);
////		RenderSystem.setShaderColor(1, 1,1 ,1);
////		RenderSystem.disableCull();
////			BuiltBuffer builtBuffer = buffer.endNullable();
////			if (builtBuffer != null) {
////				BufferRenderer.drawWithGlobalProgram(builtBuffer);
////			}
////		RenderSystem.enableCull();
	}
}