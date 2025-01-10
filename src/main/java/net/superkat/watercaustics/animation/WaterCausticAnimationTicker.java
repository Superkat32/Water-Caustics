package net.superkat.watercaustics.animation;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;
import net.superkat.watercaustics.WaterCaustics;
import net.superkat.watercaustics.config.CausticConfig;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WaterCausticAnimationTicker {

    //put all frames here
    //handle frame ticking here
    //handle giving correct frame here
    //give vertexconsumer here

    public static int tick = 0;
    public static int frame = 0;

    public static final Function<Identifier, RenderLayer> WATER_CAUSTIC_RENDER_LAYER = Util.memoize(
            texture -> {
                RenderPhase.Texture texture2 = new RenderPhase.Texture(texture, TriState.FALSE, false);
                return RenderLayer.of(
                        "water_caustic",
                        VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
                        VertexFormat.DrawMode.QUADS,
                        786432,
                        true,
                        true,
                        RenderLayer.MultiPhaseParameters.builder()
                                .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                                .program(RenderPhase.TRANSLUCENT_PROGRAM)
                                .texture(texture2)
                                .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
                                .target(RenderPhase.TRANSLUCENT_TARGET)
                                .build(true)
                );
            }
    );

    public static final Function<Identifier, RenderLayer> WATER_CAUSTIC_RENDER_LAYER_FANCY = Util.memoize(
            texture -> {
                RenderPhase.Texture texture2 = new RenderPhase.Texture(texture, TriState.FALSE, false);
                return RenderLayer.of(
                        "water_caustic_fancy",
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
            }
    );

    public static List<Identifier> WATER_CAUSTIC_FRAMES = IntStream.range(1, 16)
            .mapToObj(stage -> Identifier.of(WaterCaustics.MOD_ID, "textures/animation/water" + stage + ".png"))
            .collect(Collectors.toList());

    public static List<RenderLayer> WATER_CAUSTIC_RENDER_LAYERS = WATER_CAUSTIC_FRAMES.stream()
            .map(WATER_CAUSTIC_RENDER_LAYER).toList();

    public static List<RenderLayer> WATER_CAUSTIC_FANCY_RENDER_LAYERS = WATER_CAUSTIC_FRAMES.stream()
            .map(WATER_CAUSTIC_RENDER_LAYER_FANCY).toList();

    public static VertexConsumer getWaterCausticVertexConsumer(VertexConsumerProvider provider) {
        boolean fancy = CausticConfig.fancyRenderMode;
        RenderLayer layer;
        if(fancy) {
            layer = WATER_CAUSTIC_FANCY_RENDER_LAYERS.get(frame);
        } else {
            layer = WATER_CAUSTIC_RENDER_LAYERS.get(frame);
        }
        return provider.getBuffer(layer);
    }

    public static void tickAnimation() {
        tick++;
        if(tick >= CausticConfig.frameDelay) {
            tick = 0;
            frame++;
            if(frame >= WATER_CAUSTIC_FRAMES.size()) {
                frame = 0;
            }
        }
    }

}
