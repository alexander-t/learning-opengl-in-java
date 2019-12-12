package se.tarlinder.opengl;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

    private long window;
    private Model model;
    private Texture texture;

    public Main() {

        init();

        float[] vertices = new float[]{
                -0.5f, 0.5f, 0,
                0.5f, 0.5f, 0,
                0.5f, -0.5f, 0,

                0.5f, -0.5f, 0,
                -0.5f, -0.5f, 0,
                -0.5f, 0.5f, 0
        };

        float[] textureCoords = new float[]{
                0, 0, // Top right triangle
                1, 0,
                1, 1,

                1, 1, // Bottom left triangle
                0, 1,
                0, 0
        };

        model = new Model(vertices, textureCoords);
        texture = new Texture("/brick.png");

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

        window = glfwCreateWindow(640, 480, "OpenGL", 0, 0);
        if (window == 0) {
            throw new IllegalStateException("glfwCreateWindow()");
        }

        // Center the window
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - 640) / 2, (videoMode.height() - 480) / 2);

        // Mandatory: make the context current on the calling thread
        glfwMakeContextCurrent(window);

        //T he minimum number of screen updates to wait for until the buffers are swapped by glfwSwapBuffers. Follow vertical sync basically.
        glfwSwapInterval(1);
        glfwShowWindow(window);

        // Mandatory: Must be called prior to calling any pure glxxx methods.
        GL.createCapabilities();

        // Needed by the fixed pipeline, otherwise the texture won't be shown.
        glEnable(GL_TEXTURE_2D);
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        texture.bind();
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