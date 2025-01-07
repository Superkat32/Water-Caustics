package net.superkat.watercaustics.render;

import net.minecraft.client.render.SpriteTexturedVertexConsumer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class OverlaySpriteVertexConsumer extends SpriteTexturedVertexConsumer {
    private final VertexConsumer delegate;
    private final Sprite sprite;

    private final Matrix4f inverseTextureMatrix;
    private final Matrix3f inverseNormalMatrix;
    private final float textureScale;
    private final Vector3f normal = new Vector3f();
    private final Vector3f pos = new Vector3f();
    private float x;
    private float y;
    private float z;

    public OverlaySpriteVertexConsumer(VertexConsumer delegate, MatrixStack.Entry matrix, Sprite sprite) {
        super(delegate, sprite);
        this.delegate = delegate;
        this.sprite = sprite;
        this.inverseTextureMatrix = new Matrix4f(matrix.getPositionMatrix()).invert();
        this.inverseNormalMatrix = new Matrix3f(matrix.getNormalMatrix()).invert();
        this.textureScale = 1f;
    }

    @Override
    public VertexConsumer vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return super.vertex(x, y, z);
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        return super.texture(u, v);
//        return this.delegate.texture(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        super.normal(x, y, z);
        Vector3f vector3f = this.inverseNormalMatrix.transform(x, y, z, this.pos);
        Direction direction = Direction.getFacing(vector3f.x(), vector3f.y(), vector3f.z());
        Vector3f vector3f2 = this.inverseTextureMatrix.transformPosition(this.x, this.y, this.z, this.normal);
        vector3f2.rotateY((float) Math.PI);
        vector3f2.rotateX((float) (Math.PI));
        vector3f2.rotate(direction.getRotationQuaternion());
        float trueU = -vector3f2.x() * textureScale;
        float trueV = -vector3f2.y() * textureScale;
        super.texture(trueU, trueV);
        return this;
    }

    @Override
    public void vertex(float x, float y, float z, int color, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
//        Vector3f vector3f = this.inverseNormalMatrix.transform(x, y, z, this.pos);
//        Direction direction = Direction.getFacing(vector3f.x(), vector3f.y(), vector3f.z());
//        Vector3f vector3f2 = this.inverseTextureMatrix.transformPosition(this.x, this.y, this.z, this.normal);
//        vector3f2.rotateY((float) Math.PI);
//        vector3f2.rotateX((float) (-Math.PI / 2));
//        vector3f2.rotate(direction.getRotationQuaternion());
//        float trueU = -vector3f2.x() * this.textureScale;
//        float trueV = -vector3f2.y() * this.textureScale;
//        this.delegate.vertex(x, y, z, color, trueU, trueV, overlay, light, normalX, normalY, normalZ);
        this.vertex(x, y, z);
        this.color(color);
//        this.texture(u, v);
        this.overlay(overlay);
        this.light(light);
        this.normal(normalX, normalY, normalZ);
    }
}
