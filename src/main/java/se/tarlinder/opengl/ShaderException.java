package se.tarlinder.opengl;

/**
 * Thrown to indicate that there's something wrong with the shader; probably during construction.
 */
public class ShaderException extends RuntimeException {
    public ShaderException(String message) {
        super(message);
    }
}
