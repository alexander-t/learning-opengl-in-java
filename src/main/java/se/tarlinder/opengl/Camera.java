package se.tarlinder.opengl;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position = new Vector3f(0, 0, 0);
    private Quaternionf rotation = new Quaternionf();
    private Matrix4f projection = new Matrix4f();

    // Used in temporary operations to avoid creating a new object
    private final Quaternionf tmpQuaternionf = new Quaternionf();
    private final Vector3f tmpVector3f = new Vector3f();

    public Camera(float width, float height) {
        // Just hard code some reasonable values for now
        projection.setPerspective((float) Math.toRadians(70), width / height, 0.01f, 1000f);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public Matrix4f getTransform() {
        Matrix4f transform = new Matrix4f();
        transform.rotate(rotation.conjugate(tmpQuaternionf));
        transform.translate(position.mul(-1, tmpVector3f));
        return transform;
    }
}
