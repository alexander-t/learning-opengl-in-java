package se.tarlinder.opengl;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;

public class Texture {
    private static final int BYTES_RGBA = 4;
    private int id;
    private int width;
    private int height;

    public Texture(String resourcePath) {
        BufferedImage textureImage;
        try {
            textureImage = ImageIO.read(getClass().getResourceAsStream(resourcePath));
            width = textureImage.getWidth();
            height = textureImage.getHeight();

            int[] pixelsRaw = textureImage.getRGB(0, 0, width, height, null, 0, width);

            ByteBuffer pixelBuffer = BufferUtils.createByteBuffer(width * height * BYTES_RGBA);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = pixelsRaw[x * width + y];
                    pixelBuffer.put((byte) ((pixel >> 16) & 0xFF));
                    pixelBuffer.put((byte) ((pixel >> 8) & 0xFF));
                    pixelBuffer.put((byte) ((pixel) & 0xFF));
                    pixelBuffer.put((byte) ((pixel >> 24) & 0xFF)); // alpha
                }
            }
            // Mandatory: buffers passed to glxxx functions need to be flipped
            pixelBuffer.flip();

            // Generate an id for the texture
            id = glGenTextures();
            // and bind it to a 2D texture
            glBindTexture(GL_TEXTURE_2D, id);
            // Mandatory: it's not apparent from the documentation of glTexParameterf, but see https://www.khronos.org/opengl/wiki/Common_Mistakes section "Creating a complete texture"
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            // Not mandatory. Put here for good measure.
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuffer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void bind(int sampler) {
        if (sampler >= 0 && sampler <= 31) {
            // Make zeroth texture unit active. Assumes having only one texture loaded.
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }
}
