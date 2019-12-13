package se.tarlinder.opengl;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

    private static final int SCREEN_WIDTH = 640;
    private static final int SCREEN_HEIGHT = 480;

    private long window;
    private Model model;
    private Texture texture;
    private Shader shader;
    private Matrix4f projection;

    public Main() {

        init();

        float[] vertices = new float[]{
                -0.5f, 0.5f, 0,
                0.5f, 0.5f, 0,
                0.5f, -0.5f, 0,
                -0.5f, -0.5f, 0
        };

        float[] textureCoords = new float[]{
                0, 0,
                1, 0,
                1, 1,
                0, 1,
        };

        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };

        model = new Model(vertices, textureCoords, indices);
        texture = new Texture("/textures/brick.png");
        shader = new Shader("shader");
        projection = new Matrix4f().ortho2D(-SCREEN_WIDTH / 2, SCREEN_WIDTH / 2, -SCREEN_HEIGHT / 2, SCREEN_HEIGHT / 2).scale(128);

        while (!glfwWindowShouldClose(window)) {
            update();
            render();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        glfwTerminate();
    }

    private void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("glfwInit()");
        }

        // Reset all hints to their default values. Not strictly necessary, but ensures a clean startup.
        glfwDefaultWindowHints();
        // Set API level to OpenGL 3.1. It's not obvious why a higher level isn't supported
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        // Start with invisible window at this point. Just cosmetics.
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        // Window isn't resizable
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(SCREEN_WIDTH, SCREEN_HEIGHT, "OpenGL", 0, 0);
        if (window == 0) {
            throw new IllegalStateException("glfwCreateWindow()");
        }

        // Center the window
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - SCREEN_WIDTH) / 2, (videoMode.height() - SCREEN_HEIGHT) / 2);

        // Mandatory: make the context current on the calling thread
        glfwMakeContextCurrent(window);

        //The minimum number of screen updates to wait for until the buffers are swapped by glfwSwapBuffers. Follow vertical sync basically.
        glfwSwapInterval(1);
        glfwShowWindow(window);

        // Mandatory: Must be called prior to calling any pure glxxx methods.
        GL.createCapabilities();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", projection);
        texture.bind(0);
        model.render();
    }

    private void update() {
        if (glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS) {
            // Do something fun here
        }
    }

    public static void main(String[] argv) {
        new Main();
    }

}
