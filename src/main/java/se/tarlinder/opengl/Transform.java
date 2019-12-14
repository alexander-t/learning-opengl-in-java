package se.tarlinder.opengl;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform {
    private Vector3f position = new Vector3f(0, 0, 0);
    private Quaternionf rotation = new Quaternionf();
    private Vector3f scale = new Vector3f(1);

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Matrix4f getTransformation() {
        Matrix4f transformation = new Matrix4f();
        transformation.translate(position);
        transformation.rotate(rotation);
        transformation.scale(scale);
        return transformation;
    }
}
